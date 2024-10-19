package io.github.avapl
package adapters.postgres.repository.reservation

import cats.data.OptionT
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.error.desk.DeskNotFound
import domain.model.error.reservation._
import domain.model.error.user.UserNotFound
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.repository.reservation.ReservationRepository
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.codec.all._
import skunk.data.Completion
import skunk.implicits._

class PostgresDeskReservationRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends ReservationRepository[F, DeskReservation] {

  import PostgresDeskReservationRepository._

  override def createReservation(deskReservation: DeskReservation): F[DeskReservation] =
    session.use { session =>
      for {
        sql <- session.prepare(createDeskReservationSql)
        _ <- sql
          .execute(deskReservation)
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("reservation_desk_id_fkey") =>
              DeskNotFound(deskReservation.deskId).raiseError
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("reservation_user_id_fkey") =>
              UserNotFound(deskReservation.userId).raiseError
            case SqlState.ExclusionViolation(e) if e.constraintName.contains("reservation_overlap_excl") =>
              OverlappingReservations.raiseError
          }
      } yield deskReservation
    }

  private lazy val createDeskReservationSql: Command[DeskReservation] =
    sql"""
      INSERT INTO reservation
      VALUES      ($deskReservationEncoder)
    """.command

  override def readReservation(reservationId: UUID): F[DeskReservation] =
    session.use { session =>
      for {
        sql <- session.prepare(readDeskReservationSql)
        deskReservation <- OptionT(sql.option(reservationId)).getOrRaise(ReservationNotFound(reservationId))
      } yield deskReservation
    }

  private lazy val readDeskReservationSql: Query[UUID, DeskReservation] =
    sql"""
      SELECT *
      FROM   reservation
      WHERE  id = $uuid AND
             type = 'Desk'
    """.query(deskReservationDecoder)

  override def updateReservationState(reservationId: UUID, newState: ReservationState): F[Unit] =
    session.use { session =>
      for {
        sql <- session.prepare(updateReservationStateSql)
        _ <- sql
          .execute(reservationId *: newState *: EmptyTuple)
          .ensureOr {
            case Completion.Update(0) => ReservationNotFound(reservationId)
            case completion           => new RuntimeException(s"Expected 1 updated office, but got: $completion")
          }(_ == Completion.Update(1))
      } yield ()
    }

  override def readReservationState(reservationId: UUID): F[ReservationState] =
    session.use { session =>
      for {
        sql <- session.prepare(readReservationStateSql)
        reservationState <- OptionT(sql.option(reservationId)).getOrRaise(ReservationNotFound(reservationId))
      } yield reservationState
    }

  private lazy val readReservationStateSql: Query[UUID, ReservationState] =
    sql"""
      SELECT state
      FROM   reservation
      WHERE  id = $uuid
    """.query(reservationStateCodec)

  @nowarn("msg=match may not be exhaustive")
  private lazy val updateReservationStateSql: Command[UUID *: ReservationState *: EmptyTuple] =
    sql"""
      UPDATE reservation
      SET    state = $reservationStateCodec
      WHERE  id = $uuid
    """.command
      .contramap {
        case reservationId *: reservationState *: EmptyTuple =>
          reservationState *: reservationId *: EmptyTuple
      }
}

object PostgresDeskReservationRepository {

  private lazy val deskReservationEncoder: Encoder[DeskReservation] =
    (
      uuid *: // id
        uuid *: // user_id
        timestamp *: // created_at
        timestamp *: // reserved_from
        timestamp *: // reserved_to
        reservationStateCodec *: // state
        varchar *: // notes
        varchar *: // type
        uuid // desk_id
    ).contramap { deskReservation =>
      deskReservation.id *:
        deskReservation.userId *:
        deskReservation.createdAt *:
        deskReservation.reservedFrom *:
        deskReservation.reservedTo *:
        deskReservation.state *:
        deskReservation.notes *:
        "Desk" *:
        deskReservation.deskId *:
        EmptyTuple
    }

  @nowarn("msg=match may not be exhaustive")
  private lazy val deskReservationDecoder: Decoder[DeskReservation] =
    (
      uuid *: // id
        uuid *: // user_id
        timestamp *: // created_at
        timestamp *: // reserved_from
        timestamp *: // reserved_to
        reservationStateCodec *: // state
        varchar *: // notes
        varchar *: // type
        uuid // desk_id
    ).map {
      case id *: userId *: createdAt *: reservedFrom *: reservedTo *: state *: notes *: _ *: deskId *: EmptyTuple =>
        DeskReservation(id, userId, createdAt, reservedFrom.toLocalDate, reservedTo.toLocalDate, state, notes, deskId)
    }

  lazy val reservationStateCodec: Codec[ReservationState] =
    varchar.imap[ReservationState](ReservationState.withNameInsensitive)(_.entryName)
}

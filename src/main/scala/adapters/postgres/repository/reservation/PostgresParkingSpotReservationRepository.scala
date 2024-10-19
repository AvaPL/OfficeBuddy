package io.github.avapl
package adapters.postgres.repository.reservation

import cats.data.OptionT
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.error.parkingspot.ParkingSpotNotFound
import domain.model.error.reservation._
import domain.model.error.user.UserNotFound
import domain.model.reservation.ParkingSpotReservation
import domain.model.reservation.ReservationState
import domain.repository.reservation.ReservationRepository
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.codec.all._
import skunk.data.Completion
import skunk.implicits._

class PostgresParkingSpotReservationRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends ReservationRepository[F, ParkingSpotReservation] {

  import PostgresParkingSpotReservationRepository._

  override def createReservation(parkingSpotReservation: ParkingSpotReservation): F[ParkingSpotReservation] =
    session.use { session =>
      for {
        sql <- session.prepare(createParkingSpotReservationSql)
        _ <- sql
          .execute(parkingSpotReservation)
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("reservation_parking_spot_id_fkey") =>
              ParkingSpotNotFound(parkingSpotReservation.parkingSpotId).raiseError
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("reservation_user_id_fkey") =>
              UserNotFound(parkingSpotReservation.userId).raiseError
            case SqlState.ExclusionViolation(e) if e.constraintName.contains("parking_spot_reservation_overlap_excl") =>
              OverlappingReservations.raiseError
          }
      } yield parkingSpotReservation
    }

  private lazy val createParkingSpotReservationSql: Command[ParkingSpotReservation] =
    sql"""
      INSERT INTO reservation (id, user_id, created_at, reserved_from, reserved_to, state, notes, type, parking_spot_id, plate_number)
      VALUES      ($parkingSpotReservationEncoder)
    """.command

  override def readReservation(reservationId: UUID): F[ParkingSpotReservation] =
    session.use { session =>
      for {
        sql <- session.prepare(readParkingSpotReservationSql)
        parkingSpotReservation <- OptionT(sql.option(reservationId)).getOrRaise(ReservationNotFound(reservationId))
      } yield parkingSpotReservation
    }

  private lazy val readParkingSpotReservationSql: Query[UUID, ParkingSpotReservation] =
    sql"""
      SELECT id, user_id, created_at, reserved_from, reserved_to, state, notes, parking_spot_id, plate_number
      FROM   reservation
      WHERE  id = $uuid AND
             type = 'ParkingSpot'
    """.query(parkingSpotReservationDecoder)

  override def updateReservationState(reservationId: UUID, newState: ReservationState): F[Unit] =
    session.use { session =>
      for {
        sql <- session.prepare(updateReservationStateSql)
        _ <- sql
          .execute(reservationId *: newState *: EmptyTuple)
          .ensureOr {
            case Completion.Update(0) => ReservationNotFound(reservationId)
            case completion => new RuntimeException(s"Expected 1 updated parking spot reservation, but got: $completion")
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

object PostgresParkingSpotReservationRepository {

  private lazy val parkingSpotReservationEncoder: Encoder[ParkingSpotReservation] =
    (
      uuid *: // id
        uuid *: // user_id
        timestamp *: // created_at
        timestamp *: // reserved_from
        timestamp *: // reserved_to
        reservationStateCodec *: // state
        varchar *: // notes
        varchar *: // type
        uuid *: // parking_spot_id
        varchar // plate_number
    ).contramap { parkingSpotReservation =>
      parkingSpotReservation.id *:
        parkingSpotReservation.userId *:
        parkingSpotReservation.createdAt *:
        parkingSpotReservation.reservedFrom *:
        parkingSpotReservation.reservedTo *:
        parkingSpotReservation.state *:
        parkingSpotReservation.notes *:
        "ParkingSpot" *:
        parkingSpotReservation.parkingSpotId *:
        parkingSpotReservation.plateNumber *:
        EmptyTuple
    }

  @nowarn("msg=match may not be exhaustive")
  private lazy val parkingSpotReservationDecoder: Decoder[ParkingSpotReservation] =
    (
      uuid *: // id
        uuid *: // user_id
        timestamp *: // created_at
        timestamp *: // reserved_from
        timestamp *: // reserved_to
        reservationStateCodec *: // state
        varchar *: // notes
        uuid *: // parking_spot_id
        varchar // plate_number
    ).map {
      case id *: userId *: createdAt *: reservedFrom *: reservedTo *: state *: notes *: parkingSpotId *: plateNumber *: EmptyTuple =>
        ParkingSpotReservation(
          id = id,
          userId = userId,
          createdAt = createdAt,
          reservedFromDate = reservedFrom.toLocalDate,
          reservedToDate = reservedTo.toLocalDate,
          state = state,
          notes = notes,
          parkingSpotId = parkingSpotId,
          plateNumber = plateNumber
        )
    }
}

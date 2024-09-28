package io.github.avapl
package adapters.postgres.repository.reservation.view

import adapters.postgres.repository.reservation.PostgresReservationRepository
import cats.effect.Resource
import cats.effect.kernel.Concurrent
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.all._
import domain.model.reservation.ReservationState
import domain.model.reservation.view.DeskReservationListView
import domain.model.reservation.view.DeskReservationView
import domain.model.reservation.view.DeskView
import domain.model.reservation.view.UserView
import domain.model.view.Pagination
import domain.repository.reservation.view.ReservationViewRepository
import java.time.LocalDate
import java.util.UUID
import scala.annotation.nowarn
import scala.util.chaining._
import skunk._
import skunk.codec.all._
import skunk.data._
import skunk.implicits._

class PostgresReservationViewRepository[F[_]: Concurrent: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends ReservationViewRepository[F] {

  import PostgresReservationViewRepository._

  // TODO: Implement integration tests
  override def listDeskReservations(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationStates: Option[List[ReservationState]],
    userId: Option[UUID],
    limit: Int,
    offset: Int
  ): F[DeskReservationListView] = {
    val chunkSize = limit + 1 // Take one more element to determine if there are more elements to fetch
    val appliedFragment =
      listDeskReservationsSql(officeId, reservationFrom, reservationStates, userId, chunkSize, offset)
    session.use { session =>
      for {
        sql <- session.prepare(appliedFragment.fragment.query(deskReservationViewDecoder))
        deskReservationsViews <- sql.stream(appliedFragment.argument, chunkSize).compile.toList
      } yield {
        val hasMoreResults = deskReservationsViews.size > limit
        val pagination = Pagination(limit, offset, hasMoreResults)
        DeskReservationListView(
          reservations = deskReservationsViews.take(limit),
          pagination = pagination
        )
      }
    }
  }
}

object PostgresReservationViewRepository {

  private def listDeskReservationsSql(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationStates: Option[List[ReservationState]],
    userId: Option[UUID],
    limit: Int,
    offset: Int
  ): AppliedFragment = {
    val select = sql"""
      SELECT   id, reserved_from, reserved_to, state, notes, user_id, user_first_name, user_last_name, user_email, desk_id, desk_name
      FROM     reservation
      WHERE    desk_id IN (
        SELECT id
        FROM   desk
        WHERE  office_id = $uuid
      )
      AND reserved_from >= $timestamp
    """

    val statesFilter = sql""" AND state = ANY(${_reservationStatesEncoder})"""
    val userIdFilter = sql""" AND user_id = $uuid"""

    val appliedFilters = List(
      reservationStates.map(statesFilter),
      userId.map(userIdFilter)
    ).flatten.fold(AppliedFragment.empty)(_ |+| _)

    val orderByLimitOffset = sql"""
      ORDER BY first_name, last_name, email
      LIMIT    $int4
      OFFSET   $int4
    """

    select(officeId, reservationFrom.atStartOfDay()) |+| appliedFilters |+| orderByLimitOffset(limit, offset)
  }

  private lazy val _reservationStatesEncoder: Encoder[List[ReservationState]] =
    _varchar.asEncoder.contramap[List[ReservationState]](_.map(_.entryName).pipe(Arr(_: _*)))

  @nowarn("msg=match may not be exhaustive")
  private lazy val deskReservationViewDecoder: Decoder[DeskReservationView] =
    (
      uuid *: // id
        timestamp *: // reserved_from
        timestamp *: // reserved_to
        PostgresReservationRepository.reservationStateCodec *: // state
        varchar *: // notes
        uuid *: // user_id
        varchar *: // user_first_name
        varchar *: // user_last_name
        varchar *: // user_email
        uuid *: // desk_id
        varchar // desk_name
    ).map {
      case id *: reservedFrom *: reservedTo *: state *: notes *: userId *: userFirstName *: userLastName *: userEmail *: deskId *: deskName *: EmptyTuple =>
        DeskReservationView(
          id = id,
          reservedFromDate = reservedFrom.toLocalDate,
          reservedToDate = reservedTo.toLocalDate,
          state = state,
          notes = notes,
          user = UserView(
            id = userId,
            firstName = userFirstName,
            lastName = userLastName,
            email = userEmail
          ),
          desk = DeskView(
            id = deskId,
            name = deskName
          )
        )
    }
}

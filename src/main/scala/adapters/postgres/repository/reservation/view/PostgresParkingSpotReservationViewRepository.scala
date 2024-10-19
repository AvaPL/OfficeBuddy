package io.github.avapl
package adapters.postgres.repository.reservation.view

import adapters.postgres.repository.reservation.reservationStateCodec
import cats.effect.Resource
import cats.effect.kernel.Concurrent
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.all._
import domain.model.reservation.ReservationState
import domain.model.reservation.view.ParkingSpotReservationListView
import domain.model.reservation.view.ParkingSpotReservationView
import domain.model.reservation.view.ParkingSpotView
import domain.model.reservation.view.UserView
import domain.model.view.Pagination
import domain.repository.reservation.view.ReservationViewRepository
import java.time.LocalDate
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.codec.all._
import skunk.implicits._

class PostgresParkingSpotReservationViewRepository[F[_]: Concurrent: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends ReservationViewRepository[F, ParkingSpotReservationListView] {

  import PostgresParkingSpotReservationViewRepository._

  override def listReservations(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationStates: Option[List[ReservationState]],
    userId: Option[UUID],
    limit: Int,
    offset: Int
  ): F[ParkingSpotReservationListView] = {
    val chunkSize = limit + 1 // Take one more element to determine if there are more elements to fetch
    val appliedFragment =
      listParkingSpotReservationsSql(officeId, reservationFrom, reservationStates, userId, chunkSize, offset)
    session.use { session =>
      for {
        sql <- session.prepare(appliedFragment.fragment.query(parkingSpotReservationViewDecoder))
        parkingSpotReservationsViews <- sql.stream(appliedFragment.argument, chunkSize).compile.toList
      } yield {
        val hasMoreResults = parkingSpotReservationsViews.size > limit
        val pagination = Pagination(limit, offset, hasMoreResults)
        ParkingSpotReservationListView(
          reservations = parkingSpotReservationsViews.take(limit),
          pagination = pagination
        )
      }
    }
  }
}

object PostgresParkingSpotReservationViewRepository {

  private def listParkingSpotReservationsSql(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationStates: Option[List[ReservationState]],
    userId: Option[UUID],
    limit: Int,
    offset: Int
  ): AppliedFragment = {
    val select = sql"""
      SELECT r.id,
             r.reserved_from,
             r.reserved_to,
             r.state,
             r.notes,
             a.id AS user_id,
             a.first_name AS user_first_name,
             a.last_name AS user_last_name,
             a.email AS user_email,
             p.id AS parking_spot_id,
             p.name AS parking_spot_name,
             r.plate_number
      FROM reservation r
      LEFT JOIN parking_spot p ON parking_spot_id = p.id
      LEFT JOIN account a ON user_id = a.id
      WHERE parking_spot_id IN (
        SELECT id
        FROM   parking_spot
        WHERE  office_id = $uuid
      )
      AND $timestamp <= reserved_to
    """

    val statesFilter = sql""" AND state = ANY(${_reservationStatesEncoder})"""
    val userIdFilter = sql""" AND user_id = $uuid"""

    val appliedFilters = List(
      reservationStates.map(statesFilter),
      userId.map(userIdFilter)
    ).flatten.fold(AppliedFragment.empty)(_ |+| _)

    val orderByLimitOffset = sql"""
      ORDER BY reserved_from, reserved_to, plate_number
      LIMIT    $int4
      OFFSET   $int4
    """

    select(officeId, reservationFrom.atStartOfDay()) |+| appliedFilters |+| orderByLimitOffset(limit, offset)
  }

  @nowarn("msg=match may not be exhaustive")
  private lazy val parkingSpotReservationViewDecoder: Decoder[ParkingSpotReservationView] =
    (
      uuid *: // id
        timestamp *: // reserved_from
        timestamp *: // reserved_to
        reservationStateCodec *: // state
        varchar *: // notes
        uuid *: // user_id
        varchar *: // user_first_name
        varchar *: // user_last_name
        varchar *: // user_email
        uuid *: // parking_spot_id
        varchar *: // parking_spot_name
        varchar // plate_number
    ).map {
      case id *: reservedFrom *: reservedTo *: state *: notes *: userId *: userFirstName *: userLastName *: userEmail *: parkingSpotId *: parkingSpotName *: plateNumber *: EmptyTuple =>
        ParkingSpotReservationView(
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
          parkingSpot = ParkingSpotView(
            id = parkingSpotId,
            name = parkingSpotName
          ),
          plateNumber = plateNumber
        )
    }
}

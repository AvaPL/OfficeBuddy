package io.github.avapl
package adapters.postgres.repository.parkingspot.view

import cats.effect.kernel.Concurrent
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.parkingspot.view.ParkingSpotListView
import domain.model.parkingspot.view.ParkingSpotView
import domain.model.parkingspot.view.ReservableParkingSpotView
import domain.model.view.Pagination
import domain.repository.parkingspot.view.ParkingSpotViewRepository
import java.time.LocalDate
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.codec.all._
import skunk.implicits._

class PostgresParkingSpotViewRepository[F[_]: Concurrent: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends ParkingSpotViewRepository[F] {

  import PostgresParkingSpotViewRepository._

  override def listParkingSpots(officeId: UUID, limit: Int, offset: Int): F[ParkingSpotListView] =
    session.use { session =>
      for {
        sql <- session.prepare(listParkingSpotsSql)
        chunkSize = limit + 1 // Take one more element to determine if there are more elements to fetch
        parkingSpotViews <- sql.stream((officeId, chunkSize, offset), chunkSize).compile.toList
      } yield {
        val hasMoreResults = parkingSpotViews.size > limit
        val pagination = Pagination(limit, offset, hasMoreResults)
        ParkingSpotListView(
          parkingSpots = parkingSpotViews.take(limit),
          pagination = pagination
        )
      }
    }

  override def listParkingSpotsAvailableForReservation(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationTo: LocalDate
  ): F[List[ReservableParkingSpotView]] =
    if (reservationFrom == reservationTo || reservationFrom.isBefore(reservationTo))
      safeListParkingSpotsAvailableForReservation(officeId, reservationFrom, reservationTo)
    else
      List.empty[ReservableParkingSpotView].pure[F]

  private def safeListParkingSpotsAvailableForReservation(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationTo: LocalDate
  ) =
    session.use { session =>
      for {
        sql <- session.prepare(listParkingSpotsAvailableForReservationSql)
        reservableParkingSpots <- sql.stream((officeId, reservationFrom, reservationTo), 64).compile.toList
      } yield reservableParkingSpots
    }
}

object PostgresParkingSpotViewRepository {

  private lazy val listParkingSpotsSql: Query[UUID *: Int *: Int *: EmptyTuple, ParkingSpotView] =
    sql"""
      SELECT   *
      FROM     parking_spot
      WHERE    office_id = $uuid
        AND    is_archived = 'no'
      ORDER BY name
      LIMIT    $int4
      OFFSET   $int4
    """.query(parkingSpotViewDecoder)

  @nowarn("msg=match may not be exhaustive")
  private lazy val parkingSpotViewDecoder: Decoder[ParkingSpotView] =
    (
      uuid *: // id
        varchar *: // name
        bool *: // is_available
        _varchar *: // notes
        bool *: // is_handicapped
        bool *: // is_underground
        uuid *: // office_id
        bool // is_archived
    ).map {
      case id *: name *: isAvailable *: _ *: isHandicapped *: isUnderground *: _ *: _ *: EmptyTuple =>
        ParkingSpotView(id, name, isAvailable, isHandicapped, isUnderground)
    }

  private lazy val listParkingSpotsAvailableForReservationSql: Query[
    UUID *: LocalDate *: LocalDate *: EmptyTuple,
    ReservableParkingSpotView
  ] =
    sql"""
      SELECT p.id, p.name, p.is_handicapped, p.is_underground
      FROM   parking_spot p
      WHERE  p.office_id = $uuid
        AND  p.is_archived = 'no'
        AND  p.is_available = 'yes'
        AND  p.id NOT IN (
          SELECT r.parking_spot_id
          FROM   reservation r
          WHERE  r.type = 'ParkingSpot'
            AND  r.state IN ('Pending', 'Confirmed')
            AND  tsrange(r.reserved_from, r.reserved_to, '[]') && tsrange($date, $date, '[]')
        )
    """.query(reservableParkingSpotViewDecoder)

  @nowarn("msg=match may not be exhaustive")
  private lazy val reservableParkingSpotViewDecoder: Decoder[ReservableParkingSpotView] =
    (
      uuid *: // id
        varchar *: // name
        bool *: // is_handicapped
        bool // is_underground
    ).map {
      case id *: name *: isHandicapped *: isUnderground *: EmptyTuple =>
        ReservableParkingSpotView(id, name, isHandicapped, isUnderground)
    }
}

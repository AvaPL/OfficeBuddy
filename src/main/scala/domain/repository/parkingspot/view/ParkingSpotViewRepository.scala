package io.github.avapl
package domain.repository.parkingspot.view

import domain.model.parkingspot.view.ParkingSpotListView
import domain.model.parkingspot.view.ReservableParkingSpotView
import java.time.LocalDate
import java.util.UUID

trait ParkingSpotViewRepository[F[_]] {

  def listParkingSpots(
    officeId: UUID,
    limit: Int,
    offset: Int
  ): F[ParkingSpotListView]

  def listParkingSpotsAvailableForReservation(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationTo: LocalDate
  ): F[List[ReservableParkingSpotView]]
}

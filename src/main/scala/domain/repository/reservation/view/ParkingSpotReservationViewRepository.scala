package io.github.avapl
package domain.repository.reservation.view

import domain.model.reservation.ReservationState
import io.github.avapl.domain.model.reservation.view.ParkingSpotReservationListView
import java.time.LocalDate
import java.util.UUID

trait ParkingSpotReservationViewRepository[F[_]] {

  /**
   * @param officeId
   *   desk's office ID
   * @param reservationFrom
   *   reservation start date lower bound (inclusive)
   * @param reservationStates
   *   list of allowed reservation states
   * @param userId
   *   reservation's assigned user ID
   * @param plateNumber
   *   vehicle's plate number
   */
  def listParkingSpotReservations(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationStates: Option[List[ReservationState]] = None,
    userId: Option[UUID] = None,
    plateNumber: Option[String] = None,
    limit: Int,
    offset: Int
  ): F[ParkingSpotReservationListView]
}

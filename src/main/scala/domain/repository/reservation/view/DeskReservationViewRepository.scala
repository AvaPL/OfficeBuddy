package io.github.avapl
package domain.repository.reservation.view

import domain.model.reservation.ReservationState
import domain.model.reservation.view.DeskReservationListView
import java.time.LocalDate
import java.util.UUID

trait DeskReservationViewRepository[F[_]] {

  /**
   * @param officeId
   *   desk's office ID
   * @param reservationFrom
   *   reservation start date lower bound (inclusive)
   * @param reservationStates
   *   list of allowed reservation states
   * @param userId
   *   reservation's assigned user ID
   */
  def listDeskReservations(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationStates: Option[List[ReservationState]] = None,
    userId: Option[UUID] = None,
    limit: Int,
    offset: Int
  ): F[DeskReservationListView]
}

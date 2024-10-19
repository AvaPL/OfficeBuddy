package io.github.avapl
package domain.repository.reservation.view

import domain.model.reservation.ReservationState
import java.time.LocalDate
import java.util.UUID

/**
 * @tparam V
 *   view type
 */
trait ReservationViewRepository[F[_], V] {

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
  def listReservations(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationStates: Option[List[ReservationState]] = None,
    userId: Option[UUID] = None,
    limit: Int,
    offset: Int
  ): F[V]
}

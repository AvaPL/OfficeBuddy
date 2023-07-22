package io.github.avapl
package domain.model.error

import domain.model.reservation.ReservationState
import java.util.UUID
import scala.util.control.NoStackTrace

package object reservation {

  case object OverlappingReservations extends NoStackTrace
  case class ReservationNotFound(reservationId: UUID) extends NoStackTrace
  case class InvalidStateTransition(
    reservationId: UUID,
    currentState: ReservationState,
    newState: ReservationState
  ) extends NoStackTrace
}

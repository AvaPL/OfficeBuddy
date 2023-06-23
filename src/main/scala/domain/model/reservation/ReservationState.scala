package io.github.avapl
package domain.model.reservation

import enumeratum.EnumEntry

sealed trait ReservationState extends EnumEntry

object ReservationState {

  case object Pending extends ReservationState
  case object Confirmed extends ReservationState
  case object Rejected extends ReservationState
}

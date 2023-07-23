package io.github.avapl
package domain.model.reservation

import enumeratum.EnumEntry
import enumeratum.Enum

sealed trait ReservationState extends EnumEntry

object ReservationState extends Enum[ReservationState]{

  case object Pending extends ReservationState
  case object Cancelled extends ReservationState
  case object Confirmed extends ReservationState
  case object Rejected extends ReservationState

  override val values: IndexedSeq[ReservationState] = findValues
}

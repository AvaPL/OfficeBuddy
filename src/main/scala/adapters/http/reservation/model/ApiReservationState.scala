package io.github.avapl
package adapters.http.reservation.model

import domain.model.reservation.ReservationState
import enumeratum.CirceEnum
import enumeratum.Enum
import enumeratum.EnumEntry
import sttp.tapir.codec.enumeratum.TapirCodecEnumeratum

sealed abstract class ApiReservationState(val toDomain: ReservationState) extends EnumEntry

object ApiReservationState
  extends Enum[ApiReservationState]
  with CirceEnum[ApiReservationState]
  with TapirCodecEnumeratum {

  case object Pending extends ApiReservationState(toDomain = ReservationState.Pending)
  case object Cancelled extends ApiReservationState(toDomain = ReservationState.Cancelled)
  case object Confirmed extends ApiReservationState(toDomain = ReservationState.Confirmed)
  case object Rejected extends ApiReservationState(toDomain = ReservationState.Rejected)

  override val values: IndexedSeq[ApiReservationState] = findValues
}

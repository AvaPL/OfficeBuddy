package io.github.avapl
package adapters.http.reservation

import enumeratum.CirceEnum
import enumeratum.Enum
import enumeratum.EnumEntry
import sttp.tapir.codec.enumeratum.TapirCodecEnumeratum

sealed trait ApiReservationState extends EnumEntry

object ApiReservationState
  extends Enum[ApiReservationState]
  with CirceEnum[ApiReservationState]
  with TapirCodecEnumeratum {

  case object Pending extends ApiReservationState
  case object Cancelled extends ApiReservationState
  case object Confirmed extends ApiReservationState
  case object Rejected extends ApiReservationState

  override val values: IndexedSeq[ApiReservationState] = findValues
}

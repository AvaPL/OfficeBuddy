package io.github.avapl
package adapters.http.reservation.model.view

import adapters.http.reservation.model.ApiReservationState
import derevo.derive
import java.time.LocalDate
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("DeskReservationView")
case class ApiDeskReservationView(
  id: UUID,
  reservedFromDate: LocalDate,
  reservedToDate: LocalDate,
  state: ApiReservationState,
  notes: String,
  user: ApiUserView,
  desk: ApiDeskView
)

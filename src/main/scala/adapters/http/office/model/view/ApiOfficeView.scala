package io.github.avapl
package adapters.http.office.model.view

import derevo.derive
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("OfficeView")
case class ApiOfficeView(
  id: UUID,
  name: String,
  notes: List[String],
  address: ApiAddressView,
  officeManagers: List[ApiOfficeManagerView],
  assignedAccountsCount: Int,
  desksCount: Int,
  parkingSpotsCount: Int,
  roomsCount: Int,
  activeReservationsCount: Int
)

package io.github.avapl
package adapters.http.parkingspot.model.view

import derevo.derive
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("ParkingSpotView")
case class ApiParkingSpotView(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  isHandicapped: Boolean,
  isUnderground: Boolean
)

package io.github.avapl
package adapters.http.desk.view

import derevo.derive
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("DeskView")
case class ApiDeskView(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  isStanding: Boolean,
  monitorsCount: Short,
  hasPhone: Boolean
)

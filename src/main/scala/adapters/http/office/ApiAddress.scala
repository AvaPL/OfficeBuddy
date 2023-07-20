package io.github.avapl
package adapters.http.office

import derevo.derive
import domain.model.office.Address
import io.scalaland.chimney.dsl._
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Address")
case class ApiAddress(
  addressLine1: String,
  addressLine2: String,
  postalCode: String,
  city: String,
  country: String
) {

  lazy val toDomain: Address =
    this.transformInto[Address]
}

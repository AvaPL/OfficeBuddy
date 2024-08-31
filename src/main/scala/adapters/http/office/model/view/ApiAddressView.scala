package io.github.avapl
package adapters.http.office.model.view

import util.derevo.circe.{circeDecoder, circeEncoder}
import util.derevo.tapir.tapirSchema

import derevo.derive
import sttp.tapir.Schema.annotations.encodedName

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("AddressView")
case class ApiAddressView(
  addressLine1: String,
  addressLine2: String,
  postalCode: String,
  city: String,
  country: String
)

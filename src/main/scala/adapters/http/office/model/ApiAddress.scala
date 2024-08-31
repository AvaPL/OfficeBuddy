package io.github.avapl
package adapters.http.office.model

import derevo.derive
import domain.model.office.{Address, UpdateAddress}

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

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Address (update)")
case class ApiUpdateAddress(
  addressLine1: Option[String] = None,
  addressLine2: Option[String] = None,
  postalCode: Option[String] = None,
  city: Option[String] = None,
  country: Option[String] = None
) {

  lazy val toDomain: UpdateAddress =
    this.transformInto[UpdateAddress]
}

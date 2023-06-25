package io.github.avapl
package adapters.http.office

import domain.model.office.Office
import io.scalaland.chimney.dsl._
import java.util.UUID
import sttp.tapir.Schema

case class JsonOffice(
  id: UUID,
  name: String,
  notes: List[String],
  //
  address: JsonAddress
)

object JsonOffice {

  def fromDomain(office: Office): JsonOffice =
    office.transformInto[JsonOffice]

  implicit val tapirSchema: Schema[JsonOffice] = Schema.derived
}

case class JsonAddress(
  addressLine1: String,
  addressLine2: String,
  postalCode: String,
  city: String,
  country: String
)

object JsonAddress {
  implicit val tapirSchema: Schema[JsonAddress] = Schema.derived
}

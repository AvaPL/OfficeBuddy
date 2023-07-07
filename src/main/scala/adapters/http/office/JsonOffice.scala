package io.github.avapl
package adapters.http.office

import domain.model.office.Address
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
) {

  lazy val toDomain: Office =
    this.transformInto[Office]
}

object JsonOffice {

  def fromDomain(office: Office): JsonOffice =
    office.transformInto[JsonOffice]

  def fromJsonCreateOffice(officeId: UUID, jsonCreateOffice: JsonCreateOffice): JsonOffice =
    jsonCreateOffice.toJsonOffice(officeId)

  // TODO: Use derevo for schema
  implicit val tapirSchema: Schema[JsonOffice] = Schema.derived
}

case class JsonAddress(
  addressLine1: String,
  addressLine2: String,
  postalCode: String,
  city: String,
  country: String
) {

  lazy val toDomain: Address =
    this.transformInto[Address]
}

object JsonAddress {
  implicit val tapirSchema: Schema[JsonAddress] = Schema.derived
}

case class JsonCreateOffice(
  name: String,
  notes: List[String],
  //
  address: JsonAddress
) {

  def toJsonOffice(officeId: UUID): JsonOffice =
    this
      .into[JsonOffice]
      .withFieldConst(_.id, officeId)
      .transform
}

object JsonCreateOffice {
  implicit val tapirSchema: Schema[JsonCreateOffice] = Schema.derived
}

case class JsonUpdateOffice(
  name: String,
  notes: List[String],
  //
  address: JsonAddress
) {

  def toDomain(officeId: UUID): Office =
    this.into[Office]
      .withFieldConst(_.id, officeId)
      .transform
}

object JsonUpdateOffice {
  implicit val tapirSchema: Schema[JsonUpdateOffice] = Schema.derived
}

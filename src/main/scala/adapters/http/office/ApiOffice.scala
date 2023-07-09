package io.github.avapl
package adapters.http.office

import domain.model.office.Address
import domain.model.office.Office
import io.scalaland.chimney.dsl._
import java.util.UUID
import sttp.tapir.Schema
import sttp.tapir.Schema.annotations.encodedName

@encodedName("Office")
case class ApiOffice(
  id: UUID,
  name: String,
  notes: List[String],
  //
  address: ApiAddress
) {

  lazy val toDomain: Office =
    this.transformInto[Office]
}

object ApiOffice {

  def fromDomain(office: Office): ApiOffice =
    office.transformInto[ApiOffice]

  def fromApiCreateOffice(officeId: UUID, apiCreateOffice: ApiCreateOffice): ApiOffice =
    apiCreateOffice.toApiOffice(officeId)

  // TODO: Use derevo for schema
  implicit val tapirSchema: Schema[ApiOffice] = Schema.derived
}

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

object ApiAddress {
  implicit val tapirSchema: Schema[ApiAddress] = Schema.derived
}

@encodedName("Office (create)")
case class ApiCreateOffice(
  name: String,
  notes: List[String],
  //
  address: ApiAddress
) {

  def toApiOffice(officeId: UUID): ApiOffice =
    this
      .into[ApiOffice]
      .withFieldConst(_.id, officeId)
      .transform
}

object ApiCreateOffice {
  implicit val tapirSchema: Schema[ApiCreateOffice] = Schema.derived
}

@encodedName("Office (update)")
case class ApiUpdateOffice(
  name: String,
  notes: List[String],
  //
  address: ApiAddress
) {

  def toDomain(officeId: UUID): Office =
    this
      .into[Office]
      .withFieldConst(_.id, officeId)
      .transform
}

object ApiUpdateOffice {
  implicit val tapirSchema: Schema[ApiUpdateOffice] = Schema.derived
}

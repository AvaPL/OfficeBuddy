package io.github.avapl
package adapters.http.office

import derevo.derive
import domain.model.office.CreateOffice
import domain.model.office.Office
import domain.model.office.UpdateOffice
import io.scalaland.chimney.dsl._
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Office")
case class ApiOffice(
  id: UUID,
  name: String,
  notes: List[String],
  //
  address: ApiAddress,
  //
  isArchived: Boolean
) {

  lazy val toDomain: Office =
    this.transformInto[Office]
}

object ApiOffice {

  def fromDomain(office: Office): ApiOffice =
    office.transformInto[ApiOffice]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Office (create)")
case class ApiCreateOffice(
  name: String,
  notes: List[String],
  //
  address: ApiAddress
) {

  lazy val toDomain: CreateOffice =
    this.transformInto[CreateOffice]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Office (update)")
case class ApiUpdateOffice(
  name: Option[String] = None,
  notes: Option[List[String]] = None,
  //
  address: ApiUpdateAddress = ApiUpdateAddress()
) {

  lazy val toDomain: UpdateOffice =
    this.transformInto[UpdateOffice]
}

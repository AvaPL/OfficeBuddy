package io.github.avapl
package adapters.http.desk

import derevo.derive
import domain.model.desk.CreateDesk
import domain.model.desk.Desk
import domain.model.desk.UpdateDesk
import io.scalaland.chimney.dsl._
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Desk")
case class ApiDesk(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  isStanding: Boolean,
  monitorsCount: Short,
  hasPhone: Boolean,
  //
  officeId: UUID
)

object ApiDesk {

  def fromDomain(desk: Desk): ApiDesk =
    desk.transformInto[ApiDesk]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Desk (create)")
case class ApiCreateDesk(
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  isStanding: Boolean,
  monitorsCount: Short,
  hasPhone: Boolean,
  //
  officeId: UUID
) {

  lazy val toDomain: CreateDesk =
    this.transformInto[CreateDesk]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Desk (update)")
case class ApiUpdateDesk(
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  isStanding: Boolean,
  monitorsCount: Short,
  hasPhone: Boolean,
  //
  officeId: UUID
) {

  lazy val toDomain: UpdateDesk =
    this.transformInto[UpdateDesk]
}

package io.github.avapl
package adapters.http.desk

import domain.model.desk.{CreateDesk, Desk, UpdateDesk}

import io.scalaland.chimney.dsl._

import java.util.UUID
import sttp.tapir.Schema
import sttp.tapir.Schema.annotations.encodedName

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

  implicit val tapirSchema: Schema[ApiDesk] = Schema.derived
}

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

object ApiCreateDesk {
  implicit val tapirSchema: Schema[ApiCreateDesk] = Schema.derived
}

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

object ApiUpdateDesk {
  implicit val tapirSchema: Schema[ApiUpdateDesk] = Schema.derived
}

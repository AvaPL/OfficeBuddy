package io.github.avapl
package domain.model.desk

import io.scalaland.chimney.dsl._
import java.util.UUID

case class Desk(
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

case class CreateDesk(
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

  def toDesk(deskId: UUID): Desk =
    this
      .into[Desk]
      .withFieldConst(_.id, deskId)
      .transform
}

case class UpdateDesk(
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

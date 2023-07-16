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
  officeId: UUID,
  //
  isArchived: Boolean = false
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
      .withFieldConst(_.isArchived, false)
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
  officeId: UUID,
  //
  isArchived: Boolean = false
)

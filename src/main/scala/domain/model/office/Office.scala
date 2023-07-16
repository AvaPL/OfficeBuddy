package io.github.avapl
package domain.model.office

import io.scalaland.chimney.dsl._
import java.util.UUID

case class Office(
  id: UUID,
  name: String,
  notes: List[String],
  //
  address: Address,
  //
  isArchived: Boolean = false
)

case class CreateOffice(
  name: String,
  notes: List[String],
  //
  address: Address
) {

  def toOffice(officeId: UUID): Office =
    this
      .into[Office]
      .withFieldConst(_.id, officeId)
      .withFieldConst(_.isArchived, false)
      .transform
}

case class UpdateOffice(
  name: String,
  notes: List[String],
  //
  address: Address,
  //
  isArchived: Boolean = false
)

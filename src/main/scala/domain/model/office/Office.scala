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
  isArchived: Boolean = false,
  //
  officeManagerIds: List[UUID] = Nil
)

case class CreateOffice(
  name: String,
  notes: List[String],
  //
  address: Address
  // TODO: Add an option to provide office managers
) {

  def toOffice(officeId: UUID): Office =
    this
      .into[Office]
      .withFieldConst(_.id, officeId)
      .withFieldConst(_.isArchived, false)
      .withFieldConst(_.officeManagerIds, Nil)
      .transform
}

case class UpdateOffice(
  name: Option[String] = None,
  notes: Option[List[String]] = None,
  //
  address: UpdateAddress = UpdateAddress(),
)

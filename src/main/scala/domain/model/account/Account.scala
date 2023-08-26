package io.github.avapl
package domain.model.account

import java.util.UUID

sealed trait Account {

  def id: UUID
  def firstName: String
  def lastName: String
  def email: String
  //
  def isArchived: Boolean
}

case class UserAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean = false,
  //
  assignedOfficeId: Option[UUID]
) extends Account

case class CreateUserAccount(
  firstName: String,
  lastName: String,
  email: String,
  //
  assignedOfficeId: Option[UUID]
) {

  def toUserAccount(userId: UUID): UserAccount =
    UserAccount(
      id = userId,
      firstName = firstName,
      lastName = lastName,
      email = email,
      assignedOfficeId = assignedOfficeId
    )
}

case class OfficeManagerAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean = false,
  //
  managedOfficeIds: List[UUID]
) extends Account

case class CreateOfficeManagerAccount(
  firstName: String,
  lastName: String,
  email: String,
  //
  managedOfficeIds: List[UUID]
) {

  def toOfficeManagerAccount(officeManagerId: UUID): OfficeManagerAccount =
    OfficeManagerAccount(
      id = officeManagerId,
      firstName = firstName,
      lastName = lastName,
      email = email,
      managedOfficeIds = managedOfficeIds
    )
}

case class SuperAdminAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean = false
) extends Account

case class CreateSuperAdminAccount(
  firstName: String,
  lastName: String,
  email: String
) {

  def toSuperAdminAccount(superAdminId: UUID): SuperAdminAccount =
    SuperAdminAccount(
      id = superAdminId,
      firstName = firstName,
      lastName = lastName,
      email = email
    )
}

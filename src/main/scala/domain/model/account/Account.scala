package io.github.avapl
package domain.model.account

import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import java.util.UUID

sealed trait Account {

  def id: UUID
  def firstName: String
  def lastName: String
  def email: String
  //
  def isArchived: Boolean
  //
  def role: Role
  //
  def assignedOfficeId: Option[UUID]
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
) extends Account {
  override val role: Role = User
}

case class OfficeManagerAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean = false,
  //
  assignedOfficeId: Option[UUID] = None,
  managedOfficeIds: List[UUID] = Nil
) extends Account {
  override val role: Role = OfficeManager
}

case class SuperAdminAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean = false,
  //
  assignedOfficeId: Option[UUID] = None,
  managedOfficeIds: List[UUID] = Nil
) extends Account {
  override val role: Role = SuperAdmin
}

case class CreateAccount(
  role: Role,
  firstName: String,
  lastName: String,
  email: String,
  //
  assignedOfficeId: Option[UUID],
  managedOfficeIds: List[UUID]
) {

  def toDomain(accountId: UUID): Account =
    role match {
      case User =>
        UserAccount(
          id = accountId,
          firstName = firstName,
          lastName = lastName,
          email = email,
          assignedOfficeId = assignedOfficeId
        )
      case OfficeManager =>
        OfficeManagerAccount(
          id = accountId,
          firstName = firstName,
          lastName = lastName,
          email = email,
          assignedOfficeId = assignedOfficeId,
          managedOfficeIds = managedOfficeIds
        )
      case SuperAdmin =>
        SuperAdminAccount(
          id = accountId,
          firstName = firstName,
          lastName = lastName,
          email = email,
          assignedOfficeId = assignedOfficeId,
          managedOfficeIds = managedOfficeIds
        )
    }
}

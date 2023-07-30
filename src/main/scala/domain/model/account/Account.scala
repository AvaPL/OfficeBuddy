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

case class OfficeManagerAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean = false,
  //
  managedOfficeIds: List[UUID],
) extends Account

case class SuperAdminAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean = false
) extends Account

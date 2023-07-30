package io.github.avapl
package domain.model.user

import java.util.UUID

sealed trait Account {

  def id: UUID
  def firstName: String
  def lastName: String
  def email: String
}

case class UserAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  assignedOfficeId: UUID
) extends Account

case class OfficeManagerAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  managedOfficeIds: List[UUID]
) extends Account

case class SuperAdminAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String
) extends Account

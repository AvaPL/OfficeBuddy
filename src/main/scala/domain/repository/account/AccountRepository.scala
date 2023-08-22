package io.github.avapl
package domain.repository.account

import cats.data.NonEmptyList
import domain.model.account._
import java.util.UUID

trait AccountRepository[F[_]] {

  def createUser(user: UserAccount): F[UserAccount]
  def readUser(userId: UUID): F[UserAccount]
  def updateUserAssignedOffice(userId: UUID, officeId: Option[UUID]): F[UserAccount]

  def createOfficeManager(officeManager: OfficeManagerAccount): F[OfficeManagerAccount]
  def readOfficeManager(officeManagerId: UUID): F[OfficeManagerAccount]
  def updateOfficeManagerManagedOffices(officeManagerId: UUID, officeIds: List[UUID]): F[OfficeManagerAccount]

  def createSuperAdmin(superAdmin: SuperAdminAccount): F[SuperAdminAccount]
  def readSuperAdmin(superAdminId: UUID): F[SuperAdminAccount]

  def updateRoles(accountId: UUID, roles: NonEmptyList[Role]): F[Account]
  def archive(accountId: UUID): F[Unit]
}

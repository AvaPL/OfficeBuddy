package io.github.avapl
package domain.service.account

import cats.FlatMap
import cats.data.NonEmptyList
import cats.syntax.all._
import domain.model.account.Account
import domain.model.account.CreateOfficeManagerAccount
import domain.model.account.CreateSuperAdminAccount
import domain.model.account.CreateUserAccount
import domain.model.account.OfficeManagerAccount
import domain.model.account.Role
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import domain.repository.account.AccountRepository
import java.util.UUID
import util.FUUID

class AccountService[F[_]: FlatMap: FUUID](
  accountRepository: AccountRepository[F]
) {

  def createUser(createUser: CreateUserAccount): F[UserAccount] =
    for {
      userId <- FUUID[F].randomUUID()
      userAccount = createUser.toUserAccount(userId)
      createdUserAccount <- accountRepository.createUser(userAccount)
    } yield createdUserAccount

  def readUser(userId: UUID): F[UserAccount] =
    accountRepository.readUser(userId)

  def updateUserAssignedOffice(userId: UUID, officeId: Option[UUID]): F[UserAccount] =
    accountRepository.updateUserAssignedOffice(userId, officeId)

  def createOfficeManager(createOfficeManager: CreateOfficeManagerAccount): F[OfficeManagerAccount] =
    for {
      officeManagerId <- FUUID[F].randomUUID()
      officeManagerAccount = createOfficeManager.toOfficeManagerAccount(officeManagerId)
      createdOfficeManagerAccount <- accountRepository.createOfficeManager(officeManagerAccount)
    } yield createdOfficeManagerAccount

  def readOfficeManager(officeManagerId: UUID): F[OfficeManagerAccount] =
    accountRepository.readOfficeManager(officeManagerId)

  def updateOfficeManagerManagedOffices(officeManagerId: UUID, officeIds: List[UUID]): F[OfficeManagerAccount] =
    accountRepository.updateOfficeManagerManagedOffices(officeManagerId, officeIds)

  def createSuperAdmin(createSuperAdmin: CreateSuperAdminAccount): F[SuperAdminAccount] =
    for {
      superAdminId <- FUUID[F].randomUUID()
      superAdminAccount = createSuperAdmin.toSuperAdminAccount(superAdminId)
      createdSuperAdminAccount <- accountRepository.createSuperAdmin(superAdminAccount)
    } yield createdSuperAdminAccount

  def readSuperAdmin(superAdminId: UUID): F[SuperAdminAccount] =
    accountRepository.readSuperAdmin(superAdminId)

  def updateRoles(accountId: UUID, roles: NonEmptyList[Role]): F[Account] =
    accountRepository.updateRoles(accountId, roles)

  def archive(accountId: UUID): F[Unit] =
    accountRepository.archive(accountId)
}

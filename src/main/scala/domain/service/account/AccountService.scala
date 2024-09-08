package io.github.avapl
package domain.service.account

import cats.FlatMap
import cats.syntax.all._
import domain.model.account.Account
import domain.model.account.CreateAccount
import domain.model.account.Role
import domain.repository.account.AccountRepository
import java.util.UUID
import util.FUUID

class AccountService[F[_]: FlatMap: FUUID](
  accountRepository: AccountRepository[F]
) {

  def create(createAccount: CreateAccount): F[Account] =
    for {
      accountId <- FUUID[F].randomUUID()
      account = createAccount.toDomain(accountId)
      createdAccount <- accountRepository.create(account)
    } yield createdAccount

  def read(accountId: UUID): F[Account] =
    accountRepository.read(accountId)

  def updateAssignedOffice(accountId: UUID, officeId: Option[UUID]): F[Account] =
    accountRepository.updateAssignedOffice(accountId, officeId)

  def updateManagedOffices(accountId: UUID, officeIds: List[UUID]): F[Account] =
    // TODO: Validate that the account is either an office manager or a super admin
    accountRepository.updateManagedOffices(accountId, officeIds)

  def updateRole(accountId: UUID, role: Role): F[Account] =
    // TODO: Remove managed offices if the role is a user
    accountRepository.updateRole(accountId, role)

  def archive(accountId: UUID): F[Unit] =
    accountRepository.archive(accountId)
}

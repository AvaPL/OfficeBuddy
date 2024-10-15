package io.github.avapl
package domain.service.account

import cats.MonadThrow
import cats.syntax.all._
import domain.model.account.Account
import domain.model.account.SuperAdminAccount
import domain.model.error.account.AccountNotFound
import domain.repository.account.AccountRepository
import domain.repository.account.TemporaryPasswordRepository
import domain.service.account.SuperAdminInitService.initialSuperAdminPassword
import java.util.UUID
import org.typelevel.log4cats.Logger

class SuperAdminInitService[F[_]: Logger: MonadThrow](
  accountRepository: AccountRepository[F] with TemporaryPasswordRepository[F]
) {

  import SuperAdminInitService.initialSuperAdminAccount

  def initSuperAdmin(): F[Account] =
    for {
      currentSuperAdminAccount <- readSuperAdminAccount()
      superAdminAccount <- currentSuperAdminAccount match {
        case Some(superAdminAccount) =>
          superAdminAccount.pure[F] <* Logger[F].info("Initial super admin account already exists")
        case None =>
          createSuperAdminAccount() <* Logger[F].info("Initial super admin account created")
      }
    } yield superAdminAccount

  private def readSuperAdminAccount() =
    accountRepository
      .read(initialSuperAdminAccount.id)
      .map(_.some)
      .recover {
        case AccountNotFound(_) => None
      }

  private def createSuperAdminAccount() =
    for {
      account <- accountRepository.create(initialSuperAdminAccount)
      _ <- accountRepository.setTemporaryPassword(account.id, initialSuperAdminPassword)
    } yield account
}

object SuperAdminInitService {

  lazy val initialSuperAdminAccount: SuperAdminAccount =
    SuperAdminAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
      firstName = "superadmin",
      lastName = "superadmin",
      email = "superadmin@officebuddy.com",
      assignedOfficeId = None,
      managedOfficeIds = Nil
    )

  lazy val initialSuperAdminPassword: String = "superadmin"
}

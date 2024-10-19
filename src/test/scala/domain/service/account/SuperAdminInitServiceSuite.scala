package io.github.avapl
package domain.service.account

import cats.effect.IO
import domain.model.error.account.AccountNotFound
import domain.repository.account.AccountRepository
import domain.repository.account.TemporaryPasswordRepository
import domain.service.account.SuperAdminInitService.initialSuperAdminAccount
import domain.service.account.SuperAdminInitService.initialSuperAdminPassword

import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import weaver.SimpleIOSuite

object SuperAdminInitServiceSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN no super admin account in the repository
      | WHEN initSuperAdmin is called
      | THEN a super admin account is created
      |""".stripMargin
  ) {
    val accountRepository = mock[AccountRepository[IO] with TemporaryPasswordRepository[IO]]
    whenF(accountRepository.read(any)) thenFailWith AccountNotFound(initialSuperAdminAccount.id)
    whenF(accountRepository.create(any)) thenReturn initialSuperAdminAccount
    whenF(accountRepository.setTemporaryPassword(any, any)) thenReturn ()
    val superAdminInitService = new SuperAdminInitService(accountRepository)

    for {
      superAdminAccount <- superAdminInitService.initSuperAdmin()
    } yield {
      verify(accountRepository, times(1)).read(initialSuperAdminAccount.id)
      verify(accountRepository, times(1)).create(initialSuperAdminAccount)
      verify(accountRepository, times(1)).setTemporaryPassword(initialSuperAdminAccount.id, initialSuperAdminPassword)
      expect(superAdminAccount == initialSuperAdminAccount)
    }
  }

  test(
    """GIVEN the initial super admin account in the repository
      | WHEN initSuperAdmin is called
      | THEN the existing super admin account is returned
      |""".stripMargin
  ) {
    val accountRepository = mock[AccountRepository[IO] with TemporaryPasswordRepository[IO]]
    whenF(accountRepository.read(any)) thenReturn initialSuperAdminAccount
    val superAdminInitService = new SuperAdminInitService(accountRepository)

    for {
      superAdminAccount <- superAdminInitService.initSuperAdmin()
    } yield {
      verify(accountRepository, only).read(initialSuperAdminAccount.id)
      expect(superAdminAccount == initialSuperAdminAccount)
    }
  }

  private implicit lazy val logger: Logger[IO] =
    Slf4jLogger.getLogger[IO]
}

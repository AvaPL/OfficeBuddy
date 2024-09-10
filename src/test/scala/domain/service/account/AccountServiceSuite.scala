package io.github.avapl
package domain.service.account

import cats.effect.IO
import cats.implicits._
import domain.model.account._
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.repository.account.AccountRepository
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import util.FUUID
import weaver.SimpleIOSuite

object AccountServiceSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN an account to create
      | WHEN create is called
      | THEN a valid account is created via accountRepository
      |""".stripMargin
  ) {
    val accountToCreate = anyCreateAccount

    val accountId = anyAccountId
    implicit val fuuid: FUUID[IO] = whenF(mock[FUUID[IO]].randomUUID()) thenReturn accountId
    val accountRepository = mock[AccountRepository[IO]]
    val user = accountToCreate.toDomain(accountId)
    whenF(accountRepository.create(any)) thenReturn user
    val accountService = new AccountService[IO](accountRepository)

    for {
      createdUser <- accountService.create(accountToCreate)
    } yield {
      verify(accountRepository, only).create(eqTo(user))
      expect(createdUser == user)
    }
  }

  test(
    """GIVEN an account to create
      | WHEN create is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val accountToCreate = anyCreateAccount

    val duplicateAccountEmail = DuplicateAccountEmail(accountToCreate.email)
    val accountRepository = whenF(mock[AccountRepository[IO]].create(any)) thenFailWith duplicateAccountEmail
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.create(accountToCreate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == duplicateAccountEmail)
    }
  }

  test(
    """GIVEN an account ID
      | WHEN read is called
      | THEN the account is read via accountRepository
      |""".stripMargin
  ) {
    val accountId = anyAccountId

    val accountRepository = mock[AccountRepository[IO]]
    val user = anyUserAccount.copy(id = accountId)
    whenF(accountRepository.read(any)) thenReturn user
    val accountService = new AccountService[IO](accountRepository)

    for {
      readUser <- accountService.read(accountId)
    } yield {
      verify(accountRepository, only).read(eqTo(accountId))
      expect(readUser == user)
    }
  }

  test(
    """GIVEN an account ID
      | WHEN read is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val accountId = anyAccountId

    val accountNotFound = AccountNotFound(accountId)
    val accountRepository = whenF(mock[AccountRepository[IO]].read(any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.read(accountId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == accountNotFound)
    }
  }

  test(
    """GIVEN an account ID and office ID
      | WHEN updateAssignedOffice is called
      | THEN the account assigned office is updated via accountRepository
      |""".stripMargin
  ) {
    val accountId = anyAccountId
    val officeId = anyOfficeId

    val accountRepository = mock[AccountRepository[IO]]
    whenF(accountRepository.updateAssignedOffice(any, any)) thenReturn anyUserAccount
    val accountService = new AccountService[IO](accountRepository)

    for {
      _ <- accountService.updateAssignedOffice(accountId, officeId.some)
    } yield {
      verify(accountRepository, only).updateAssignedOffice(eqTo(accountId), eqTo(officeId.some))
      success
    }
  }

  test(
    """GIVEN an account ID and office ID
      | WHEN updateAssignedOffice is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val accountId = anyAccountId
    val officeId = anyOfficeId

    val accountNotFound = AccountNotFound(accountId)
    val accountRepository =
      whenF(mock[AccountRepository[IO]].updateAssignedOffice(any, any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.updateAssignedOffice(accountId, officeId.some).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == accountNotFound)
    }
  }

  test(
    """GIVEN an office manager ID and office ID
      | WHEN updateManagedOffices is called
      | THEN the managed offices are updated via accountRepository
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val officeIds = List(anyOfficeId)

    val accountRepository = mock[AccountRepository[IO]]
    whenF(accountRepository.updateManagedOffices(any, any)) thenReturn anyOfficeManagerAccount
    val accountService = new AccountService[IO](accountRepository)

    for {
      _ <- accountService.updateManagedOffices(officeManagerId, officeIds)
    } yield {
      verify(accountRepository, only).updateManagedOffices(eqTo(officeManagerId), eqTo(officeIds))
      success
    }
  }

  test(
    """GIVEN an office manager ID and office ID
      | WHEN updateManagedOffices is called
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val officeIds = List(anyOfficeId)

    val accountNotFound = AccountNotFound(officeManagerId)
    val accountRepository =
      whenF(mock[AccountRepository[IO]].updateManagedOffices(any, any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.updateManagedOffices(officeManagerId, officeIds).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == accountNotFound)
    }
  }

  test(
    """GIVEN a user ID and an OfficeManager role
      | WHEN updateRole is called
      | THEN the role is updated via accountRepository
      |""".stripMargin
  ) {
    val user = anyUserAccount

    val userId = user.id
    val role = Role.OfficeManager

    val accountRepository = mock[AccountRepository[IO]]
    val officeManager = OfficeManagerAccount(
      id = user.id,
      firstName = user.firstName,
      lastName = user.lastName,
      email = user.email,
      managedOfficeIds = Nil
    )
    whenF(accountRepository.updateRole(any, any)) thenReturn officeManager
    val accountService = new AccountService[IO](accountRepository)

    for {
      updatedAccount <- accountService.updateRole(userId, role)
    } yield {
      verify(accountRepository, only).updateRole(eqTo(userId), eqTo(role))
      expect(updatedAccount == officeManager)
    }
  }

  test(
    """GIVEN an account ID
      | WHEN updateRole is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val accountId = anyAccountId

    val accountNotFound = AccountNotFound(accountId)
    val accountRepository = whenF(mock[AccountRepository[IO]].updateRole(any, any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.updateRole(accountId, Role.User).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == accountNotFound)
    }
  }

  test(
    """GIVEN an account ID
      | WHEN archive is called
      | THEN the account is archived via accountRepository
      |""".stripMargin
  ) {
    val accountId = anyAccountId

    val accountRepository = mock[AccountRepository[IO]]
    whenF(accountRepository.archive(any)) thenReturn ()
    val accountService = new AccountService[IO](accountRepository)

    for {
      _ <- accountService.archive(accountId)
    } yield {
      verify(accountRepository, only).archive(eqTo(accountId))
      success
    }
  }

  test(
    """GIVEN an account ID
      | WHEN archive is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val accountId = anyAccountId

    val repositoryException = new RuntimeException("intended exception")
    val accountRepository = whenF(mock[AccountRepository[IO]].archive(any)) thenFailWith repositoryException
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.archive(accountId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == repositoryException)
    }
  }

  private lazy val anyCreateAccount = CreateAccount(
    role = Role.OfficeManager,
    firstName = "Test",
    lastName = "User",
    email = "test.user@localhost",
    assignedOfficeId = Some(anyOfficeId),
    managedOfficeIds = List(anyOfficeId)
  )

  private lazy val anyUserAccount = UserAccount(
    id = anyAccountId,
    firstName = "Test",
    lastName = "User",
    email = "test.user@localhost",
    assignedOfficeId = Some(anyOfficeId)
  )

  private lazy val anyOfficeManagerAccount = OfficeManagerAccount(
    id = anyAccountId,
    firstName = "Test",
    lastName = "User",
    email = "test.office.manager@localhost",
    managedOfficeIds = List(anyOfficeId)
  )

  private lazy val anyAccountId = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40")

  private lazy val anyOfficeId = UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed")
}

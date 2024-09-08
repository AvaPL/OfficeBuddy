package io.github.avapl
package domain.service.account

import cats.data.NonEmptyList
import cats.effect.IO
import cats.implicits._
import domain.model.account.CreateOfficeManagerAccount
import domain.model.account.CreateSuperAdminAccount
import domain.model.account.CreateUserAccount
import domain.model.account.OfficeManagerAccount
import domain.model.account.Role
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
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
    """GIVEN a user to create
      | WHEN createUser is called
      | THEN a valid user is created via accountRepository
      |""".stripMargin
  ) {
    val userToCreate = anyCreateAccount

    val userId = anyAccountId
    implicit val fuuid: FUUID[IO] = whenF(mock[FUUID[IO]].randomUUID()) thenReturn userId
    val accountRepository = mock[AccountRepository[IO]]
    val user = userToCreate.toUserAccount(userId)
    whenF(accountRepository.createUser(any)) thenReturn user
    val accountService = new AccountService[IO](accountRepository)

    for {
      createdUser <- accountService.create(userToCreate)
    } yield {
      verify(accountRepository, only).createUser(eqTo(user))
      expect(createdUser == user)
    }
  }

  test(
    """GIVEN a user to create
      | WHEN createUser is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val userToCreate = anyCreateAccount

    val duplicateAccountEmail = DuplicateAccountEmail(userToCreate.email)
    val accountRepository = whenF(mock[AccountRepository[IO]].createUser(any)) thenFailWith duplicateAccountEmail
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.create(userToCreate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == duplicateAccountEmail)
    }
  }

  test(
    """GIVEN a user ID
      | WHEN readUser is called
      | THEN the user is read via accountRepository
      |""".stripMargin
  ) {
    val userId = anyAccountId

    val accountRepository = mock[AccountRepository[IO]]
    val user = anyUserAccount.copy(id = userId)
    whenF(accountRepository.readUser(any)) thenReturn user
    val accountService = new AccountService[IO](accountRepository)

    for {
      readUser <- accountService.read(userId)
    } yield {
      verify(accountRepository, only).readUser(eqTo(userId))
      expect(readUser == user)
    }
  }

  test(
    """GIVEN a user ID
      | WHEN readUser is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val userId = anyAccountId

    val accountNotFound = AccountNotFound(userId)
    val accountRepository = whenF(mock[AccountRepository[IO]].readUser(any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.read(userId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == accountNotFound)
    }
  }

  test(
    """GIVEN a user ID and office ID
      | WHEN updateUserAssignedOffice is called
      | THEN the user assigned office is updated via accountRepository
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val officeId = anyOfficeId

    val accountRepository = mock[AccountRepository[IO]]
    whenF(accountRepository.updateUserAssignedOffice(any, any)) thenReturn anyUserAccount
    val accountService = new AccountService[IO](accountRepository)

    for {
      _ <- accountService.updateAssignedOffice(userId, officeId.some)
    } yield {
      verify(accountRepository, only).updateUserAssignedOffice(eqTo(userId), eqTo(officeId.some))
      success
    }
  }

  test(
    """GIVEN a user ID and office ID
      | WHEN updateUserAssignedOffice is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val officeId = anyOfficeId

    val accountNotFound = AccountNotFound(userId)
    val accountRepository =
      whenF(mock[AccountRepository[IO]].updateUserAssignedOffice(any, any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.updateAssignedOffice(userId, officeId.some).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == accountNotFound)
    }
  }

  test(
    """GIVEN an officeManager to create
      | WHEN createOfficeManager is called
      | THEN a valid officeManager is created via accountRepository
      |""".stripMargin
  ) {
    val officeManagerToCreate = anyCreateOfficeManagerAccount

    val officeManagerId = anyAccountId
    implicit val fuuid: FUUID[IO] = whenF(mock[FUUID[IO]].randomUUID()) thenReturn officeManagerId
    val accountRepository = mock[AccountRepository[IO]]
    val officeManager = officeManagerToCreate.toOfficeManagerAccount(officeManagerId)
    whenF(accountRepository.createOfficeManager(any)) thenReturn officeManager
    val accountService = new AccountService[IO](accountRepository)

    for {
      createdOfficeManager <- accountService.createOfficeManager(officeManagerToCreate)
    } yield {
      verify(accountRepository, only).createOfficeManager(eqTo(officeManager))
      expect(createdOfficeManager == officeManager)
    }
  }

  test(
    """GIVEN an officeManager to create
      | WHEN createOfficeManager is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val officeManagerToCreate = anyCreateOfficeManagerAccount

    val duplicateAccountEmail = DuplicateAccountEmail(officeManagerToCreate.email)
    val accountRepository =
      whenF(mock[AccountRepository[IO]].createOfficeManager(any)) thenFailWith duplicateAccountEmail
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.createOfficeManager(officeManagerToCreate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == duplicateAccountEmail)
    }
  }

  test(
    """GIVEN an officeManager ID
      | WHEN readOfficeManager is called
      | THEN the officeManager is read via accountRepository
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId

    val accountRepository = mock[AccountRepository[IO]]
    val officeManager = anyOfficeManagerAccount.copy(id = officeManagerId)
    whenF(accountRepository.readOfficeManager(any)) thenReturn officeManager
    val accountService = new AccountService[IO](accountRepository)

    for {
      readOfficeManager <- accountService.readOfficeManager(officeManagerId)
    } yield {
      verify(accountRepository, only).readOfficeManager(eqTo(officeManagerId))
      expect(readOfficeManager == officeManager)
    }
  }

  test(
    """GIVEN an officeManager ID
      | WHEN readOfficeManager is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId

    val accountNotFound = AccountNotFound(officeManagerId)
    val accountRepository = whenF(mock[AccountRepository[IO]].readOfficeManager(any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.readOfficeManager(officeManagerId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == accountNotFound)
    }
  }

  test(
    """GIVEN an office manager ID and office ID
      | WHEN updateOfficeManagerManagedOffices is called
      | THEN the managed offices are updated via accountRepository
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val officeIds = List(anyOfficeId)

    val accountRepository = mock[AccountRepository[IO]]
    whenF(accountRepository.updateOfficeManagerManagedOffices(any, any)) thenReturn anyOfficeManagerAccount
    val accountService = new AccountService[IO](accountRepository)

    for {
      _ <- accountService.updateManagedOffices(officeManagerId, officeIds)
    } yield {
      verify(accountRepository, only).updateOfficeManagerManagedOffices(eqTo(officeManagerId), eqTo(officeIds))
      success
    }
  }

  test(
    """GIVEN an office manager ID and office ID
      | WHEN updateOfficeManagerManagedOffices is called
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val officeIds = List(anyOfficeId)

    val accountNotFound = AccountNotFound(officeManagerId)
    val accountRepository =
      whenF(mock[AccountRepository[IO]].updateOfficeManagerManagedOffices(any, any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.updateManagedOffices(officeManagerId, officeIds).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == accountNotFound)
    }
  }

  test(
    """GIVEN a superAdmin to create
      | WHEN createSuperAdmin is called
      | THEN a valid superAdmin is created via accountRepository
      |""".stripMargin
  ) {
    val superAdminToCreate = anyCreateSuperAdminAccount

    val superAdminId = anyAccountId
    implicit val fuuid: FUUID[IO] = whenF(mock[FUUID[IO]].randomUUID()) thenReturn superAdminId
    val accountRepository = mock[AccountRepository[IO]]
    val superAdmin = superAdminToCreate.toSuperAdminAccount(superAdminId)
    whenF(accountRepository.createSuperAdmin(any)) thenReturn superAdmin
    val accountService = new AccountService[IO](accountRepository)

    for {
      createdSuperAdmin <- accountService.createSuperAdmin(superAdminToCreate)
    } yield {
      verify(accountRepository, only).createSuperAdmin(eqTo(superAdmin))
      expect(createdSuperAdmin == superAdmin)
    }
  }

  test(
    """GIVEN a superAdmin to create
      | WHEN createSuperAdmin is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val superAdminToCreate = anyCreateSuperAdminAccount

    val duplicateAccountEmail = DuplicateAccountEmail(superAdminToCreate.email)
    val accountRepository = whenF(mock[AccountRepository[IO]].createSuperAdmin(any)) thenFailWith duplicateAccountEmail
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.createSuperAdmin(superAdminToCreate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == duplicateAccountEmail)
    }
  }

  test(
    """GIVEN a superAdmin ID
      | WHEN readSuperAdmin is called
      | THEN the superAdmin is read via accountRepository
      |""".stripMargin
  ) {
    val superAdminId = anyAccountId

    val accountRepository = mock[AccountRepository[IO]]
    val superAdmin = anySuperAdminAccount.copy(id = superAdminId)
    whenF(accountRepository.readSuperAdmin(any)) thenReturn superAdmin
    val accountService = new AccountService[IO](accountRepository)

    for {
      readSuperAdmin <- accountService.readSuperAdmin(superAdminId)
    } yield {
      verify(accountRepository, only).readSuperAdmin(eqTo(superAdminId))
      expect(readSuperAdmin == superAdmin)
    }
  }

  test(
    """GIVEN a superAdmin ID
      | WHEN readSuperAdmin is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val superAdminId = anyAccountId

    val accountNotFound = AccountNotFound(superAdminId)
    val accountRepository = whenF(mock[AccountRepository[IO]].readSuperAdmin(any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.readSuperAdmin(superAdminId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == accountNotFound)
    }
  }

  test(
    """GIVEN a user ID and an OfficeManager role
      | WHEN updateRoles is called
      | THEN the roles are updated via accountRepository
      |""".stripMargin
  ) {
    val user = anyUserAccount

    val userId = user.id
    val roles = NonEmptyList.one(Role.OfficeManager)

    val accountRepository = mock[AccountRepository[IO]]
    val officeManager = OfficeManagerAccount(
      id = user.id,
      firstName = user.firstName,
      lastName = user.lastName,
      email = user.email,
      managedOfficeIds = Nil
    )
    whenF(accountRepository.updateRoles(any, any)) thenReturn officeManager
    val accountService = new AccountService[IO](accountRepository)

    for {
      updatedAccount <- accountService.updateRoles(userId, roles)
    } yield {
      verify(accountRepository, only).updateRoles(eqTo(userId), eqTo(roles))
      expect(updatedAccount == officeManager)
    }
  }

  test(
    """GIVEN an account ID
      | WHEN updateRoles is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val accountId = anyAccountId

    val accountNotFound = AccountNotFound(accountId)
    val accountRepository = whenF(mock[AccountRepository[IO]].updateRoles(any, any)) thenFailWith accountNotFound
    val accountService = new AccountService[IO](accountRepository)

    for {
      result <- accountService.updateRoles(accountId, NonEmptyList.one(Role.User)).attempt
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

  private lazy val anyUserAccount = UserAccount(
    id = anyAccountId,
    firstName = "Test",
    lastName = "User",
    email = "test.user@localhost",
    assignedOfficeId = Some(anyOfficeId)
  )

  private lazy val anyCreateAccount = CreateUserAccount(
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

  private lazy val anyCreateOfficeManagerAccount = CreateOfficeManagerAccount(
    firstName = "Test",
    lastName = "User",
    email = "test.user@localhost",
    managedOfficeIds = List(anyOfficeId)
  )

  private lazy val anySuperAdminAccount = SuperAdminAccount(
    id = anyAccountId,
    firstName = "Test",
    lastName = "User",
    email = "test.super.admin@localhost"
  )

  private lazy val anyCreateSuperAdminAccount = CreateSuperAdminAccount(
    firstName = "Test",
    lastName = "User",
    email = "test.super.admin@localhost"
  )

  private lazy val anyAccountId = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40")

  private lazy val anyOfficeId = UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed")
}

package io.github.avapl
package adapters.postgres.repository.account

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.IO
import cats.effect.Resource
import cats.instances.all._
import cats.syntax.all._
import domain.model.account.OfficeManagerAccount
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.office.OfficeNotFound
import domain.model.office.Address
import domain.model.office.Office
import java.util.UUID
import skunk.Command
import skunk.Session
import skunk.Void
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresAccountRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(name: TestName)(run: PostgresAccountRepository[IO] => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresAccountRepository = new PostgresAccountRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        run(postgresAccountRepository)
    }

  beforeTest(
    """GIVEN a user account to create
      | WHEN create is called
      | THEN the user should be inserted into Postgres
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount

    for {
      _ <- accountRepository.create(user)
      readUser <- accountRepository.read(user.id)
    } yield expect(readUser == user)
  }

  beforeTest(
    """GIVEN an office manager account to create
      | WHEN createOfficeManager is called
      | THEN the office manager should be inserted into Postgres
      |""".stripMargin
  ) { accountRepository =>
    val officeManager = anyOfficeManagerAccount

    for {
      _ <- accountRepository.create(officeManager)
      readOfficeManager <- accountRepository.read(officeManager.id)
    } yield expect(readOfficeManager == officeManager)
  }

  beforeTest(
    """GIVEN a super admin account to create
      | WHEN create is called
      | THEN the super admin should be inserted into Postgres
      |""".stripMargin
  ) { accountRepository =>
    val superAdmin = anySuperAdminAccount

    for {
      _ <- accountRepository.create(superAdmin)
      readSuperAdmin <- accountRepository.read(superAdmin.id)
    } yield expect(readSuperAdmin == superAdmin)
  }

  beforeTest(
    """GIVEN an account with non-existent office ID assigned
      | WHEN create is called
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { accountRepository =>
    val officeId = UUID.fromString("5980c269-9c59-41f0-aef6-f09834493c41")
    val account = anyUserAccount.copy(assignedOfficeId = Some(officeId))

    for {
      result <- accountRepository.create(account).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(officeId)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """GIVEN an existing account and a new account with the same email
      | WHEN create is called
      | THEN the call should fail with DuplicateAccountEmail
      |""".stripMargin
  ) { accountRepository =>
    val account = anyUserAccount
    val accountWithTheSameEmail = account.copy(id = UUID.fromString("ef3dcba5-b693-4c5e-a5b7-6a9ef384ed67"))

    for {
      _ <- accountRepository.create(account)
      result <- accountRepository.create(accountWithTheSameEmail).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateAccountEmail = DuplicateAccountEmail(account.email)
        expect(throwable == duplicateAccountEmail)
    }
  }

  beforeTest(
    """GIVEN a non-existent account ID
      | WHEN read is called
      | THEN the call should fail with AccountNotFound
      |""".stripMargin
  ) { accountRepository =>
    val accountId = UUID.fromString("850bac54-0251-4cdf-ab8d-ef457fa57622")

    for {
      result <- accountRepository.read(accountId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val accountNotFound = AccountNotFound(accountId)
        expect(throwable == accountNotFound)
    }
  }

  beforeTest(
    """GIVEN an account without assigned office
      | WHEN updateAssignedOffice is called with an office ID
      | THEN the office should be assigned to the account
      |""".stripMargin
  ) { accountRepository =>
    val account = anyUserAccount.copy(assignedOfficeId = None)
    val newOfficeId = officeId1

    for {
      _ <- accountRepository.create(account)
      _ <- accountRepository.updateAssignedOffice(account.id, Some(newOfficeId))
      readAccount <- accountRepository.read(account.id)
    } yield expect(readAccount.assignedOfficeId.contains(newOfficeId))
  }

  beforeTest(
    """GIVEN an account with assigned office
      | WHEN updateAssignedOffice is called with a different office ID
      | THEN the assigned office should be updated
      |""".stripMargin
  ) { accountRepository =>
    val account = anyUserAccount.copy(assignedOfficeId = Some(officeId1))
    val newOfficeId = officeId2

    for {
      _ <- accountRepository.create(account)
      _ <- accountRepository.updateAssignedOffice(account.id, Some(newOfficeId))
      readAccount <- accountRepository.read(account.id)
    } yield expect(readAccount.assignedOfficeId.contains(newOfficeId))
  }

  beforeTest(
    """GIVEN an account with assigned office
      | WHEN updateAssignedOffice is called with None
      | THEN the assigned office should be removed
      |""".stripMargin
  ) { accountRepository =>
    val account = anyUserAccount.copy(assignedOfficeId = Some(officeId1))

    for {
      _ <- accountRepository.create(account)
      _ <- accountRepository.updateAssignedOffice(account.id, None)
      readAccount <- accountRepository.read(account.id)
    } yield expect(readAccount.assignedOfficeId.isEmpty)
  }

  beforeTest(
    """WHEN updateAssignedOffice is called with non-existent account ID
      |THEN the call should fail with AccountNotFound
      |""".stripMargin
  ) { accountRepository =>
    val accountId = UUID.fromString("850bac54-0251-4cdf-ab8d-ef457fa57622")

    for {
      result <- accountRepository.updateAssignedOffice(accountId, Some(officeId1)).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val accountNotFound = AccountNotFound(accountId)
        expect(throwable == accountNotFound)
    }
  }

  beforeTest(
    """GIVEN a user account
      | WHEN updateRole is called with OfficeManager role
      | THEN the user should be promoted to an office manager
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount

    for {
      _ <- accountRepository.create(user)
      officeManager <- accountRepository.updateRole(user.id, OfficeManager)
    } yield {
      val expectedOfficeManager = OfficeManagerAccount(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        assignedOfficeId = user.assignedOfficeId
      )
      expect(officeManager == expectedOfficeManager)
    }
  }

  beforeTest(
    """GIVEN an office manager account
      | WHEN updateRole is called with User role
      | THEN the office manager should be demoted to a user
      |""".stripMargin
  ) { accountRepository =>
    val officeManager = anyOfficeManagerAccount

    for {
      _ <- accountRepository.create(officeManager)
      user <- accountRepository.updateRole(officeManager.id, User)
    } yield {
      val expectedUser = UserAccount(
        id = officeManager.id,
        firstName = officeManager.firstName,
        lastName = officeManager.lastName,
        email = officeManager.email,
        assignedOfficeId = officeManager.assignedOfficeId
      )
      expect(user == expectedUser)
    }
  }

  beforeTest(
    """GIVEN an office manager account
      | WHEN updateRole is called with User role and then again with OfficeManager role
      | THEN the resulting account should have managed offices removed because of being demoted to a user previously
      |""".stripMargin
  ) { accountRepository =>

    val officeManager = anyOfficeManagerAccount

    for {
      _ <- accountRepository.create(officeManager)
      user <- accountRepository.updateRole(officeManager.id, User)
      officeManager <- accountRepository.updateRole(user.id, OfficeManager)
    } yield {
      val expectedOfficeManager = OfficeManagerAccount(
        id = officeManager.id,
        firstName = officeManager.firstName,
        lastName = officeManager.lastName,
        email = officeManager.email,
        assignedOfficeId = officeManager.assignedOfficeId,
        managedOfficeIds = Nil
      )
      expect(officeManager == expectedOfficeManager)
    }
  }

  beforeTest(
    """GIVEN a user account
      | WHEN updateRole is called with SuperAdmin role
      | THEN the user should be promoted to a super admin
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount

    for {
      _ <- accountRepository.create(user)
      superAdmin <- accountRepository.updateRole(user.id, SuperAdmin)
    } yield {
      val expectedSuperAdmin = SuperAdminAccount(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        assignedOfficeId = user.assignedOfficeId
      )
      expect(superAdmin == expectedSuperAdmin)
    }
  }

  beforeTest(
    """GIVEN a super admin account
      | WHEN updateRole is called OfficeManager role
      | THEN the super admin should be demoted to an office manager
      |""".stripMargin
  ) { accountRepository =>
    val superAdmin = anySuperAdminAccount

    for {
      _ <- accountRepository.create(superAdmin)
      officeManager <- accountRepository.updateRole(superAdmin.id, OfficeManager)
    } yield {
      val expectedOfficeManager = OfficeManagerAccount(
        id = superAdmin.id,
        firstName = superAdmin.firstName,
        lastName = superAdmin.lastName,
        email = superAdmin.email,
        assignedOfficeId = superAdmin.assignedOfficeId,
        managedOfficeIds = superAdmin.managedOfficeIds
      )
      expect(officeManager == expectedOfficeManager)
    }
  }

  beforeTest(
    """GIVEN a user account
      | WHEN updateRole is called with User role
      | THEN the call should not fail (no-op)
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount

    for {
      _ <- accountRepository.create(user)
      updatedUser <- accountRepository.updateRole(user.id, User)
    } yield expect(updatedUser == user)
  }

  beforeTest(
    """GIVEN a non-existent account ID
      | WHEN updateRole is called
      | THEN the call should fail with AccountNotFound
      |""".stripMargin
  ) { accountRepository =>
    val accountId = UUID.fromString("1ee19aea-51ec-4d51-93d5-f2d3da1c40ac")

    for {
      result <- accountRepository.updateRole(accountId, User).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val accountNotFound = AccountNotFound(accountId)
        expect(throwable == accountNotFound)
    }
  }

  beforeTest(
    """GIVEN an existing account
      | WHEN archive is called on its ID
      | THEN the account should be archived
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount

    for {
      _ <- accountRepository.create(user)
      _ <- accountRepository.archive(user.id)
      user <- accountRepository.read(user.id)
    } yield expect(user.isArchived)
  }

  beforeTest(
    """WHEN archive is called on nonexistent account ID
      |THEN the call should not fail (no-op)
      |""".stripMargin
  ) { accountRepository =>
    val accountId = UUID.fromString("0b2285ed-c806-44c8-9f9c-e041dec61178")

    for {
      _ <- accountRepository.archive(accountId)
    } yield success
  }

  beforeTest(
    """GIVEN a existing account ID
      | WHEN readAccountEmail is called
      | THEN the account email should be returned
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount

    for {
      _ <- accountRepository.create(user)
      email <- accountRepository.readAccountEmail(user.id)
    } yield expect(email == user.email)
  }

  beforeTest(
    """GIVEN a non-existent account ID
      | WHEN readAccountEmail is called
      | THEN the call should fail with AccountNotFound
      |""".stripMargin
  ) { accountRepository =>
    val accountId = UUID.fromString("5058343a-e7cb-469b-8c6f-e4c3de86384d")

    for {
      result <- accountRepository.readAccountEmail(accountId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val accountNotFound = AccountNotFound(accountId)
        expect(throwable == accountNotFound)
    }
  }

  private def truncateTables(session: Resource[IO, Session[IO]]) =
    truncateAccountTable(session) >>
      truncateOfficeTable(session)

  private def truncateAccountTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE account CASCADE
      """.command
    session.use(_.execute(sql))
  }

  private def truncateOfficeTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE office CASCADE
      """.command
    session.use(_.execute(sql))
  }

  private def insertOffices(session: Resource[IO, Session[IO]]) = {
    val officeRepository = new PostgresOfficeRepository[IO](session)
    val office1 = anyOffice(officeId1, "office1")
    val office2 = anyOffice(officeId2, "office2")
    List(office1, office2).parTraverse_(officeRepository.create)
  }

  private def anyOffice(officeId: UUID, name: String) = Office(
    id = officeId,
    name = name,
    notes = List("Test", "Notes"),
    address = Address(
      addressLine1 = "Test Street",
      addressLine2 = "Building 42",
      postalCode = "12-345",
      city = "Wroclaw",
      country = "Poland"
    )
  )

  private lazy val officeId1 = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")
  private lazy val officeId2 = UUID.fromString("c1e29bfd-5a8a-468f-ba27-4673c42fec04")

  private lazy val anyUserAccount = UserAccount(
    id = UUID.fromString("8b7a9bdd-b729-4427-83c3-6eaee3c97171"),
    firstName = "Test",
    lastName = "User",
    email = "test.user@postgres.localhost",
    assignedOfficeId = Some(officeId1)
  )

  private lazy val anyOfficeManagerAccount = OfficeManagerAccount(
    id = UUID.fromString("fa3c2fb4-73a1-4c2a-be69-f995d2fbbb73"),
    firstName = "Test",
    lastName = "OfficeManager",
    email = "test.office.manager@postgres.localhost",
    assignedOfficeId = Some(officeId1),
    managedOfficeIds = List(officeId1, officeId2)
  )

  private lazy val anySuperAdminAccount = SuperAdminAccount(
    id = UUID.fromString("78aef5b8-e7e7-4880-a4d7-3535eaa00c6a"),
    firstName = "Test",
    lastName = "SuperAdmin",
    email = "test.super.admin@postgres.localhost",
    assignedOfficeId = Some(officeId1),
    managedOfficeIds = List(officeId1, officeId2)
  )
}

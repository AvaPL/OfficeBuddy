package io.github.avapl
package adapters.postgres.repository.account

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.IO
import cats.effect.Resource
import cats.instances.all._
import cats.syntax.all._
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
      | WHEN createUser is called
      | THEN the user should be inserted into Postgres
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount

    for {
      _ <- accountRepository.createUser(user)
      readUser <- accountRepository.readUser(user.id)
    } yield expect(readUser == user)
  }

  beforeTest(
    """GIVEN a user account with non-existent office ID assigned
      | WHEN createUser is called
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { accountRepository =>
    val officeId = UUID.fromString("5980c269-9c59-41f0-aef6-f09834493c41")
    val user = anyUserAccount.copy(assignedOfficeId = Some(officeId))

    for {
      result <- accountRepository.createUser(user).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(officeId)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """GIVEN an existing user account and a new user account with the same email
      | WHEN createUser is called
      | THEN the call should fail with DuplicateAccountEmail
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount
    val userWithTheSameEmail = user.copy(id = UUID.fromString("ef3dcba5-b693-4c5e-a5b7-6a9ef384ed67"))

    for {
      _ <- accountRepository.createUser(user)
      result <- accountRepository.createUser(userWithTheSameEmail).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateAccountEmail = DuplicateAccountEmail(user.email)
        expect(throwable == duplicateAccountEmail)
    }
  }

  beforeTest(
    """GIVEN a non-existent user ID
      | WHEN readUser is called
      | THEN the call should fail with AccountNotFound
      |""".stripMargin
  ) { accountRepository =>
    val userId = UUID.fromString("850bac54-0251-4cdf-ab8d-ef457fa57622")

    for {
      result <- accountRepository.readUser(userId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val accountNotFound = AccountNotFound(userId)
        expect(throwable == accountNotFound)
    }
  }

  beforeTest(
    """GIVEN a user without assigned office
      | WHEN updateUserAssignedOffice is called with an office ID
      | THEN the office should be assigned to the user
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount.copy(assignedOfficeId = None)
    val newOfficeId = officeId1

    for {
      _ <- accountRepository.createUser(user)
      _ <- accountRepository.updateUserAssignedOffice(user.id, Some(newOfficeId))
      readUser <- accountRepository.readUser(user.id)
    } yield expect(readUser.assignedOfficeId.contains(newOfficeId))
  }

  beforeTest(
    """GIVEN a user with assigned office
      | WHEN updateUserAssignedOffice is called with a different office ID
      | THEN the assigned office should be updated
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount.copy(assignedOfficeId = Some(officeId1))
    val newOfficeId = officeId2

    for {
      _ <- accountRepository.createUser(user)
      _ <- accountRepository.updateUserAssignedOffice(user.id, Some(newOfficeId))
      readUser <- accountRepository.readUser(user.id)
    } yield expect(readUser.assignedOfficeId.contains(newOfficeId))
  }

  beforeTest(
    """GIVEN a user with assigned office
      | WHEN updateUserAssignedOffice is called with None
      | THEN the assigned office should be removed
      |""".stripMargin
  ) { accountRepository =>
    val user = anyUserAccount.copy(assignedOfficeId = Some(officeId1))

    for {
      _ <- accountRepository.createUser(user)
      _ <- accountRepository.updateUserAssignedOffice(user.id, None)
      readUser <- accountRepository.readUser(user.id)
    } yield expect(readUser.assignedOfficeId.isEmpty)
  }

  beforeTest(
    """WHEN updateUserAssignedOffice is called with non-existent user ID
      |THEN the call should fail with AccountNotFound
      |""".stripMargin
  ) { accountRepository =>
    val userId = UUID.fromString("850bac54-0251-4cdf-ab8d-ef457fa57622")

    for {
      result <- accountRepository.updateUserAssignedOffice(userId, Some(officeId1)).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val accountNotFound = AccountNotFound(userId)
        expect(throwable == accountNotFound)
    }
  }

  beforeTest(
    """GIVEN an office manager account to create
      | WHEN createOfficeManager is called
      | THEN the office manager should be inserted into Postgres
      |""".stripMargin
  ) { accountRepository =>
    val officeManager = anyOfficeManagerAccount

    for {
      _ <- accountRepository.createOfficeManager(officeManager)
      readOfficeManager <- accountRepository.readOfficeManager(officeManager.id)
    } yield expect(readOfficeManager == officeManager)
  }

  beforeTest(
    """GIVEN an existing office manager account and a new office manager account with the same email
      | WHEN createOfficeManager is called
      | THEN the call should fail with DuplicateAccountEmail
      |""".stripMargin
  ) { accountRepository =>
    val officeManager = anyOfficeManagerAccount
    val officeManagerWithTheSameEmail = officeManager.copy(id = UUID.fromString("ef3dcba5-b693-4c5e-a5b7-6a9ef384ed67"))

    for {
      _ <- accountRepository.createOfficeManager(officeManager)
      result <- accountRepository.createOfficeManager(officeManagerWithTheSameEmail).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateAccountEmail = DuplicateAccountEmail(officeManager.email)
        expect(throwable == duplicateAccountEmail)
    }
  }

  beforeTest(
    """GIVEN a non-existent office manager ID
      | WHEN readOfficeManager is called
      | THEN the call should fail with AccountNotFound
      |""".stripMargin
  ) { accountRepository =>
    val officeManagerId = UUID.fromString("850bac54-0251-4cdf-ab8d-ef457fa57622")

    for {
      result <- accountRepository.readOfficeManager(officeManagerId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val accountNotFound = AccountNotFound(officeManagerId)
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

  private lazy val anyUserAccount = PostgresUserAccount(
    id = UUID.fromString("8b7a9bdd-b729-4427-83c3-6eaee3c97171"),
    email = "test.user@postgres.localhost",
    isArchived = false,
    assignedOfficeId = Some(officeId1)
  )

  private lazy val anyOfficeManagerAccount = PostgresOfficeManagerAccount(
    id = UUID.fromString("8b7a9bdd-b729-4427-83c3-6eaee3c97171"),
    email = "test.office.manager@postgres.localhost",
    isArchived = false
  )
}

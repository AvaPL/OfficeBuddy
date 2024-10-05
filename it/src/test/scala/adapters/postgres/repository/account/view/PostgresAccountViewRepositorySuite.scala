package io.github.avapl
package adapters.postgres.repository.account.view

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.account.PostgresAccountRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import domain.model.account.OfficeManagerAccount
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.User
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import domain.model.account.view.OfficeView
import domain.model.office.Address
import domain.model.office.Office
import java.util.UUID
import skunk._
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

// TODO: Fix integration tests
object PostgresAccountViewRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(
    name: TestName
  )(run: (PostgresAccountRepository[IO], PostgresAccountViewRepository[IO]) => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresAccountRepository = new PostgresAccountRepository[IO](session)
      lazy val postgresAccountViewRepository = new PostgresAccountViewRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        run(postgresAccountRepository, postgresAccountViewRepository)
    }

  beforeTest(
    """GIVEN 1 account in the database with assigned office and managed offices
      | WHEN listAccounts is called
      | THEN the result view contains the account with assigned office and managed offices
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val account = anyOfficeManagerAccount.copy(
      assignedOfficeId = Some(officeId1),
      managedOfficeIds = List(officeId1, officeId2)
    )

    for {
      _ <- accountRepository.create(account)
      accountListView <- accountViewRepository.listAccounts(limit = 10, offset = 0)
    } yield expect.all(
      accountListView.accounts.map(_.id) == List(account.id),
      accountListView.accounts.head.assignedOffice.contains(OfficeView(officeId1, officeName1)),
      accountListView.accounts.head.managedOffices == List(
        OfficeView(officeId1, officeName1),
        OfficeView(officeId2, officeName2)
      )
    )
  }

  beforeTest(
    """
      |GIVEN 3 accounts in the database
      | WHEN listAccounts is called with limit 2 and offsets 0 and 2
      | THEN return two pages of results
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val account1 = anyUserAccount.copy(firstName = "Adam", lastName = "Doe")
    val account2 = anyOfficeManagerAccount.copy(firstName = "Jane", lastName = "Doe")
    val account3 = anySuperAdminAccount.copy(firstName = "John", lastName = "Smith")

    for {
      _ <- accountRepository.create(account1)
      _ <- accountRepository.create(account2)
      _ <- accountRepository.create(account3)
      accountListView1 <- accountViewRepository.listAccounts(limit = 2, offset = 0)
      accountListView2 <- accountViewRepository.listAccounts(limit = 2, offset = 2)
    } yield expect.all(
      accountListView1.accounts.map(_.id) == List(account1.id, account2.id),
      accountListView1.pagination.hasMoreResults,
      accountListView2.accounts.map(_.id) == List(account3.id),
      !accountListView2.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 1 account in the database
      | WHEN listAccounts is called with limit 1 and offset 1
      | THEN return an empty list of results
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val account = anyUserAccount

    for {
      _ <- accountRepository.create(account)
      accountListView <- accountViewRepository.listAccounts(limit = 1, offset = 1)
    } yield expect.all(
      accountListView.accounts.isEmpty,
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 1 archived account in the database
      | WHEN listAccounts is called with limit 1 and offset 0
      | THEN return an empty list of results
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val account = anyUserAccount.copy(isArchived = true)

    for {
      _ <- accountRepository.create(account)
      accountListView <- accountViewRepository.listAccounts(limit = 1, offset = 0)
    } yield expect.all(
      accountListView.accounts.isEmpty,
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 3 accounts in the database
      | WHEN listAccounts is called with textSearchQuery matching first name of 2 accounts (case-insensitive)
      | THEN return matching accounts
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val notMatchingAccount = anyUserAccount.copy(firstName = "Adam", lastName = "Doe")
    val matchingAccount1 = anyOfficeManagerAccount.copy(firstName = "Jane", lastName = "Doe")
    val matchingAccount2 = anySuperAdminAccount.copy(firstName = "Jane", lastName = "Smith")

    for {
      _ <- accountRepository.create(notMatchingAccount)
      _ <- accountRepository.create(matchingAccount1)
      _ <- accountRepository.create(matchingAccount2)
      accountListView <- accountViewRepository.listAccounts(textSearchQuery = Some("jane"), limit = 10, offset = 0)
    } yield expect.all(
      accountListView.accounts.map(_.id) == List(matchingAccount1.id, matchingAccount2.id),
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 3 accounts in the database
      | WHEN listAccounts is called with textSearchQuery matching last name of 2 accounts (case-insensitive)
      | THEN return matching accounts
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val matchingAccount1 = anyUserAccount.copy(firstName = "Adam", lastName = "Doe")
    val matchingAccount2 = anyOfficeManagerAccount.copy(firstName = "Jane", lastName = "Doe")
    val notMatchingAccount = anySuperAdminAccount.copy(firstName = "John", lastName = "Smith")

    for {
      _ <- accountRepository.create(matchingAccount1)
      _ <- accountRepository.create(matchingAccount2)
      _ <- accountRepository.create(notMatchingAccount)
      accountListView <- accountViewRepository.listAccounts(textSearchQuery = Some("doe"), limit = 10, offset = 0)
    } yield expect.all(
      accountListView.accounts.map(_.id) == List(matchingAccount1.id, matchingAccount2.id),
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 3 accounts in the database
      | WHEN listAccounts is called with textSearchQuery matching email of 2 accounts (case-insensitive)
      | THEN return matching accounts
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val matchingAccount1 = anyUserAccount.copy(
      firstName = "Adam",
      lastName = "Doe",
      email = "account1@foo.com"
    )
    val matchingAccount2 = anyOfficeManagerAccount.copy(
      firstName = "Jane",
      lastName = "Doe",
      email = "account2@foo.com"
    )
    val notMatchingAccount = anySuperAdminAccount.copy(
      firstName = "John",
      lastName = "Smith",
      email = "account@bar.com"
    )

    for {
      _ <- accountRepository.create(matchingAccount1)
      _ <- accountRepository.create(matchingAccount2)
      _ <- accountRepository.create(notMatchingAccount)
      accountListView <- accountViewRepository.listAccounts(textSearchQuery = Some("FOO.COM"), limit = 10, offset = 0)
    } yield expect.all(
      accountListView.accounts.map(_.id) == List(matchingAccount1.id, matchingAccount2.id),
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 3 accounts in the database
      | WHEN listAccounts is called with textSearchQuery matching first name and part of last name of 1 account
      | THEN return the matching account
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val matchingAccount = anyUserAccount.copy(firstName = "Jane", lastName = "Doe")
    val notMatchingAccount1 = anyOfficeManagerAccount.copy(firstName = "Jane", lastName = "Smith")
    val notMatchingAccount2 = anySuperAdminAccount.copy(firstName = "Alice", lastName = "Smith")

    for {
      _ <- accountRepository.create(matchingAccount)
      _ <- accountRepository.create(notMatchingAccount1)
      _ <- accountRepository.create(notMatchingAccount2)
      accountListView <- accountViewRepository.listAccounts(textSearchQuery = Some("jane d"), limit = 10, offset = 0)
    } yield expect.all(
      accountListView.accounts.map(_.id) == List(matchingAccount.id),
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 3 accounts in the database
      | WHEN listAccounts is called with filters matching 2 accounts, limit 1 and offset 1
      | THEN return the matching account and no more results
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val matchingAccount1 = anyUserAccount.copy(firstName = "Adam", lastName = "Doe")
    val matchingAccount2 = anyOfficeManagerAccount.copy(firstName = "Jane", lastName = "Doe")
    val notMatchingAccount = anySuperAdminAccount.copy(firstName = "John", lastName = "Smith")

    for {
      _ <- accountRepository.create(matchingAccount1)
      _ <- accountRepository.create(matchingAccount2)
      _ <- accountRepository.create(notMatchingAccount)
      accountListView <- accountViewRepository.listAccounts(textSearchQuery = Some("doe"), limit = 1, offset = 1)
    } yield expect.all(
      accountListView.accounts.map(_.id) == List(matchingAccount2.id),
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 3 accounts in the database
      | WHEN listAccounts is called with officeId matching assigned office of 1 account and managed office of 1 account
      | THEN return matching accounts
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val matchingAccount1 = anyUserAccount.copy(
      firstName = "Adam",
      lastName = "Doe",
      assignedOfficeId = Some(officeId1)
    )
    val matchingAccount2 = anyOfficeManagerAccount.copy(
      firstName = "Jane",
      lastName = "Doe",
      assignedOfficeId = Some(officeId2),
      managedOfficeIds = List(officeId1, officeId2)
    )
    val notMatchingAccount = anySuperAdminAccount.copy(
      firstName = "John",
      lastName = "Smith",
      assignedOfficeId = None,
      managedOfficeIds = Nil
    )

    for {
      _ <- accountRepository.create(matchingAccount1)
      _ <- accountRepository.create(matchingAccount2)
      _ <- accountRepository.create(notMatchingAccount)
      accountListView <- accountViewRepository.listAccounts(officeId = Some(officeId1), limit = 10, offset = 0)
    } yield expect.all(
      accountListView.accounts.map(_.id) == List(matchingAccount1.id, matchingAccount2.id),
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 3 accounts in the database
      | WHEN listAccounts is called with roles matching 2 accounts
      | THEN return matching accounts
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val matchingAccount1 = anyUserAccount.copy(firstName = "Adam", lastName = "Doe")
    val matchingAccount2 = anyOfficeManagerAccount.copy(firstName = "Jane", lastName = "Doe")
    val notMatchingAccount = anySuperAdminAccount.copy(firstName = "John", lastName = "Smith")

    for {
      _ <- accountRepository.create(matchingAccount1)
      _ <- accountRepository.create(matchingAccount2)
      _ <- accountRepository.create(notMatchingAccount)
      accountListView <- accountViewRepository.listAccounts(
        roles = Some(List(User, OfficeManager)),
        limit = 10,
        offset = 0
      )
    } yield expect.all(
      accountListView.accounts.map(_.id) == List(matchingAccount1.id, matchingAccount2.id),
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 4 accounts in the database
      | WHEN listAccounts is called with textSearchQuery, officeId, and roles matching 1 account
      | THEN return the matching account
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val matchingAccount = anyUserAccount.copy(
      id = anyAccountId1,
      firstName = "Adam",
      lastName = "Doe",
      email = "ad@foo.com",
      assignedOfficeId = Some(officeId1)
    )
    val notMatchingAccount1 = anyUserAccount.copy(
      id = anyAccountId2,
      firstName = "John", // not matching textSearchQuery
      lastName = "Doe",
      email = "john.doe@example.com",
      assignedOfficeId = Some(officeId1)
    )
    val notMatchingAccount2 = anyUserAccount.copy(
      id = anyAccountId3,
      firstName = "Adam",
      lastName = "Doe",
      email = "ad@bar.com",
      assignedOfficeId = Some(officeId2) // not matching officeId
    )
    val notMatchingAccount3 = anyOfficeManagerAccount.copy( // not matching role
      id = anyAccountId4,
      firstName = "Adam",
      lastName = "Doe",
      email = "ad@baz.com",
      assignedOfficeId = Some(officeId1),
      managedOfficeIds = List(officeId1)
    )

    for {
      _ <- accountRepository.create(matchingAccount)
      _ <- accountRepository.create(notMatchingAccount1)
      _ <- accountRepository.create(notMatchingAccount3)
      _ <- accountRepository.create(notMatchingAccount2)
      accountListView <- accountViewRepository.listAccounts(
        textSearchQuery = Some("adam"),
        officeId = Some(officeId1),
        roles = Some(List(User)),
        limit = 10,
        offset = 0
      )
    } yield expect.all(
      accountListView.accounts.map(_.id) == List(matchingAccount.id),
      !accountListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 accounts in the database
      | WHEN listAccounts is called with textSearchQuery, officeId and roles not matching any account
      | THEN return an empty list of results
      |""".stripMargin
  ) { (accountRepository, accountViewRepository) =>
    val notMatchingAccount1 = anyUserAccount.copy(
      id = anyAccountId2,
      firstName = "John", // not matching textSearchQuery
      lastName = "Doe",
      email = "john.doe@example.com",
      assignedOfficeId = Some(officeId1)
    )
    val notMatchingAccount2 = anyUserAccount.copy(
      id = anyAccountId3,
      firstName = "Adam",
      lastName = "Doe",
      email = "ad@bar.com",
      assignedOfficeId = Some(officeId2) // not matching officeId
    )
    val notMatchingAccount3 = anyOfficeManagerAccount.copy( // not matching role
      id = anyAccountId4,
      firstName = "Adam",
      lastName = "Doe",
      email = "ad@baz.com",
      assignedOfficeId = Some(officeId1),
      managedOfficeIds = List(officeId1)
    )

    for {
      _ <- accountRepository.create(notMatchingAccount1)
      _ <- accountRepository.create(notMatchingAccount3)
      _ <- accountRepository.create(notMatchingAccount2)
      accountListView <- accountViewRepository.listAccounts(
        textSearchQuery = Some("adam"),
        officeId = Some(officeId1),
        roles = Some(List(User)),
        limit = 10,
        offset = 0
      )
    } yield expect.all(
      accountListView.accounts.isEmpty,
      !accountListView.pagination.hasMoreResults
    )
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
    val office1 = anyOffice(officeId1, officeName1)
    val office2 = anyOffice(officeId2, officeName2)
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
  private lazy val officeName1 = "office1"
  private lazy val officeId2 = UUID.fromString("c1e29bfd-5a8a-468f-ba27-4673c42fec04")
  private lazy val officeName2 = "office2"

  private lazy val anyUserAccount = UserAccount(
    id = anyAccountId1,
    firstName = "Test",
    lastName = "User",
    email = "test.user@postgres.localhost",
    assignedOfficeId = Some(officeId1)
  )

  private lazy val anyOfficeManagerAccount = OfficeManagerAccount(
    id = anyAccountId2,
    firstName = "Test",
    lastName = "OfficeManager",
    email = "test.office.manager@postgres.localhost",
    assignedOfficeId = Some(officeId1),
    managedOfficeIds = List(officeId1, officeId2)
  )

  private lazy val anySuperAdminAccount = SuperAdminAccount(
    id = anyAccountId4,
    firstName = "Test",
    lastName = "SuperAdmin",
    email = "test.super.admin@postgres.localhost",
    assignedOfficeId = Some(officeId1),
    managedOfficeIds = List(officeId1, officeId2)
  )

  private lazy val anyAccountId1 = UUID.fromString("8896d352-b175-436f-9bb4-7f3b24068c8b")
  private lazy val anyAccountId2 = UUID.fromString("e5b77139-b2c6-43d5-b4f1-d07887cc4cb9")
  private lazy val anyAccountId3 = UUID.fromString("bfdca928-8298-4da9-8cf9-7ad208a24f6c")
  private lazy val anyAccountId4 = UUID.fromString("8180e374-14ef-434e-af73-c5693d568b51")
}

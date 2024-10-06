package io.github.avapl
package adapters.postgres.repository.office.view

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.account.PostgresAccountRepository
import adapters.postgres.repository.desk.PostgresDeskRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.IO
import cats.effect.Resource
import domain.model.account.OfficeManagerAccount
import domain.model.account.UserAccount
import domain.model.desk.Desk
import domain.model.office.Address
import domain.model.office.Office
import java.time.LocalDateTime
import java.util.UUID
import skunk._
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresOfficeViewRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(
    name: TestName
  )(
    run: (
      PostgresOfficeRepository[IO],
      PostgresOfficeViewRepository[IO],
      PostgresAccountRepository[IO],
      PostgresDeskRepository[IO]
    ) => IO[Expectations]
  ): Unit =
    test(name) { session =>
      lazy val postgresOfficeRepository = new PostgresOfficeRepository[IO](session)
      lazy val postgresOfficeViewRepository = new PostgresOfficeViewRepository[IO](session)
      lazy val postgresAccountRepository = new PostgresAccountRepository[IO](session)
      lazy val postgresDeskRepository = new PostgresDeskRepository[IO](session)
      truncateTables(session) >> run(
        postgresOfficeRepository,
        postgresOfficeViewRepository,
        postgresAccountRepository,
        postgresDeskRepository
      )
    }

  beforeTest(
    """GIVEN 2 offices in the database having no office managers
      | WHEN listOffices is called
      | THEN return an office view with no office managers
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, _, _) =>
    val office1 = anyOffice.copy(id = anyOfficeId1, name = "office1")
    val office2 = anyOffice.copy(id = anyOfficeId2, name = "office2")

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 10, offset = 0)
    } yield expect.all(
      officeListView.offices.forall(_.officeManagers.isEmpty),
      officeListView.offices.size == 2,
      !officeListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 2 offices in the database having office managers
      | WHEN listOffices is called
      | THEN return an office view with its office managers
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, accountRepository, _) =>
    val office1 = anyOffice.copy(id = anyOfficeId1, name = "office1")
    val office2 = anyOffice.copy(id = anyOfficeId2, name = "office2")
    val office1Manager = anyOfficeManager(id = anyAccountId1, "officeManager1")
    val office2Manager = anyOfficeManager(id = anyAccountId2, "officeManager2")
    val bothOfficesManager = anyOfficeManager(id = anyAccountId3, "officeManager3")
    val office1ManagerIds = List(office1Manager.id, bothOfficesManager.id)
    val office2ManagerIds = List(office2Manager.id, bothOfficesManager.id)

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      _ <- accountRepository.create(office1Manager)
      _ <- accountRepository.create(office2Manager)
      _ <- accountRepository.create(bothOfficesManager)
      _ <- officeRepository.updateOfficeManagers(office1.id, officeManagerIds = office1ManagerIds)
      _ <- officeRepository.updateOfficeManagers(office2.id, officeManagerIds = office2ManagerIds)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 10, offset = 0)
    } yield expect.all(
      officeListView.offices.find(_.id == office1.id).exists(_.officeManagers.map(_.id) == office1ManagerIds),
      officeListView.offices.find(_.id == office2.id).exists(_.officeManagers.map(_.id) == office2ManagerIds),
      officeListView.offices.size == 2,
      !officeListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 office in the database with archived office manager
      | WHEN listOffices is called
      | THEN return an office view with no office managers
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, accountRepository, _) =>
    val office = anyOffice
    val archivedOfficeManager = anyOfficeManager(id = anyAccountId1, "archivedOfficeManager")

    for {
      _ <- officeRepository.create(office)
      _ <- accountRepository.create(archivedOfficeManager)
      _ <- officeRepository.updateOfficeManagers(office.id, officeManagerIds = List(archivedOfficeManager.id))
      _ <- accountRepository.archive(archivedOfficeManager.id)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 10, offset = 0)
    } yield expect.all(
      officeListView.offices.forall(_.officeManagers.isEmpty),
      officeListView.offices.size == 1,
      !officeListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 2 offices in the database having no assigned accounts
      | WHEN listOffices is called
      | THEN return an office view with assigned accounts count equal to 0
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, _, _) =>
    val office1 = anyOffice.copy(id = anyOfficeId1, name = "office1")
    val office2 = anyOffice.copy(id = anyOfficeId2, name = "office2")

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 10, offset = 0)
    } yield expect.all(
      officeListView.offices.forall(_.assignedAccountsCount == 0),
      officeListView.offices.size == 2,
      !officeListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 2 offices in the database having assigned accounts
      | WHEN listOffices is called
      | THEN return an office view with their assigned accounts count
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, accountRepository, _) =>
    val office1 = anyOffice.copy(id = anyOfficeId1, name = "office1")
    val office2 = anyOffice.copy(id = anyOfficeId2, name = "office2")
    val office1User =
      anyUser(id = anyAccountId1, "user1", assignedOfficeId = Some(office1.id))
    val office1OfficeManager =
      anyOfficeManager(id = anyAccountId2, "officeManager1", assignedOfficeId = Some(office1.id))
    val office2OfficeManager =
      anyOfficeManager(id = anyAccountId3, "officeManager2", assignedOfficeId = Some(office2.id))

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      _ <- accountRepository.create(office1User)
      _ <- accountRepository.create(office1OfficeManager)
      _ <- accountRepository.create(office2OfficeManager)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 10, offset = 0)
    } yield expect.all(
      officeListView.offices.find(_.id == office1.id).exists(_.assignedAccountsCount == 2),
      officeListView.offices.find(_.id == office2.id).exists(_.assignedAccountsCount == 1),
      officeListView.offices.size == 2,
      !officeListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 office in the database with archived assigned account
      | WHEN listOffices is called
      | THEN return an office view with assigned accounts count equal to 0
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, accountRepository, _) =>
    val office = anyOffice
    val archivedUser = anyUser(id = anyAccountId1, "archivedUser", assignedOfficeId = Some(office.id))

    for {
      _ <- officeRepository.create(office)
      _ <- accountRepository.create(archivedUser)
      _ <- accountRepository.archive(archivedUser.id)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 10, offset = 0)
    } yield expect.all(
      officeListView.offices.forall(_.assignedAccountsCount == 0),
      officeListView.offices.size == 1,
      !officeListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 2 offices in the database having no desks
      | WHEN listOffices is called
      | THEN return an office view with desks count equal to 0
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, _, _) =>
    val office1 = anyOffice.copy(id = anyOfficeId1, name = "office1")
    val office2 = anyOffice.copy(id = anyOfficeId2, name = "office2")

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 10, offset = 0)
    } yield expect.all(
      officeListView.offices.forall(_.desksCount == 0),
      officeListView.offices.size == 2,
      !officeListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 2 offices in the database having desks
        | WHEN listOffices is called
        | THEN return an office view with their desks count
        |""".stripMargin
  ) { (officeRepository, officeViewRepository, _, deskRepository) =>
    val office1 = anyOffice.copy(id = anyOfficeId1, name = "office1")
    val office2 = anyOffice.copy(id = anyOfficeId2, name = "office2")
    val office1Desk1 = anyDesk(anyDeskId1, "desk 1.1", office1.id)
    val office1Desk2 = anyDesk(anyDeskId2, "desk 1.2", office1.id)
    val office2Desk1 = anyDesk(anyDeskId3, "desk 2.1", office2.id)

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      _ <- deskRepository.create(office1Desk1)
      _ <- deskRepository.create(office1Desk2)
      _ <- deskRepository.create(office2Desk1)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 10, offset = 0)
    } yield expect.all(
      officeListView.offices.find(_.id == office1.id).exists(_.desksCount == 2),
      officeListView.offices.find(_.id == office2.id).exists(_.desksCount == 1),
      officeListView.offices.size == 2,
      !officeListView.pagination.hasMoreResults
    )
  }

  // TODO: Test reservations count

  beforeTest(
    """
      |GIVEN 3 offices in the database
      | WHEN listOffices is called with limit 2 and offsets 0 and 2
      | THEN return two pages of results
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, _, _) =>
    val office1 = anyOffice.copy(id = anyOfficeId1, name = "office1")
    val office2 = anyOffice.copy(id = anyOfficeId2, name = "office2")
    val office3 = anyOffice.copy(id = anyOfficeId3, name = "office3")

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      _ <- officeRepository.create(office3)
      officeListView1 <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 2, offset = 0)
      officeListView2 <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 2, offset = 2)
    } yield expect.all(
      officeListView1.offices.map(_.id) == List(office1.id, office2.id),
      officeListView1.pagination.hasMoreResults,
      officeListView2.offices.map(_.id) == List(office3.id),
      !officeListView2.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 1 office in the database
      | WHEN listOffices is called with limit 1 and offset 1
      | THEN return an empty list of results
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, _, _) =>
    val office = anyOffice

    for {
      _ <- officeRepository.create(office)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 1, offset = 1)
    } yield expect.all(
      officeListView.offices.isEmpty,
      !officeListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN 1 archived office in the database
      | WHEN listOffices is called with limit 1 and offset 0
      | THEN return an empty list of results
      |""".stripMargin
  ) { (officeRepository, officeViewRepository, _, _) =>
    val archivedOffice = anyOffice.copy(isArchived = true)

    for {
      _ <- officeRepository.create(archivedOffice)
      officeListView <- officeViewRepository.listOffices(now = anyLocalDateTime, limit = 1, offset = 0)
    } yield expect(officeListView.offices.isEmpty)
  }

  private def truncateTables(session: Resource[IO, Session[IO]]) =
    truncateAccountTable(session) >>
      truncateDeskTable(session) >>
      truncateOfficeTable(session)

  private def truncateAccountTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE account CASCADE
      """.command
    session.use(_.execute(sql))
  }

  private def truncateDeskTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE desk CASCADE
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

  private lazy val anyLocalDateTime = LocalDateTime.parse("2024-10-06T12:00:00")

  private lazy val anyOffice = Office(
    id = anyOfficeId1,
    name = anyOfficeName,
    notes = anyOfficeNotes,
    address = anyOfficeAddress
  )

  private lazy val anyOfficeId1 = UUID.fromString("4f99984c-e371-4b77-a184-7003f6281b8d")
  private lazy val anyOfficeId2 = UUID.fromString("50ca0557-4c8a-4d6f-8703-c9cd3da5ea9e")
  private lazy val anyOfficeId3 = UUID.fromString("68873232-a0c3-4d1d-9a76-762bf962c1bc")

  private lazy val anyOfficeName =
    "Test Office"

  private lazy val anyOfficeNotes =
    List("Test", "Notes")

  private lazy val anyOfficeAddress = Address(
    addressLine1 = "Test Street",
    addressLine2 = "Building 42",
    postalCode = "12-345",
    city = "Wroclaw",
    country = "Poland"
  )

  private def anyUser(id: UUID, emailPrefix: String, assignedOfficeId: Option[UUID] = None) = UserAccount(
    id = id,
    firstName = emailPrefix,
    lastName = emailPrefix.reverse,
    email = s"$emailPrefix@example.com",
    assignedOfficeId = assignedOfficeId
  )

  private def anyOfficeManager(id: UUID, emailPrefix: String, assignedOfficeId: Option[UUID] = None) =
    OfficeManagerAccount(
      id = id,
      firstName = emailPrefix,
      lastName = emailPrefix.reverse,
      email = s"$emailPrefix@example.com",
      assignedOfficeId = assignedOfficeId,
      managedOfficeIds = Nil
    )

  private lazy val anyAccountId1 = UUID.fromString("ee289e1e-3aba-446e-af46-3c6577da361f")
  private lazy val anyAccountId2 = UUID.fromString("fd45e81a-1253-4284-b1ce-6f5a12824f2d")
  private lazy val anyAccountId3 = UUID.fromString("d1d66147-3c6b-4af7-8827-2369a48d3675")

  private def anyDesk(id: UUID, name: String, officeId: UUID) = Desk(
    id = id,
    name = name,
    isAvailable = true,
    notes = List("Test", "Notes"),
    isStanding = false,
    monitorsCount = 2,
    hasPhone = false,
    officeId = officeId
  )

  private lazy val anyDeskId1 = UUID.fromString("cecd6ead-2d55-4eef-901e-273aaf6fb23a")
  private lazy val anyDeskId2 = UUID.fromString("4a565b76-eabf-497c-9ea9-7f935ffdfcd6")
  private lazy val anyDeskId3 = UUID.fromString("56a1eb04-5014-48f6-9241-e7f169b16177")
}

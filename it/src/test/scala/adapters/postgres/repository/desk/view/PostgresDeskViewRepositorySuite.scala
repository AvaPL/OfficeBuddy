package io.github.avapl
package adapters.postgres.repository.desk.view

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.desk.PostgresDeskRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import domain.model.desk.Desk
import domain.model.office.Address
import domain.model.office.Office
import java.util.UUID
import skunk._
import skunk.Command
import skunk.Session
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresDeskViewRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(
    name: TestName
  )(run: (PostgresDeskRepository[IO], PostgresDeskViewRepository[IO]) => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresDeskRepository = new PostgresDeskRepository[IO](session)
      lazy val postgresDeskViewRepository = new PostgresDeskViewRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        run(postgresDeskRepository, postgresDeskViewRepository)
    }

  beforeTest(
    """GIVEN 3 desks in the database assigned to an office
      | WHEN listDesks is called with limit 2 and offsets 0 and 2
      | THEN return two pages of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository) =>
    val officeId = officeId1
    val desk1 = anyDesk.copy(id = anyDeskId1, name = "desk1", officeId = officeId)
    val desk2 = anyDesk.copy(id = anyDeskId2, name = "desk2", officeId = officeId)
    val desk3 = anyDesk.copy(id = anyDeskId3, name = "desk3", officeId = officeId)

    for {
      _ <- deskRepository.create(desk1)
      _ <- deskRepository.create(desk2)
      _ <- deskRepository.create(desk3)
      deskListView1 <- deskViewRepository.listDesks(officeId1, limit = 2, offset = 0)
      deskListView2 <- deskViewRepository.listDesks(officeId1, limit = 2, offset = 2)
    } yield expect.all(
      deskListView1.desks.map(_.id) == List(desk1.id, desk2.id),
      deskListView1.pagination.hasMoreResults,
      deskListView2.desks.map(_.id) == List(desk3.id),
      !deskListView2.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 desk in the database assigned to an office
      | WHEN listDesks is called with limit 1 and offset 1
      | THEN return an empty list of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository) =>
    val desk = anyDesk

    for {
      _ <- deskRepository.create(desk)
      deskListView <- deskViewRepository.listDesks(desk.officeId, limit = 1, offset = 1)
    } yield expect.all(
      deskListView.desks.isEmpty,
      !deskListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 archived desk in the database assigned to an office
      | WHEN listDesks is called with limit 1 and offset 0
      | THEN return an empty list of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository) =>
    val desk = anyDesk.copy(isArchived = true)

    for {
      _ <- deskRepository.create(desk)
      deskListView <- deskViewRepository.listDesks(desk.officeId, limit = 1, offset = 0)
    } yield expect.all(
      deskListView.desks.isEmpty,
      !deskListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 desks in the database
      | WHEN listDesks is called with officeId matching 2 desks
      | THEN return matching desks
      |""".stripMargin
  ) { (deskRepository, deskViewRepository) =>
    val matchingDesk1 = anyDesk.copy(id = anyDeskId1, name = "desk1", officeId = officeId1)
    val matchingDesk2 = anyDesk.copy(id = anyDeskId2, name = "desk2", officeId = officeId1)
    val notMatchingDesk = anyDesk.copy(id = anyDeskId3, name = "desk3", officeId = officeId2)

    for {
      _ <- deskRepository.create(matchingDesk1)
      _ <- deskRepository.create(matchingDesk2)
      _ <- deskRepository.create(notMatchingDesk)
      deskListView <- deskViewRepository.listDesks(officeId1, limit = 10, offset = 0)
    } yield expect.all(
      deskListView.desks.map(_.id) == List(matchingDesk1.id, matchingDesk2.id),
      !deskListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 desks in the database
      | WHEN listDesks is called with officeId matching 2 desks, limit 1 and offset 1
      | THEN return the matching desk and no more results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository) =>
    val matchingDesk1 = anyDesk.copy(id = anyDeskId1, name = "desk1", officeId = officeId1)
    val matchingDesk2 = anyDesk.copy(id = anyDeskId2, name = "desk2", officeId = officeId1)
    val notMatchingDesk = anyDesk.copy(id = anyDeskId3, name = "desk3", officeId = officeId2)

    for {
      _ <- deskRepository.create(matchingDesk1)
      _ <- deskRepository.create(matchingDesk2)
      _ <- deskRepository.create(notMatchingDesk)
      deskListView <- deskViewRepository.listDesks(officeId1, limit = 1, offset = 1)
    } yield expect.all(
      deskListView.desks.map(_.id) == List(matchingDesk2.id),
      !deskListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 desk in the database assigned to an office
      | WHEN listDesks is called with officeId not matching the desk
      | THEN return an empty list of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository) =>
    val notMatchingDesk = anyDesk.copy(officeId = officeId2)

    for {
      _ <- deskRepository.create(notMatchingDesk)
      deskListView <- deskViewRepository.listDesks(officeId1, limit = 10, offset = 0)
    } yield expect.all(
      deskListView.desks.isEmpty,
      !deskListView.pagination.hasMoreResults
    )
  }

  private def truncateTables(session: Resource[IO, Session[IO]]) =
    truncateDeskTable(session) >>
      truncateOfficeTable(session)

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

  private lazy val anyDesk = Desk(
    id = anyDeskId1,
    name = "Test Desk",
    isAvailable = true,
    notes = List("Test", "Notes"),
    isStanding = false,
    monitorsCount = 2,
    hasPhone = false,
    officeId = officeId1
  )

  private lazy val anyDeskId1 = UUID.fromString("cecd6ead-2d55-4eef-901e-273aaf6fb23a")
  private lazy val anyDeskId2 = UUID.fromString("4a565b76-eabf-497c-9ea9-7f935ffdfcd6")
  private lazy val anyDeskId3 = UUID.fromString("56a1eb04-5014-48f6-9241-e7f169b16177")
}

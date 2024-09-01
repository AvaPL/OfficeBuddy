package io.github.avapl
package adapters.postgres.repository.office

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.office.view.PostgresOfficeViewRepository
import cats.effect.IO
import cats.effect.Resource
import domain.model.office.Address
import domain.model.office.Office
import java.util.UUID
import skunk._
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresOfficeViewRepositoryTest extends IOSuite with PostgresFixture {

  private def beforeTest(
    name: TestName
  )(run: (PostgresOfficeRepository[IO], PostgresOfficeViewRepository[IO]) => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresOfficeRepository = new PostgresOfficeRepository[IO](session)
      lazy val postgresOfficeViewRepository = new PostgresOfficeViewRepository[IO](session)
      truncateOfficeTable(session) >> run(postgresOfficeRepository, postgresOfficeViewRepository)
    }

  // TODO: Test office managers
  // TODO: Test counters

  beforeTest(
    """
      |GIVEN three offices in the database
      | WHEN listOffices is called with limit 2 and offsets 0 and 2
      | THEN return two pages of results
      |""".stripMargin
  ) { (officeRepository, officeViewRepository) =>
    val office1 = anyOffice.copy(id = anyOfficeId1, name = "office1")
    val office2 = anyOffice.copy(id = anyOfficeId2, name = "office2")
    val office3 = anyOffice.copy(id = anyOfficeId3, name = "office3")

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      _ <- officeRepository.create(office3)
      officeListView1 <- officeViewRepository.listOffices(limit = 2, offset = 0)
      officeListView2 <- officeViewRepository.listOffices(limit = 2, offset = 2)
    } yield expect.all(
      officeListView1.offices.map(_.id) == List(office1.id, office2.id),
      officeListView1.pagination.hasMoreResults,
      officeListView2.offices.map(_.id) == List(office3.id),
      !officeListView2.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN one office in the database
      | WHEN listOffices is called with limit 1 and offset 1
      | THEN return an empty list of results
      |""".stripMargin
  ) { (officeRepository, officeViewRepository) =>
    val office = anyOffice

    for {
      _ <- officeRepository.create(office)
      officeListView <- officeViewRepository.listOffices(limit = 1, offset = 1)
    } yield expect.all(
      officeListView.offices.isEmpty,
      !officeListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """
      |GIVEN one archived office in the database
      | WHEN listOffices is called with limit 1 and offset 0
      | THEN return an empty list of results
      |""".stripMargin
  ) { (officeRepository, officeViewRepository) =>
    val archivedOffice = anyOffice.copy(isArchived = true)

    for {
      _ <- officeRepository.create(archivedOffice)
      officeListView <- officeViewRepository.listOffices(limit = 1, offset = 0)
    } yield expect(officeListView.offices.isEmpty)
  }

  private def truncateOfficeTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE office CASCADE
      """.command
    session.use(_.execute(sql))
  }

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
}

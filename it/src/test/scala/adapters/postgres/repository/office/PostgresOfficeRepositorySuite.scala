package io.github.avapl
package adapters.postgres.repository.office

import adapters.postgres.fixture.PostgresFixture
import cats.effect.IO
import cats.effect.Resource
import com.softwaremill.quicklens._
import domain.model.error.office.DuplicateOfficeName
import domain.model.error.office.OfficeNotFound
import domain.model.office.Address
import domain.model.office.Office
import domain.model.office.UpdateOffice
import java.util.UUID
import skunk.Command
import skunk.Session
import skunk.Void
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresOfficeRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(name: TestName)(run: PostgresOfficeRepository[IO] => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresOfficeRepository = new PostgresOfficeRepository[IO](session)
      truncateOfficeTable(session) >> run(postgresOfficeRepository)
    }

  beforeTest(
    """
      |GIVEN an office to create
      | WHEN create is called
      | THEN the office should be inserted into Postgres
      |""".stripMargin
  ) { officeRepository =>
    val office = anyOffice

    for {
      _ <- officeRepository.create(office)
      readOffice <- officeRepository.read(office.id)
    } yield expect(readOffice == office)
  }

  beforeTest(
    """
      |GIVEN an existing office and a new office with the same name
      | WHEN create is called
      | THEN the call should fail with DuplicateOfficeName
      |""".stripMargin
  ) { officeRepository =>
    val office = anyOffice
    val officeWithTheSameName = office.copy(
      id = UUID.fromString("fe58cb74-1125-4e19-a087-6dbbd7165811")
    )

    for {
      _ <- officeRepository.create(office)
      result <- officeRepository.create(officeWithTheSameName).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateOfficeName = DuplicateOfficeName(office.name)
        expect(throwable == duplicateOfficeName)
    }
  }

  beforeTest(
    """
      |GIVEN a non-existent office ID
      | WHEN read is called
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { officeRepository =>
    val officeId = anyOfficeId

    for {
      result <- officeRepository.read(officeId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(officeId)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN an existing office and an update
      | WHEN update is called
      | THEN the office should be updated
      |""".stripMargin
  ) { officeRepository =>
    val office = anyOffice
    val officeUpdate = anyUpdateOffice
      .modifyAll(
        _.name,
        _.address.addressLine1,
        _.address.addressLine2,
        _.address.postalCode,
        _.address.city,
        _.address.country
      )
      .using(_ + "updated")
      .modify(_.notes)
      .using("updated" :: _)

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.update(office.id, officeUpdate)
      readOffice <- officeRepository.read(office.id)
    } yield {
      val expectedOffice = Office(office.id, officeUpdate.name, officeUpdate.notes, officeUpdate.address)
      expect(readOffice == expectedOffice)
    }
  }

  beforeTest(
    """
      |GIVEN an existing office
      | WHEN update is called without any changes
      | THEN the call should not fail (no-op)
      |""".stripMargin
  ) { officeRepository =>
    val office = anyOffice
    val officeUpdate = UpdateOffice(office.name, office.notes, office.address)

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.update(office.id, officeUpdate)
    } yield success
  }

  beforeTest(
    """
      |GIVEN an update for nonexistent office
      | WHEN update is called
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { officeRepository =>
    val officeId = anyOfficeId
    val officeUpdate = anyUpdateOffice

    for {
      result <- officeRepository.update(officeId, officeUpdate).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(officeId)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN 2 existing offices and an update
      | WHEN an office with the name given in the update already exists
      | THEN the call should fail with DuplicateOfficeName
      |""".stripMargin
  ) { officeRepository =>
    val office1 = anyOffice
    val office2 = anyOffice.copy(
      id = UUID.fromString("96cb8558-8fe8-4330-b50a-00e7e1917757"),
      name = "other"
    )
    val officeUpdate = anyUpdateOffice.copy(name = office2.name)

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      result <- officeRepository.update(office1.id, officeUpdate).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateOfficeName = DuplicateOfficeName(office2.name)
        expect(throwable == duplicateOfficeName)
    }
  }

  beforeTest(
    """
      |GIVEN an existing office
      | WHEN archive is called on its ID
      | THEN the office should be archived
      |""".stripMargin
  ) { officeRepository =>
    val office = anyOffice

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.archive(office.id)
      office <- officeRepository.read(office.id)
    } yield expect(office.isArchived)
  }

  beforeTest(
    """
      |WHEN archive is called on nonexistent office ID
      |THEN the call should not fail (no-op)
      |""".stripMargin
  ) { officeRepository =>
    val officeId = anyOfficeId

    for {
      _ <- officeRepository.archive(officeId)
    } yield success
  }

  private def truncateOfficeTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE office CASCADE
      """.command
    session.use(_.execute(sql))
  }

  private lazy val anyOffice = Office(
    id = anyOfficeId,
    name = anyOfficeName,
    notes = anyOfficeNotes,
    address = anyOfficeAddress
  )

  private lazy val anyUpdateOffice = UpdateOffice(
    name = anyOfficeName,
    notes = anyOfficeNotes,
    address = anyOfficeAddress
  )

  private lazy val anyOfficeId =
    UUID.fromString("4f99984c-e371-4b77-a184-7003f6281b8d")

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

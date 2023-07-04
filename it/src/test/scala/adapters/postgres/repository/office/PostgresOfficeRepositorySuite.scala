package io.github.avapl
package adapters.postgres.repository.office

import adapters.postgres.migration.FlywayMigration
import cats.effect.IO
import cats.effect.Resource
import com.softwaremill.quicklens._
import domain.model.office.Address
import domain.model.office.Office
import domain.repository.office.OfficeRepository.DuplicateOfficeName
import domain.repository.office.OfficeRepository.OfficeNotFound
import java.util.UUID
import natchez.Trace.Implicits.noop
import skunk.Command
import skunk.Session
import skunk.Void
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresOfficeRepositorySuite extends IOSuite {

  override val maxParallelism: Int = 1
  override type Res = Resource[IO, Session[IO]]
  override def sharedResource: Resource[IO, Res] = { // TODO: Extract to a common place and simplify
    val host = "localhost"
    val port = 2345
    val user = "postgres"
    val password = "postgres"
    val database = "office_buddy"
    val session = Session.pooled(
      host = host,
      port = port,
      user = user,
      password = Some(password),
      database = database,
      max = 10
    )
    val migration = new FlywayMigration[IO](
      host = host,
      port = port,
      user = user,
      password = password,
      database = database
    )
    session
      .evalTap(_ => migration.run())
  }

  // TODO: Extract to a common place and simplify
  private def beforeTest(name: TestName)(run: PostgresOfficeRepository[IO] => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresOfficeRepository = new PostgresOfficeRepository[IO](session)
      truncateOfficeTable(session) >> run(postgresOfficeRepository)
    }

  private def truncateOfficeTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE office
      """.command
    session.use(_.execute(sql))
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
      |GIVEN an existing office and an update
      | WHEN update is called
      | THEN the office should be updated
      |""".stripMargin
  ) { officeRepository =>
    val office = anyOffice
    val updatedOffice = office
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
      _ <- officeRepository.update(updatedOffice)
      readOffice <- officeRepository.read(office.id)
    } yield expect(readOffice == updatedOffice)
  }

  beforeTest(
    """
      |GIVEN an existing office
      | WHEN update is called without any changes
      | THEN the call should not fail (no-op)
      |""".stripMargin
  ) { officeRepository =>
    val office = anyOffice

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.update(office)
    } yield success
  }

  beforeTest(
    """
      |GIVEN an update for nonexistent office
      | WHEN update is called
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { officeRepository =>
    val updatedOffice = anyOffice

    for {
      result <- officeRepository.update(updatedOffice).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(updatedOffice.id)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN an existing office
      | WHEN delete is called on its ID
      | THEN the office should be deleted
      |""".stripMargin
  ) { officeRepository =>
    val office = anyOffice

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.delete(office.id)
      result <- officeRepository.read(office.id).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(office.id)
        println(throwable)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """
      |WHEN delete is called on nonexistent office ID
      |THEN the call should not fail (no-op)
      |""".stripMargin
  ) { officeRepository =>
    val officeId = anyOfficeId

    for {
      _ <- officeRepository.delete(officeId)
    } yield success
  }

  private lazy val anyOffice = Office(
    id = anyOfficeId,
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

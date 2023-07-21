package io.github.avapl
package adapters.postgres.repository.desk

import adapters.postgres.migration.FlywayMigration
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.IO
import cats.effect.Resource
import com.softwaremill.quicklens._
import domain.model.desk.Desk
import domain.model.desk.UpdateDesk
import domain.model.error.desk.DeskNotFound
import domain.model.error.desk.DuplicateDeskNameForOffice
import domain.model.office.Address
import domain.model.office.Office
import java.util.UUID
import natchez.Trace.Implicits.noop
import skunk.Command
import skunk.Session
import skunk.Void
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresDeskRepositorySuite extends IOSuite {

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
  private def beforeTest(
    name: TestName
  )(run: (PostgresOfficeRepository[IO], PostgresDeskRepository[IO]) => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresDeskRepository = new PostgresDeskRepository[IO](session)
      lazy val officeRepository = new PostgresOfficeRepository[IO](session)
      truncateDeskTable(session) >> truncateOfficeTable(session) >> run(officeRepository, postgresDeskRepository)
    }

  private def truncateOfficeTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
      TRUNCATE TABLE office CASCADE
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

  beforeTest(
    """
      |GIVEN a desk to create
      | WHEN create is called
      | THEN the desk should be inserted into Postgres
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val desk = anyDesk

    for {
      _ <- officeRepository.create(anyOffice.copy(id = desk.officeId)) // required by foreign key
      _ <- deskRepository.create(desk)
      readDesk <- deskRepository.read(desk.id)
    } yield expect(readDesk == desk)
  }

  beforeTest(
    """
      |GIVEN an existing desk and a new desk with the same name for the same office
      | WHEN create is called
      | THEN the call should fail with DuplicateDeskNameForOffice
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val desk = anyDesk
    val deskWithTheSameName = desk.copy(
      id = UUID.fromString("fe58cb74-1125-4e19-a087-6dbbd7165811")
    )

    for {
      _ <- officeRepository.create(anyOffice.copy(id = desk.officeId)) // required by foreign key
      _ <- deskRepository.create(desk)
      result <- deskRepository.create(deskWithTheSameName).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateDeskName = DuplicateDeskNameForOffice(desk.name, desk.officeId)
        expect(throwable == duplicateDeskName)
    }
  }

  beforeTest(
    """
      |GIVEN an existing desk and a new desk with the same name for different offices
      | WHEN create is called
      | THEN both desk should be inserted successfully
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val desk = anyDesk
    val deskWithTheSameName = desk.copy(
      id = UUID.fromString("fe58cb74-1125-4e19-a087-6dbbd7165811"),
      officeId = UUID.fromString("c1e29bfd-5a8a-468f-ba27-4673c42fec04")
    )

    for {
      _ <- officeRepository.create(anyOffice.copy(id = desk.officeId)) // required by foreign key
      _ <- deskRepository.create(desk)
      _ <- officeRepository.create( // required by foreign key
        anyOffice.copy(id = deskWithTheSameName.officeId, name = "other")
      )
      _ <- deskRepository.create(deskWithTheSameName)
    } yield success
  }

  beforeTest(
    """
      |GIVEN an existing desk and an update
      | WHEN update is called
      | THEN the desk should be updated
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val desk = anyDesk
    val deskUpdate = anyUpdateDesk
      .copy(officeId = UUID.fromString("c1e29bfd-5a8a-468f-ba27-4673c42fec04"))
      .modify(_.name)
      .using(_ + "updated")
      .modify(_.notes)
      .using("updated" :: _)
      .modify(_.monitorsCount)
      .using(c => (c + 1).toShort)
      .modifyAll(
        _.isAvailable,
        _.isStanding,
        _.hasPhone
      )
      .using(!_)

    for {
      _ <- officeRepository.create(anyOffice.copy(id = desk.officeId)) // required by foreign key
      _ <- deskRepository.create(desk)
      _ <- officeRepository.create(anyOffice.copy(id = deskUpdate.officeId, name = "other")) // required by foreign key
      _ <- deskRepository.update(desk.id, deskUpdate)
      readDesk <- deskRepository.read(desk.id)
    } yield {
      val expectedDesk = Desk(
        id = desk.id,
        name = deskUpdate.name,
        isAvailable = deskUpdate.isAvailable,
        notes = deskUpdate.notes,
        isStanding = deskUpdate.isStanding,
        monitorsCount = deskUpdate.monitorsCount,
        hasPhone = deskUpdate.hasPhone,
        officeId = deskUpdate.officeId
      )
      expect(readDesk == expectedDesk)
    }
  }

  beforeTest(
    """
      |GIVEN an existing desk
      | WHEN update is called without any changes
      | THEN the call should not fail (no-op)
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val desk = anyDesk
    val deskUpdate = UpdateDesk(
      name = desk.name,
      isAvailable = desk.isAvailable,
      notes = desk.notes,
      isStanding = desk.isStanding,
      monitorsCount = desk.monitorsCount,
      hasPhone = desk.hasPhone,
      officeId = desk.officeId
    )

    for {
      _ <- officeRepository.create(anyOffice.copy(id = desk.officeId)) // required by foreign key
      _ <- deskRepository.create(desk)
      _ <- deskRepository.update(desk.id, deskUpdate)
    } yield success
  }

  beforeTest(
    """
      |GIVEN an update for nonexistent desk
      | WHEN update is called
      | THEN the call should fail with DeskNotFound
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val deskId = anyDeskId
    val deskUpdate = anyUpdateDesk

    for {
      result <- deskRepository.update(deskId, deskUpdate).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val deskNotFound = DeskNotFound(deskId)
        expect(throwable == deskNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN 2 existing desks and an update
      | WHEN a desk with the name given in the update already exists for the given office
      | THEN the call should fail with DuplicateDeskName
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val desk1 = anyDesk
    val desk2 = desk1.copy(
      id = UUID.fromString("96cb8558-8fe8-4330-b50a-00e7e1917757"),
      name = "other"
    )
    val deskUpdate = anyUpdateDesk.copy(name = desk2.name)

    for {
      _ <- officeRepository.create(anyOffice.copy(id = desk1.officeId)) // required by foreign key
      _ <- deskRepository.create(desk1)
      _ <- deskRepository.create(desk2)
      result <- deskRepository.update(desk1.id, deskUpdate).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateDeskName = DuplicateDeskNameForOffice(desk2.name, desk2.officeId)
        expect(throwable == duplicateDeskName)
    }
  }

  beforeTest(
    """
      |GIVEN 2 existing desks and an update
      | WHEN a desk with the name given in the update exists in another office
      | THEN the call should succeed
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val desk1 = anyDesk
    val desk2 = desk1.copy(
      id = UUID.fromString("96cb8558-8fe8-4330-b50a-00e7e1917757"),
      name = "other",
      officeId = UUID.fromString("c1e29bfd-5a8a-468f-ba27-4673c42fec04")
    )
    val deskUpdate = anyUpdateDesk.copy(name = desk2.name)

    for {
      _ <- officeRepository.create(anyOffice.copy(id = desk1.officeId)) // required by foreign key
      _ <- deskRepository.create(desk1)
      _ <- officeRepository.create(anyOffice.copy(id = desk2.officeId, name = "other")) // required by foreign key
      _ <- deskRepository.create(desk2)
      _ <- deskRepository.update(desk1.id, deskUpdate).attempt
    } yield success
  }

  beforeTest(
    """
      |GIVEN an existing desk
      | WHEN archive is called on its ID
      | THEN the desk should be archived
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val desk = anyDesk

    for {
      _ <- officeRepository.create(anyOffice.copy(id = desk.officeId)) // required by foreign key
      _ <- deskRepository.create(desk)
      _ <- deskRepository.archive(desk.id)
      desk <- deskRepository.read(desk.id)
    } yield expect(desk.isArchived)
  }

  beforeTest(
    """
      |WHEN archive is called on nonexistent desk ID
      |THEN the call should not fail (no-op)
      |""".stripMargin
  ) { (officeRepository, deskRepository) =>
    val deskId = anyDeskId

    for {
      _ <- deskRepository.archive(deskId)
    } yield success
  }

  private lazy val anyOffice = Office(
    id = anyOfficeId,
    name = "Test Office",
    notes = List("Test", "Notes"),
    address = Address(
      addressLine1 = "Test Street",
      addressLine2 = "Building 42",
      postalCode = "12-345",
      city = "Wroclaw",
      country = "Poland"
    )
  )

  private lazy val anyOfficeId: UUID = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")

  private lazy val anyDesk = Desk(
    id = anyDeskId,
    name = anyDeskName,
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = anyOfficeId
  )

  private lazy val anyUpdateDesk = UpdateDesk(
    name = anyDeskName,
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = anyOfficeId
  )

  private lazy val anyDeskId = UUID.fromString("4f99984c-e371-4b77-a184-7003f6281b8d")

  private lazy val anyDeskName = "107.1"
}

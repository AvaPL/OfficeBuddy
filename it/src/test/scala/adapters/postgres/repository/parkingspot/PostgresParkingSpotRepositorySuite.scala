package io.github.avapl
package adapters.postgres.repository.parkingspot

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import domain.model.parkingspot.{ParkingSpot, UpdateParkingSpot}
import domain.model.error.parkingspot.{ParkingSpotNotFound, DuplicateParkingSpotNameForOffice}
import domain.model.error.office.OfficeNotFound
import domain.model.office.{Address, Office}
import java.util.UUID
import skunk.Command
import skunk.Session
import skunk.Void
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresParkingSpotRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(name: TestName)(run: PostgresParkingSpotRepository[IO] => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresParkingSpotRepository = new PostgresParkingSpotRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        run(postgresParkingSpotRepository)
    }

  beforeTest(
    """
      |GIVEN a parking spot to create
      | WHEN create is called
      | THEN the parking spot should be inserted into Postgres
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpot = anyParkingSpot

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      readParkingSpot <- parkingSpotRepository.read(parkingSpot.id)
    } yield expect(readParkingSpot == parkingSpot)
  }

  beforeTest(
    """
      |GIVEN a parking spot with non-existent office ID
      | WHEN create is called
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { parkingSpotRepository =>
    val officeId = UUID.fromString("3a75af1c-dee0-431b-ab41-7e551d77277c")
    val parkingSpot = anyParkingSpot.copy(officeId = officeId)

    for {
      result <- parkingSpotRepository.create(parkingSpot).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(officeId)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN an existing parking spot and a new parking spot with the same name for the same office
      | WHEN create is called
      | THEN the call should fail with DuplicateParkingSpotNameForOffice
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpot = anyParkingSpot
    val parkingSpotWithTheSameName = parkingSpot.copy(
      id = UUID.fromString("fe58cb74-1125-4e19-a087-6dbbd7165811")
    )

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      result <- parkingSpotRepository.create(parkingSpotWithTheSameName).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateParkingSpotName = DuplicateParkingSpotNameForOffice(parkingSpot.name.some, parkingSpot.officeId.some)
        expect(throwable == duplicateParkingSpotName)
    }
  }

  beforeTest(
    """
      |GIVEN an existing parking spot and a new parking spot with the same name for different offices
      | WHEN create is called
      | THEN both parking spots should be inserted successfully
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpot = anyParkingSpot
    val parkingSpotWithTheSameName = parkingSpot.copy(
      id = UUID.fromString("fe58cb74-1125-4e19-a087-6dbbd7165811"),
      officeId = officeId2
    )

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      _ <- parkingSpotRepository.create(parkingSpotWithTheSameName)
    } yield success
  }

  beforeTest(
    """
      |GIVEN a non-existent parking spot ID
      | WHEN read is called
      | THEN the call should fail with ParkingSpotNotFound
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpotId = anyParkingSpotId

    for {
      result <- parkingSpotRepository.read(parkingSpotId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val parkingSpotNotFound = ParkingSpotNotFound(parkingSpotId)
        expect(throwable == parkingSpotNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN an existing parking spot and an update
      | WHEN update is called
      | THEN the parking spot should be updated
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpot = anyParkingSpot
    val parkingSpotUpdate = UpdateParkingSpot( // only some properties updated
      name = Some(parkingSpot.name + "updated"),
      notes = Some("updated" :: anyParkingSpotNotes),
      isHandicapped = Some(!parkingSpot.isHandicapped)
    )

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      _ <- parkingSpotRepository.update(parkingSpot.id, parkingSpotUpdate)
      readParkingSpot <- parkingSpotRepository.read(parkingSpot.id)
    } yield {
      val expectedParkingSpot = ParkingSpot(
        id = parkingSpot.id,
        name = parkingSpotUpdate.name.get,
        isAvailable = parkingSpot.isAvailable,
        notes = parkingSpotUpdate.notes.get,
        isHandicapped = parkingSpotUpdate.isHandicapped.get,
        isUnderground = parkingSpot.isUnderground,
        officeId = parkingSpot.officeId,
        isArchived = parkingSpot.isArchived
      )
      expect(readParkingSpot == expectedParkingSpot)
    }
  }

  beforeTest(
    """
      |GIVEN an existing parking spot
      | WHEN update is called without any changes
      | THEN the call should not fail (no-op)
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpot = anyParkingSpot
    val parkingSpotUpdate = UpdateParkingSpot()

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      _ <- parkingSpotRepository.update(parkingSpot.id, parkingSpotUpdate)
    } yield success
  }

  beforeTest(
    """
      |GIVEN an update for nonexistent parking spot
      | WHEN update is called
      | THEN the call should fail with ParkingSpotNotFound
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpotId = anyParkingSpotId
    val parkingSpotUpdate = anyUpdateParkingSpot

    for {
      result <- parkingSpotRepository.update(parkingSpotId, parkingSpotUpdate).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val parkingSpotNotFound = ParkingSpotNotFound(parkingSpotId)
        expect(throwable == parkingSpotNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN 2 existing parking spots and an update
      | WHEN a parking spot with the name given in the update already exists for the given office
      | THEN the call should fail with DuplicateParkingSpotName
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpot1 = anyParkingSpot
    val parkingSpot2 = parkingSpot1.copy(
      id = UUID.fromString("96cb8558-8fe8-4330-b50a-00e7e1917757"),
      name = "other"
    )
    val parkingSpotUpdate = anyUpdateParkingSpot.copy(name = Some(parkingSpot2.name))

    for {
      _ <- parkingSpotRepository.create(parkingSpot1)
      _ <- parkingSpotRepository.create(parkingSpot2)
      result <- parkingSpotRepository.update(parkingSpot1.id, parkingSpotUpdate).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateParkingSpotName = DuplicateParkingSpotNameForOffice(parkingSpot2.name.some, parkingSpot2.officeId.some)
        expect(throwable == duplicateParkingSpotName)
    }
  }

  beforeTest(
    """
      |GIVEN 2 existing parking spots and an update
      | WHEN a parking spot with the name given in the update exists in another office
      | THEN the call should succeed
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpot1 = anyParkingSpot
    val parkingSpot2 = parkingSpot1.copy(
      id = UUID.fromString("96cb8558-8fe8-4330-b50a-00e7e1917757"),
      name = "other",
      officeId = officeId2
    )
    val parkingSpotUpdate = anyUpdateParkingSpot.copy(name = Some(parkingSpot2.name))

    for {
      _ <- parkingSpotRepository.create(parkingSpot1)
      _ <- parkingSpotRepository.create(parkingSpot2)
      _ <- parkingSpotRepository.update(parkingSpot1.id, parkingSpotUpdate).attempt
    } yield success
  }

  beforeTest(
    """
      |GIVEN an existing parking spot
      | WHEN archive is called on its ID
      | THEN the parking spot should be archived
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpot = anyParkingSpot

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      _ <- parkingSpotRepository.archive(parkingSpot.id)
      parkingSpot <- parkingSpotRepository.read(parkingSpot.id)
    } yield expect(parkingSpot.isArchived)
  }

  beforeTest(
    """
      |WHEN archive is called on nonexistent parking spot ID
      |THEN the call should not fail (no-op)
      |""".stripMargin
  ) { parkingSpotRepository =>
    val parkingSpotId = anyParkingSpotId

    for {
      _ <- parkingSpotRepository.archive(parkingSpotId)
    } yield success
  }

  private def truncateTables(session: Resource[IO, Session[IO]]) =
    truncateParkingSpotTable(session) >>
      truncateOfficeTable(session)

  private def truncateParkingSpotTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE parking_spot CASCADE
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

  private lazy val anyParkingSpot = ParkingSpot(
    id = anyParkingSpotId,
    name = "A1",
    isAvailable = true,
    notes = List("Near the entrance"),
    isHandicapped = true,
    isUnderground = true,
    officeId = officeId1
  )

  private lazy val anyUpdateParkingSpot = UpdateParkingSpot(
    name = Some("A1"),
    isAvailable = Some(true),
    notes = Some(List("Near the entrance")),
    isHandicapped = Some(true),
    isUnderground = Some(true),
    officeId = Some(officeId1)
  )

  private lazy val anyParkingSpotId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")

  private lazy val anyParkingSpotNotes =
    List("Near the entrance", "Reserved for visitors")
}

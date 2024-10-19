package io.github.avapl
package domain.service.parkingspot

import cats.effect.IO
import cats.syntax.all._
import domain.model.error.parkingspot.DuplicateParkingSpotNameForOffice
import domain.model.error.parkingspot.ParkingSpotNotFound
import domain.model.parkingspot.CreateParkingSpot
import domain.model.parkingspot.ParkingSpot
import domain.model.parkingspot.UpdateParkingSpot
import domain.repository.parkingspot.ParkingSpotRepository
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import util.FUUID
import weaver.SimpleIOSuite

object ParkingSpotServiceSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN a parking spot to create
      | WHEN createParkingSpot is called
      | THEN a valid parking spot is created via parkingSpotRepository
      |""".stripMargin
  ) {
    val parkingSpotToCreate = anyCreateParkingSpot

    val parkingSpotId = anyParkingSpotId
    implicit val fuuid: FUUID[IO] = whenF(mock[FUUID[IO]].randomUUID()) thenReturn parkingSpotId
    val parkingSpotRepository = mock[ParkingSpotRepository[IO]]
    val parkingSpot = parkingSpotToCreate.toParkingSpot(parkingSpotId)
    whenF(parkingSpotRepository.create(any)) thenReturn parkingSpot
    val parkingSpotService = new ParkingSpotService[IO](parkingSpotRepository)

    for {
      createdParkingSpot <- parkingSpotService.createParkingSpot(parkingSpotToCreate)
    } yield {
      verify(parkingSpotRepository, only).create(eqTo(parkingSpot))
      expect(createdParkingSpot == parkingSpot)
    }
  }

  test(
    """GIVEN a parking spot to create
      | WHEN createParkingSpot is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val parkingSpotToCreate = anyCreateParkingSpot

    val duplicateParkingSpotName = DuplicateParkingSpotNameForOffice(name.some, parkingSpotToCreate.officeId.some)
    val parkingSpotRepository = whenF(mock[ParkingSpotRepository[IO]].create(any)) thenFailWith duplicateParkingSpotName
    val parkingSpotService = new ParkingSpotService[IO](parkingSpotRepository)

    for {
      result <- parkingSpotService.createParkingSpot(parkingSpotToCreate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == duplicateParkingSpotName)
    }
  }

  test(
    """GIVEN a parking spot ID
      | WHEN readParkingSpot is called
      | THEN the parking spot is read via parkingSpotRepository
      |""".stripMargin
  ) {
    val parkingSpotId = anyParkingSpotId

    val parkingSpotRepository = mock[ParkingSpotRepository[IO]]
    val parkingSpot = anyParkingSpot.copy(id = parkingSpotId)
    whenF(parkingSpotRepository.read(any)) thenReturn parkingSpot
    val parkingSpotService = new ParkingSpotService[IO](parkingSpotRepository)

    for {
      readParkingSpot <- parkingSpotService.readParkingSpot(parkingSpotId)
    } yield {
      verify(parkingSpotRepository, only).read(eqTo(parkingSpotId))
      expect(readParkingSpot == parkingSpot)
    }
  }

  test(
    """GIVEN a parking spot ID
      | WHEN readParkingSpot is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val parkingSpotId = anyParkingSpotId

    val parkingSpotNotFound = ParkingSpotNotFound(parkingSpotId)
    val parkingSpotRepository = whenF(mock[ParkingSpotRepository[IO]].read(any)) thenFailWith parkingSpotNotFound
    val parkingSpotService = new ParkingSpotService[IO](parkingSpotRepository)

    for {
      result <- parkingSpotService.readParkingSpot(parkingSpotId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == parkingSpotNotFound)
    }
  }

  test(
    """GIVEN a parking spot update
      | WHEN updateParkingSpot is called
      | THEN the parking spot is updated via parkingSpotRepository
      |""".stripMargin
  ) {
    val parkingSpotId = anyParkingSpotId
    val parkingSpotUpdate = anyUpdateParkingSpot

    val parkingSpotRepository = mock[ParkingSpotRepository[IO]]
    val parkingSpot = ParkingSpot(
      id = parkingSpotId,
      name = parkingSpotUpdate.name.get,
      isAvailable = parkingSpotUpdate.isAvailable.get,
      notes = parkingSpotUpdate.notes.get,
      isHandicapped = parkingSpotUpdate.isHandicapped.get,
      isUnderground = parkingSpotUpdate.isUnderground.get,
      officeId = parkingSpotUpdate.officeId.get
    )
    whenF(parkingSpotRepository.update(any, any)) thenReturn parkingSpot
    val parkingSpotService = new ParkingSpotService[IO](parkingSpotRepository)

    for {
      updatedParkingSpot <- parkingSpotService.updateParkingSpot(parkingSpotId, parkingSpotUpdate)
    } yield {
      verify(parkingSpotRepository, only).update(eqTo(parkingSpotId), eqTo(parkingSpotUpdate))
      expect(updatedParkingSpot == parkingSpot)
    }
  }

  test(
    """GIVEN a parking spot update
      | WHEN updateParkingSpot is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val parkingSpotId = anyParkingSpotId
    val parkingSpotUpdate = anyUpdateParkingSpot

    val parkingSpotNotFound = ParkingSpotNotFound(parkingSpotId)
    val parkingSpotRepository = whenF(mock[ParkingSpotRepository[IO]].update(any, any)) thenFailWith parkingSpotNotFound
    val parkingSpotService = new ParkingSpotService[IO](parkingSpotRepository)

    for {
      result <- parkingSpotService.updateParkingSpot(parkingSpotId, parkingSpotUpdate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == parkingSpotNotFound)
    }
  }

  test(
    """GIVEN a parking spot ID
      | WHEN archiveParkingSpot is called
      | THEN the parking spot is archived via parkingSpotRepository
      |""".stripMargin
  ) {
    val parkingSpotId = anyParkingSpotId

    val parkingSpotRepository = mock[ParkingSpotRepository[IO]]
    whenF(parkingSpotRepository.archive(any)) thenReturn ()
    val parkingSpotService = new ParkingSpotService[IO](parkingSpotRepository)

    for {
      _ <- parkingSpotService.archiveParkingSpot(parkingSpotId)
    } yield {
      verify(parkingSpotRepository, only).archive(eqTo(parkingSpotId))
      success
    }
  }

  test(
    """GIVEN a parking spot ID
      | WHEN archiveParkingSpot is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val parkingSpotId = anyParkingSpotId

    val repositoryException = new RuntimeException("intended exception")
    val parkingSpotRepository = whenF(mock[ParkingSpotRepository[IO]].archive(any)) thenFailWith repositoryException
    val parkingSpotService = new ParkingSpotService[IO](parkingSpotRepository)

    for {
      result <- parkingSpotService.archiveParkingSpot(parkingSpotId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == repositoryException)
    }
  }

  private lazy val anyParkingSpot = ParkingSpot(
    id = anyParkingSpotId,
    name = "A1",
    isAvailable = true,
    notes = List("Near the entrance"),
    isHandicapped = false,
    isUnderground = true,
    officeId = anyOfficeId
  )

  private lazy val anyCreateParkingSpot = CreateParkingSpot(
    name = "A1",
    isAvailable = true,
    notes = List("Near the entrance"),
    isHandicapped = false,
    isUnderground = true,
    officeId = anyOfficeId
  )

  private lazy val anyUpdateParkingSpot = UpdateParkingSpot(
    name = Some("A1"),
    isAvailable = Some(true),
    notes = Some(List("Near the entrance")),
    isHandicapped = Some(true),
    isUnderground = Some(false),
    officeId = Some(anyOfficeId)
  )

  private lazy val anyParkingSpotId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")

  private lazy val anyOfficeId: UUID = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")
}

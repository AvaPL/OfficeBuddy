package io.github.avapl
package domain.service.office

import cats.effect.IO
import domain.model.error.office.DuplicateOfficeName
import domain.model.error.office.OfficeNotFound
import domain.model.office.Address
import domain.model.office.CreateOffice
import domain.model.office.Office
import domain.model.office.UpdateAddress
import domain.model.office.UpdateOffice
import domain.repository.office.OfficeRepository
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import util.FUUID
import weaver.SimpleIOSuite

object OfficeServiceSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN an office to create
      | WHEN createOffice is called
      | THEN a valid office is created via officeRepository
      |""".stripMargin
  ) {
    val officeToCreate = anyCreateOffice

    val officeId = anyOfficeId
    implicit val fuuid: FUUID[IO] = whenF(mock[FUUID[IO]].randomUUID()) thenReturn officeId
    val officeRepository = mock[OfficeRepository[IO]]
    val office = officeToCreate.toOffice(officeId)
    whenF(officeRepository.create(any)) thenReturn office
    val officeService = new OfficeService[IO](officeRepository)

    for {
      createdOffice <- officeService.createOffice(officeToCreate)
    } yield {
      verify(officeRepository, only).create(eqTo(office))
      expect(createdOffice == office)
    }
  }

  test(
    """GIVEN an office to create
      | WHEN createOffice is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val officeToCreate = anyCreateOffice

    val duplicateOfficeName = DuplicateOfficeName(name)
    val officeRepository = whenF(mock[OfficeRepository[IO]].create(any)) thenFailWith duplicateOfficeName
    val officeService = new OfficeService[IO](officeRepository)

    for {
      result <- officeService.createOffice(officeToCreate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == duplicateOfficeName)
    }
  }

  test(
    """GIVEN an office ID
      | WHEN readOffice is called
      | THEN the office is read via officeRepository
      |""".stripMargin
  ) {
    val officeId = anyOfficeId

    val officeRepository = mock[OfficeRepository[IO]]
    val office = anyOffice.copy(id = officeId)
    whenF(officeRepository.read(any)) thenReturn office
    val officeService = new OfficeService[IO](officeRepository)

    for {
      readOffice <- officeService.readOffice(officeId)
    } yield {
      verify(officeRepository, only).read(eqTo(officeId))
      expect(readOffice == office)
    }
  }

  test(
    """GIVEN an office ID
      | WHEN readOffice is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val officeId = anyOfficeId

    val officeNotFound = OfficeNotFound(officeId)
    val officeRepository = whenF(mock[OfficeRepository[IO]].read(any)) thenFailWith officeNotFound
    val officeService = new OfficeService[IO](officeRepository)

    for {
      result <- officeService.readOffice(officeId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == officeNotFound)
    }
  }

  test(
    """GIVEN an office update
      | WHEN updateOffice is called
      | THEN the office is updated via officeRepository
      |""".stripMargin
  ) {
    val officeId = anyOfficeId
    val officeUpdate = anyUpdateOffice

    val officeRepository = mock[OfficeRepository[IO]]
    val address = Address(
      addressLine1 = officeUpdate.address.addressLine1.get,
      addressLine2 = officeUpdate.address.addressLine2.get,
      postalCode = officeUpdate.address.postalCode.get,
      city = officeUpdate.address.city.get,
      country = officeUpdate.address.country.get
    )
    val office = Office(officeId, officeUpdate.name.get, officeUpdate.notes.get, address)
    whenF(officeRepository.update(any, any)) thenReturn office
    val officeService = new OfficeService[IO](officeRepository)

    for {
      updatedOffice <- officeService.updateOffice(officeId, officeUpdate)
    } yield {
      verify(officeRepository, only).update(eqTo(officeId), eqTo(officeUpdate))
      expect(updatedOffice == office)
    }
  }

  test(
    """GIVEN an office update
      | WHEN updateOffice is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val officeId = anyOfficeId
    val officeUpdate = anyUpdateOffice

    val officeNotFound = OfficeNotFound(officeId)
    val officeRepository = whenF(mock[OfficeRepository[IO]].update(any, any)) thenFailWith officeNotFound
    val officeService = new OfficeService[IO](officeRepository)

    for {
      result <- officeService.updateOffice(officeId, officeUpdate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == officeNotFound)
    }
  }

  test(
    """GIVEN an office ID and a list of office manager IDs
      | WHEN updateOfficeManagers is called
      | THEN the office managers are updated via officeRepository
      |""".stripMargin
  ) {
    val officeId = anyOfficeId
    val officeManagerIds = List(anyOfficeManagerId1, anyOfficeManagerId2)

    val officeRepository = mock[OfficeRepository[IO]]
    whenF(officeRepository.updateOfficeManagers(any, any)) thenReturn anyOffice
    val officeService = new OfficeService[IO](officeRepository)

    for {
      _ <- officeService.updateOfficeManagers(officeId, officeManagerIds)
    } yield {
      verify(officeRepository, only).updateOfficeManagers(eqTo(officeId), eqTo(officeManagerIds))
      success
    }
  }

  test(
    """GIVEN an office ID and a list of office manager IDs
      | WHEN updateOfficeManagers is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val officeId = anyOfficeId
    val officeManagerIds = List(anyOfficeManagerId1, anyOfficeManagerId2)

    val officeNotFound = OfficeNotFound(officeId)
    val officeRepository = whenF(mock[OfficeRepository[IO]].updateOfficeManagers(any, any)) thenFailWith officeNotFound
    val officeService = new OfficeService[IO](officeRepository)

    for {
      result <- officeService.updateOfficeManagers(officeId, officeManagerIds).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == officeNotFound)
    }
  }

  test(
    """GIVEN an office ID
      | WHEN archiveOffice is called
      | THEN the office is archived via officeRepository
      |""".stripMargin
  ) {
    val officeId = anyOfficeId

    val officeRepository = mock[OfficeRepository[IO]]
    whenF(officeRepository.archive(any)) thenReturn ()
    val officeService = new OfficeService[IO](officeRepository)

    for {
      _ <- officeService.archiveOffice(officeId)
    } yield {
      verify(officeRepository, only).archive(eqTo(officeId))
      success
    }
  }

  test(
    """GIVEN an office ID
      | WHEN archiveOffice is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val officeId = anyOfficeId

    val repositoryException = new RuntimeException("intended exception")
    val officeRepository = whenF(mock[OfficeRepository[IO]].archive(any)) thenFailWith repositoryException
    val officeService = new OfficeService[IO](officeRepository)

    for {
      result <- officeService.archiveOffice(officeId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == repositoryException)
    }
  }

  private lazy val anyOffice = Office(
    id = anyOfficeId,
    name = anyOfficeName,
    notes = anyOfficeNotes,
    address = anyOfficeAddress
  )

  private lazy val anyCreateOffice = CreateOffice(
    name = anyOfficeName,
    notes = anyOfficeNotes,
    address = anyOfficeAddress
  )

  private lazy val anyUpdateOffice = UpdateOffice(
    name = Some(anyOfficeName),
    notes = Some(anyOfficeNotes),
    address = anyOfficeUpdateAddress
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

  private lazy val anyOfficeUpdateAddress = UpdateAddress(
    addressLine1 = Some("Test Street"),
    addressLine2 = Some("Building 42"),
    postalCode = Some("12-345"),
    city = Some("Wroclaw"),
    country = Some("Poland")
  )

  private lazy val anyOfficeManagerId1 = UUID.fromString("7a0bd2d7-c0cf-4ebd-a0c8-db046d2398a3")
  private lazy val anyOfficeManagerId2 = UUID.fromString("c0cf4b14-ecd0-4374-ac71-159955ab031b")
}

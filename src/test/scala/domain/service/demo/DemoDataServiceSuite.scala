package io.github.avapl
package domain.service.demo

import cats.effect.IO
import cats.effect.std.Random
import domain.model.account.UserAccount
import domain.model.appmetadata.AppMetadata
import domain.model.appmetadata.AppMetadata.IsDemoDataLoaded
import domain.model.desk.Desk
import domain.model.office.Address
import domain.model.office.Office
import domain.model.reservation.DeskReservation
import domain.model.reservation.ParkingSpotReservation
import domain.model.reservation.ReservationState.Confirmed
import domain.repository.account.AccountRepository
import domain.repository.appmetadata.AppMetadataRepository
import domain.repository.desk.DeskRepository
import domain.repository.office.OfficeRepository
import domain.repository.reservation.ReservationRepository
import io.github.avapl.domain.model.parkingspot.ParkingSpot
import io.github.avapl.domain.repository.parkingspot.ParkingSpotRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import weaver.SimpleIOSuite

object DemoDataServiceSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN a demo data service
      | WHEN loadDemoData is called and no app metadata is present
      | THEN at least 1 office, 1 account, 1 desk, 1 desk reservation, 1 parking spot, 1 parking spot reservation is created
      |""".stripMargin
  ) {
    val appMetadataRepository = mock[AppMetadataRepository[IO]]
    whenF(appMetadataRepository.get[IsDemoDataLoaded](any)) thenReturn Option.empty[IsDemoDataLoaded]
    whenF(appMetadataRepository.set(any)) thenAnswer [AppMetadata] identity
    val accountRepository: AccountRepository[IO] =
      whenF(mock[AccountRepository[IO]].create(any)) thenReturn anyUserAccount
    val officeRepository: OfficeRepository[IO] =
      whenF(mock[OfficeRepository[IO]].create(any)) thenReturn anyOffice
    val deskRepository: DeskRepository[IO] =
      whenF(mock[DeskRepository[IO]].create(any)) thenReturn anyDesk
    val deskReservationRepository: ReservationRepository[IO, DeskReservation] =
      whenF(mock[ReservationRepository[IO, DeskReservation]].createReservation(any)) thenReturn anyDeskReservation
    val parkingSpotRepository: ParkingSpotRepository[IO] =
      whenF(mock[ParkingSpotRepository[IO]].create(any)) thenReturn anyParkingSpot
    val parkingSpotReservationRepository: ReservationRepository[IO, ParkingSpotReservation] =
      whenF(
        mock[ReservationRepository[IO, ParkingSpotReservation]].createReservation(any)
      ) thenReturn anyParkingSpotReservation

    for {
      random <- random
      demoDataService = {
        implicit val r = random
        new DemoDataService[IO](
          appMetadataRepository,
          accountRepository,
          officeRepository,
          deskRepository,
          deskReservationRepository,
          parkingSpotRepository,
          parkingSpotReservationRepository
        )
      }
      _ <- demoDataService.loadDemoData()
    } yield {
      verify(appMetadataRepository, times(1)).get[IsDemoDataLoaded]
      verify(appMetadataRepository, times(1)).set(IsDemoDataLoaded(true))
      verify(accountRepository, atLeast(1)).create(any)
      verify(officeRepository, atLeast(1)).create(any)
      verify(deskRepository, atLeast(1)).create(any)
      verify(deskReservationRepository, atLeast(1)).createReservation(any)
      verify(parkingSpotRepository, atLeast(1)).create(any)
      verify(parkingSpotReservationRepository, atLeast(1)).createReservation(any)
      success
    }
  }

  test(
    """GIVEN a demo data service
      | WHEN loadDemoData is called and IsDemoDataLoaded is set to false
      | THEN at least 1 office, 1 account, 1 desk, 1 desk reservation, 1 parking spot, 1 parking spot reservation is created
      |""".stripMargin
  ) {
    val appMetadataRepository = mock[AppMetadataRepository[IO]]
    whenF(appMetadataRepository.get[IsDemoDataLoaded]) thenReturn Some(IsDemoDataLoaded(false))
    whenF(appMetadataRepository.set(any)) thenAnswer [AppMetadata] identity
    val accountRepository: AccountRepository[IO] =
      whenF(mock[AccountRepository[IO]].create(any)) thenReturn anyUserAccount
    val officeRepository: OfficeRepository[IO] =
      whenF(mock[OfficeRepository[IO]].create(any)) thenReturn anyOffice
    val deskRepository: DeskRepository[IO] =
      whenF(mock[DeskRepository[IO]].create(any)) thenReturn anyDesk
    val deskReservationRepository: ReservationRepository[IO, DeskReservation] =
      whenF(mock[ReservationRepository[IO, DeskReservation]].createReservation(any)) thenReturn anyDeskReservation
    val parkingSpotRepository: ParkingSpotRepository[IO] =
      whenF(mock[ParkingSpotRepository[IO]].create(any)) thenReturn anyParkingSpot
    val parkingSpotReservationRepository: ReservationRepository[IO, ParkingSpotReservation] =
      whenF(
        mock[ReservationRepository[IO, ParkingSpotReservation]].createReservation(any)
      ) thenReturn anyParkingSpotReservation

    for {
      random <- random
      demoDataService = {
        implicit val r = random
        new DemoDataService[IO](
          appMetadataRepository,
          accountRepository,
          officeRepository,
          deskRepository,
          deskReservationRepository,
          parkingSpotRepository,
          parkingSpotReservationRepository
        )
      }
      _ <- demoDataService.loadDemoData()
    } yield {
      verify(appMetadataRepository, times(1)).get[IsDemoDataLoaded]
      verify(appMetadataRepository, times(1)).set(IsDemoDataLoaded(true))
      verify(accountRepository, atLeast(1)).create(any)
      verify(officeRepository, atLeast(1)).create(any)
      verify(deskRepository, atLeast(1)).create(any)
      verify(deskReservationRepository, atLeast(1)).createReservation(any)
      verify(parkingSpotRepository, atLeast(1)).create(any)
      verify(parkingSpotReservationRepository, atLeast(1)).createReservation(any)
      success
    }
  }

  test(
    """GIVEN a demo data service
      | WHEN loadDemoData is called and IsDemoDataLoaded is set to true
      | THEN no additional demo data is created
      |""".stripMargin
  ) {
    val appMetadataRepository: AppMetadataRepository[IO] =
      whenF(mock[AppMetadataRepository[IO]].get[IsDemoDataLoaded]) thenReturn Some(IsDemoDataLoaded(true))
    val accountRepository = mock[AccountRepository[IO]]
    val officeRepository = mock[OfficeRepository[IO]]
    val deskRepository = mock[DeskRepository[IO]]
    val deskReservationRepository = mock[ReservationRepository[IO, DeskReservation]]
    val parkingSpotRepository = mock[ParkingSpotRepository[IO]]
    val parkingSpotReservationRepository = mock[ReservationRepository[IO, ParkingSpotReservation]]

    for {
      random <- random
      demoDataService = {
        implicit val r = random
        new DemoDataService[IO](
          appMetadataRepository,
          accountRepository,
          officeRepository,
          deskRepository,
          deskReservationRepository,
          parkingSpotRepository,
          parkingSpotReservationRepository
        )
      }
      _ <- demoDataService.loadDemoData()
    } yield {
      verify(appMetadataRepository, only).get[IsDemoDataLoaded]
      verify(accountRepository, never).create(any)
      verify(officeRepository, never).create(any)
      verify(deskRepository, never).create(any)
      verify(deskReservationRepository, never).createReservation(any)
      verify(parkingSpotRepository, never).create(any)
      verify(parkingSpotReservationRepository, never).createReservation(any)
      success
    }
  }

  private lazy val anyUserAccount = UserAccount(
    id = UUID.fromString("8b7a9bdd-b729-4427-83c3-6eaee3c97171"),
    firstName = "Test",
    lastName = "User",
    email = "test.user@officebuddy.com",
    assignedOfficeId = None
  )

  private lazy val anyOffice = Office(
    id = UUID.fromString("d3c0dd9d-7052-4eb0-9084-337b9d3b969b"),
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

  private lazy val anyDesk = Desk(
    id = UUID.fromString("dcf79f8c-85bb-4e03-b6f4-04cee232a030"),
    name = "107.1",
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = anyOffice.id
  )

  private lazy val anyDeskReservation = DeskReservation(
    id = UUID.fromString("cb906434-83a7-4d1b-94b5-4efac8c5e93c"),
    userId = anyUserAccount.id,
    createdAt = LocalDateTime.parse("2024-09-23T12:00:00"),
    reservedFromDate = LocalDate.parse("2024-09-24"),
    reservedToDate = LocalDate.parse("2024-09-27"),
    state = Confirmed,
    notes = "Test notes",
    deskId = anyDesk.id
  )

  private lazy val anyParkingSpot = ParkingSpot(
    id = UUID.fromString("d3c0dd9d-7052-4eb0-9084-337b9d3b969b"),
    name = "Test Parking Spot",
    isAvailable = true,
    notes = List("Test", "Notes"),
    isHandicapped = false,
    isUnderground = false,
    officeId = anyOffice.id
  )

  private lazy val anyParkingSpotReservation = ParkingSpotReservation(
    id = UUID.fromString("cb906434-83a7-4d1b-94b5-4efac8c5e93c"),
    userId = anyUserAccount.id,
    createdAt = LocalDateTime.parse("2024-09-23T12:00:00"),
    reservedFromDate = LocalDate.parse("2024-09-24"),
    reservedToDate = LocalDate.parse("2024-09-27"),
    state = Confirmed,
    notes = "Test notes",
    parkingSpotId = anyParkingSpot.id,
    plateNumber = "DSW 58100"
  )

  private implicit lazy val random: IO[Random[IO]] =
    Random.scalaUtilRandomSeedInt[IO](0)

  private implicit lazy val logger: Logger[IO] =
    Slf4jLogger.getLogger[IO]
}

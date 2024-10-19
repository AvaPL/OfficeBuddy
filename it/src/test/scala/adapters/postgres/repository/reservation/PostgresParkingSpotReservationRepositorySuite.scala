package io.github.avapl
package adapters.postgres.repository.reservation

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.account.PostgresAccountRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import adapters.postgres.repository.parkingspot.PostgresParkingSpotRepository
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import com.softwaremill.quicklens._
import domain.model.account.UserAccount
import domain.model.error.parkingspot.ParkingSpotNotFound
import domain.model.error.reservation.OverlappingReservations
import domain.model.error.reservation.ReservationNotFound
import domain.model.error.user.UserNotFound
import domain.model.office.Address
import domain.model.office.Office
import domain.model.parkingspot.ParkingSpot
import domain.model.reservation.ParkingSpotReservation
import domain.model.reservation.ReservationState
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import skunk.Command
import skunk.Session
import skunk.Void
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresParkingSpotReservationRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(name: TestName)(run: PostgresParkingSpotReservationRepository[IO] => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresReservationRepository = new PostgresParkingSpotReservationRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        insertUsers(session) >>
        insertParkingSpots(session) >>
        run(postgresReservationRepository)
    }

  beforeTest(
    """GIVEN a parking spot reservation to create
      | WHEN createReservation is called
      | THEN the reservation should be inserted into Postgres
      |""".stripMargin
  ) { reservationRepository =>
    val reservation = anyParkingSpotReservation

    for {
      _ <- reservationRepository.createReservation(reservation)
      readReservation <- reservationRepository.readReservation(reservation.id)
    } yield expect(readReservation == reservation)
  }

  beforeTest(
    """GIVEN a parking spot reservation with non-existent parking spot ID
      | WHEN createReservation is called
      | THEN the call should fail with ParkingSpotNotFound
      |""".stripMargin
  ) { reservationRepository =>
    val parkingSpotId = UUID.fromString("1df91a65-80a9-400b-af78-5c72897e75f7")
    val reservation = anyParkingSpotReservation.copy(parkingSpotId = parkingSpotId)

    for {
      result <- reservationRepository.createReservation(reservation).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val parkingSpotNotFound = ParkingSpotNotFound(parkingSpotId)
        expect(throwable == parkingSpotNotFound)
    }
  }

  beforeTest(
    """GIVEN a parking spot reservation with non-existent user ID
      | WHEN createReservation is called
      | THEN the call should fail with UserNotFound
      |""".stripMargin
  ) { reservationRepository =>
    val userId = UUID.fromString("fe124fff-e7fa-4ac8-a6c0-a2357aaa2dd9")
    val reservation = anyParkingSpotReservation.copy(userId = userId)

    for {
      result <- reservationRepository.createReservation(reservation).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val userNotFound = UserNotFound(userId)
        expect(throwable == userNotFound)
    }
  }

  beforeTest(
    """GIVEN parking spot reservations that overlap with existing reservation
      | WHEN createReservation is called
      | THEN the call should fail with OverlappingReservations
      |""".stripMargin
  ) { reservationRepository =>
    val reservation = anyParkingSpotReservation.copy(
      reservedFromDate = LocalDate.parse("2023-07-21"),
      reservedToDate = LocalDate.parse("2023-07-24")
    )
    val overlappingReservation1 = reservation
      .copy(id = UUID.fromString("30c9119e-a866-48c8-85cd-1a5b4ee193a3"))
      .modifyAll(_.reservedFromDate, _.reservedToDate)
      .using(_.plusDays(1))
    val overlappingReservation2 = reservation
      .copy(id = UUID.fromString("195eda92-5556-4eb7-b225-46059e86fe8a"))
      .modifyAll(_.reservedFromDate, _.reservedToDate)
      .using(_.minusDays(1))
    val overlappingReservation3 = reservation
      .copy(id = UUID.fromString("9c31d5c5-a523-450a-84ca-a444e3a60d6a"))
      .modify(_.reservedFromDate)
      .using(_.minusDays(1))
      .modify(_.reservedToDate)
      .using(_.plusDays(1))
    val overlappingReservation4 = reservation
      .copy(id = UUID.fromString("151ca0b8-3061-4b61-8e6a-d937387d4c34"))
      .modify(_.reservedFromDate)
      .using(_.plusDays(1))
      .modify(_.reservedToDate)
      .using(_.minusDays(1))
    val overlappingReservation5 = reservation
      .copy(id = UUID.fromString("3586cef5-6ab7-4a35-904c-2af18ac3fc54"))
      .modify(_.reservedFromDate)
      .setTo(reservation.reservedToDate)

    for {
      _ <- reservationRepository.createReservation(reservation)
      results <- List(
        overlappingReservation1,
        overlappingReservation2,
        overlappingReservation3,
        overlappingReservation4,
        overlappingReservation5
      ).traverse(reservationRepository.createReservation(_).attempt)
    } yield forEach(results) { result =>
      matches(result) {
        case Left(throwable) =>
          expect(throwable == OverlappingReservations)
      }
    }
  }

  beforeTest(
    """GIVEN 2 parking spot reservations for the same parking spot one right after another
      | WHEN createReservation is called
      | THEN both reservations should be inserted successfully
      |""".stripMargin
  ) { reservationRepository =>
    val reservation1 = anyParkingSpotReservation.copy(
      id = parkingSpotId1,
      reservedFromDate = LocalDate.parse("2023-07-23"),
      reservedToDate = LocalDate.parse("2023-07-24")
    )
    val reservation2 = anyParkingSpotReservation.copy(
      id = UUID.fromString("195eda92-5556-4eb7-b225-46059e86fe8a"),
      reservedFromDate = LocalDate.parse("2023-07-25"),
      reservedToDate = LocalDate.parse("2023-07-25")
    )

    for {
      _ <- reservationRepository.createReservation(reservation1)
      _ <- reservationRepository.createReservation(reservation2)
    } yield success
  }

  beforeTest(
    """GIVEN 2 parking spot reservations at the same time for different parking spots
      | WHEN createReservation is called
      | THEN both reservations should be inserted successfully
      |""".stripMargin
  ) { reservationRepository =>
    val reservation1 = anyParkingSpotReservation.copy(parkingSpotId = parkingSpotId1)
    val reservation2 = reservation1.copy(
      id = UUID.fromString("30c9119e-a866-48c8-85cd-1a5b4ee193a3"),
      parkingSpotId = parkingSpotId2
    )

    for {
      _ <- reservationRepository.createReservation(reservation1)
      _ <- reservationRepository.createReservation(reservation2)
    } yield success
  }

  beforeTest(
    """GIVEN a non-existent reservation ID
      | WHEN readReservation is called
      | THEN the call should fail with ReservationNotFound
      |""".stripMargin
  ) { reservationRepository =>
    val reservationId = anyReservationId

    for {
      result <- reservationRepository.readReservation(reservationId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val reservationNotFound = ReservationNotFound(reservationId)
        expect(throwable == reservationNotFound)
    }
  }

  beforeTest(
    """GIVEN a non-existent reservation ID
      | WHEN readReservationState is called
      | THEN the call should fail with ReservationNotFound
      |""".stripMargin
  ) { reservationRepository =>
    val reservationId = anyReservationId

    for {
      result <- reservationRepository.readReservationState(reservationId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val reservationNotFound = ReservationNotFound(reservationId)
        expect(throwable == reservationNotFound)
    }
  }

  beforeTest(
    """GIVEN a reservation and a state
      | WHEN updateReservationState is called
      | THEN the state should be updated
      |""".stripMargin
  ) { reservationRepository =>
    val reservation = anyParkingSpotReservation.copy(state = ReservationState.Pending)
    val newState = ReservationState.Confirmed

    for {
      _ <- reservationRepository.createReservation(reservation)
      _ <- reservationRepository.updateReservationState(reservation.id, newState)
      readState <- reservationRepository.readReservationState(reservation.id)
    } yield expect(readState == newState)
  }

  beforeTest(
    """GIVEN a reservation and a state
      | WHEN updateReservationState is called with the same state as the current state
      | THEN the call should not fail (no-op)
      |""".stripMargin
  ) { reservationRepository =>
    val reservation = anyParkingSpotReservation

    for {
      _ <- reservationRepository.createReservation(reservation)
      _ <- reservationRepository.updateReservationState(reservation.id, reservation.state)
    } yield success
  }

  beforeTest(
    """GIVEN a non-existent reservation ID and a state
      | WHEN updateReservationState is called
      | THEN the call should fail with ReservationNotFound
      |""".stripMargin
  ) { reservationRepository =>
    val reservationId = anyReservationId
    val newState = ReservationState.Confirmed

    for {
      result <- reservationRepository.updateReservationState(reservationId, newState).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val reservationNotFound = ReservationNotFound(reservationId)
        expect(throwable == reservationNotFound)
    }
  }

  private def truncateTables(session: Resource[IO, Session[IO]]) =
    truncateReservationTable(session) >>
      truncateParkingSpotTable(session) >>
      truncateAccountTable(session) >>
      truncateOfficeTable(session)

  private def truncateReservationTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE reservation CASCADE
      """.command
    session.use(_.execute(sql))
  }

  private def truncateParkingSpotTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE parking_spot CASCADE
      """.command
    session.use(_.execute(sql))
  }

  private def truncateAccountTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE account CASCADE
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
    val office = anyOffice(officeId1)
    officeRepository.create(office)
  }

  private def anyOffice(officeId: UUID) = Office(
    id = officeId,
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

  private def insertUsers(session: Resource[IO, Session[IO]]) = {
    val accountRepository = new PostgresAccountRepository[IO](session)
    val user = anyUser(userId1)
    accountRepository.create(user)
  }

  private def anyUser(userId: UUID) = UserAccount(
    id = userId,
    firstName = "Test",
    lastName = "User",
    email = "test.user@postgres.localhost",
    assignedOfficeId = Some(officeId1)
  )

  private def insertParkingSpots(session: Resource[IO, Session[IO]]) = {
    val parkingSpotRepository = new PostgresParkingSpotRepository[IO](session)
    val parkingSpot1 = anyParkingSpot(parkingSpotId1, "A1", officeId1)
    val parkingSpot2 = anyParkingSpot(parkingSpotId2, "A2", officeId1)
    List(parkingSpot1, parkingSpot2).parTraverse_(parkingSpotRepository.create)
  }

  private def anyParkingSpot(parkingSpotId: UUID, name: String, officeId: UUID) = ParkingSpot(
    id = parkingSpotId,
    name = name,
    isAvailable = true,
    notes = List("Near the entrance"),
    isHandicapped = true,
    isUnderground = true,
    officeId = officeId
  )

  private lazy val officeId1 = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")

  private lazy val userId1 = UUID.fromString("8c229781-4235-4e59-bc20-04abb620cbfa")

  private lazy val parkingSpotId1 = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")

  private lazy val parkingSpotId2 = UUID.fromString("75f0c1ff-8cf0-4161-a82a-0cae74078d46")

  private lazy val anyParkingSpotReservation = ParkingSpotReservation(
    id = anyReservationId,
    userId = userId1,
    createdAt = LocalDateTime.parse("2023-07-18T20:41:00"),
    reservedFromDate = LocalDate.parse("2023-07-19"),
    reservedToDate = LocalDate.parse("2023-07-20"),
    state = ReservationState.Pending,
    notes = "Reserved for a visitor",
    parkingSpotId = parkingSpotId1,
    plateNumber = "ABC 123"
  )

  private lazy val anyReservationId = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")
}

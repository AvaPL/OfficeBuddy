package io.github.avapl
package adapters.postgres.repository.reservation

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.account.PostgresAccountRepository
import adapters.postgres.repository.desk.PostgresDeskRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import com.softwaremill.quicklens._
import domain.model.account.UserAccount
import domain.model.desk.Desk
import domain.model.error.desk.DeskNotFound
import domain.model.error.reservation.OverlappingReservations
import domain.model.error.reservation.ReservationNotFound
import domain.model.error.user.UserNotFound
import domain.model.office.Address
import domain.model.office.Office
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import skunk.Command
import skunk.Session
import skunk.Void
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresReservationRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(name: TestName)(run: PostgresReservationRepository[IO] => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresReservationRepository = new PostgresReservationRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        insertUsers(session) >>
        insertDesks(session) >>
        run(postgresReservationRepository)
    }

  beforeTest(
    """GIVEN a desk reservation to create
      | WHEN createDeskReservation is called
      | THEN the desk reservation should be inserted into Postgres
      |""".stripMargin
  ) { reservationRepository =>
    val deskReservation = anyDeskReservation

    for {
      _ <- reservationRepository.createDeskReservation(deskReservation)
      readDeskReservation <- reservationRepository.readDeskReservation(deskReservation.id)
    } yield expect(readDeskReservation == deskReservation)
  }

  beforeTest(
    """GIVEN a desk reservation with non-existent desk ID
      | WHEN createDeskReservation is called
      | THEN the call should fail with DeskNotFound
      |""".stripMargin
  ) { reservationRepository =>
    val deskId = UUID.fromString("1df91a65-80a9-400b-af78-5c72897e75f7")
    val deskReservation = anyDeskReservation.copy(deskId = deskId)

    for {
      result <- reservationRepository.createDeskReservation(deskReservation).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val deskNotFound = DeskNotFound(deskId)
        expect(throwable == deskNotFound)
    }
  }

  beforeTest(
    """GIVEN a desk reservation with non-existent user ID
      | WHEN createDeskReservation is called
      | THEN the call should fail with UserNotFound
      |""".stripMargin
  ) { reservationRepository =>
    val userId = UUID.fromString("fe124fff-e7fa-4ac8-a6c0-a2357aaa2dd9")
    val deskReservation = anyDeskReservation.copy(userId = userId)

    for {
      result <- reservationRepository.createDeskReservation(deskReservation).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val userNotFound = UserNotFound(userId)
        expect(throwable == userNotFound)
    }
  }

  beforeTest(
    """GIVEN desk reservations that overlap with existing reservation
      | WHEN createDeskReservation is called
      | THEN the call should fail with OverlappingReservations
      |""".stripMargin
  ) { reservationRepository =>
    val deskReservation = anyDeskReservation.copy(
      reservedFrom = LocalDate.parse("2023-07-21").atStartOfDay(),
      reservedTo = LocalDate.parse("2023-07-24").atTime(LocalTime.MAX)
    )
    val overlappingDeskReservation1 = deskReservation
      .copy(id = UUID.fromString("30c9119e-a866-48c8-85cd-1a5b4ee193a3"))
      .modifyAll(_.reservedFrom, _.reservedFrom)
      .using(_.plusDays(1))
    val overlappingDeskReservation2 = deskReservation
      .copy(id = UUID.fromString("195eda92-5556-4eb7-b225-46059e86fe8a"))
      .modifyAll(_.reservedFrom, _.reservedFrom)
      .using(_.minusDays(1))
    val overlappingDeskReservation3 = deskReservation
      .copy(id = UUID.fromString("9c31d5c5-a523-450a-84ca-a444e3a60d6a"))
      .modify(_.reservedFrom)
      .using(_.minusDays(1))
      .modify(_.reservedTo)
      .using(_.plusDays(1))
    val overlappingDeskReservation4 = deskReservation
      .copy(id = UUID.fromString("151ca0b8-3061-4b61-8e6a-d937387d4c34"))
      .modify(_.reservedFrom)
      .using(_.plusDays(1))
      .modify(_.reservedTo)
      .using(_.minusDays(1))

    for {
      _ <- reservationRepository.createDeskReservation(deskReservation)
      results <- List(
        overlappingDeskReservation1,
        overlappingDeskReservation2,
        overlappingDeskReservation3,
        overlappingDeskReservation4
      ).traverse(reservationRepository.createDeskReservation(_).attempt)
    } yield forEach(results) { result =>
      matches(result) {
        case Left(throwable) =>
          expect(throwable == OverlappingReservations)
      }
    }
  }

  beforeTest(
    """GIVEN 2 desk reservations at the same time for different desks
      | WHEN createDeskReservation is called
      | THEN both reservation should be inserted successfully
      |""".stripMargin
  ) { reservationRepository =>
    val deskReservation1 = anyDeskReservation.copy(deskId = deskId1)
    val deskReservation2 = deskReservation1.copy(
      id = UUID.fromString("30c9119e-a866-48c8-85cd-1a5b4ee193a3"),
      deskId = deskId2
    )

    for {
      _ <- reservationRepository.createDeskReservation(deskReservation1)
      _ <- reservationRepository.createDeskReservation(deskReservation2)
    } yield success
  }

  beforeTest(
    """GIVEN a non-existent reservation ID
      | WHEN readDeskReservation is called
      | THEN the call should fail with ReservationNotFound
      |""".stripMargin
  ) { reservationRepository =>
    val reservationId = anyReservationId

    for {
      result <- reservationRepository.readDeskReservation(reservationId).attempt
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
    val deskReservation = anyDeskReservation.copy(state = ReservationState.Pending)
    val newState = ReservationState.Confirmed

    for {
      _ <- reservationRepository.createDeskReservation(deskReservation)
      _ <- reservationRepository.updateReservationState(deskReservation.id, newState)
      readState <- reservationRepository.readReservationState(deskReservation.id)
    } yield expect(readState == newState)
  }

  beforeTest(
    """GIVEN a reservation and a state
      | WHEN updateReservationState is called with the same state as the current state
      | THEN the call should not fail (no-op)
      |""".stripMargin
  ) { reservationRepository =>
    val deskReservation = anyDeskReservation

    for {
      _ <- reservationRepository.createDeskReservation(deskReservation)
      _ <- reservationRepository.updateReservationState(deskReservation.id, deskReservation.state)
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
      truncateDeskTable(session) >>
      truncateAccountTable(session) >>
      truncateOfficeTable(session)

  private def truncateReservationTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE reservation CASCADE
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

  private def insertDesks(session: Resource[IO, Session[IO]]) = {
    val deskRepository = new PostgresDeskRepository[IO](session)
    val desk1 = anyDesk(deskId1, "desk1", officeId1)
    val desk2 = anyDesk(deskId2, "desk2", officeId1)
    List(desk1, desk2).parTraverse_(deskRepository.create)
  }

  private def anyDesk(deskId: UUID, name: String, officeId: UUID) = Desk(
    id = deskId,
    name = name,
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = officeId
  )

  private lazy val officeId1 = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")

  private lazy val userId1 = UUID.fromString("8c229781-4235-4e59-bc20-04abb620cbfa")

  private lazy val deskId1 = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")

  private lazy val deskId2 = UUID.fromString("75f0c1ff-8cf0-4161-a82a-0cae74078d46")

  private lazy val anyDeskReservation = DeskReservation(
    id = anyReservationId,
    userId = userId1,
    createdAt = LocalDateTime.parse("2023-07-18T20:41:00"),
    reservedFrom = LocalDateTime.parse("2023-07-19T00:00:00"),
    reservedTo = LocalDateTime.parse("2023-07-20T23:59:59"),
    state = ReservationState.Pending,
    notes = "Please remove the duck from the desk, it scares me",
    deskId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")
  )

  private lazy val anyReservationId = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")
}

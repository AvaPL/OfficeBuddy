package io.github.avapl
package adapters.postgres.repository.desk.view

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.account.PostgresAccountRepository
import adapters.postgres.repository.desk.PostgresDeskRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import adapters.postgres.repository.reservation.PostgresDeskReservationRepository
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import domain.model.account.UserAccount
import domain.model.desk.Desk
import domain.model.office.Address
import domain.model.office.Office
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.model.reservation.ReservationState._
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import skunk._
import skunk.Command
import skunk.Session
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresDeskViewRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(
    name: TestName
  )(
    run: (
      PostgresDeskRepository[IO],
      PostgresDeskViewRepository[IO],
      PostgresDeskReservationRepository[IO]
    ) => IO[Expectations]
  ): Unit =
    test(name) { session =>
      lazy val postgresDeskRepository = new PostgresDeskRepository[IO](session)
      lazy val postgresDeskViewRepository = new PostgresDeskViewRepository[IO](session)
      lazy val postgresReservationRepository = new PostgresDeskReservationRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        insertUsers(session) >>
        run(postgresDeskRepository, postgresDeskViewRepository, postgresReservationRepository)
    }

  beforeTest(
    """GIVEN 3 desks in the database assigned to an office
      | WHEN listDesks is called with limit 2 and offsets 0 and 2
      | THEN return two pages of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val officeId = officeId1
    val desk1 = anyDesk.copy(id = anyDeskId1, name = "desk1", officeId = officeId)
    val desk2 = anyDesk.copy(id = anyDeskId2, name = "desk2", officeId = officeId)
    val desk3 = anyDesk.copy(id = anyDeskId3, name = "desk3", officeId = officeId)

    for {
      _ <- deskRepository.create(desk1)
      _ <- deskRepository.create(desk2)
      _ <- deskRepository.create(desk3)
      deskListView1 <- deskViewRepository.listDesks(officeId1, limit = 2, offset = 0)
      deskListView2 <- deskViewRepository.listDesks(officeId1, limit = 2, offset = 2)
    } yield expect.all(
      deskListView1.desks.map(_.id) == List(desk1.id, desk2.id),
      deskListView1.pagination.hasMoreResults,
      deskListView2.desks.map(_.id) == List(desk3.id),
      !deskListView2.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 desk in the database assigned to an office
      | WHEN listDesks is called with limit 1 and offset 1
      | THEN return an empty list of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val desk = anyDesk

    for {
      _ <- deskRepository.create(desk)
      deskListView <- deskViewRepository.listDesks(desk.officeId, limit = 1, offset = 1)
    } yield expect.all(
      deskListView.desks.isEmpty,
      !deskListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 archived desk in the database assigned to an office
      | WHEN listDesks is called with limit 1 and offset 0
      | THEN return an empty list of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val desk = anyDesk.copy(isArchived = true)

    for {
      _ <- deskRepository.create(desk)
      deskListView <- deskViewRepository.listDesks(desk.officeId, limit = 1, offset = 0)
    } yield expect.all(
      deskListView.desks.isEmpty,
      !deskListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 desks in the database
      | WHEN listDesks is called with officeId matching 2 desks
      | THEN return matching desks
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val matchingDesk1 = anyDesk.copy(id = anyDeskId1, name = "desk1", officeId = officeId1)
    val matchingDesk2 = anyDesk.copy(id = anyDeskId2, name = "desk2", officeId = officeId1)
    val notMatchingDesk = anyDesk.copy(id = anyDeskId3, name = "desk3", officeId = officeId2)

    for {
      _ <- deskRepository.create(matchingDesk1)
      _ <- deskRepository.create(matchingDesk2)
      _ <- deskRepository.create(notMatchingDesk)
      deskListView <- deskViewRepository.listDesks(officeId1, limit = 10, offset = 0)
    } yield expect.all(
      deskListView.desks.map(_.id) == List(matchingDesk1.id, matchingDesk2.id),
      !deskListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 desks in the database
      | WHEN listDesks is called with officeId matching 2 desks, limit 1 and offset 1
      | THEN return the matching desk and no more results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val matchingDesk1 = anyDesk.copy(id = anyDeskId1, name = "desk1", officeId = officeId1)
    val matchingDesk2 = anyDesk.copy(id = anyDeskId2, name = "desk2", officeId = officeId1)
    val notMatchingDesk = anyDesk.copy(id = anyDeskId3, name = "desk3", officeId = officeId2)

    for {
      _ <- deskRepository.create(matchingDesk1)
      _ <- deskRepository.create(matchingDesk2)
      _ <- deskRepository.create(notMatchingDesk)
      deskListView <- deskViewRepository.listDesks(officeId1, limit = 1, offset = 1)
    } yield expect.all(
      deskListView.desks.map(_.id) == List(matchingDesk2.id),
      !deskListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 desk in the database assigned to an office
      | WHEN listDesks is called with officeId not matching the desk
      | THEN return an empty list of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val notMatchingDesk = anyDesk.copy(officeId = officeId2)

    for {
      _ <- deskRepository.create(notMatchingDesk)
      deskListView <- deskViewRepository.listDesks(officeId1, limit = 10, offset = 0)
    } yield expect.all(
      deskListView.desks.isEmpty,
      !deskListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 desks in the database assigned to an office
      | WHEN listDesksAvailableForReservation is called and no reservations exist
      | THEN return all desks
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val officeId = officeId1
    val desk1 = anyDesk.copy(id = anyDeskId1, name = "desk1", officeId = officeId)
    val desk2 = anyDesk.copy(id = anyDeskId2, name = "desk2", officeId = officeId)
    val desk3 = anyDesk.copy(id = anyDeskId3, name = "desk3", officeId = officeId)
    val reservationFrom = LocalDate.parse("2024-09-23")
    val reservationTo = reservationFrom.plusDays(1)

    for {
      _ <- deskRepository.create(desk1)
      _ <- deskRepository.create(desk2)
      _ <- deskRepository.create(desk3)
      reservableDesks <- deskViewRepository.listDesksAvailableForReservation(officeId, reservationFrom, reservationTo)
    } yield expect(reservableDesks.map(_.id) == List(desk1.id, desk2.id, desk3.id))
  }

  beforeTest(
    """GIVEN 5 desks in the database assigned to an office, where 4 have Confirmed reservations
      | WHEN listDesksAvailableForReservation is called with period overlapping all reservations
      | THEN return only the free desks
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, reservationRepository) =>
    val officeId = officeId1
    val reservedDesk1 = anyDesk.copy(id = anyDeskId1, name = "desk1", officeId = officeId)
    val reservedDesk2 = anyDesk.copy(id = anyDeskId2, name = "desk2", officeId = officeId)
    val reservedDesk3 = anyDesk.copy(id = anyDeskId3, name = "desk3", officeId = officeId)
    val reservedDesk4 = anyDesk.copy(id = anyDeskId4, name = "desk4", officeId = officeId)
    val freeDesk = anyDesk.copy(id = anyDeskId5, name = "desk5", officeId = officeId)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-24")

    val reservationDesk1 = createDeskReservation(
      id = anyReservationId1,
      reservedFromDate = LocalDate.parse("2024-09-19"),
      reservedToDate = LocalDate.parse("2024-09-25"),
      deskId = reservedDesk1.id
    )
    val reservationDesk2 = createDeskReservation(
      id = anyReservationId2,
      reservedFromDate = LocalDate.parse("2024-09-21"),
      reservedToDate = LocalDate.parse("2024-09-23"),
      deskId = reservedDesk2.id
    )
    val reservationDesk3 = createDeskReservation(
      id = anyReservationId3,
      reservedFromDate = LocalDate.parse("2024-09-21"),
      reservedToDate = LocalDate.parse("2024-09-25"),
      deskId = reservedDesk3.id
    )
    val reservationDesk4 = createDeskReservation(
      id = anyReservationId4,
      reservedFromDate = LocalDate.parse("2024-09-19"),
      reservedToDate = LocalDate.parse("2024-09-23"),
      deskId = reservedDesk4.id
    )

    for {
      _ <- deskRepository.create(reservedDesk1)
      _ <- deskRepository.create(reservedDesk2)
      _ <- deskRepository.create(reservedDesk3)
      _ <- deskRepository.create(reservedDesk4)
      _ <- deskRepository.create(freeDesk)
      _ <- reservationRepository.createReservation(reservationDesk1)
      _ <- reservationRepository.createReservation(reservationDesk2)
      _ <- reservationRepository.createReservation(reservationDesk3)
      _ <- reservationRepository.createReservation(reservationDesk4)
      reservableDesks <- deskViewRepository.listDesksAvailableForReservation(officeId, reservationFrom, reservationTo)
    } yield expect(reservableDesks.map(_.id) == List(freeDesk.id))
  }

  beforeTest(
    """GIVEN 3 desks in the database assigned to an office, where all have Confirmed reservations
      | WHEN listDesksAvailableForReservation is called with period overlapping only some of the reservations
      | THEN return only the free desks
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, reservationRepository) =>
    val officeId = officeId1
    val desk1 = anyDesk.copy(id = anyDeskId1, name = "desk1", officeId = officeId)
    val desk2 = anyDesk.copy(id = anyDeskId2, name = "desk2", officeId = officeId)
    val desk3 = anyDesk.copy(id = anyDeskId3, name = "desk3", officeId = officeId)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-21")

    val overlappingReservationDesk1 = createDeskReservation(
      id = anyReservationId1,
      reservedFromDate = LocalDate.parse("2024-09-20"),
      reservedToDate = LocalDate.parse("2024-09-20"),
      deskId = desk1.id
    )
    val overlappingReservationDesk2 = createDeskReservation(
      id = anyReservationId2,
      reservedFromDate = LocalDate.parse("2024-09-21"),
      reservedToDate = LocalDate.parse("2024-09-21"),
      deskId = desk2.id
    )
    val nonOverlappingReservationDesk3 = createDeskReservation(
      id = anyReservationId3,
      reservedFromDate = LocalDate.parse("2024-09-22"),
      reservedToDate = LocalDate.parse("2024-09-22"),
      deskId = desk3.id
    )

    for {
      _ <- deskRepository.create(desk1)
      _ <- deskRepository.create(desk2)
      _ <- deskRepository.create(desk3)
      _ <- reservationRepository.createReservation(overlappingReservationDesk1)
      _ <- reservationRepository.createReservation(overlappingReservationDesk2)
      _ <- reservationRepository.createReservation(nonOverlappingReservationDesk3)
      reservableDesks <- deskViewRepository.listDesksAvailableForReservation(officeId, reservationFrom, reservationTo)
    } yield expect(reservableDesks.map(_.id) == List(desk3.id))
  }

  beforeTest(
    """GIVEN 2 desks with the same name in 2 offices and reservation of the desk in office 2
      | WHEN listDesksAvailableForReservation is called with office 1 and period overlapping the reservation of desk in office 2
      | THEN return the free desk in office 1
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, reservationRepository) =>
    val office1Desk = anyDesk.copy(id = anyDeskId1, name = "desk", officeId = officeId1)
    val office2Desk = anyDesk.copy(id = anyDeskId2, name = "desk", officeId = officeId2)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-21")

    val office2DeskReservation = createDeskReservation(
      id = anyReservationId1,
      reservedFromDate = reservationFrom,
      reservedToDate = reservationTo,
      deskId = office2Desk.id
    )

    for {
      _ <- deskRepository.create(office1Desk)
      _ <- deskRepository.create(office2Desk)
      _ <- reservationRepository.createReservation(office2DeskReservation)
      reservableDesks <- deskViewRepository.listDesksAvailableForReservation(officeId1, reservationFrom, reservationTo)
    } yield expect(reservableDesks.map(_.id) == List(office1Desk.id))
  }

  beforeTest(
    """GIVEN 1 desk assigned to an office and a Pending reservation
      | WHEN listDesksAvailableForReservation is called with period overlapping the reservation
      | THEN return an empty list of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, reservationRepository) =>
    val desk = anyDesk.copy(id = anyDeskId1, name = "desk", officeId = officeId1)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-21")

    val pendingReservation = createDeskReservation(
      id = anyReservationId1,
      reservedFromDate = reservationFrom,
      reservedToDate = reservationTo,
      state = Pending,
      deskId = desk.id
    )

    for {
      _ <- deskRepository.create(desk)
      _ <- reservationRepository.createReservation(pendingReservation)
      reservableDesks <- deskViewRepository.listDesksAvailableForReservation(officeId1, reservationFrom, reservationTo)
    } yield expect(reservableDesks.isEmpty)
  }

  beforeTest(
    """GIVEN 1 desk assigned to an office and Cancelled and Rejected reservations
      | WHEN listDesksAvailableForReservation is called with period overlapping the reservations
      | THEN the desk is returned as free
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, reservationRepository) =>
    val desk = anyDesk.copy(id = anyDeskId1, name = "desk", officeId = officeId1)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-21")

    def createOverlappingReservation(id: UUID, state: ReservationState) = createDeskReservation(
      id = id,
      reservedFromDate = reservationFrom,
      reservedToDate = reservationTo,
      state = state,
      deskId = desk.id
    )

    val cancelledReservation = createOverlappingReservation(id = anyReservationId1, state = Cancelled)
    val rejectedReservation = createOverlappingReservation(id = anyReservationId2, state = Rejected)

    for {
      _ <- deskRepository.create(desk)
      _ <- reservationRepository.createReservation(cancelledReservation)
      _ <- reservationRepository.createReservation(rejectedReservation)
      reservableDesks <- deskViewRepository.listDesksAvailableForReservation(officeId1, reservationFrom, reservationTo)
    } yield expect(reservableDesks.map(_.id) == List(desk.id))
  }

  beforeTest(
    """GIVEN 1 archived desk and 1 unavailable desk
      | WHEN listDesksAvailableForReservation is called
      | THEN return an empty list of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val archivedDesk =
      anyDesk.copy(id = anyDeskId1, name = "archivedDesk", officeId = officeId1, isArchived = true)
    val unavailableDesk =
      anyDesk.copy(id = anyDeskId2, name = "unavailableDesk", officeId = officeId1, isAvailable = false)

    for {
      _ <- deskRepository.create(archivedDesk)
      _ <- deskRepository.create(unavailableDesk)
      reservableDesks <- deskViewRepository.listDesksAvailableForReservation(
        officeId1,
        reservationFrom = LocalDate.parse("2024-09-20"),
        reservationTo = LocalDate.parse("2024-09-21")
      )
    } yield expect(reservableDesks.isEmpty)
  }

  beforeTest(
    """GIVEN 1 desk assigned to an office
      | WHEN listDesksAvailableForReservation is called with reservationFrom equal to reservationTo
      | THEN return the desk
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val desk = anyDesk.copy(id = anyDeskId1, name = "desk", officeId = officeId1)

    for {
      _ <- deskRepository.create(desk)
      reservableDesks <- deskViewRepository.listDesksAvailableForReservation(
        officeId1,
        reservationFrom = LocalDate.parse("2024-09-20"),
        reservationTo = LocalDate.parse("2024-09-20")
      )
    } yield expect(reservableDesks.map(_.id) == List(desk.id))
  }

  beforeTest(
    """GIVEN 1 desk assigned to an office
      | WHEN listDesksAvailableForReservation is called with reservationFrom after reservationTo
      | THEN return an empty list of results
      |""".stripMargin
  ) { (deskRepository, deskViewRepository, _) =>
    val desk = anyDesk.copy(id = anyDeskId1, name = "desk", officeId = officeId1)

    for {
      _ <- deskRepository.create(desk)
      reservableDesks <- deskViewRepository.listDesksAvailableForReservation(
        officeId1,
        reservationFrom = LocalDate.parse("2024-09-21"),
        reservationTo = LocalDate.parse("2024-09-20")
      )
    } yield expect(reservableDesks.isEmpty)
  }

  // TODO: Verify that other types of reservations (parking, meeting room) are not considered as overlapping

  private def truncateTables(session: Resource[IO, Session[IO]]) =
    truncateReservationTable(session) >>
      truncateAccountTable(session) >>
      truncateDeskTable(session) >>
      truncateOfficeTable(session)

  private def truncateReservationTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE reservation CASCADE
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

  private def truncateDeskTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE desk CASCADE
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

  private def insertUsers(session: Resource[IO, Session[IO]]) = {
    val userRepository = new PostgresAccountRepository[IO](session)
    val user1 = anyUser(userId1, "John", "Doe")
    val user2 = anyUser(userId2, "Jane", "Smith")
    List(user1, user2).parTraverse_(userRepository.create)
  }

  private def anyUser(userId: UUID, firstName: String, lastName: String) =
    UserAccount(
      id = userId,
      firstName = firstName,
      lastName = lastName,
      email = s"$firstName.$lastName@example.com",
      assignedOfficeId = None
    )

  private def createDeskReservation(
    id: UUID,
    deskId: UUID,
    reservedFromDate: LocalDate,
    reservedToDate: LocalDate,
    state: ReservationState = Confirmed
  ) = DeskReservation(
    id = id,
    userId = userId1,
    createdAt = LocalDateTime.parse("2024-09-23T12:00:00"),
    reservedFromDate = reservedFromDate,
    reservedToDate = reservedToDate,
    state = state,
    notes = "",
    deskId = deskId
  )

  private lazy val officeId1 = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")
  private lazy val officeId2 = UUID.fromString("c1e29bfd-5a8a-468f-ba27-4673c42fec04")

  private lazy val userId1 = UUID.fromString("59b07490-6a69-4912-9777-9076fb23c9db")
  private lazy val userId2 = UUID.fromString("feb53d33-c2a6-4734-b178-4edd058c83f2")

  private lazy val anyDesk = Desk(
    id = anyDeskId1,
    name = "Test Desk",
    isAvailable = true,
    notes = List("Test", "Notes"),
    isStanding = false,
    monitorsCount = 2,
    hasPhone = false,
    officeId = officeId1
  )

  private lazy val anyDeskId1 = UUID.fromString("cecd6ead-2d55-4eef-901e-273aaf6fb23a")
  private lazy val anyDeskId2 = UUID.fromString("4a565b76-eabf-497c-9ea9-7f935ffdfcd6")
  private lazy val anyDeskId3 = UUID.fromString("56a1eb04-5014-48f6-9241-e7f169b16177")
  private lazy val anyDeskId4 = UUID.fromString("ab01dc75-a1ef-4b99-9bdb-875cfccd7d4c")
  private lazy val anyDeskId5 = UUID.fromString("2de6785c-626a-464c-a5ca-b165d6f00e30")

  private lazy val anyReservationId1 = UUID.fromString("eb2cc050-de32-4087-87e3-cac25dc64a92")
  private lazy val anyReservationId2 = UUID.fromString("32afa97c-388f-43ba-a737-3121642452c4")
  private lazy val anyReservationId3 = UUID.fromString("c16737f6-b5ae-45a9-9ea0-9e3e7fb2568d")
  private lazy val anyReservationId4 = UUID.fromString("547d7782-cf8b-44ea-95cc-9aa9dac63553")
}

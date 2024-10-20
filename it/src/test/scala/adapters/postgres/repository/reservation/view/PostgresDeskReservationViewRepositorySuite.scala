package io.github.avapl
package adapters.postgres.repository.reservation.view

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.account.PostgresAccountRepository
import adapters.postgres.repository.desk.PostgresDeskRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import adapters.postgres.repository.reservation.PostgresDeskReservationRepository
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import com.softwaremill.quicklens._
import domain.model.account.UserAccount
import domain.model.desk.Desk
import domain.model.office.Address
import domain.model.office.Office
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState.Cancelled
import domain.model.reservation.ReservationState.Confirmed
import domain.model.reservation.ReservationState.Pending
import domain.model.reservation.ReservationState.Rejected
import domain.model.reservation.view.DeskView
import domain.model.reservation.view.UserView
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

object PostgresDeskReservationViewRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(
    name: TestName
  )(run: (PostgresDeskReservationRepository[IO], PostgresDeskReservationViewRepository[IO]) => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresReservationRepository = new PostgresDeskReservationRepository[IO](session)
      lazy val postgresReservationViewRepository = new PostgresDeskReservationViewRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        insertDesks(session) >>
        insertUsers(session) >>
        run(postgresReservationRepository, postgresReservationViewRepository)
    }

  beforeTest(
    """GIVEN 1 desk reservation in the database
      | WHEN listDeskReservations is called with matching filters
      | THEN the result view contains the reservation with user and desk details
      |""".stripMargin
  ) {
    case (reservationRepository, reservationViewRepository) =>
      val reservation = anyDeskReservation.copy(id = anyDeskReservationId1, deskId = office1DeskId1)

      for {
        _ <- reservationRepository.createReservation(reservation)
        reservationListView <- reservationViewRepository.listDeskReservations(
          officeId1,
          reservation.reservedFromDate,
          limit = 10,
          offset = 0
        )
      } yield expect.all(
        reservationListView.reservations.map(_.id) == List(reservation.id),
        reservationListView.reservations.head.desk == DeskView(office1DeskId1, office1DeskName1),
        reservationListView.reservations.head.user == UserView(userId1, userFirstName1, userLastName1, userEmail1)
      )
  }

  beforeTest(
    """GIVEN 3 desk reservations in the database in one office
      | WHEN listDeskReservations is called with limit 2 and offsets 0 and 2
      | THEN return two pages of results
      |""".stripMargin
  ) { (reservationRepository, reservationViewRepository) =>
    val reservationFrom = LocalDate.parse("2024-09-24")
    val reservationTo = reservationFrom.plusDays(3)

    def createDeskReservation(id: UUID, deskId: UUID) =
      anyDeskReservation.copy(
        id = id,
        deskId = deskId,
        reservedFromDate = reservationFrom,
        reservedToDate = reservationTo
      )

    val reservation1 = createDeskReservation(anyDeskReservationId1, deskId = office1DeskId1)
    val reservation2 = createDeskReservation(anyDeskReservationId2, deskId = office1DeskId2)
    val reservation3 = createDeskReservation(anyDeskReservationId3, deskId = office1DeskId3)

    for {
      _ <- reservationRepository.createReservation(reservation1)
      _ <- reservationRepository.createReservation(reservation2)
      _ <- reservationRepository.createReservation(reservation3)
      reservationListView1 <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservationFrom,
        limit = 2,
        offset = 0
      )
      reservationListView2 <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservationFrom,
        limit = 2,
        offset = 2
      )
    } yield expect.all(
      reservationListView1.reservations.map(_.id) == List(reservation1.id, reservation2.id),
      reservationListView1.pagination.hasMoreResults,
      reservationListView2.reservations.map(_.id) == List(reservation3.id),
      !reservationListView2.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 desk reservation in the database
      | WHEN listDeskReservations is called with limit 1 and offset 1
      | THEN return an empty list of results
      |""".stripMargin
  ) { (reservationRepository, reservationViewRepository) =>
    val reservation = anyDeskReservation.copy(id = anyDeskReservationId1, deskId = office1DeskId1)

    for {
      _ <- reservationRepository.createReservation(reservation)
      reservationListView <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservation.reservedFromDate,
        limit = 1,
        offset = 1
      )
    } yield expect.all(
      reservationListView.reservations.isEmpty,
      !reservationListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 reservations in the database in two offices
      | WHEN listDeskReservations is called with officeId matching 2 reservations
      | THEN return matching reservations
      |""".stripMargin
  ) { (reservationRepository, reservationViewRepository) =>
    val reservationFrom = LocalDate.parse("2024-09-24")
    val reservationTo = reservationFrom.plusDays(3)

    def createDeskReservation(id: UUID, deskId: UUID) =
      anyDeskReservation.copy(
        id = id,
        deskId = deskId,
        reservedFromDate = reservationFrom,
        reservedToDate = reservationTo
      )

    val matchingReservation1 = createDeskReservation(anyDeskReservationId1, deskId = office1DeskId1)
    val matchingReservation2 = createDeskReservation(anyDeskReservationId2, deskId = office1DeskId2)
    val notMatchingReservation = createDeskReservation(anyDeskReservationId3, deskId = office2DeskId1)

    for {
      _ <- reservationRepository.createReservation(matchingReservation1)
      _ <- reservationRepository.createReservation(matchingReservation2)
      _ <- reservationRepository.createReservation(notMatchingReservation)
      reservationListView <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservationFrom,
        limit = 10,
        offset = 0
      )
    } yield expect.all(
      reservationListView.reservations.map(_.id) == List(matchingReservation1.id, matchingReservation2.id),
      !reservationListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 reservations in the database in one office
      | WHEN listDeskReservations is called with reservationFrom matching 2 reservations
      | THEN return matching reservations
      |""".stripMargin
  ) { (reservationRepository, reservationViewRepository) =>
    val notMatchingReservation = anyDeskReservation.copy(
      id = anyDeskReservationId3,
      deskId = office1DeskId3,
      reservedFromDate = LocalDate.parse("2024-09-24"),
      reservedToDate = LocalDate.parse("2024-09-24")
    )
    val matchingReservation1 = anyDeskReservation.copy(
      id = anyDeskReservationId1,
      deskId = office1DeskId1,
      reservedFromDate = LocalDate.parse("2024-09-25"),
      reservedToDate = LocalDate.parse("2024-09-27")
    )
    val matchingReservation2 = anyDeskReservation.copy(
      id = anyDeskReservationId2,
      deskId = office1DeskId2,
      reservedFromDate = LocalDate.parse("2024-09-26"),
      reservedToDate = LocalDate.parse("2024-09-27")
    )

    for {
      _ <- reservationRepository.createReservation(notMatchingReservation)
      _ <- reservationRepository.createReservation(matchingReservation1)
      _ <- reservationRepository.createReservation(matchingReservation2)
      reservationListView <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservationFrom = LocalDate.parse("2024-09-25"),
        limit = 10,
        offset = 0
      )
    } yield expect.all(
      reservationListView.reservations.map(_.id) == List(matchingReservation1.id, matchingReservation2.id),
      !reservationListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 reservation in the database
      | WHEN listDeskReservations is called with reservationFrom between reservedFromDate and reservedToDate
      | THEN return the matching reservation
      |""".stripMargin
  ) {
    case (reservationRepository, reservationViewRepository) =>
      val reservation = anyDeskReservation.copy(
        id = anyDeskReservationId1,
        deskId = office1DeskId1,
        reservedFromDate = LocalDate.parse("2024-09-24"),
        reservedToDate = LocalDate.parse("2024-09-27")
      )

      for {
        _ <- reservationRepository.createReservation(reservation)
        reservationListView <- reservationViewRepository.listDeskReservations(
          officeId1,
          reservationFrom = LocalDate.parse("2024-09-25"),
          limit = 10,
          offset = 0
        )
      } yield expect.all(
        reservationListView.reservations.map(_.id) == List(reservation.id),
        !reservationListView.pagination.hasMoreResults
      )
  }

  beforeTest(
    """GIVEN 4 reservations in the database in one office
      | WHEN listDeskReservations is called with reservationStates matching 2 reservations
      | THEN return matching reservations
      |""".stripMargin
  ) { (reservationRepository, reservationViewRepository) =>
    val matchingReservation1 = anyDeskReservation.copy(
      id = anyDeskReservationId1,
      deskId = office1DeskId1,
      state = Pending
    )
    val matchingReservation2 = anyDeskReservation.copy(
      id = anyDeskReservationId2,
      deskId = office1DeskId2,
      state = Confirmed
    )
    val notMatchingReservation1 = anyDeskReservation.copy(
      id = anyDeskReservationId3,
      deskId = office1DeskId3,
      state = Rejected
    )
    val notMatchingReservation2 = anyDeskReservation.copy(
      id = anyDeskReservationId4,
      deskId = office1DeskId4,
      state = Cancelled
    )

    for {
      _ <- reservationRepository.createReservation(matchingReservation1)
      _ <- reservationRepository.createReservation(matchingReservation2)
      _ <- reservationRepository.createReservation(notMatchingReservation1)
      _ <- reservationRepository.createReservation(notMatchingReservation2)
      reservationListView <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservationFrom = LocalDate.EPOCH,
        reservationStates = Some(List(Pending, Confirmed)),
        limit = 10,
        offset = 0
      )
    } yield expect.all(
      reservationListView.reservations.map(_.id) == List(matchingReservation1.id, matchingReservation2.id),
      !reservationListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 reservations in the database in one office
      | WHEN listDeskReservations is called with userId matching 2 reservations
      | THEN return matching reservations
      |""".stripMargin
  ) { (reservationRepository, reservationViewRepository) =>
    val matchingReservation1 = anyDeskReservation.copy(
      id = anyDeskReservationId1,
      deskId = office1DeskId1,
      userId = userId1
    )
    val matchingReservation2 = anyDeskReservation.copy(
      id = anyDeskReservationId2,
      deskId = office1DeskId2,
      userId = userId1
    )
    val notMatchingReservation = anyDeskReservation.copy(
      id = anyDeskReservationId3,
      deskId = office1DeskId3,
      userId = userId2
    )

    for {
      _ <- reservationRepository.createReservation(matchingReservation1)
      _ <- reservationRepository.createReservation(matchingReservation2)
      _ <- reservationRepository.createReservation(notMatchingReservation)
      reservationListView <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservationFrom = LocalDate.EPOCH,
        userId = Some(userId1),
        limit = 10,
        offset = 0
      )
    } yield expect.all(
      reservationListView.reservations.map(_.id) == List(matchingReservation1.id, matchingReservation2.id),
      !reservationListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 5 reservations in the database
      | WHEN listDeskReservations is called with officeId, reservationFrom, reservationStates, and userId matching 1 reservation
      | THEN return the matching reservation
      |""".stripMargin
  ) { (reservationRepository, reservationViewRepository) =>
    val matchingReservation = anyDeskReservation.copy(
      id = anyDeskReservationId1,
      deskId = office1DeskId1,
      reservedFromDate = LocalDate.parse("2024-09-24"),
      reservedToDate = LocalDate.parse("2024-09-27"),
      state = Confirmed,
      userId = userId1
    )
    val notMatchingReservation1 = matchingReservation.copy(
      id = anyDeskReservationId2,
      deskId = office2DeskId1 // not matching office
    )
    val notMatchingReservation2 = matchingReservation
      .copy(
        id = anyDeskReservationId3,
        deskId = office1DeskId2
      )
      .modifyAll(_.reservedFromDate, _.reservedToDate)
      .using(_.minusDays(7)) // not matching reservationFrom
    val notMatchingReservation3 = matchingReservation.copy(
      id = anyDeskReservationId4,
      deskId = office1DeskId3,
      state = Cancelled // not matching state
    )
    val notMatchingReservation4 = matchingReservation.copy(
      id = anyDeskReservationId5,
      deskId = office1DeskId4,
      userId = userId2 // not matching userId
    )

    for {
      _ <- reservationRepository.createReservation(matchingReservation)
      _ <- reservationRepository.createReservation(notMatchingReservation1)
      _ <- reservationRepository.createReservation(notMatchingReservation2)
      _ <- reservationRepository.createReservation(notMatchingReservation3)
      _ <- reservationRepository.createReservation(notMatchingReservation4)
      reservationListView <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservationFrom = matchingReservation.reservedFromDate,
        reservationStates = Some(List(matchingReservation.state)),
        userId = Some(matchingReservation.userId),
        limit = 10,
        offset = 0
      )
    } yield expect.all(
      reservationListView.reservations.map(_.id) == List(matchingReservation.id),
      !reservationListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 4 reservations in the database
      | WHEN listDeskReservations is called with officeId, reservationFrom, reservationStates, and userId not matching any reservation
      | THEN return an empty list of results
      |""".stripMargin
  ) { (reservationRepository, reservationViewRepository) =>
    val matchingReservation = anyDeskReservation.copy(
      id = anyDeskReservationId1,
      deskId = office1DeskId1,
      reservedFromDate = LocalDate.parse("2024-09-24"),
      reservedToDate = LocalDate.parse("2024-09-27"),
      state = Confirmed,
      userId = userId1
    )
    val notMatchingReservation1 = matchingReservation.copy(
      id = anyDeskReservationId2,
      deskId = office2DeskId1 // not matching office
    )
    val notMatchingReservation2 = matchingReservation
      .copy(
        id = anyDeskReservationId3,
        deskId = office1DeskId2
      )
      .modifyAll(_.reservedFromDate, _.reservedToDate)
      .using(_.minusDays(7)) // not matching reservationFrom
    val notMatchingReservation3 = matchingReservation.copy(
      id = anyDeskReservationId4,
      deskId = office1DeskId3,
      state = Cancelled // not matching state
    )
    val notMatchingReservation4 = matchingReservation.copy(
      id = anyDeskReservationId5,
      deskId = office1DeskId4,
      userId = userId2 // not matching userId
    )

    for {
      _ <- reservationRepository.createReservation(notMatchingReservation1)
      _ <- reservationRepository.createReservation(notMatchingReservation2)
      _ <- reservationRepository.createReservation(notMatchingReservation3)
      _ <- reservationRepository.createReservation(notMatchingReservation4)
      reservationListView <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservationFrom = matchingReservation.reservedFromDate,
        reservationStates = Some(List(matchingReservation.state)),
        userId = Some(matchingReservation.userId),
        limit = 10,
        offset = 0
      )
    } yield expect.all(
      reservationListView.reservations.isEmpty,
      !reservationListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 reservations in the database
      | WHEN listDeskReservations is called with filters matching 2 reservations, limit 1 and offset 1
      | THEN return the matching reservation and no more results
      |""".stripMargin
  ) { (reservationRepository, reservationViewRepository) =>
    val reservationFrom = LocalDate.parse("2024-09-24")
    val reservationTo = reservationFrom.plusDays(3)

    def createDeskReservation(id: UUID, deskId: UUID) =
      anyDeskReservation.copy(
        id = id,
        deskId = deskId,
        reservedFromDate = reservationFrom,
        reservedToDate = reservationTo
      )

    val matchingReservation1 = createDeskReservation(
      id = anyDeskReservationId1,
      deskId = office1DeskId1
    )
    val matchingReservation2 = createDeskReservation(
      id = anyDeskReservationId2,
      deskId = office1DeskId2
    )
    val notMatchingReservation = createDeskReservation(
      id = anyDeskReservationId3,
      deskId = office2DeskId1 // not matching office
    )

    for {
      _ <- reservationRepository.createReservation(matchingReservation1)
      _ <- reservationRepository.createReservation(matchingReservation2)
      _ <- reservationRepository.createReservation(notMatchingReservation)
      reservationListView <- reservationViewRepository.listDeskReservations(
        officeId1,
        reservationFrom,
        limit = 1,
        offset = 1
      )
    } yield expect.all(
      reservationListView.reservations.map(_.id) == List(matchingReservation2.id),
      !reservationListView.pagination.hasMoreResults
    )
  }

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

  private def insertDesks(session: Resource[IO, Session[IO]]) = {
    val deskRepository = new PostgresDeskRepository[IO](session)
    val office1Desk1 = anyDesk(office1DeskId1, office1DeskName1, officeId1)
    val office1Desk2 = anyDesk(office1DeskId2, office1DeskName2, officeId1)
    val office1Desk3 = anyDesk(office1DeskId3, office1DeskName3, officeId1)
    val office1Desk4 = anyDesk(office1DeskId4, office1DeskName4, officeId1)
    val office2Desk1 = anyDesk(office2DeskId1, office2DeskName1, officeId2)
    List(office1Desk1, office1Desk2, office1Desk3, office1Desk4, office2Desk1)
      .parTraverse_(deskRepository.create)
  }

  private def anyDesk(id: UUID, name: String, officeId: UUID) = Desk(
    id = id,
    name = name,
    isAvailable = true,
    notes = List("Test", "Notes"),
    isStanding = false,
    monitorsCount = 2,
    hasPhone = false,
    officeId = officeId
  )

  private def insertUsers(session: Resource[IO, Session[IO]]) = {
    val userRepository = new PostgresAccountRepository[IO](session)
    val user1 = UserAccount(userId1, userFirstName1, userLastName1, userEmail1, assignedOfficeId = None)
    val user2 = UserAccount(userId2, userFirstName2, userLastName2, userEmail2, assignedOfficeId = None)
    List(user1, user2).parTraverse_(userRepository.create)
  }

  private lazy val officeId1 = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")
  private lazy val officeId2 = UUID.fromString("c1e29bfd-5a8a-468f-ba27-4673c42fec04")

  private lazy val office1DeskId1 = UUID.fromString("c6bf22d5-9631-4985-8aed-94d8429c2a19")
  private lazy val office1DeskName1 = "desk 1.1"
  private lazy val office1DeskId2 = UUID.fromString("3b2c8499-5c8e-468e-be10-bc6dfe4fd88d")
  private lazy val office1DeskName2 = "desk 1.2"
  private lazy val office1DeskId3 = UUID.fromString("934b1aa0-ce24-4ea7-9c4a-0f33af92e0f0")
  private lazy val office1DeskName3 = "desk 1.3"
  private lazy val office1DeskId4 = UUID.fromString("038b6c3e-c810-4dad-8b2c-81f86258e70f")
  private lazy val office1DeskName4 = "desk 1.4"
  private lazy val office2DeskId1 = UUID.fromString("a64882be-c0e5-4d20-bda0-59182ac1c7fa")
  private lazy val office2DeskName1 = "desk 2.1"

  private lazy val userId1 = UUID.fromString("59b07490-6a69-4912-9777-9076fb23c9db")
  private lazy val userFirstName1 = "John"
  private lazy val userLastName1 = "Doe"
  private lazy val userEmail1 = "john.doe@example.com"
  private lazy val userId2 = UUID.fromString("feb53d33-c2a6-4734-b178-4edd058c83f2")
  private lazy val userFirstName2 = "Jane"
  private lazy val userLastName2 = "Smith"
  private lazy val userEmail2 = "jane.smith@example.com"

  private lazy val anyDeskReservation = DeskReservation(
    id = anyDeskReservationId1,
    userId = userId1,
    createdAt = LocalDateTime.parse("2024-09-23T12:00:00"),
    reservedFromDate = LocalDate.parse("2024-09-24"),
    reservedToDate = LocalDate.parse("2024-09-27"),
    state = Confirmed,
    notes = "Test notes",
    deskId = office1DeskId1
  )

  private lazy val anyDeskReservationId1 = UUID.fromString("eb2cc050-de32-4087-87e3-cac25dc64a92")
  private lazy val anyDeskReservationId2 = UUID.fromString("32afa97c-388f-43ba-a737-3121642452c4")
  private lazy val anyDeskReservationId3 = UUID.fromString("c16737f6-b5ae-45a9-9ea0-9e3e7fb2568d")
  private lazy val anyDeskReservationId4 = UUID.fromString("547d7782-cf8b-44ea-95cc-9aa9dac63553")
  private lazy val anyDeskReservationId5 = UUID.fromString("aa1531a7-c47c-450a-bfb9-5c70188b76a1")
}

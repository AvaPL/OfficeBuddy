package io.github.avapl
package adapters.postgres.repository.parkingspot.view

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.account.PostgresAccountRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import adapters.postgres.repository.parkingspot.PostgresParkingSpotRepository
import adapters.postgres.repository.reservation.PostgresParkingSpotReservationRepository
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import domain.model.account.UserAccount
import domain.model.office.Address
import domain.model.office.Office
import domain.model.parkingspot.ParkingSpot
import domain.model.reservation.ParkingSpotReservation
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

object PostgresParkingSpotViewRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(
    name: TestName
  )(
    run: (
      PostgresParkingSpotRepository[IO],
      PostgresParkingSpotViewRepository[IO],
      PostgresParkingSpotReservationRepository[IO]
    ) => IO[Expectations]
  ): Unit =
    test(name) { session =>
      lazy val postgresParkingSpotRepository = new PostgresParkingSpotRepository[IO](session)
      lazy val postgresParkingSpotViewRepository = new PostgresParkingSpotViewRepository[IO](session)
      lazy val postgresReservationRepository = new PostgresParkingSpotReservationRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        insertUsers(session) >>
        run(postgresParkingSpotRepository, postgresParkingSpotViewRepository, postgresReservationRepository)
    }

  beforeTest(
    """GIVEN 3 parking spots in the database assigned to an office
      | WHEN listParkingSpots is called with limit 2 and offsets 0 and 2
      | THEN return two pages of results
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val officeId = officeId1
    val parkingSpot1 = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot1", officeId = officeId)
    val parkingSpot2 = anyParkingSpot.copy(id = anyParkingSpotId2, name = "parkingSpot2", officeId = officeId)
    val parkingSpot3 = anyParkingSpot.copy(id = anyParkingSpotId3, name = "parkingSpot3", officeId = officeId)

    for {
      _ <- parkingSpotRepository.create(parkingSpot1)
      _ <- parkingSpotRepository.create(parkingSpot2)
      _ <- parkingSpotRepository.create(parkingSpot3)
      parkingSpotListView1 <- parkingSpotViewRepository.listParkingSpots(officeId1, limit = 2, offset = 0)
      parkingSpotListView2 <- parkingSpotViewRepository.listParkingSpots(officeId1, limit = 2, offset = 2)
    } yield expect.all(
      parkingSpotListView1.parkingSpots.map(_.id) == List(parkingSpot1.id, parkingSpot2.id),
      parkingSpotListView1.pagination.hasMoreResults,
      parkingSpotListView2.parkingSpots.map(_.id) == List(parkingSpot3.id),
      !parkingSpotListView2.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 parking spot in the database assigned to an office
      | WHEN listParkingSpots is called with limit 1 and offset 1
      | THEN return an empty list of results
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val parkingSpot = anyParkingSpot

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      parkingSpotListView <- parkingSpotViewRepository.listParkingSpots(parkingSpot.officeId, limit = 1, offset = 1)
    } yield expect.all(
      parkingSpotListView.parkingSpots.isEmpty,
      !parkingSpotListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 archived parking spot in the database assigned to an office
      | WHEN listParkingSpots is called with limit 1 and offset 0
      | THEN return an empty list of results
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val parkingSpot = anyParkingSpot.copy(isArchived = true)

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      parkingSpotListView <- parkingSpotViewRepository.listParkingSpots(parkingSpot.officeId, limit = 1, offset = 0)
    } yield expect.all(
      parkingSpotListView.parkingSpots.isEmpty,
      !parkingSpotListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 parking spots in the database
      | WHEN listParkingSpots is called with officeId matching 2 parking spots
      | THEN return matching parking spots
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val matchingParkingSpot1 = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot1", officeId = officeId1)
    val matchingParkingSpot2 = anyParkingSpot.copy(id = anyParkingSpotId2, name = "parkingSpot2", officeId = officeId1)
    val notMatchingParkingSpot =
      anyParkingSpot.copy(id = anyParkingSpotId3, name = "parkingSpot3", officeId = officeId2)

    for {
      _ <- parkingSpotRepository.create(matchingParkingSpot1)
      _ <- parkingSpotRepository.create(matchingParkingSpot2)
      _ <- parkingSpotRepository.create(notMatchingParkingSpot)
      parkingSpotListView <- parkingSpotViewRepository.listParkingSpots(officeId1, limit = 10, offset = 0)
    } yield expect.all(
      parkingSpotListView.parkingSpots.map(_.id) == List(matchingParkingSpot1.id, matchingParkingSpot2.id),
      !parkingSpotListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 parking spots in the database
      | WHEN listParkingSpots is called with officeId matching 2 parking spots, limit 1 and offset 1
      | THEN return the matching parking spot and no more results
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val matchingParkingSpot1 = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot1", officeId = officeId1)
    val matchingParkingSpot2 = anyParkingSpot.copy(id = anyParkingSpotId2, name = "parkingSpot2", officeId = officeId1)
    val notMatchingParkingSpot =
      anyParkingSpot.copy(id = anyParkingSpotId3, name = "parkingSpot3", officeId = officeId2)

    for {
      _ <- parkingSpotRepository.create(matchingParkingSpot1)
      _ <- parkingSpotRepository.create(matchingParkingSpot2)
      _ <- parkingSpotRepository.create(notMatchingParkingSpot)
      parkingSpotListView <- parkingSpotViewRepository.listParkingSpots(officeId1, limit = 1, offset = 1)
    } yield expect.all(
      parkingSpotListView.parkingSpots.map(_.id) == List(matchingParkingSpot2.id),
      !parkingSpotListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 1 parking spot in the database assigned to an office
      | WHEN listParkingSpots is called with officeId not matching the parking spot
      | THEN return an empty list of results
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val notMatchingParkingSpot = anyParkingSpot.copy(officeId = officeId2)

    for {
      _ <- parkingSpotRepository.create(notMatchingParkingSpot)
      parkingSpotListView <- parkingSpotViewRepository.listParkingSpots(officeId1, limit = 10, offset = 0)
    } yield expect.all(
      parkingSpotListView.parkingSpots.isEmpty,
      !parkingSpotListView.pagination.hasMoreResults
    )
  }

  beforeTest(
    """GIVEN 3 parking spots in the database assigned to an office
      | WHEN listParkingSpotsAvailableForReservation is called and no reservations exist
      | THEN return all parking spots
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val officeId = officeId1
    val parkingSpot1 = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot1", officeId = officeId)
    val parkingSpot2 = anyParkingSpot.copy(id = anyParkingSpotId2, name = "parkingSpot2", officeId = officeId)
    val parkingSpot3 = anyParkingSpot.copy(id = anyParkingSpotId3, name = "parkingSpot3", officeId = officeId)
    val reservationFrom = LocalDate.parse("2024-09-23")
    val reservationTo = reservationFrom.plusDays(1)

    for {
      _ <- parkingSpotRepository.create(parkingSpot1)
      _ <- parkingSpotRepository.create(parkingSpot2)
      _ <- parkingSpotRepository.create(parkingSpot3)
      reservableParkingSpots <- parkingSpotViewRepository.listParkingSpotsAvailableForReservation(officeId, reservationFrom, reservationTo)
    } yield expect(reservableParkingSpots.map(_.id) == List(parkingSpot1.id, parkingSpot2.id, parkingSpot3.id))
  }

  beforeTest(
    """GIVEN 5 parking spots in the database assigned to an office, where 4 have Confirmed reservations
      | WHEN listParkingSpotsAvailableForReservation is called with period overlapping all reservations
      | THEN return only the free parking spots
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, reservationRepository) =>
    val officeId = officeId1
    val reservedParkingSpot1 = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot1", officeId = officeId)
    val reservedParkingSpot2 = anyParkingSpot.copy(id = anyParkingSpotId2, name = "parkingSpot2", officeId = officeId)
    val reservedParkingSpot3 = anyParkingSpot.copy(id = anyParkingSpotId3, name = "parkingSpot3", officeId = officeId)
    val reservedParkingSpot4 = anyParkingSpot.copy(id = anyParkingSpotId4, name = "parkingSpot4", officeId = officeId)
    val freeParkingSpot = anyParkingSpot.copy(id = anyParkingSpotId5, name = "parkingSpot5", officeId = officeId)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-24")

    val reservationParkingSpot1 = createParkingSpotReservation(
      id = anyReservationId1,
      reservedFromDate = LocalDate.parse("2024-09-19"),
      reservedToDate = LocalDate.parse("2024-09-25"),
      parkingSpotId = reservedParkingSpot1.id
    )
    val reservationParkingSpot2 = createParkingSpotReservation(
      id = anyReservationId2,
      reservedFromDate = LocalDate.parse("2024-09-21"),
      reservedToDate = LocalDate.parse("2024-09-23"),
      parkingSpotId = reservedParkingSpot2.id
    )
    val reservationParkingSpot3 = createParkingSpotReservation(
      id = anyReservationId3,
      reservedFromDate = LocalDate.parse("2024-09-21"),
      reservedToDate = LocalDate.parse("2024-09-25"),
      parkingSpotId = reservedParkingSpot3.id
    )
    val reservationParkingSpot4 = createParkingSpotReservation(
      id = anyReservationId4,
      reservedFromDate = LocalDate.parse("2024-09-19"),
      reservedToDate = LocalDate.parse("2024-09-23"),
      parkingSpotId = reservedParkingSpot4.id
    )

    for {
      _ <- parkingSpotRepository.create(reservedParkingSpot1)
      _ <- parkingSpotRepository.create(reservedParkingSpot2)
      _ <- parkingSpotRepository.create(reservedParkingSpot3)
      _ <- parkingSpotRepository.create(reservedParkingSpot4)
      _ <- parkingSpotRepository.create(freeParkingSpot)
      _ <- reservationRepository.createReservation(reservationParkingSpot1)
      _ <- reservationRepository.createReservation(reservationParkingSpot2)
      _ <- reservationRepository.createReservation(reservationParkingSpot3)
      _ <- reservationRepository.createReservation(reservationParkingSpot4)
      reservableParkingSpots <- parkingSpotViewRepository.listParkingSpotsAvailableForReservation(officeId, reservationFrom, reservationTo)
    } yield expect(reservableParkingSpots.map(_.id) == List(freeParkingSpot.id))
  }

  beforeTest(
    """GIVEN 3 parking spots in the database assigned to an office, where all have Confirmed reservations
      | WHEN listParkingSpotsAvailableForReservation is called with period overlapping only some of the reservations
      | THEN return only the free parking spots
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, reservationRepository) =>
    val officeId = officeId1
    val parkingSpot1 = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot1", officeId = officeId)
    val parkingSpot2 = anyParkingSpot.copy(id = anyParkingSpotId2, name = "parkingSpot2", officeId = officeId)
    val parkingSpot3 = anyParkingSpot.copy(id = anyParkingSpotId3, name = "parkingSpot3", officeId = officeId)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-21")

    val overlappingReservationParkingSpot1 = createParkingSpotReservation(
      id = anyReservationId1,
      reservedFromDate = LocalDate.parse("2024-09-20"),
      reservedToDate = LocalDate.parse("2024-09-20"),
      parkingSpotId = parkingSpot1.id
    )
    val overlappingReservationParkingSpot2 = createParkingSpotReservation(
      id = anyReservationId2,
      reservedFromDate = LocalDate.parse("2024-09-21"),
      reservedToDate = LocalDate.parse("2024-09-21"),
      parkingSpotId = parkingSpot2.id
    )
    val nonOverlappingReservationParkingSpot3 = createParkingSpotReservation(
      id = anyReservationId3,
      reservedFromDate = LocalDate.parse("2024-09-22"),
      reservedToDate = LocalDate.parse("2024-09-22"),
      parkingSpotId = parkingSpot3.id
    )

    for {
      _ <- parkingSpotRepository.create(parkingSpot1)
      _ <- parkingSpotRepository.create(parkingSpot2)
      _ <- parkingSpotRepository.create(parkingSpot3)
      _ <- reservationRepository.createReservation(overlappingReservationParkingSpot1)
      _ <- reservationRepository.createReservation(overlappingReservationParkingSpot2)
      _ <- reservationRepository.createReservation(nonOverlappingReservationParkingSpot3)
      reservableParkingSpots <- parkingSpotViewRepository.listParkingSpotsAvailableForReservation(officeId, reservationFrom, reservationTo)
    } yield expect(reservableParkingSpots.map(_.id) == List(parkingSpot3.id))
  }

  beforeTest(
    """GIVEN 2 parking spots with the same name in 2 offices and reservation of the parking spot in office 2
      | WHEN listParkingSpotsAvailableForReservation is called with office 1 and period overlapping the reservation of parking spot in office 2
      | THEN return the free parking spot in office 1
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, reservationRepository) =>
    val office1ParkingSpot = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot", officeId = officeId1)
    val office2ParkingSpot = anyParkingSpot.copy(id = anyParkingSpotId2, name = "parkingSpot", officeId = officeId2)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-21")

    val office2ParkingSpotReservation = createParkingSpotReservation(
      id = anyReservationId1,
      reservedFromDate = reservationFrom,
      reservedToDate = reservationTo,
      parkingSpotId = office2ParkingSpot.id
    )

    for {
      _ <- parkingSpotRepository.create(office1ParkingSpot)
      _ <- parkingSpotRepository.create(office2ParkingSpot)
      _ <- reservationRepository.createReservation(office2ParkingSpotReservation)
      reservableParkingSpots <- parkingSpotViewRepository.listParkingSpotsAvailableForReservation(officeId1, reservationFrom, reservationTo)
    } yield expect(reservableParkingSpots.map(_.id) == List(office1ParkingSpot.id))
  }

  beforeTest(
    """GIVEN 1 parking spot assigned to an office and a Pending reservation
      | WHEN listParkingSpotsAvailableForReservation is called with period overlapping the reservation
      | THEN return an empty list of results
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, reservationRepository) =>
    val parkingSpot = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot", officeId = officeId1)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-21")

    val pendingReservation = createParkingSpotReservation(
      id = anyReservationId1,
      reservedFromDate = reservationFrom,
      reservedToDate = reservationTo,
      state = Pending,
      parkingSpotId = parkingSpot.id
    )

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      _ <- reservationRepository.createReservation(pendingReservation)
      reservableParkingSpots <- parkingSpotViewRepository.listParkingSpotsAvailableForReservation(officeId1, reservationFrom, reservationTo)
    } yield expect(reservableParkingSpots.isEmpty)
  }

  beforeTest(
    """GIVEN 1 parking spot assigned to an office and Cancelled and Rejected reservations
      | WHEN listParkingSpotsAvailableForReservation is called with period overlapping the reservations
      | THEN the parking spot is returned as free
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, reservationRepository) =>
    val parkingSpot = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot", officeId = officeId1)

    val reservationFrom = LocalDate.parse("2024-09-20")
    val reservationTo = LocalDate.parse("2024-09-21")

    def createOverlappingReservation(id: UUID, state: ReservationState) = createParkingSpotReservation(
      id = id,
      reservedFromDate = reservationFrom,
      reservedToDate = reservationTo,
      state = state,
      parkingSpotId = parkingSpot.id
    )

    val cancelledReservation = createOverlappingReservation(id = anyReservationId1, state = Cancelled)
    val rejectedReservation = createOverlappingReservation(id = anyReservationId2, state = Rejected)

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      _ <- reservationRepository.createReservation(cancelledReservation)
      _ <- reservationRepository.createReservation(rejectedReservation)
      reservableParkingSpots <- parkingSpotViewRepository.listParkingSpotsAvailableForReservation(officeId1, reservationFrom, reservationTo)
    } yield expect(reservableParkingSpots.map(_.id) == List(parkingSpot.id))
  }

  beforeTest(
    """GIVEN 1 archived parking spot and 1 unavailable parking spot
      | WHEN listParkingSpotsAvailableForReservation is called
      | THEN return an empty list of results
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val archivedParkingSpot =
      anyParkingSpot.copy(id = anyParkingSpotId1, name = "archivedParkingSpot", officeId = officeId1, isArchived = true)
    val unavailableParkingSpot =
      anyParkingSpot.copy(id = anyParkingSpotId2, name = "unavailableParkingSpot", officeId = officeId1, isAvailable = false)

    for {
      _ <- parkingSpotRepository.create(archivedParkingSpot)
      _ <- parkingSpotRepository.create(unavailableParkingSpot)
      reservableParkingSpots <- parkingSpotViewRepository.listParkingSpotsAvailableForReservation(
        officeId1,
        reservationFrom = LocalDate.parse("2024-09-20"),
        reservationTo = LocalDate.parse("2024-09-21")
      )
    } yield expect(reservableParkingSpots.isEmpty)
  }

  beforeTest(
    """GIVEN 1 parking spot assigned to an office
      | WHEN listParkingSpotsAvailableForReservation is called with reservationFrom equal to reservationTo
      | THEN return the parking spot
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val parkingSpot = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot", officeId = officeId1)

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      reservableParkingSpots <- parkingSpotViewRepository.listParkingSpotsAvailableForReservation(
        officeId1,
        reservationFrom = LocalDate.parse("2024-09-20"),
        reservationTo = LocalDate.parse("2024-09-20")
      )
    } yield expect(reservableParkingSpots.map(_.id) == List(parkingSpot.id))
  }

  beforeTest(
    """GIVEN 1 parking spot assigned to an office
      | WHEN listParkingSpotsAvailableForReservation is called with reservationFrom after reservationTo
      | THEN return an empty list of results
      |""".stripMargin
  ) { (parkingSpotRepository, parkingSpotViewRepository, _) =>
    val parkingSpot = anyParkingSpot.copy(id = anyParkingSpotId1, name = "parkingSpot", officeId = officeId1)

    for {
      _ <- parkingSpotRepository.create(parkingSpot)
      reservableParkingSpots <- parkingSpotViewRepository.listParkingSpotsAvailableForReservation(
        officeId1,
        reservationFrom = LocalDate.parse("2024-09-21"),
        reservationTo = LocalDate.parse("2024-09-20")
      )
    } yield expect(reservableParkingSpots.isEmpty)
  }

  // TODO: Verify that other types of reservations (desk, meeting room) are not considered as overlapping

  private def truncateTables(session: Resource[IO, Session[IO]]) =
    truncateReservationTable(session) >>
      truncateAccountTable(session) >>
      truncateParkingSpotTable(session) >>
      truncateOfficeTable(session)

  private def truncateReservationTable(session: Resource[IO, Session[IO]]) = {
    val sql =
      sql"""
      TRUNCATE TABLE reservation CASCADE
    """.command
    session.use(_.execute(sql))
  }

  private def truncateAccountTable(session: Resource[IO, Session[IO]]) = {
    val sql =
      sql"""
      TRUNCATE TABLE account CASCADE
    """.command
    session.use(_.execute(sql))
  }

  private def truncateParkingSpotTable(session: Resource[IO, Session[IO]]) = {
    val sql =
      sql"""
      TRUNCATE TABLE parking_spot CASCADE
    """.command
    session.use(_.execute(sql))
  }

  private def truncateOfficeTable(session: Resource[IO, Session[IO]]) = {
    val sql =
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

  private def createParkingSpotReservation(
    id: UUID,
    parkingSpotId: UUID,
    reservedFromDate: LocalDate,
    reservedToDate: LocalDate,
    state: ReservationState = Confirmed
  ) = ParkingSpotReservation(
    id = id,
    userId = userId1,
    createdAt = LocalDateTime.parse("2024-09-23T12:00:00"),
    reservedFromDate = reservedFromDate,
    reservedToDate = reservedToDate,
    state = state,
    notes = "",
    parkingSpotId = parkingSpotId,
    plateNumber = "DSW 58100"
  )

  private lazy val officeId1 = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")
  private lazy val officeId2 = UUID.fromString("c1e29bfd-5a8a-468f-ba27-4673c42fec04")

  private lazy val userId1 = UUID.fromString("59b07490-6a69-4912-9777-9076fb23c9db")
  private lazy val userId2 = UUID.fromString("feb53d33-c2a6-4734-b178-4edd058c83f2")

  private lazy val anyParkingSpot = ParkingSpot(
    id = anyParkingSpotId1,
    name = "Test Parking Spot",
    isAvailable = true,
    notes = List("Test", "Notes"),
    isHandicapped = false,
    isUnderground = false,
    officeId = officeId1
  )

  private lazy val anyParkingSpotId1 = UUID.fromString("cecd6ead-2d55-4eef-901e-273aaf6fb23a")
  private lazy val anyParkingSpotId2 = UUID.fromString("4a565b76-eabf-497c-9ea9-7f935ffdfcd6")
  private lazy val anyParkingSpotId3 = UUID.fromString("56a1eb04-5014-48f6-9241-e7f169b16177")
  private lazy val anyParkingSpotId4 = UUID.fromString("ab01dc75-a1ef-4b99-9bdb-875cfccd7d4c")
  private lazy val anyParkingSpotId5 = UUID.fromString("2de6785c-626a-464c-a5ca-b165d6f00e30")

  private lazy val anyReservationId1 = UUID.fromString("eb2cc050-de32-4087-87e3-cac25dc64a92")
  private lazy val anyReservationId2 = UUID.fromString("32afa97c-388f-43ba-a737-3121642452c4")
  private lazy val anyReservationId3 = UUID.fromString("c16737f6-b5ae-45a9-9ea0-9e3e7fb2568d")
  private lazy val anyReservationId4 = UUID.fromString("547d7782-cf8b-44ea-95cc-9aa9dac63553")
}

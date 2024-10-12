package io.github.avapl
package domain.service.demo

import cats.Monad
import cats.MonadThrow
import cats.Parallel
import cats.effect.kernel.Clock
import cats.effect.std.Random
import cats.syntax.all._
import domain.model.account.Account
import domain.model.account.OfficeManagerAccount
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import domain.model.desk.Desk
import domain.model.office.Address
import domain.model.office.Office
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState.Cancelled
import domain.model.reservation.ReservationState.Confirmed
import domain.model.reservation.ReservationState.Pending
import domain.model.reservation.ReservationState.Rejected
import domain.repository.account.AccountRepository
import domain.repository.desk.DeskRepository
import domain.repository.office.OfficeRepository
import domain.repository.reservation.ReservationRepository
import domain.service.demo.DemoDataService.Accounts
import domain.service.demo.DemoDataService.Offices
import domain.service.demo.DemoDataService.generateDesk
import domain.service.demo.DemoDataService.generateDeskReservation
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import org.typelevel.log4cats.Logger
import util.FUUID

class DemoDataService[F[_]: Clock: FUUID: Logger: MonadThrow: Parallel: Random](
  accountRepository: AccountRepository[F],
  officeRepository: OfficeRepository[F],
  deskRepository: DeskRepository[F],
  reservationRepository: ReservationRepository[F]
) {

  // TODO: Find a way to detect if the test data is already loaded
  // TODO: Loading demo data should be done within a transaction
  def loadDemoData(): F[Unit] =
    for {
      offices <- loadOffices()
      accounts <- loadAccounts()
      desks <- loadDesks()
      reservations <- loadDeskReservations(desks)
      _ <- Logger[F].info(
        s"Demo data loaded (${offices.size} offices, ${accounts.size} accounts, ${desks.size} desks, ${reservations.size} reservations)"
      )
    } yield ()

  private def loadOffices(): F[List[Office]] =
    Offices.allOffices.parTraverse(officeRepository.create)

  private def loadAccounts(): F[List[Account]] =
    (Accounts.allSuperAdmins ++ Accounts.allOfficeManagers ++ Accounts.allUsers).parTraverse(accountRepository.create)

  private def loadDesks(): F[List[Desk]] =
    Offices.allOffices.map(_.id).parFlatTraverse(loadOfficeDesks)

  private def loadOfficeDesks(officeId: UUID): F[List[Desk]] =
    for {
      startingFloor <- Random[F].betweenInt(1, 6)
      numberOfFloors <- Random[F].betweenInt(1, 4)
      desks <- (startingFloor until startingFloor + numberOfFloors).toList.parFlatTraverse(loadFloorDesks(officeId, _))
    } yield desks

  private def loadFloorDesks(officeId: UUID, floor: Int): F[List[Desk]] =
    for {
      roomsCount <- Random[F].betweenInt(5, 10)
      desks <- (1 to roomsCount).toList.parFlatTraverse(loadRoomDesks(officeId, floor, _))
    } yield desks

  private def loadRoomDesks(officeId: UUID, floor: Int, roomIndex: Int): F[List[Desk]] =
    for {
      desksCount <- Random[F].betweenInt(1, 10)
      desks <- (1 to desksCount).toList.parTraverse(loadDesk(officeId, floor, roomIndex, _))
    } yield desks

  private def loadDesk(officeId: UUID, floor: Int, roomIndex: Int, deskIndex: Int): F[Desk] =
    generateDesk(
      officeId,
      roomNumber = floor * 100 + roomIndex,
      deskNumber = deskIndex
    ).flatMap(deskRepository.create)

  private def loadDeskReservations(desks: List[Desk]): F[List[DeskReservation]] =
    for {
      now <- Clock[F].realTimeInstant.map(LocalDateTime.ofInstant(_, ZoneOffset.UTC))
      reservations <- loadDeskReservations(desks, now)
    } yield reservations

  private def loadDeskReservations(desks: List[Desk], now: LocalDateTime): F[List[DeskReservation]] =
    desks.parFlatTraverse { desk =>
      Random[F].nextBoolean.flatMap { hasReservation =>
        if (hasReservation)
          loadDeskReservation(desk, now).map(List(_))
        else
          List.empty[DeskReservation].pure[F]
      }
    }

  private def loadDeskReservation(desk: Desk, now: LocalDateTime): F[DeskReservation] =
    for {
      user <- Random[F].elementOf(Accounts.allUsers ++ Accounts.allOfficeManagers)
      reservation <- loadDeskReservation(user, desk, now)
    } yield reservation

  private def loadDeskReservation(user: Account, desk: Desk, now: LocalDateTime): F[DeskReservation] =
    generateDeskReservation(user.id, desk.id, now)
      .flatMap(reservationRepository.createDeskReservation)
}

object DemoDataService {

  object Offices {

    lazy val office1: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000001"),
      name = "Wroclaw Office",
      notes = List("Everyone's favorite", "The funniest one"),
      address = Address(
        addressLine1 = "Powstancow Slaskich 9",
        addressLine2 = "Floor 1",
        postalCode = "53-332",
        city = "Wroclaw",
        country = "Poland"
      ),
      officeManagerIds = Nil
    )

    lazy val office2: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
      name = "New York Office",
      notes = Nil,
      address = Address(
        addressLine1 = "5th Avenue",
        addressLine2 = "Floor 10",
        postalCode = "10001",
        city = "New York",
        country = "United States"
      ),
      officeManagerIds = Nil
    )

    lazy val office3: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000003"),
      name = "Tokyo Office",
      notes = List("High-tech hub"),
      address = Address(
        addressLine1 = "Shibuya Crossing",
        addressLine2 = "Building 3",
        postalCode = "150-0002",
        city = "Tokyo",
        country = "Japan"
      ),
      officeManagerIds = Nil
    )

    lazy val office4: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000004"),
      name = "London Office",
      notes = Nil,
      address = Address(
        addressLine1 = "221B Baker Street",
        addressLine2 = "",
        postalCode = "NW1 6XE",
        city = "London",
        country = "United Kingdom"
      ),
      officeManagerIds = Nil
    )

    lazy val office5: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000005"),
      name = "Sydney Office",
      notes = List("Near the Opera House"),
      address = Address(
        addressLine1 = "Circular Quay",
        addressLine2 = "Suite 5",
        postalCode = "2000",
        city = "Sydney",
        country = "Australia"
      ),
      officeManagerIds = Nil
    )

    lazy val office6: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000006"),
      name = "Berlin Office",
      notes = Nil,
      address = Address(
        addressLine1 = "Alexanderplatz",
        addressLine2 = "Floor 2",
        postalCode = "10178",
        city = "Berlin",
        country = "Germany"
      ),
      officeManagerIds = Nil
    )

    lazy val office7: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000007"),
      name = "Paris Office",
      notes = List("Close to the Eiffel Tower"),
      address = Address(
        addressLine1 = "Champs-Élysées",
        addressLine2 = "Building A",
        postalCode = "75008",
        city = "Paris",
        country = "France"
      ),
      officeManagerIds = Nil
    )

    lazy val office8: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000008"),
      name = "Toronto Office",
      notes = List("Great view of the CN Tower"),
      address = Address(
        addressLine1 = "Front Street",
        addressLine2 = "Suite 100",
        postalCode = "M5J 2N1",
        city = "Toronto",
        country = "Canada"
      ),
      officeManagerIds = Nil
    )

    lazy val office9: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000009"),
      name = "Dubai Office",
      notes = List("Luxurious and modern"),
      address = Address(
        addressLine1 = "Burj Khalifa",
        addressLine2 = "Floor 148",
        postalCode = "00000",
        city = "Dubai",
        country = "UAE"
      ),
      officeManagerIds = Nil
    )

    lazy val office10: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000010"),
      name = "São Paulo Office",
      notes = List("Vibrant and dynamic"),
      address = Address(
        addressLine1 = "Avenida Paulista",
        addressLine2 = "Floor 20",
        postalCode = "01310-100",
        city = "São Paulo",
        country = "Brazil"
      ),
      officeManagerIds = Nil
    )

    lazy val office11: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000011"),
      name = "Madrid Office",
      notes = List("Located in the heart of the city"),
      address = Address(
        addressLine1 = "Gran Via",
        addressLine2 = "Floor 5",
        postalCode = "28013",
        city = "Madrid",
        country = "Spain"
      ),
      officeManagerIds = Nil
    )

    lazy val office12: Office = Office(
      id = UUID.fromString("00000000-0000-0000-0000-000000000012"),
      name = "Cape Town Office",
      notes = List("Beautiful view of Table Mountain"),
      address = Address(
        addressLine1 = "Long Street",
        addressLine2 = "Suite 10",
        postalCode = "8001",
        city = "Cape Town",
        country = "South Africa"
      ),
      officeManagerIds = Nil
    )

    lazy val allOffices: List[Office] = List(
      office1,
      office2,
      office3,
      office4,
      office5,
      office6,
      office7,
      office8,
      office9,
      office10,
      office11,
      office12
    )
  }

  object Accounts {

    lazy val superAdmin1: SuperAdminAccount = SuperAdminAccount(
      id = UUID.fromString("00000000-0000-0000-0000-200000000001"),
      firstName = "Pawel",
      lastName = "Cembaluk",
      email = "pawel.cembaluk@officebuddy.com",
      assignedOfficeId = Some(Offices.office1.id),
      managedOfficeIds = List(Offices.office1.id, Offices.office2.id)
    )

    lazy val allSuperAdmins: List[SuperAdminAccount] = List(superAdmin1)

    lazy val officeManager1: OfficeManagerAccount = OfficeManagerAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000101"),
      firstName = "John",
      lastName = "Doe",
      email = "john.doe@officebuddy.com",
      assignedOfficeId = Some(Offices.office3.id),
      managedOfficeIds = List(Offices.office3.id)
    )

    lazy val officeManager2: OfficeManagerAccount = OfficeManagerAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000102"),
      firstName = "Jane",
      lastName = "Doe",
      email = "jd@example.com",
      assignedOfficeId = Some(Offices.office4.id),
      managedOfficeIds = List(Offices.office4.id)
    )

    lazy val officeManager3: OfficeManagerAccount = OfficeManagerAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000103"),
      firstName = "Alice",
      lastName = "Smith",
      email = "alice@smith.com",
      assignedOfficeId = Some(Offices.office5.id),
      managedOfficeIds = List(Offices.office5.id)
    )

    lazy val officeManager4: OfficeManagerAccount = OfficeManagerAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000104"),
      firstName = "Bob",
      lastName = "Smith",
      email = "bob@bob.bob",
      assignedOfficeId = Some(Offices.office6.id),
      managedOfficeIds = List(Offices.office6.id)
    )

    lazy val officeManager5: OfficeManagerAccount = OfficeManagerAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000105"),
      firstName = "Eve",
      lastName = "Johnson",
      email = "evej@jj.eu",
      assignedOfficeId = Some(Offices.office7.id),
      managedOfficeIds = List(Offices.office7.id, Offices.office8.id)
    )

    lazy val officeManager6: OfficeManagerAccount = OfficeManagerAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000106"),
      firstName = "Charlie",
      lastName = "Brown",
      email = "ilovechocolate@pudding.io",
      assignedOfficeId = Some(Offices.office9.id),
      managedOfficeIds = List(Offices.office9.id)
    )

    lazy val officeManager7: OfficeManagerAccount = OfficeManagerAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000107"),
      firstName = "Daisy",
      lastName = "Johnson",
      email = "daisy@officebuddy.com",
      assignedOfficeId = Some(Offices.office10.id),
      managedOfficeIds = List(Offices.office10.id)
    )

    lazy val officeManager8: OfficeManagerAccount = OfficeManagerAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000108"),
      firstName = "Ella",
      lastName = "Brown",
      email = "ella.brown@mymail.uk",
      assignedOfficeId = Some(Offices.office11.id),
      managedOfficeIds = List(Offices.office11.id)
    )

    lazy val officeManager9: OfficeManagerAccount = OfficeManagerAccount(
      id = UUID.fromString("00000000-0000-0000-0000-000000000109"),
      firstName = "Fiona",
      lastName = "Johnson",
      email = "fiona@shrek.co",
      assignedOfficeId = Some(Offices.office12.id),
      managedOfficeIds = List(Offices.office12.id)
    )

    lazy val allOfficeManagers: List[OfficeManagerAccount] = List(
      officeManager1,
      officeManager2,
      officeManager3,
      officeManager4,
      officeManager5,
      officeManager6,
      officeManager7,
      officeManager8,
      officeManager9
    )

    lazy val user1: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000001"),
      firstName = "Joshua",
      lastName = "Dee",
      email = "joshua.dee@example.com",
      assignedOfficeId = Some(Offices.office1.id)
    )

    lazy val user2: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000002"),
      firstName = "Emily",
      lastName = "Smith",
      email = "emily.smith@example.com",
      assignedOfficeId = Some(Offices.office2.id)
    )

    lazy val user3: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000003"),
      firstName = "Michael",
      lastName = "Johnson",
      email = "michael.johnson@example.com",
      assignedOfficeId = Some(Offices.office3.id)
    )

    lazy val user4: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000004"),
      firstName = "Sarah",
      lastName = "Williams",
      email = "sarah.williams@example.com",
      assignedOfficeId = None
    )

    lazy val user5: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000005"),
      firstName = "David",
      lastName = "Brown",
      email = "david.brown@example.com",
      assignedOfficeId = Some(Offices.office5.id)
    )

    lazy val user6: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000006"),
      firstName = "Jessica",
      lastName = "Jones",
      email = "jessica.jones@example.com",
      assignedOfficeId = Some(Offices.office6.id)
    )

    lazy val user7: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000007"),
      firstName = "Daniel",
      lastName = "Garcia",
      email = "daniel.garcia@example.com",
      assignedOfficeId = Some(Offices.office7.id)
    )

    lazy val user8: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000008"),
      firstName = "Laura",
      lastName = "Martinez",
      email = "laura.martinez@example.com",
      assignedOfficeId = Some(Offices.office8.id)
    )

    lazy val user9: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000009"),
      firstName = "James",
      lastName = "Rodriguez",
      email = "james.rodriguez@example.com",
      assignedOfficeId = Some(Offices.office9.id)
    )

    lazy val user10: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000010"),
      firstName = "Sophia",
      lastName = "Hernandez",
      email = "sophia.hernandez@example.com",
      assignedOfficeId = Some(Offices.office10.id)
    )

    lazy val user11: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000011"),
      firstName = "Matthew",
      lastName = "Lopez",
      email = "matthew.lopez@example.com",
      assignedOfficeId = Some(Offices.office11.id)
    )

    lazy val user12: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000012"),
      firstName = "Olivia",
      lastName = "Gonzalez",
      email = "olivia.gonzalez@example.com",
      assignedOfficeId = Some(Offices.office12.id)
    )

    lazy val user13: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000013"),
      firstName = "Christopher",
      lastName = "Wilson",
      email = "christopher.wilson@example.com",
      assignedOfficeId = Some(Offices.office1.id)
    )

    lazy val user14: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000014"),
      firstName = "Emma",
      lastName = "Anderson",
      email = "emma.anderson@example.com",
      assignedOfficeId = Some(Offices.office2.id)
    )

    lazy val user15: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000015"),
      firstName = "Joshua",
      lastName = "Thomas",
      email = "joshua.thomas@example.com",
      assignedOfficeId = None
    )

    lazy val user16: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000016"),
      firstName = "Isabella",
      lastName = "Taylor",
      email = "isabella.taylor@example.com",
      assignedOfficeId = Some(Offices.office4.id)
    )

    lazy val user17: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000017"),
      firstName = "Andrew",
      lastName = "Moore",
      email = "andrew.moore@example.com",
      assignedOfficeId = Some(Offices.office5.id)
    )

    lazy val user18: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000018"),
      firstName = "Mia",
      lastName = "Jackson",
      email = "mia.jackson@example.com",
      assignedOfficeId = Some(Offices.office6.id)
    )

    lazy val user19: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000019"),
      firstName = "Ethan",
      lastName = "Martin",
      email = "ethan.martin@example.com",
      assignedOfficeId = Some(Offices.office7.id)
    )

    lazy val user20: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000020"),
      firstName = "Ava",
      lastName = "Lee",
      email = "ava.lee@example.com",
      assignedOfficeId = Some(Offices.office8.id)
    )

    lazy val user21: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000021"),
      firstName = "Alexander",
      lastName = "Perez",
      email = "alexander.perez@example.com",
      assignedOfficeId = Some(Offices.office9.id)
    )

    lazy val user22: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000022"),
      firstName = "Charlotte",
      lastName = "Thompson",
      email = "charlotte.thompson@example.com",
      assignedOfficeId = Some(Offices.office10.id)
    )

    lazy val user23: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000023"),
      firstName = "Benjamin",
      lastName = "White",
      email = "benjamin.white@example.com",
      assignedOfficeId = Some(Offices.office11.id)
    )

    lazy val user24: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000024"),
      firstName = "Amelia",
      lastName = "Harris",
      email = "amelia.harris@example.com",
      assignedOfficeId = Some(Offices.office12.id)
    )

    lazy val user25: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000025"),
      firstName = "Lucas",
      lastName = "Clark",
      email = "lucas.clark@example.com",
      assignedOfficeId = Some(Offices.office1.id)
    )

    lazy val user26: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000026"),
      firstName = "Harper",
      lastName = "Lewis",
      email = "harper.lewis@example.com",
      assignedOfficeId = Some(Offices.office2.id)
    )

    lazy val user27: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000027"),
      firstName = "Henry",
      lastName = "Walker",
      email = "henry.walker@example.com",
      assignedOfficeId = Some(Offices.office3.id)
    )

    lazy val user28: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000028"),
      firstName = "Evelyn",
      lastName = "Hall",
      email = "evelyn.hall@example.com",
      assignedOfficeId = Some(Offices.office4.id)
    )

    lazy val user29: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000029"),
      firstName = "Sebastian",
      lastName = "Allen",
      email = "sebastian.allen@example.com",
      assignedOfficeId = Some(Offices.office5.id)
    )

    lazy val user30: UserAccount = UserAccount(
      id = UUID.fromString("00000000-0000-0000-0000-100000000030"),
      firstName = "Abigail",
      lastName = "Young",
      email = "abigail.young@example.com",
      assignedOfficeId = Some(Offices.office6.id)
    )

    lazy val allUsers: List[UserAccount] = List(
      user1,
      user2,
      user3,
      user4,
      user5,
      user6,
      user7,
      user8,
      user9,
      user10,
      user11,
      user12,
      user13,
      user14,
      user15,
      user16,
      user17,
      user18,
      user19,
      user20,
      user21,
      user22,
      user23,
      user24,
      user25,
      user26,
      user27,
      user28,
      user29,
      user30
    )
  }

  def generateDesk[F[_]: FUUID: Monad: Random](officeId: UUID, roomNumber: Int, deskNumber: Int): F[Desk] =
    for {
      id <- FUUID[F].randomUUID()
      isAvailable <- Random[F].betweenInt(0, 100).map(_ < 95)
      notes <- Random[F].betweenInt(0, 100).map { int =>
        if (int < 66) exampleDeskNotes(int % exampleDeskNotes.size)
        else ""
      }
      isStanding <- Random[F].nextBoolean
      monitorsCount <- Random[F].betweenInt(0, 4)
      hasPhone <- Random[F].nextBoolean
    } yield Desk(
      id = id,
      name = s"$roomNumber/$deskNumber",
      isAvailable = isAvailable,
      notes = List(notes),
      isStanding = isStanding,
      monitorsCount = monitorsCount.toShort,
      hasPhone = hasPhone,
      officeId = officeId
    )

  private lazy val exampleDeskNotes = List(
    "Great view of the city",
    "Close to the kitchen",
    "Near the window",
    "Next to the printer",
    "Spacious and comfortable",
    "Quiet and peaceful",
    "Good for focused work",
    "Ideal for team collaboration",
    "Perfect for video calls"
  )

  def generateDeskReservation[F[_]: FUUID: Monad: Random](
    userId: UUID,
    deskId: UUID,
    now: LocalDateTime
  ): F[DeskReservation] =
    for {
      id <- FUUID[F].randomUUID()
      createdAtMinusDays <- Random[F].betweenInt(0, 8)
      reservedFromPlusDays <- Random[F].betweenInt(-3, 15)
      reservationDurationDays <- Random[F].betweenInt(1, 6)
      state <- Random[F].betweenInt(0, 100).map { int =>
        if (int < 10) Rejected
        else if (int < 25) Cancelled
        else if (int < 40) Pending
        else Confirmed
      }
      notes <- Random[F].betweenInt(0, 100).map { int =>
        if (int < 66) exampleDeskReservationNotes(int % exampleDeskReservationNotes.size)
        else ""
      }
    } yield {
      val reservedFromDate = now.toLocalDate.plusDays(reservedFromPlusDays)
      val reservedToDate = reservedFromDate.plusDays(
        reservationDurationDays - 1
      ) // if reservationDurationDays == 1, then reservedFromDate == reservedToDate
      val createdAt = reservedFromDate.minusDays(createdAtMinusDays)

      DeskReservation(
        id = id,
        userId = userId,
        createdAt = createdAt.atTime(now.toLocalTime),
        reservedFromDate = reservedFromDate,
        reservedToDate = reservedToDate,
        state = state,
        notes = notes,
        deskId = deskId
      )
    }

  private lazy val exampleDeskReservationNotes = List(
    "Important meeting",
    "Client visit",
    "Team workshop",
    "Training session",
    "Interview",
    "Product demo",
    "Networking event",
    "Office party",
    "Birthday celebration"
  )
}

package io.github.avapl
package adapters.postgres.repository.office

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.account.PostgresAccountRepository
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import domain.model.error.account.AccountNotFound
import domain.model.error.office.DuplicateOfficeName
import domain.model.error.office.OfficeNotFound
import domain.model.office.Address
import domain.model.office.Office
import domain.model.office.UpdateAddress
import domain.model.office.UpdateOffice
import io.github.avapl.domain.model.account.OfficeManagerAccount
import java.util.UUID
import skunk.Command
import skunk.Session
import skunk.Void
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresOfficeRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(
    name: TestName
  )(run: (PostgresOfficeRepository[IO], PostgresAccountRepository[IO]) => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresOfficeRepository = new PostgresOfficeRepository[IO](session)
      lazy val postgresAccountRepository = new PostgresAccountRepository[IO](session)
      truncateTables(session) >>
        insertOfficeManagers(postgresAccountRepository) >>
        run(postgresOfficeRepository, postgresAccountRepository)
    }

  beforeTest(
    """
      |GIVEN an office to create
      | WHEN create is called
      | THEN the office should be inserted into Postgres
      |""".stripMargin
  ) { (officeRepository, _) =>
    val office = anyOffice

    for {
      _ <- officeRepository.create(office)
      readOffice <- officeRepository.read(office.id)
    } yield expect(readOffice == office)
  }

  beforeTest(
    """
      |GIVEN an existing office and a new office with the same name
      | WHEN create is called
      | THEN the call should fail with DuplicateOfficeName
      |""".stripMargin
  ) { (officeRepository, _) =>
    val office = anyOffice
    val officeWithTheSameName = office.copy(
      id = UUID.fromString("fe58cb74-1125-4e19-a087-6dbbd7165811")
    )

    for {
      _ <- officeRepository.create(office)
      result <- officeRepository.create(officeWithTheSameName).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateOfficeName = DuplicateOfficeName(office.name)
        expect(throwable == duplicateOfficeName)
    }
  }

  beforeTest(
    """
      |GIVEN a non-existent office ID
      | WHEN read is called
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { (officeRepository, _) =>
    val officeId = anyOfficeId

    for {
      result <- officeRepository.read(officeId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(officeId)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN an existing office and an update
      | WHEN update is called
      | THEN the office should be updated
      |""".stripMargin
  ) { (officeRepository, _) =>
    val office = anyOffice
    val officeUpdate = UpdateOffice( // only some properties updated
      name = Some(office.name + "updated"),
      notes = Some("updated" :: anyOfficeNotes),
      address = UpdateAddress(
        addressLine2 = Some(office.address.addressLine2 + "updated"),
        country = Some(office.address.country + "updated")
      )
    )

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.update(office.id, officeUpdate)
      readOffice <- officeRepository.read(office.id)
    } yield {
      val expectedAddress = office.address.copy(
        addressLine2 = officeUpdate.address.addressLine2.get,
        country = officeUpdate.address.country.get
      )
      val expectedOffice = Office(office.id, officeUpdate.name.get, officeUpdate.notes.get, expectedAddress)
      expect(readOffice == expectedOffice)
    }
  }

  beforeTest(
    """
      |GIVEN an existing office
      | WHEN update is called without any changes
      | THEN the call should not fail (no-op)
      |""".stripMargin
  ) { (officeRepository, _) =>
    val office = anyOffice
    val officeUpdate = UpdateOffice()

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.update(office.id, officeUpdate)
    } yield success
  }

  beforeTest(
    """
      |GIVEN an update for nonexistent office
      | WHEN update is called
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { (officeRepository, _) =>
    val officeId = anyOfficeId
    val officeUpdate = anyUpdateOffice

    for {
      result <- officeRepository.update(officeId, officeUpdate).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(officeId)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN 2 existing offices and an update
      | WHEN an office with the name given in the update already exists
      | THEN the call should fail with DuplicateOfficeName
      |""".stripMargin
  ) { (officeRepository, _) =>
    val office1 = anyOffice
    val office2 = anyOffice.copy(
      id = UUID.fromString("96cb8558-8fe8-4330-b50a-00e7e1917757"),
      name = "other"
    )
    val officeUpdate = anyUpdateOffice.copy(name = Some(office2.name))

    for {
      _ <- officeRepository.create(office1)
      _ <- officeRepository.create(office2)
      result <- officeRepository.update(office1.id, officeUpdate).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateOfficeName = DuplicateOfficeName(office2.name)
        expect(throwable == duplicateOfficeName)
    }
  }

  beforeTest(
    """GIVEN an office without managers
      | WHEN updateOfficeManagers is called with a list of manager IDs
      | THEN the managers should be assigned to the office
      |""".stripMargin
  ) { (officeRepository, accountRepository) =>
    val office = anyOffice
    val officeManagerIds = List(officeManagerId1, officeManagerId2)

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.updateOfficeManagers(office.id, officeManagerIds)
      officeManager1 <- accountRepository.read(officeManagerId1)
      officeManager2 <- accountRepository.read(officeManagerId2)
    } yield expect.all(
      officeManager1.asInstanceOf[OfficeManagerAccount].managedOfficeIds.contains(office.id),
      officeManager2.asInstanceOf[OfficeManagerAccount].managedOfficeIds.contains(office.id)
    )
  }

  beforeTest(
    """GIVEN an office with managers
      | WHEN updateOfficeManagers is called with a different list of manager IDs
      | THEN the office managers should be updated
      |""".stripMargin
  ) { (officeRepository, accountRepository) =>
    val office = anyOffice
    val officeManagerIds = List(officeManagerId1, officeManagerId2)
    val updatedOfficeManagerIds = List(officeManagerId3, officeManagerId4)

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.updateOfficeManagers(office.id, officeManagerIds)
      _ <- officeRepository.updateOfficeManagers(office.id, updatedOfficeManagerIds)
      officeManager1 <- accountRepository.read(officeManagerId1)
      officeManager2 <- accountRepository.read(officeManagerId2)
      officeManager3 <- accountRepository.read(officeManagerId3)
      officeManager4 <- accountRepository.read(officeManagerId4)
    } yield expect.all(
      officeManager1.asInstanceOf[OfficeManagerAccount].managedOfficeIds.isEmpty,
      officeManager2.asInstanceOf[OfficeManagerAccount].managedOfficeIds.isEmpty,
      officeManager3.asInstanceOf[OfficeManagerAccount].managedOfficeIds.contains(office.id),
      officeManager4.asInstanceOf[OfficeManagerAccount].managedOfficeIds.contains(office.id)
    )
  }

  beforeTest(
    """GIVEN an office with managers
      | WHEN updateOfficeManagers is called with an empty list
      | THEN the office managers should be removed
      |""".stripMargin
  ) { (officeRepository, accountRepository) =>
    val office = anyOffice
    val officeManagerIds = List(officeManagerId1, officeManagerId2)

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.updateOfficeManagers(office.id, officeManagerIds)
      _ <- officeRepository.updateOfficeManagers(office.id, officeManagerIds = Nil)
      officeManager1 <- accountRepository.read(officeManagerId1)
      officeManager2 <- accountRepository.read(officeManagerId2)
    } yield expect.all(
      officeManager1.asInstanceOf[OfficeManagerAccount].managedOfficeIds.isEmpty,
      officeManager2.asInstanceOf[OfficeManagerAccount].managedOfficeIds.isEmpty
    )
  }

  beforeTest(
    """WHEN updateOfficeManagers is called on non-existent office ID
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { (officeRepository, accountRepository) =>
    val officeId = UUID.fromString("d5b047e2-1410-4c39-93b0-b1fa6fc562f3")

    for {
      result <- officeRepository.updateOfficeManagers(officeId, officeManagerIds = List(officeManagerId1)).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val officeNotFound = OfficeNotFound(officeId)
        expect(throwable == officeNotFound)
    }
  }

  beforeTest(
    """GIVEN an office
      | WHEN updateOfficeManagers is called with non-existent manager ID
      | THEN the call should fail with AccountNotFound
      |""".stripMargin
  ) { (officeRepository, _) =>
    val office = anyOffice
    val nonExistentOfficeManagerId = UUID.fromString("82e21d1d-1f4c-4cd6-b313-5cb0de4d663f")
    val newOfficeManagerIds = List(officeManagerId1, nonExistentOfficeManagerId)

    for {
      _ <- officeRepository.create(office)
      result <- officeRepository.updateOfficeManagers(office.id, newOfficeManagerIds).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val accountNotFound = AccountNotFound(nonExistentOfficeManagerId)
        expect(throwable == accountNotFound)
    }
  }

  beforeTest(
    """
      |GIVEN an existing office
      | WHEN archive is called on its ID
      | THEN the office should be archived
      |""".stripMargin
  ) { (officeRepository, _) =>
    val office = anyOffice

    for {
      _ <- officeRepository.create(office)
      _ <- officeRepository.archive(office.id)
      office <- officeRepository.read(office.id)
    } yield expect(office.isArchived)
  }

  beforeTest(
    """
      |WHEN archive is called on nonexistent office ID
      |THEN the call should not fail (no-op)
      |""".stripMargin
  ) { (officeRepository, _) =>
    val officeId = anyOfficeId

    for {
      _ <- officeRepository.archive(officeId)
    } yield success
  }

  private def truncateTables(session: Resource[IO, Session[IO]]) =
    truncateAccountTable(session) >>
      truncateOfficeTable(session)

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

  private def insertOfficeManagers(accountRepository: PostgresAccountRepository[IO]) = {
    val officeManager1 = anyOfficeManager(officeManagerId1, "officeManager1")
    val officeManager2 = anyOfficeManager(officeManagerId2, "officeManager2")
    val officeManager3 = anyOfficeManager(officeManagerId3, "officeManager3")
    val officeManager4 = anyOfficeManager(officeManagerId4, "officeManager4")
    List(officeManager1, officeManager2, officeManager3, officeManager4).parTraverse_(accountRepository.create)
  }

  private def anyOfficeManager(accountId: UUID, emailPrefix: String) = OfficeManagerAccount(
    id = accountId,
    firstName = "Test",
    lastName = "OfficeManager",
    email = s"$emailPrefix@example.com",
    assignedOfficeId = None,
    managedOfficeIds = Nil
  )

  private val officeManagerId1 = UUID.fromString("46441698-8ab3-464b-b8dc-78ab0b474789")
  private val officeManagerId2 = UUID.fromString("0edc76d8-0e4b-4f68-a87e-ec7eade71247")
  private val officeManagerId3 = UUID.fromString("c3ba2eba-7b3e-40a7-aba2-f47f94025283")
  private val officeManagerId4 = UUID.fromString("ef825d34-8af0-468b-a5fb-314e899dda33")

  private lazy val anyOffice = Office(
    id = anyOfficeId,
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
}

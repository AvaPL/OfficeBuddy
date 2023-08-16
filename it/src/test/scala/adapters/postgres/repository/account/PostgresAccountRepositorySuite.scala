package io.github.avapl
package adapters.postgres.repository.account

import adapters.postgres.fixture.PostgresFixture
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.IO
import cats.effect.Resource
import cats.instances.all._
import cats.syntax.all._
import domain.model.office.Address
import domain.model.office.Office
import java.util.UUID
import skunk.Command
import skunk.Session
import skunk.Void
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresAccountRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(name: TestName)(run: PostgresAccountRepository[IO] => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresAccountRepository = new PostgresAccountRepository[IO](session)
      truncateTables(session) >>
        insertOffices(session) >>
        run(postgresAccountRepository)
    }

  beforeTest(
    """GIVEN a user account to create
      | WHEN createUser is called
      | THEN the user should be inserted into Postgres
      |""".stripMargin
  ) { accountRepository =>
    IO(failure("implement"))
  }

  beforeTest(
    """GIVEN a user account with non-existent office ID assigned
      | WHEN createUser is called
      | THEN the call should fail with OfficeNotFound
      |""".stripMargin
  ) { accountRepository =>
    IO(failure("implement"))
  }

  beforeTest(
    """GIVEN an existing user account and a new user account with the same email
      | WHEN createUser is called
      | THEN the call should fail with DuplicateAccountEmail
      |""".stripMargin
  ) { accountRepository =>
    IO(failure("implement"))
  }

  beforeTest(
    """GIVEN a non-existent user ID
      | WHEN readUser is called
      | THEN the call should fail with AccountNotFound
      |""".stripMargin
  ) { accountRepository =>
    IO(failure("implement"))
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

  private lazy val officeId1 = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")

  private lazy val officeId2 = UUID.fromString("c1e29bfd-5a8a-468f-ba27-4673c42fec04")
}

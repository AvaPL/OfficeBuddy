package io.github.avapl
package adapters.postgres.repository.appmetadata

import adapters.postgres.fixture.PostgresFixture
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import domain.model.appmetadata.AppMetadata
import domain.model.appmetadata.AppMetadata.IsDemoDataLoaded
import skunk._
import skunk.implicits._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object PostgresAppMetadataRepositorySuite extends IOSuite with PostgresFixture {

  private def beforeTest(name: TestName)(run: PostgresAppMetadataRepository[IO] => IO[Expectations]): Unit =
    test(name) { session =>
      lazy val postgresAppMetadataRepository = new PostgresAppMetadataRepository[IO](session)
      truncateAppMetadataTable(session) >>
        run(postgresAppMetadataRepository)
    }

  beforeTest(
    """GIVEN an app metadata key
      | WHEN get is called and the key does not exist
      | THEN the result should be empty
      |""".stripMargin
  ) { appMetadataRepository =>
    for {
      readAppMetadata <- appMetadataRepository.get[IsDemoDataLoaded]
    } yield expect(readAppMetadata.isEmpty)
  }

  beforeTest(
    """GIVEN an app metadata to set
      | WHEN set is called
      | THEN the app metadata should be persisted
      |""".stripMargin
  ) { appMetadataRepository =>
    val appMetadata = IsDemoDataLoaded(true)

    for {
      _ <- appMetadataRepository.set(appMetadata)
      readAppMetadata <- appMetadataRepository.get[IsDemoDataLoaded]
    } yield expect(readAppMetadata.contains(appMetadata))
  }

  beforeTest(
    """GIVEN an app metadata to set
      | WHEN set is called twice
      | THEN the app metadata should be updated
      |""".stripMargin
  ) { appMetadataRepository =>
    val appMetadata = IsDemoDataLoaded(false)
    val updatedAppMetadata = IsDemoDataLoaded(true)

    for {
      _ <- appMetadataRepository.set(appMetadata)
      _ <- appMetadataRepository.set(updatedAppMetadata)
      readAppMetadata <- appMetadataRepository.get[IsDemoDataLoaded]
    } yield expect(readAppMetadata.contains(updatedAppMetadata))
  }

  beforeTest(
    """GIVEN a list of app metadata
      | WHEN get is called for every type
      | THEN no call should fail because of missing decoder
      |""".stripMargin
  ) { appMetadataRepository =>
    List[AppMetadata](
      IsDemoDataLoaded(true)
    ).traverse { // ^ add an element to the list each time a new AppMetadata is added
      case IsDemoDataLoaded(_) => appMetadataRepository.get[IsDemoDataLoaded]
    }.as(success)
  }

  private def truncateAppMetadataTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE app_metadata CASCADE
      """.command
    session.use(_.execute(sql))
  }
}

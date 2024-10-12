package io.github.avapl
package adapters.postgres.repository.appmetadata

import adapters.postgres.fixture.PostgresFixture
import cats.effect.IO
import cats.effect.Resource
import domain.model.appmetadata.AppMetadata.IsDemoDataLoaded
import domain.model.appmetadata.AppMetadataKey
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
    val appMetadataKey = AppMetadataKey.IsDemoDataLoaded

    for {
      readAppMetadata <- appMetadataRepository.get(appMetadataKey)
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
      readAppMetadata <- appMetadataRepository.get(appMetadata.key)
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
      readAppMetadata <- appMetadataRepository.get(appMetadata.key)
    } yield expect(readAppMetadata.contains(updatedAppMetadata))
  }

  private def truncateAppMetadataTable(session: Resource[IO, Session[IO]]) = {
    val sql: Command[Void] =
      sql"""
        TRUNCATE TABLE app_metadata CASCADE
      """.command
    session.use(_.execute(sql))
  }
}

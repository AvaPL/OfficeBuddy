package io.github.avapl
package adapters.postgres.fixture

import adapters.postgres.migration.FlywayMigration
import cats.effect.IO
import cats.effect.Resource
import natchez.Trace.Implicits.noop
import skunk.Session
import weaver.IOSuite

trait PostgresFixture {
  this: IOSuite =>

  override val maxParallelism: Int = 1
  override type Res = Resource[IO, Session[IO]]

  override def sharedResource: Resource[IO, Res] = {
    val host = "localhost"
    val port = 2345
    val user = "postgres"
    val password = "postgres"
    val database = "office_buddy"
    val session = Session.pooled(
      host = host,
      port = port,
      user = user,
      password = Some(password),
      database = database,
      max = 10
    )
    val migration = new FlywayMigration[IO](
      host = host,
      port = port,
      user = user,
      password = password,
      database = database
    )
    session
      .evalTap(_ => migration.run())
  }
}

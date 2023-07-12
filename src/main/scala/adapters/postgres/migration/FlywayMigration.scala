package io.github.avapl
package adapters.postgres.migration

import cats.effect.Sync
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

class FlywayMigration[F[_]: Sync](
  host: String,
  port: Int,
  user: String,
  password: String,
  database: String
) {

  private val flyway = Flyway
    .configure()
    .dataSource(
      s"jdbc:postgresql://$host:$port/$database",
      user,
      password
    )
    .loggers("slf4j")
    .load()

  def run(): F[MigrateResult] =
    Sync[F].blocking {
      flyway.migrate()
    }
}

package io.github.avapl
package adapters.postgres.migration

import cats.effect.kernel.Async
import fly4s.core.Fly4s
import fly4s.core.data.MigrateResult

class FlywayMigration[F[_]: Async](
  host: String,
  port: Int,
  user: String,
  password: String,
  database: String
) {

  private val fly4s = Fly4s.make[F](
    url = s"jdbc:postgresql://$host:$port/$database",
    user = Some(user),
    password = Some(password.toCharArray)
  )

  def run(): F[MigrateResult] =
    fly4s.use(_.migrate)
}

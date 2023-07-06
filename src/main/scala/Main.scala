package io.github.avapl

import adapters.http.office.OfficeRoutes
import adapters.postgres.migration.FlywayMigration
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.Async
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.std.Console
import cats.implicits._
import com.comcast.ip4s.Host
import com.comcast.ip4s.IpLiteralSyntax
import com.comcast.ip4s.Port
import domain.service.office.OfficeService
import natchez.Trace.Implicits.noop
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import skunk.Session

object Main extends IOApp.Simple {

  // TODO: Add logging
  // TODO: Replace noop Skunk tracing with logs
  override def run: IO[Unit] = runF[IO]

  private def runF[F[_]: Async: Console] = {
    val host = "localhost" // TODO: Introduce config
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
    val migration = new FlywayMigration[F](
      host = host,
      port = port,
      user = user,
      password = password,
      database = database
    )
    migration.run() *> [Nothing] session.flatMap { session =>
      val officeRepository = new PostgresOfficeRepository[F](session)
      val officeService = new OfficeService[F](officeRepository)
      val officeRoutes = new OfficeRoutes[F](officeService)
      runHttpServer[F](
        host = ipv4"0.0.0.0", // TODO: Introduce config
        port = port"8080",
        routes = officeRoutes.routes
      )
    }.useForever
  }

  private def runHttpServer[F[_]: Async](host: Host, port: Port, routes: HttpRoutes[F]) =
    EmberServerBuilder
      .default[F]
      .withHost(host)
      .withPort(port)
      .withHttpApp(Router("/" -> routes).orNotFound)
      .build
}

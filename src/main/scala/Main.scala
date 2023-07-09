package io.github.avapl

import adapters.http.ApiError
import adapters.http.ApiError.BadRequest
import adapters.http.office.OfficeEndpoints
import adapters.postgres.migration.FlywayMigration
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.Async
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.std.Console
import cats.implicits._
import com.comcast.ip4s.IpLiteralSyntax
import domain.service.office.OfficeService
import io.circe.generic.auto._
import natchez.Trace.Implicits.noop
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import skunk.Session
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.model.ValuedEndpointOutput
import sttp.tapir.swagger.bundle.SwaggerInterpreter

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
      val officeEndpoints = new OfficeEndpoints[F](officeService)
      // TODO: How to specify the app version?
      // TODO: Add relative paths to v1/v2 APIs
      // TODO: Remove "Links" column
      // TODO: Add a better description
      val docsEndpoints =
        SwaggerInterpreter().fromServerEndpoints(officeEndpoints.endpoints, "Office Buddy", "0.1.0-SNAPSHOT")
      val routes = Http4sServerInterpreter(
        Http4sServerOptions.customiseInterceptors.defaultHandlers { errorMessage =>
          ValuedEndpointOutput(jsonBody[ApiError.BadRequest], BadRequest(errorMessage))
        }.options
      ).toRoutes(officeEndpoints.endpoints ++ docsEndpoints)

      EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0") // TODO: Introduce config
        .withPort(port"8080")
        .withHttpApp(Router("/" -> routes).orNotFound)
        .build
    }.useForever
  }
}

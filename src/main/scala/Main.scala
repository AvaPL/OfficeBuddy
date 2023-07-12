package io.github.avapl

import adapters.http.ApiError
import adapters.http.office.OfficeEndpoints
import adapters.postgres.migration.FlywayMigration
import adapters.postgres.repository.office.PostgresOfficeRepository
import cats.effect.Async
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Sync
import cats.effect.std.Console
import cats.implicits._
import com.comcast.ip4s.IpLiteralSyntax
import domain.service.office.OfficeService
import io.circe.generic.auto._
import natchez.Trace.Implicits.noop
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import skunk.Session
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.model.ValuedEndpointOutput
import sttp.tapir.swagger.SwaggerUIOptions
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import util.BuildInfo

object Main extends IOApp.Simple {

  implicit def logger[F[_]: Sync]: Logger[F] =
    Slf4jLogger.getLogger[F]

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
      val docsEndpoints = SwaggerInterpreter(
        // TODO: /api/internal/ is duplicated here and in the router, extract to config?
        swaggerUIOptions = SwaggerUIOptions.default.contextPath(List("api", "internal"))
      )
        .fromServerEndpoints(officeEndpoints.endpoints, BuildInfo.name, BuildInfo.version)
      val apiInternalRoutes = Http4sServerInterpreter(
        Http4sServerOptions.customiseInterceptors.defaultHandlers { errorMessage =>
          ValuedEndpointOutput(jsonBody[ApiError.BadRequest], ApiError.BadRequest(errorMessage))
        }.options
      ).toRoutes(officeEndpoints.endpoints)
      val docsRoutes = Http4sServerInterpreter().toRoutes(docsEndpoints)

      EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0") // TODO: Introduce config
        .withPort(port"8080")
        .withHttpApp(
          Router(
            "/" -> docsRoutes,
            "/api/internal/" -> apiInternalRoutes
          ).orNotFound
        )
        .build
    }.useForever
  }
}

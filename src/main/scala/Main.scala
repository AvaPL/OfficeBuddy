package io.github.avapl

import adapters.http.ApiError
import adapters.http.desk.DeskEndpoints
import adapters.http.office.OfficeEndpoints
import adapters.http.reservation.ReservationEndpoints
import adapters.postgres.migration.FlywayMigration
import adapters.postgres.repository.desk.PostgresDeskRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import adapters.postgres.repository.reservation.PostgresReservationRepository
import cats.Applicative
import cats.data.NonEmptyList
import cats.effect._
import cats.effect.std.Console
import cats.implicits._
import config.AppConfig
import config.HttpConfig
import config.PostgresConfig
import domain.service.desk.DeskService
import domain.service.office.OfficeService
import domain.service.reservation.ReservationService
import natchez.Trace.Implicits.noop
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax._
import skunk.Session
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.ServerEndpoint
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

  private def runF[F[_]: Async: Console] =
    for {
      config <- loadConfig()
      _ <- runDatabaseMigrations(config.postgres)
      session = createPostgresSessionPool(config.postgres)
      endpoints <- session.use(createEndpoints(_))
      _ <- runHttpServer(config.http, endpoints)
    } yield ()

  private def loadConfig[F[_]: Sync]() =
    ConfigSource.default.loadF[F, AppConfig]()

  private def runDatabaseMigrations[F[_]: Sync](config: PostgresConfig) =
    new FlywayMigration[F](
      host = config.host.show,
      port = config.port.value,
      user = config.user,
      password = config.password,
      database = config.database
    ).run()

  private def createPostgresSessionPool[F[_]: Async: Console](config: PostgresConfig) =
    Session.pooled(
      host = config.host.show,
      port = config.port.value,
      user = config.user,
      password = Some(config.password),
      database = config.database,
      max = config.maxConcurrentSessions
    )

  private def createEndpoints[F[_]: Async](session: Resource[F, Session[F]]) =
    Applicative[F].pure {
      val officeRepository = new PostgresOfficeRepository[F](session)
      val deskRepository = new PostgresDeskRepository[F](session)
      val reservationRepository = new PostgresReservationRepository[F](session)

      val officeService = new OfficeService[F](officeRepository)
      val deskService = new DeskService[F](deskRepository)
      val reservationService = new ReservationService[F](reservationRepository)

      val officeEndpoints = new OfficeEndpoints[F](officeService).endpoints
      val deskEndpoints = new DeskEndpoints[F](deskService).endpoints
      val reservationEndpoints = new ReservationEndpoints[F](reservationService).endpoints

      officeEndpoints <+> deskEndpoints <+> reservationEndpoints
    }

  private def runHttpServer[F[_]: Async](
    config: HttpConfig,
    endpoints: List[ServerEndpoint[Any, F]]
  ) =
    EmberServerBuilder
      .default[F]
      .withHost(config.host)
      .withPort(config.port)
      .withHttpApp(router(config.internalApiBasePath, endpoints))
      .build
      .use[Unit](_ => Spawn[F].never)

  private def router[F[_]: Async](
    internalApiBasePath: NonEmptyList[String],
    endpoints: List[ServerEndpoint[Any, F]]
  ) =
    Router(
      "/" -> docsRoutes(internalApiBasePath, endpoints),
      internalApiBasePath.mkString_("/", "/", "") -> internalApiRoutes(endpoints)
    ).orNotFound

  private def docsRoutes[F[_]: Async](
    internalApiBasePath: NonEmptyList[String],
    endpoints: List[ServerEndpoint[Any, F]]
  ) = {
    val options = SwaggerUIOptions.default.contextPath(internalApiBasePath.toList)
    val swaggerInterpreter = SwaggerInterpreter(swaggerUIOptions = options)
      .fromServerEndpoints(endpoints, BuildInfo.name, BuildInfo.version)
    Http4sServerInterpreter().toRoutes(swaggerInterpreter)
  }

  private def internalApiRoutes[F[_]: Async](endpoints: List[ServerEndpoint[Any, F]]) = {
    val options = Http4sServerOptions.customiseInterceptors
      .defaultHandlers(apiErrorHandler) // TODO: Move the handler (or server creation) somewhere else?
      .options
    Http4sServerInterpreter(options).toRoutes(endpoints)
  }

  private def apiErrorHandler(errorMessage: String) =
    ValuedEndpointOutput(jsonBody[ApiError.BadRequest], ApiError.BadRequest(errorMessage))
}

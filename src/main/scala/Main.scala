package io.github.avapl

import adapters.facade.repository.account.KeycloakPostgresAccountRepository
import adapters.http.ApiError
import adapters.http.account.AccountEndpoints
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
import config.KeycloakConfig
import config.PostgresConfig
import domain.service.account.AccountService
import domain.service.desk.DeskService
import domain.service.office.OfficeService
import domain.service.reservation.ReservationService
import io.github.avapl.adapters.keycloak.auth.repository.KeycloakPublicKeyRepository
import io.github.avapl.adapters.keycloak.auth.service.KeycloakRolesExtractorService
import natchez.Trace.Implicits.noop
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.keycloak.admin.client.Keycloak
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
      keycloak = createKeycloakClient(config.keycloak)
      endpoints <- session.use(createEndpoints(_, keycloak, config.keycloak.appRealmName))
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

  private def createKeycloakClient(config: KeycloakConfig) =
    Keycloak.getInstance(
      config.serverUrl,
      config.masterRealmName,
      config.adminUser,
      config.adminPassword,
      config.adminClientId
    )

  private def createEndpoints[F[_]: Async](
    session: Resource[F, Session[F]],
    keycloak: Keycloak,
    appRealmName: String
  ) =
    for {
      publicKeyRepository <- KeycloakPublicKeyRepository[F](keycloak, appRealmName)
    } yield {
      val officeRepository = new PostgresOfficeRepository[F](session)
      val deskRepository = new PostgresDeskRepository[F](session)
      val reservationRepository = new PostgresReservationRepository[F](session)
      val accountRepository = KeycloakPostgresAccountRepository[F](keycloak, appRealmName, session)

      val officeService = new OfficeService[F](officeRepository)
      val deskService = new DeskService[F](deskRepository)
      val reservationService = new ReservationService[F](reservationRepository)
      val accountService = new AccountService[F](accountRepository)
      val rolesExtractorService = KeycloakRolesExtractorService

      val officeEndpoints = new OfficeEndpoints[F](officeService, publicKeyRepository, rolesExtractorService).endpoints
      val deskEndpoints = new DeskEndpoints[F](deskService, publicKeyRepository, rolesExtractorService).endpoints
      val reservationEndpoints = new ReservationEndpoints[F](reservationService).endpoints
      val accountEndpoints =
        new AccountEndpoints[F](accountService, publicKeyRepository, rolesExtractorService).endpoints

      officeEndpoints <+> deskEndpoints <+> reservationEndpoints <+> accountEndpoints
    }

  private def runHttpServer[F[_]: Async](
    config: HttpConfig,
    endpoints: List[ServerEndpoint[Any, F]]
  ) =
    EmberServerBuilder
      .default[F]
      .withHost(config.host)
      .withPort(config.port)
      .withHttpApp(router(config.swaggerUIPath, config.internalApiBasePath, endpoints))
      .build
      .use[Unit](_ => Spawn[F].never)

  private def router[F[_]: Async](
    swaggerUIPath: NonEmptyList[String],
    internalApiBasePath: NonEmptyList[String],
    endpoints: List[ServerEndpoint[Any, F]]
  ) =
    Router(
      "/" -> docsRoutes(swaggerUIPath, internalApiBasePath, endpoints),
      internalApiBasePath.mkString_("/", "/", "") -> internalApiRoutes(endpoints)
    ).orNotFound

  private def docsRoutes[F[_]: Async](
    swaggerUIPath: NonEmptyList[String],
    internalApiBasePath: NonEmptyList[String],
    endpoints: List[ServerEndpoint[Any, F]]
  ) = {
    val options = SwaggerUIOptions.default
      .pathPrefix(swaggerUIPath.toList)
      .contextPath(internalApiBasePath.toList)
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

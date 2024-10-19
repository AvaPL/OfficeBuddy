package io.github.avapl

import adapters.facade.repository.account.KeycloakPostgresAccountRepository
import adapters.http.ApiError
import adapters.http.account.AccountEndpoints
import adapters.http.desk.DeskEndpoints
import adapters.http.office.OfficeEndpoints
import adapters.http.reservation.DeskReservationEndpoints
import adapters.keycloak.auth.repository.KeycloakPublicKeyRepository
import adapters.keycloak.auth.service.KeycloakClaimsExtractorService
import adapters.postgres.migration.FlywayMigration
import adapters.postgres.repository.account.view.PostgresAccountViewRepository
import adapters.postgres.repository.appmetadata.PostgresAppMetadataRepository
import adapters.postgres.repository.desk.PostgresDeskRepository
import adapters.postgres.repository.desk.view.PostgresDeskViewRepository
import adapters.postgres.repository.office.PostgresOfficeRepository
import adapters.postgres.repository.office.view.PostgresOfficeViewRepository
import adapters.postgres.repository.reservation.PostgresDeskReservationRepository
import adapters.postgres.repository.reservation.view.PostgresDeskReservationViewRepository
import cats.data.NonEmptyList
import cats.effect._
import cats.effect.std.Console
import cats.effect.std.Random
import cats.implicits._
import config.AppConfig
import config.HttpConfig
import config.KeycloakConfig
import config.PostgresConfig
import domain.model.reservation.DeskReservation
import domain.model.reservation.view.DeskReservationListView
import domain.repository.account.AccountRepository
import domain.repository.account.TemporaryPasswordRepository
import domain.repository.account.view.AccountViewRepository
import domain.repository.appmetadata.AppMetadataRepository
import domain.repository.desk.DeskRepository
import domain.repository.desk.view.DeskViewRepository
import domain.repository.office.OfficeRepository
import domain.repository.office.view.OfficeViewRepository
import domain.repository.reservation.ReservationRepository
import domain.repository.reservation.view.ReservationViewRepository
import domain.service.account.AccountService
import domain.service.account.SuperAdminInitService
import domain.service.demo.DemoDataService
import domain.service.desk.DeskService
import domain.service.office.OfficeService
import domain.service.reservation.DeskReservationService
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
import util.FUUID

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
      repositories <- session.use(createRepositories(_, keycloak, config.keycloak.appRealmName).pure[F])
      _ <- loadInitialSuperAdmin(repositories.accountRepository)
      _ <- loadDemoData(repositories, config.demoDataEnabled)
      endpoints <- createEndpoints(repositories, keycloak, config.keycloak.appRealmName)
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

  private def createRepositories[F[_]: Concurrent: Sync](
    session: Resource[F, Session[F]],
    keycloak: Keycloak,
    appRealmName: String
  ) = {
    val monadCancelThrow = Concurrent[F]
    Repositories(
      appMetadataRepository = new PostgresAppMetadataRepository[F](session)(monadCancelThrow),
      officeRepository = new PostgresOfficeRepository[F](session)(monadCancelThrow),
      deskRepository = new PostgresDeskRepository[F](session)(monadCancelThrow),
      deskReservationRepository = new PostgresDeskReservationRepository[F](session)(monadCancelThrow),
      accountRepository = KeycloakPostgresAccountRepository[F](keycloak, appRealmName, session),
      officeViewRepository = new PostgresOfficeViewRepository[F](session)(implicitly, monadCancelThrow),
      deskViewRepository = new PostgresDeskViewRepository[F](session)(implicitly, monadCancelThrow),
      deskReservationViewRepository = new PostgresDeskReservationViewRepository[F](session)(implicitly, monadCancelThrow),
      accountViewRepository = new PostgresAccountViewRepository[F](session)(implicitly, monadCancelThrow)
    )
  }

  private def loadInitialSuperAdmin[F[_]: Sync](
    accountRepository: AccountRepository[F] with TemporaryPasswordRepository[F]
  ) =
    new SuperAdminInitService[F](accountRepository).initSuperAdmin()

  private def loadDemoData[F[_]: FUUID: Sync](
    repositories: Repositories[F],
    demoDataEnabled: Boolean
  ): F[Unit] =
    if (demoDataEnabled) loadDemoData(repositories)
    else ().pure[F]

  private def loadDemoData[F[_]: FUUID: Sync](repositories: Repositories[F]): F[Unit] =
    for {
      random <- Random.scalaUtilRandomSeedInt(0) // Arbitrarily chosen seed to make the demo data deterministic
      demoDataService = {
        implicit val r = random
        new DemoDataService[F](
          repositories.appMetadataRepository,
          repositories.accountRepository,
          repositories.officeRepository,
          repositories.deskRepository,
          repositories.deskReservationRepository
        )
      }
      _ <- demoDataService.loadDemoData()
    } yield ()

  private def createEndpoints[F[_]: Async](
    repositories: Repositories[F],
    keycloak: Keycloak,
    appRealmName: String
  ) =
    for {
      publicKeyRepository <- KeycloakPublicKeyRepository[F](keycloak, appRealmName)
    } yield {
      val officeService = new OfficeService[F](repositories.officeRepository)
      val deskService = new DeskService[F](repositories.deskRepository)
      val deskReservationService = new DeskReservationService[F](repositories.deskReservationRepository)
      val accountService = new AccountService[F](repositories.accountRepository)
      val rolesExtractorService = KeycloakClaimsExtractorService

      val officeEndpoints = new OfficeEndpoints[F](
        officeService,
        repositories.officeViewRepository, // TODO: Views should also be a part of services
        publicKeyRepository,
        rolesExtractorService
      ).endpoints
      val deskEndpoints = new DeskEndpoints[F](
        deskService,
        repositories.deskViewRepository,
        publicKeyRepository,
        rolesExtractorService
      ).endpoints
      val deskReservationEndpoints = new DeskReservationEndpoints[F](
        deskReservationService,
        repositories.deskReservationViewRepository,
        publicKeyRepository,
        rolesExtractorService
      ).endpoints
      val accountEndpoints = new AccountEndpoints[F](
        accountService,
        repositories.accountViewRepository,
        publicKeyRepository,
        rolesExtractorService
      ).endpoints

      officeEndpoints <+> deskEndpoints <+> deskReservationEndpoints <+> accountEndpoints
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

  private case class Repositories[F[_]](
    appMetadataRepository: AppMetadataRepository[F],
    officeRepository: OfficeRepository[F],
    deskRepository: DeskRepository[F],
    deskReservationRepository: ReservationRepository[F, DeskReservation],
    accountRepository: AccountRepository[F] with TemporaryPasswordRepository[F],
    officeViewRepository: OfficeViewRepository[F],
    deskViewRepository: DeskViewRepository[F],
    deskReservationViewRepository: ReservationViewRepository[F, DeskReservationListView],
    accountViewRepository: AccountViewRepository[F]
  )
}

package io.github.avapl
package adapters.http.desk

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import adapters.http.ApiError
import adapters.http.SecuredApiEndpoint
import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.User
import domain.model.error.desk.DeskNotFound
import domain.model.error.desk.DuplicateDeskNameForOffice
import domain.model.error.office.OfficeNotFound
import domain.service.desk.DeskService
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

class DeskEndpoints[F[_]: Clock: MonadThrow](
  deskService: DeskService[F],
  override val publicKeyRepository: PublicKeyRepository[F],
  override val rolesExtractor: ClaimsExtractorService
) extends SecuredApiEndpoint[F] {

  override protected val apiEndpointName: String = "desk"

  val endpoints: List[ServerEndpoint[Any, F]] =
    createDeskEndpoint ::
      readDeskEndpoint ::
      updateDeskEndpoint ::
      archiveDeskEndpoint ::
      Nil

  private lazy val createDeskEndpoint =
    securedEndpoint(requiredRole = OfficeManager).post
      .summary("Create a desk")
      .description("Required role: office manager")
      .in(
        jsonBody[ApiCreateDesk]
          .example(apiCreateDeskExample)
      )
      .out(
        statusCode(StatusCode.Created) and jsonBody[ApiDesk]
          .description("Desk created")
          .example(apiDeskExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Office with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Desk with the given name already exists in the office")
        )
      )
      .serverLogic(_ => createDesk)

  private def createDesk(apiCreateDesk: ApiCreateDesk) =
    deskService
      .createDesk(apiCreateDesk.toDomain)
      .map(ApiDesk.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(officeId) =>
          ApiError.BadRequest(s"Office [id: $officeId] was not found").asLeft
        case DuplicateDeskNameForOffice(name, officeId) =>
          ApiError.Conflict(s"Desk '$name' is already defined for office [id: $officeId]").asLeft
      }

  private lazy val readDeskEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Find a desk by ID")
      .description("Required role: user")
      .in(path[UUID]("deskId"))
      .out(
        jsonBody[ApiDesk]
          .description("Found desk")
          .example(apiDeskExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Desk with the given ID was not found")
        )
      )
      .serverLogic(_ => readDesk)

  private def readDesk(deskId: UUID) =
    deskService
      .readDesk(deskId)
      .map(ApiDesk.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case DeskNotFound(deskId) => ApiError.NotFound(s"Desk [id: $deskId] was not found").asLeft
      }

  private lazy val updateDeskEndpoint =
    securedEndpoint(requiredRole = OfficeManager).patch
      .summary("Update a desk")
      .description("Required role: office manager")
      .in(
        path[UUID]("deskId") and jsonBody[ApiUpdateDesk]
          .example(apiUpdateDeskExample)
      )
      .out(
        jsonBody[ApiDesk]
          .description("Updated desk")
          .example(apiDeskExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Office with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Desk with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Desk with the given name already exists in the office")
        )
      )
      .serverLogic(_ => (updateDesk _).tupled)

  private def updateDesk(deskId: UUID, apiUpdateDesk: ApiUpdateDesk) =
    deskService
      .updateDesk(deskId, apiUpdateDesk.toDomain)
      .map(ApiDesk.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case DuplicateDeskNameForOffice(name, officeId) =>
          ApiError.Conflict(s"Desk '$name' is already defined for office [id: $officeId]").asLeft
        case DeskNotFound(deskId) =>
          ApiError.NotFound(s"Desk [id: $deskId] was not found").asLeft
        case OfficeNotFound(officeId) =>
          ApiError.BadRequest(s"Office [id: $officeId] was not found").asLeft
      }

  private lazy val archiveDeskEndpoint =
    securedEndpoint(requiredRole = OfficeManager).delete
      .summary("Archive a desk")
      .description(
        """Archives a desk. The desk is NOT deleted. The operation is idempotent ie. if the desk doesn't exist, the operation doesn't fail.
          |
          |Required role: office manager
          |""".stripMargin
      )
      .in(path[UUID]("deskId"))
      .out(
        statusCode(StatusCode.NoContent)
          .description("Desk archived or not found")
      )
      .serverLogic(_ => archiveDesk)

  private def archiveDesk(deskId: UUID) =
    deskService
      .archiveDesk(deskId)
      .as(().asRight[ApiError])

  private lazy val apiDeskExample = ApiDesk(
    id = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b"),
    name = "107.1",
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298"),
    isArchived = false
  )

  private lazy val apiCreateDeskExample = ApiCreateDesk(
    name = "107.1",
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")
  )

  private lazy val apiUpdateDeskExample = ApiUpdateDesk(
    name = "107.1",
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298"),
    isArchived = false
  )
}

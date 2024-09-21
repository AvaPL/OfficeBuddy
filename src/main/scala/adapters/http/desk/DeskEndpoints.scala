package io.github.avapl
package adapters.http.desk

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import adapters.http.ApiError
import adapters.http.SecuredApiEndpoint
import adapters.http.desk.view.ApiDeskListView
import adapters.http.desk.view.ApiDeskView
import adapters.http.model.view.ApiPagination
import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.User
import domain.model.error.desk.DeskNotFound
import domain.model.error.desk.DuplicateDeskNameForOffice
import domain.model.error.office.OfficeNotFound
import domain.repository.desk.view.DeskViewRepository
import domain.service.desk.DeskService
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

class DeskEndpoints[F[_]: Clock: MonadThrow](
  deskService: DeskService[F],
  deskViewRepository: DeskViewRepository[F],
  override val publicKeyRepository: PublicKeyRepository[F],
  override val claimsExtractor: ClaimsExtractorService
) extends SecuredApiEndpoint[F] {

  override protected val apiEndpointName: String = "desk"

  val endpoints: List[ServerEndpoint[Any, F]] =
    createDeskEndpoint ::
      readDeskEndpoint ::
      updateDeskEndpoint ::
      archiveDeskEndpoint ::
      deskListViewEndpoint ::
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

  private lazy val deskListViewEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Desks list view")
      .description(
        """List of desks assigned to an office to be displayed in the UI.
          |
          |Required role: user
          |""".stripMargin
      )
      .in("view" / "list")
      .in(
        query[UUID]("office_id")
          .description("Assigned office ID")
          .example(officeIdExample)
      )
      .in(
        query[Int]("limit")
          .description("Maximum number of results to return (pagination)")
          .validate(Validator.min(1))
          .example(10)
      )
      .in(
        query[Int]("offset")
          .description("Number of results to skip (pagination)")
          .validate(Validator.min(0))
          .example(0)
      )
      .out(
        jsonBody[ApiDeskListView]
          .description("List of desks")
          .example(apiDeskListViewExample)
      )
      .serverLogic(_ => (deskListView _).tupled)

  private def deskListView(
    officeId: UUID,
    limit: Int,
    offset: Int
  ) =
    deskViewRepository
      .listDesks(officeId, limit, offset)
      .map(ApiDeskListView.fromDomain)
      .map(_.asRight[ApiError])

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

  private lazy val officeIdExample = UUID.fromString("d5c4921a-3f7f-474f-8cba-e6fc8cb6c545")

  private lazy val apiDeskListViewExample = ApiDeskListView(
    desks = List(
      ApiDeskView(
        id = UUID.fromString("1a84cc45-11db-468b-bf69-125ed293f6c9"),
        name = "107.1",
        isAvailable = true,
        notes = List("Rubik's Cube on the desk"),
        isStanding = true,
        monitorsCount = 2,
        hasPhone = true
      ),
      ApiDeskView(
        id = UUID.fromString("95af1416-0526-42af-8f25-71b09a6793d0"),
        name = "107.2",
        isAvailable = true,
        notes = List("Near the window"),
        isStanding = false,
        monitorsCount = 1,
        hasPhone = false
      ),
      ApiDeskView(
        id = UUID.fromString("71659873-7921-4bbf-9cdc-e62bac4b6177"),
        name = "108.1",
        isAvailable = false,
        notes = Nil,
        isStanding = false,
        monitorsCount = 2,
        hasPhone = true
      )
    ),
    pagination = ApiPagination(
      limit = 3,
      offset = 0,
      hasMoreResults = true
    )
  )
}

package io.github.avapl
package adapters.http.office

import adapters.http.ApiError
import adapters.http.BaseEndpoint
import cats.effect.Async
import cats.syntax.all._
import domain.model.error.office.DuplicateOfficeName
import domain.model.error.office.OfficeNotFound
import domain.service.office.OfficeService
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

class OfficeEndpoints[F[_]: Async](
  officeService: OfficeService[F]
) extends BaseEndpoint {

  override protected val baseEndpointName: String = "office"

  val endpoints: List[ServerEndpoint[Any, F]] =
    createOfficeEndpoint ::
      readOfficeEndpoint ::
      updateOfficeEndpoint ::
      archiveOfficeEndpoint ::
      Nil

  private lazy val createOfficeEndpoint =
    baseEndpoint.post
      .summary("Create an office")
      .in(
        jsonBody[ApiCreateOffice]
          .example(apiCreateOfficeExample)
      )
      .out(
        statusCode(StatusCode.Created) and jsonBody[ApiOffice]
          .description("Office created")
          .example(apiOfficeExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Office with the given name already exists")
        )
      )
      .serverLogic(createOffice)

  private def createOffice(apiCreateOffice: ApiCreateOffice) =
    officeService
      .createOffice(apiCreateOffice.toDomain)
      .map(ApiOffice.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case DuplicateOfficeName(name) => ApiError.Conflict(s"Office '$name' is already defined").asLeft
      }

  private lazy val readOfficeEndpoint =
    baseEndpoint.get
      .summary("Find an office by ID")
      .in(path[UUID]("officeId"))
      .out(
        jsonBody[ApiOffice]
          .description("Found office")
          .example(apiOfficeExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Office with the given ID was not found")
        )
      )
      .serverLogic(readOffice)

  private def readOffice(officeId: UUID) =
    officeService
      .readOffice(officeId)
      .map(ApiOffice.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(officeId) => ApiError.NotFound(s"Office [id: $officeId] was not found").asLeft
      }

  private lazy val updateOfficeEndpoint =
    baseEndpoint.patch
      .summary("Update an office")
      .in(
        path[UUID]("officeId") and jsonBody[ApiUpdateOffice]
          .example(apiUpdateOfficeExample)
      )
      .out(
        jsonBody[ApiOffice]
          .description("Updated office")
          .example(apiOfficeExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Office with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Office with the given name already exists")
        )
      )
      .serverLogic((updateOffice _).tupled)

  private def updateOffice(officeId: UUID, apiUpdateOffice: ApiUpdateOffice) =
    officeService
      .updateOffice(officeId, apiUpdateOffice.toDomain)
      .map(ApiOffice.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(officeId)  => ApiError.NotFound(s"Office [id: $officeId] was not found").asLeft
        case DuplicateOfficeName(name) => ApiError.Conflict(s"Office '$name' is already defined").asLeft
      }

  private lazy val archiveOfficeEndpoint =
    baseEndpoint.delete
      .summary("Archive an office")
      .description(
        "Archives an office. The office is NOT deleted. The operation is idempotent ie. if the office doesn't exist, the operation doesn't fail."
      )
      .in(path[UUID]("officeId"))
      .out(
        statusCode(StatusCode.NoContent)
          .description("Office archived or not found")
      )
      .serverLogic(archiveOffice)

  private def archiveOffice(officeId: UUID) =
    officeService
      .archiveOffice(officeId)
      .as(().asRight[ApiError])

  private lazy val apiOfficeExample = ApiOffice(
    id = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298"),
    name = "Wroclaw",
    notes = List("Everyone's favorite", "The funniest one"),
    address = apiAddressExample,
    isArchived = false
  )

  private lazy val apiAddressExample = ApiAddress(
    addressLine1 = "Powstancow Slaskich 9",
    addressLine2 = "1st floor",
    postalCode = "53-332",
    city = "Wroclaw",
    country = "Poland"
  )

  private lazy val apiCreateOfficeExample = ApiCreateOffice(
    name = "Wroclaw",
    notes = List("Everyone's favorite", "The funniest one"),
    address = apiAddressExample
  )

  private lazy val apiUpdateOfficeExample = ApiUpdateOffice(
    name = "Wroclaw",
    notes = List("Everyone's favorite", "The funniest one"),
    address = apiAddressExample,
    isArchived = false
  )
}

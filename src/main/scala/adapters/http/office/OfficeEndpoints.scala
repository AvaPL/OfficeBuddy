package io.github.avapl
package adapters.http.office

import adapters.http.ApiError
import cats.effect.Async
import cats.syntax.all._
import domain.repository.office.OfficeRepository._
import domain.service.office.OfficeService
import io.circe.generic.auto._ // TODO: derevo should be used instead
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

// TODO: Add unit tests
class OfficeEndpoints[F[_]: Async](
  officeService: OfficeService[F]
) {

  val endpoints: List[ServerEndpoint[Any, F]] =
    createOfficeEndpoint ::
      readOfficeEndpoint ::
      updateOfficeEndpoint ::
      deleteOfficeEndpoint ::
      Nil

  // TODO: Probably the base endpoint should be defined in some central part
  private lazy val baseEndpoint =
    endpoint
      .withTag("office")
      .in("office")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(
            statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
              .description("Malformed parameters or request body")
          ),
          oneOfDefaultVariant(
            statusCode(StatusCode.InternalServerError) and jsonBody[ApiError.InternalServerError]
              .description("Internal server error")
          )
        )
      )

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

  private def readOffice(id: UUID) =
    officeService
      .readOffice(id)
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
      .serverLogic((updateOffice _).tupled)

  private def updateOffice(officeId: UUID, apiUpdateOffice: ApiUpdateOffice) =
    officeService
      .updateOffice(officeId, apiUpdateOffice.toDomain)
      .map(ApiOffice.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(id: UUID) => ApiError.NotFound(s"Office [id: $id] was not found").asLeft
      }

  private lazy val deleteOfficeEndpoint =
    baseEndpoint.delete
      .summary("Delete an office")
      .description("The deletion is idempotent ie. if the office doesn't exist, the operation doesn't fail.")
      .in(path[UUID]("officeId"))
      .out(
        statusCode(StatusCode.NoContent)
          .description("Office deleted or not found")
      )
      .serverLogic(deleteOffice)

  private def deleteOffice(id: UUID) =
    officeService
      .deleteOffice(id)
      .as(().asRight[ApiError])

  private lazy val apiOfficeExample = ApiOffice(
    id = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298"),
    name = "Wroclaw",
    notes = List("Everyone's favorite", "The funniest one"),
    address = apiAddressExample
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
    address = apiAddressExample
  )
}

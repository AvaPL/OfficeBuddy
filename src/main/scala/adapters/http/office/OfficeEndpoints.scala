package io.github.avapl
package adapters.http.office

import adapters.http.ApiError
import cats.effect.Async
import cats.syntax.all._
import domain.repository.office.OfficeRepository._
import domain.service.office.OfficeService
import io.circe.generic.auto._
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
      .in("api" / "internal" / "office")
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
      .in(jsonBody[JsonCreateOffice])
      .out(
        statusCode(StatusCode.Created) and jsonBody[JsonOffice]
          .description("Office created")
      )
      .errorOutVariant(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Office with the given name already exists")
        )
      )
      .serverLogic(createOffice)

  private def createOffice(jsonCreateOffice: JsonCreateOffice) =
    officeService
      .createOffice(jsonCreateOffice.name, jsonCreateOffice.notes, jsonCreateOffice.address.toDomain)
      .map(officeId => JsonOffice.fromJsonCreateOffice(officeId, jsonCreateOffice))
      .map(_.asRight[ApiError])
      .recover {
        case DuplicateOfficeName(name) => ApiError.Conflict(s"Office '$name' is already defined").asLeft
      }

  private lazy val readOfficeEndpoint =
    baseEndpoint.get
      .summary("Find an office by ID")
      .in(path[UUID]("officeId")) // TODO: Add examples
      .out(
        jsonBody[JsonOffice] // TODO: Add examples
          .description("Found office")
      )
      .errorOutVariant(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Office with the given ID was not found")
        )
      )
      .serverLogic(readOffice)

  private def readOffice(id: UUID) =
    officeService
      .readOffice(id)
      .map(JsonOffice.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(officeId) => ApiError.NotFound(s"Office [id: $officeId] was not found").asLeft
      }

  private lazy val updateOfficeEndpoint =
    baseEndpoint.patch
      .summary("Update an office")
      .in(path[UUID]("officeId") and jsonBody[JsonUpdateOffice])
      .out(
        jsonBody[JsonOffice]
          .description("Updated office")
      )
      .errorOutVariant(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Office with the given ID was not found")
        )
      )
      .serverLogic((updateOffice _).tupled)

  private def updateOffice(officeId: UUID, jsonUpdateOffice: JsonUpdateOffice) = {
    val domainOffice = jsonUpdateOffice.toDomain(officeId)
    officeService
      .updateOffice(domainOffice)
      .as(JsonOffice.fromDomain(domainOffice))
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(id: UUID) => ApiError.NotFound(s"Office [id: $id] was not found").asLeft
      }
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
}

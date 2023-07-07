package io.github.avapl
package adapters.http.office

import cats.effect.Async
import cats.syntax.all._
import domain.repository.office.OfficeRepository._
import domain.service.office.OfficeService
import io.circe.generic.auto._
import java.util.UUID
import org.http4s._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s._

// TODO: Add unit tests
class OfficeRoutes[F[_]: Async](
  officeService: OfficeService[F]
) {

  // TODO: Probably the base endpoint should be defined in some central part
  private val baseEndpoint =
    endpoint
      .in("api" / "v1" / "office")
      .errorOut(statusCode and jsonBody[OfficeApiError])

  private val createOfficeEndpoint =
    baseEndpoint.post
      .in(jsonBody[JsonCreateOffice])
      .out(statusCode(StatusCode.Created) and jsonBody[JsonOffice])
      .serverLogic(createOffice)

  private def createOffice(jsonCreateOffice: JsonCreateOffice) =
    officeService
      .createOffice(jsonCreateOffice.name, jsonCreateOffice.notes, jsonCreateOffice.address.toDomain)
      .map(officeId => JsonOffice.fromJsonCreateOffice(officeId, jsonCreateOffice))
      .map(_.asRight[(StatusCode, OfficeApiError)])
      .recover {
        case DuplicateOfficeName(name) =>
          val officeApiError = OfficeApiError(s"Office '$name' is already defined")
          (StatusCode.Conflict -> officeApiError).asLeft
      }

  private val readOfficeEndpoint =
    baseEndpoint.get
      .in(path[UUID]("id"))
      .out(jsonBody[JsonOffice])
      .serverLogic(readOffice)

  private def readOffice(id: UUID) =
    officeService
      .readOffice(id)
      .map(JsonOffice.fromDomain)
      .map(_.asRight[(StatusCode, OfficeApiError)])
      .recover {
        case OfficeNotFound(officeId) =>
          val officeApiError = OfficeApiError(s"Office [id: $officeId] was not found")
          (StatusCode.NotFound -> officeApiError).asLeft
      }

  private val updateOfficeEndpoint =
    baseEndpoint.patch
      .in(path[UUID]("id") and jsonBody[JsonUpdateOffice])
      .out(jsonBody[JsonOffice])
      .serverLogic((updateOffice _).tupled)

  private def updateOffice(officeId: UUID, jsonUpdateOffice: JsonUpdateOffice) = {
    val domainOffice = jsonUpdateOffice.toDomain(officeId)
    officeService
      .updateOffice(domainOffice)
      .as(JsonOffice.fromDomain(domainOffice))
      .map(_.asRight[(StatusCode, OfficeApiError)])
      .recover {
        case OfficeNotFound(id: UUID) =>
          val officeApiError = OfficeApiError(s"Office [id: $id] was not found")
          (StatusCode.NotFound -> officeApiError).asLeft
      }
  }

  private val deleteOfficeEndpoint =
    baseEndpoint.delete
      .in(path[UUID]("id"))
      .out(statusCode(StatusCode.NoContent))
      .serverLogic(deleteOffice)

  private def deleteOffice(id: UUID) =
    officeService
      .deleteOffice(id)
      .as(().asRight[(StatusCode, OfficeApiError)])

  // TODO: Add OpenAPI docs endpoint
  val routes: HttpRoutes[F] =
    Http4sServerInterpreter().toRoutes(
      createOfficeEndpoint ::
        readOfficeEndpoint ::
        updateOfficeEndpoint ::
        deleteOfficeEndpoint ::
        Nil
    )
}

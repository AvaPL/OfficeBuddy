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

  // TODO: Implement
//  private val createOfficeEndpoint =
//    baseEndpoint.post

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
        case OfficeNotFound(id: UUID) =>
          val officeApiError = OfficeApiError(s"Office [id: $id] was not found")
          (StatusCode.NotFound -> officeApiError).asLeft
      }

  // TODO: Implement
//  private val updateOfficeEndpoint =
//    baseEndpoint.patch

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
      readOfficeEndpoint ::
        deleteOfficeEndpoint ::
        Nil
    )
}

package io.github.avapl
package adapters.http.office

import cats.effect.Async
import cats.syntax.all._
import domain.repository.office.OfficeRepository._
import domain.service.office.OfficeService
import io.circe.generic.auto._
import java.util.UUID
import org.http4s.HttpRoutes
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s._

class OfficeRoutes[F[_]: Async](
  officeService: OfficeService[F]
) {

  // TODO: Probably the base endpoint should be defined in some central part
  private val baseEndpoint =
    endpoint
      .in("api" / "v1")
      .errorOut(jsonBody[OfficeApiError])

  private val readOfficeEndpoint =
    baseEndpoint.get
      .in(path[UUID]("id"))
      .out(jsonBody[JsonOffice])

  private val readOfficeRoute =
    readOfficeEndpoint
      .serverLogic { id =>
        officeService
          .readOffice(id)
          .map(JsonOffice.fromDomain(_).asRight[OfficeApiError])
          .recover {
            case OfficeNotFound(id: UUID) => OfficeApiError(s"Office [id: $id] was not found").asLeft
          }
      }

  // TODO: Probably routes should be defined in some central part
  val routes: HttpRoutes[F] =
    Http4sServerInterpreter().toRoutes(readOfficeRoute)
}

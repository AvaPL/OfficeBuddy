package io.github.avapl
package adapters.http

import io.circe.generic.auto._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._


// TODO: Add unit tests for error handling
trait BaseEndpoint {

  protected def baseEndpointName: String

  protected lazy val baseEndpoint: Endpoint[Unit, Unit, ApiError, Unit, Any] =
    endpoint
      .withTag(baseEndpointName)
      .in(baseEndpointName)
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
}

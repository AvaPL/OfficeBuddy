package io.github.avapl
package adapters.http

import cats.effect.IO
import cats.syntax.all._
import sttp.client3._
import sttp.client3.testing.SttpBackendStub
import sttp.model.StatusCode
import sttp.tapir.integ.cats.effect.CatsMonadError
import sttp.tapir.server.stub.TapirStubInterpreter
import weaver.SimpleIOSuite

object BaseEndpointSuite extends SimpleIOSuite {

  test(
    """GIVEN base endpoint
      | WHEN the output is a success
      | THEN a correct status code and body is returned
      |""".stripMargin
  ) {
    for {
      response <- sendRequest(().asRight.pure[IO])
    } yield expect.all(
      response.code == StatusCode.Ok,
      response.body == "".asRight
    )
  }

  test(
    """GIVEN base endpoint
      | WHEN the output is a BadRequest
      | THEN 400 status code and body with an error message is returned
      |""".stripMargin
  ) {
    val error = ApiError.BadRequest("test message")

    for {
      response <- sendRequest(error.asLeft.pure[IO])
    } yield expect.all(
      response.code == StatusCode.BadRequest,
      response.body == s"""{"message":"${error.message}"}""".asLeft
    )
  }

  test(
    """GIVEN base endpoint
      | WHEN the output is an InternalServerError
      | THEN 500 status code and body with an error message is returned
      |""".stripMargin
  ) {
    val error = ApiError.InternalServerError("test message")

    for {
      response <- sendRequest(error.asLeft.pure[IO])
    } yield expect.all(
      response.code == StatusCode.InternalServerError,
      response.body == s"""{"message":"${error.message}"}""".asLeft
    )
  }

  test(
    """GIVEN base endpoint
      | WHEN the server logic fails with an exception
      | THEN 500 status code and body with a generic error message is returned
      |""".stripMargin
  ) {
    val exception = new RuntimeException("test message")

    for {
      response <- sendRequest(exception.raiseError)
    } yield expect.all(
      response.code == StatusCode.InternalServerError,
      response.body == "Internal server error".asLeft
    )
  }

  private def sendRequest(result: IO[Either[ApiError, Unit]]) = {
    val name = "test"
    val endpoint = new BaseEndpoint {
      val baseEndpointName: String = name
    }.baseEndpoint.get.serverLogic(_ => result)
    val request = basicRequest.get(uri"http://test.com/$name")
    val backendStub = TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]))
      .whenServerEndpointRunLogic(endpoint)
      .backend()
    request.send(backendStub)
  }
}

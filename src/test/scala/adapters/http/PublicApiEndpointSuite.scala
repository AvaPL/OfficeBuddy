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

object PublicApiEndpointSuite extends SimpleIOSuite {

  test(
    """GIVEN public endpoint
      | WHEN the output is a success
      | THEN 200 OK status code and body is returned
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
    """GIVEN public endpoint
      | WHEN the output is a BadRequest
      | THEN 400 BadRequest status code and body with an error message is returned
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
    """GIVEN public endpoint
      | WHEN the output is an InternalServerError
      | THEN 500 InternalServerError status code and body with an error message is returned
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
    """GIVEN public endpoint
      | WHEN the server logic fails with an exception
      | THEN 500 InternalServerError status code and body with a generic error message is returned
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
    val endpoint = new PublicApiEndpoint {
      val apiEndpointName: String = name
    }.publicEndpoint.get.serverLogic(_ => result)
    val request = basicRequest.get(uri"http://test.com/$name")
    val backendStub = TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]))
      .whenServerEndpointRunLogic(endpoint)
      .backend()
    request.send(backendStub)
  }
}

package io.github.avapl
package adapters.http

import cats.effect.IO
import cats.syntax.all._
import sttp.client3._
import sttp.client3.testing.SttpBackendStub
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.integ.cats.effect.CatsMonadError
import sttp.tapir.server.stub.TapirStubInterpreter
import weaver.SimpleIOSuite

object PaginationInputSuite extends SimpleIOSuite {

  test(
    """GIVEN an endpoint with pagination input
      | WHEN the endpoint is called with valid limit and offset
      | THEN success status code is returned
      |""".stripMargin
  ) {
    val params = Map(
      "limit" -> "10",
      "offset" -> "0"
    )

    for {
      response <- sendRequest(params)
    } yield expect(response.code.isSuccess)
  }

  test(
    """GIVEN an endpoint with pagination input
      | WHEN the endpoint is called with limit less than 1
      | THEN 400 Bad Request status code is returned
      |""".stripMargin
  ) {
    val params = Map(
      "limit" -> "0",
      "offset" -> "0"
    )

    for {
      response <- sendRequest(params)
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN an endpoint with pagination input
      | WHEN the endpoint is called with negative offset
      | THEN 400 Bad Request status code is returned
      |""".stripMargin
  ) {
    val params = Map(
      "limit" -> "10",
      "offset" -> "-1"
    )

    for {
      response <- sendRequest(params)
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN an endpoint with pagination input
      | WHEN the endpoint is called with limit that is not a number
      | THEN 400 Bad Request status code is returned
      |""".stripMargin
  ) {
    val params = Map(
      "limit" -> "notANumber",
      "offset" -> "0"
    )

    for {
      response <- sendRequest(params)
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN an endpoint with pagination input
      | WHEN the endpoint is called with offset that is not a number
      | THEN 400 Bad Request status code is returned
      |""".stripMargin
  ) {
    val params = Map(
      "limit" -> "10",
      "offset" -> "notANumber"
    )

    for {
      response <- sendRequest(params)
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN an endpoint with pagination input
      | WHEN the endpoint is called without limit
      | THEN 400 Bad Request status code is returned
      |""".stripMargin
  ) {
    val params = Map(
      "offset" -> "0"
    )

    for {
      response <- sendRequest(params)
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN an endpoint with pagination input
      | WHEN the endpoint is called without offset
      | THEN 400 Bad Request status code is returned
      |""".stripMargin
  ) {
    val params = Map(
      "limit" -> "10"
    )

    for {
      response <- sendRequest(params)
    } yield expect(response.code == StatusCode.BadRequest)
  }

  private def sendRequest(
    params: Map[String, String]
  ) = {
    val endpointWithPaginationInput =
      endpoint.get
        .in(new PaginationInput {}.paginationLimitAndOffset)
        .serverLogic(_ => ().asRight[Unit].pure[IO])
    val request = basicRequest.get(uri"http://test.com/".withParams(params))
    val backendStub = TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]))
      .whenServerEndpointRunLogic(endpointWithPaginationInput)
      .backend()
    request.send(backendStub)
  }
}

package io.github.avapl
package adapters.http

import sttp.tapir.EndpointInput
import sttp.tapir.Validator
import sttp.tapir.query

trait PaginationInput {

  lazy val paginationLimitAndOffset: EndpointInput[(Int, Int)] =
    limit and offset

  private lazy val limit = query[Int]("limit")
    .description("Maximum number of results to return (pagination)")
    .validate(Validator.min(1))
    .example(10)

  private lazy val offset = query[Int]("offset")
    .description("Number of results to skip (pagination)")
    .validate(Validator.min(0))
    .example(0)
}

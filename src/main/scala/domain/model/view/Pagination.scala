package io.github.avapl
package domain.model.view

case class Pagination(
  limit: Int,
  offset: Int,
  hasMoreResults: Boolean
)

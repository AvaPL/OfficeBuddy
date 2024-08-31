package io.github.avapl
package domain.model.office.view

case class OfficeListView(
  offices: List[OfficeView],
  pagination: Pagination
)

case class Pagination(
  limit: Int,
  offset: Int,
  hasMoreResults: Boolean
)

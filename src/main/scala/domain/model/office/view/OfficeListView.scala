package io.github.avapl
package domain.model.office.view

import domain.model.view.Pagination

case class OfficeListView(
  offices: List[OfficeView],
  pagination: Pagination
)



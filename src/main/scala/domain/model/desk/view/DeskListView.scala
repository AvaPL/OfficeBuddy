package io.github.avapl
package domain.model.desk.view

import domain.model.view.Pagination

case class DeskListView(
  desks: List[DeskView],
  pagination: Pagination
)

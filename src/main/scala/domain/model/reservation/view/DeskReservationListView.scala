package io.github.avapl
package domain.model.reservation.view

import domain.model.view.Pagination

case class DeskReservationListView(
  reservations: List[DeskReservationView],
  pagination: Pagination
)

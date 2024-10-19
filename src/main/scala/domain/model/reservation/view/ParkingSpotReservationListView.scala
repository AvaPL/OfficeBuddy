package io.github.avapl
package domain.model.reservation.view

import domain.model.view.Pagination

case class ParkingSpotReservationListView(
  reservations: List[ParkingSpotReservationView],
  pagination: Pagination
)

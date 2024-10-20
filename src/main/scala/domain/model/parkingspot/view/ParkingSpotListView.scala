package io.github.avapl
package domain.model.parkingspot.view

import domain.model.view.Pagination

case class ParkingSpotListView(
  parkingSpots: List[ParkingSpotView],
  pagination: Pagination
)

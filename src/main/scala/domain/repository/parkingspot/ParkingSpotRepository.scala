package io.github.avapl
package domain.repository.parkingspot

import domain.model.parkingspot.{ParkingSpot, UpdateParkingSpot}
import java.util.UUID

trait ParkingSpotRepository[F[_]] {

  def create(parkingSpot: ParkingSpot): F[ParkingSpot]
  def read(parkingSpotId: UUID): F[ParkingSpot]
  def update(parkingSpotId: UUID, updateParkingSpot: UpdateParkingSpot): F[ParkingSpot]
  def archive(parkingSpotId: UUID): F[Unit]
}

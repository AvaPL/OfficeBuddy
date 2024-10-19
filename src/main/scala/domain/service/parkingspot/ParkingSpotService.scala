package io.github.avapl
package domain.service.parkingspot

import cats.FlatMap
import cats.syntax.all._
import domain.model.parkingspot.CreateParkingSpot
import domain.model.parkingspot.ParkingSpot
import domain.model.parkingspot.UpdateParkingSpot
import domain.repository.parkingspot.ParkingSpotRepository
import java.util.UUID
import util.FUUID

class ParkingSpotService[F[_]: FlatMap: FUUID](
  parkingSpotRepository: ParkingSpotRepository[F]
) {

  def createParkingSpot(createParkingSpot: CreateParkingSpot): F[ParkingSpot] =
    for {
      parkingSpotId <- FUUID[F].randomUUID()
      parkingSpot = createParkingSpot.toParkingSpot(parkingSpotId)
      createdParkingSpot <- parkingSpotRepository.create(parkingSpot)
    } yield createdParkingSpot

  def readParkingSpot(parkingSpotId: UUID): F[ParkingSpot] =
    parkingSpotRepository.read(parkingSpotId)

  def updateParkingSpot(parkingSpotId: UUID, updateParkingSpot: UpdateParkingSpot): F[ParkingSpot] =
    parkingSpotRepository.update(parkingSpotId, updateParkingSpot)

  def archiveParkingSpot(parkingSpotId: UUID): F[Unit] =
    parkingSpotRepository.archive(parkingSpotId)
}

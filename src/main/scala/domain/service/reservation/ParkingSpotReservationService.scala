package io.github.avapl
package domain.service.reservation

import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.reservation.CreateParkingSpotReservation
import domain.model.reservation.ParkingSpotReservation
import domain.repository.reservation.ReservationRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import util.FUUID

class ParkingSpotReservationService[F[_]: Clock: FUUID: MonadThrow](
  reservationRepository: ReservationRepository[F, ParkingSpotReservation]
) extends ReservationService[F, ParkingSpotReservation](reservationRepository) {

  override protected def toReservation(createReservation: CreateParkingSpotReservation): F[ParkingSpotReservation] =
    for {
      reservationId <- FUUID[F].randomUUID()
      createdAt <- nowUtc
    } yield createReservation.toParkingSpotReservation(reservationId, createdAt)

  private lazy val nowUtc =
    Clock[F].realTimeInstant.map(LocalDateTime.ofInstant(_, ZoneOffset.UTC))
}

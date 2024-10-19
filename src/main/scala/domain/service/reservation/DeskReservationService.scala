package io.github.avapl
package domain.service.reservation

import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.reservation.CreateDeskReservation
import domain.model.reservation.DeskReservation
import domain.repository.reservation.ReservationRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import util.FUUID

class DeskReservationService[F[_]: Clock: FUUID: MonadThrow](
  reservationRepository: ReservationRepository[F, DeskReservation]
) extends ReservationService[F, DeskReservation](reservationRepository) {

  override protected def toReservation(createReservation: CreateDeskReservation): F[DeskReservation] =
    for {
      reservationId <- FUUID[F].randomUUID()
      createdAt <- nowUtc
    } yield createReservation.toDeskReservation(reservationId, createdAt)

  private lazy val nowUtc =
    Clock[F].realTimeInstant.map(LocalDateTime.ofInstant(_, ZoneOffset.UTC))
}

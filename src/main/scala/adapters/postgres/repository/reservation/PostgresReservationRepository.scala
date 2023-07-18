package io.github.avapl
package adapters.postgres.repository.reservation

import cats.effect.kernel.Resource
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.repository.reservation.ReservationRepository
import java.util.UUID
import skunk.Session

// TODO: Implement
// TODO: Add integration tests
class PostgresReservationRepository[F[_]](
  session: Resource[F, Session[F]]
) extends ReservationRepository[F] {

  override def createDeskReservation(deskReservation: DeskReservation): F[DeskReservation] = ???

  override def readDeskReservation(reservationId: UUID): F[DeskReservation] = ???

  override def updateReservationState(reservationId: UUID, newState: ReservationState): F[Unit] = ???
}

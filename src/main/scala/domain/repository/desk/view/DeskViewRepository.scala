package io.github.avapl
package domain.repository.desk.view

import domain.model.desk.view.DeskListView
import domain.model.desk.view.ReservableDeskView
import java.time.LocalDate
import java.util.UUID

trait DeskViewRepository[F[_]] {

  def listDesks(
    officeId: UUID,
    limit: Int,
    offset: Int
  ): F[DeskListView]

  def listDesksAvailableForReservation(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationTo: LocalDate
  ): F[List[ReservableDeskView]]
}

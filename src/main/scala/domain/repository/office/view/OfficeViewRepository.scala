package io.github.avapl
package domain.repository.office.view

import domain.model.office.view.OfficeListView
import java.time.LocalDateTime

trait OfficeViewRepository[F[_]] {

  /**
   * @param now
   *   Current date and time in UTC, used to determine active reservations
   */
  def listOffices(now: LocalDateTime, limit: Int, offset: Int): F[OfficeListView]
}

package io.github.avapl
package domain.repository.office.view

import domain.model.office.view.OfficeListView

trait OfficeViewRepository[F[_]] {
  def listOffices(limit: Int, offset: Int): F[OfficeListView]
}

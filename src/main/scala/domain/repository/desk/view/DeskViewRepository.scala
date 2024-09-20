package io.github.avapl
package domain.repository.desk.view

import io.github.avapl.domain.model.desk.view.DeskListView

import java.util.UUID

trait DeskViewRepository[F[_]] {

  def listDesks(
    officeId: UUID,
    limit: Int,
    offset: Int
  ): F[DeskListView]
}

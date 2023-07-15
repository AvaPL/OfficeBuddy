package io.github.avapl
package domain.repository.desk

import domain.model.desk.Desk
import domain.model.desk.UpdateDesk
import java.util.UUID

trait DeskRepository[F[_]] {

  def create(desk: Desk): F[Desk]
  def read(deskId: UUID): F[Desk]
  def update(deskId: UUID, updateDesk: UpdateDesk): F[Desk]
  def archive(deskId: UUID): F[Unit]
}

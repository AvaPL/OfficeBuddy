package io.github.avapl
package domain.repository.office

import domain.model.office.Office
import domain.model.office.UpdateOffice
import java.util.UUID

trait OfficeRepository[F[_]] {

  def create(office: Office): F[Office]
  def read(officeId: UUID): F[Office]
  def update(officeId: UUID, updateOffice: UpdateOffice): F[Office]
  def updateOfficeManagers(officeId: UUID, officeManagerIds: List[UUID]): F[Office]
  def archive(officeId: UUID): F[Unit]
}

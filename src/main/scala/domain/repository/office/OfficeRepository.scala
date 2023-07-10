package io.github.avapl
package domain.repository.office

import domain.model.office.Office
import domain.model.office.UpdateOffice
import java.util.UUID
import scala.util.control.NoStackTrace

trait OfficeRepository[F[_]] {

  def create(office: Office): F[Office]
  def read(officeId: UUID): F[Office]
  def update(officeId: UUID, updateOffice: UpdateOffice): F[Office]
  def delete(officeId: UUID): F[Unit]
}

object OfficeRepository {

  case class OfficeNotFound(officeId: UUID) extends NoStackTrace
  case class DuplicateOfficeName(name: String) extends NoStackTrace
}

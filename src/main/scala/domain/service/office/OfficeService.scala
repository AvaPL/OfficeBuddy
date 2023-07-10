package io.github.avapl
package domain.service.office

import cats.FlatMap
import cats.syntax.all._
import domain.model.office.CreateOffice
import domain.model.office.Office
import domain.model.office.UpdateOffice
import domain.repository.office.OfficeRepository
import java.util.UUID
import util.FUUID

class OfficeService[F[_]: FlatMap](
  officeRepository: OfficeRepository[F]
)(implicit fuuid: FUUID[F]) {

  def createOffice(createOffice: CreateOffice): F[Office] =
    for {
      officeId <- fuuid.randomUUID()
      office = createOffice.toOffice(officeId)
      createdOffice <- officeRepository.create(office)
    } yield createdOffice

  def readOffice(officeId: UUID): F[Office] =
    officeRepository.read(officeId)

  def updateOffice(officeId: UUID, updateOffice: UpdateOffice): F[Office] =
    officeRepository.update(officeId, updateOffice)

  def deleteOffice(officeId: UUID): F[Unit] =
    officeRepository.delete(officeId)
}

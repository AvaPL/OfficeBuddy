package io.github.avapl
package domain.service.office

import cats.FlatMap
import cats.syntax.all._
import domain.model.office.Address
import domain.model.office.Office
import domain.repository.office.OfficeRepository
import java.util.UUID
import util.FUUID

class OfficeService[F[_]: FlatMap](
  officeRepository: OfficeRepository[F]
)(implicit fuuid: FUUID[F]) {

  def createOffice(name: String, notes: List[String], address: Address): F[UUID] =
    for {
      id <- fuuid.randomUUID()
      office = Office(id, name, notes, address)
      _ <- officeRepository.create(office)
    } yield id

  def readOffice(officeId: UUID): F[Office] =
    officeRepository.read(officeId)

  def updateOffice(office: Office): F[Unit] =
    officeRepository.update(office)

  def deleteOffice(officeId: UUID): F[Unit] =
    officeRepository.delete(officeId)
}

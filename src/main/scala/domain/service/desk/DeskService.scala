package io.github.avapl
package domain.service.desk

import cats.FlatMap
import cats.syntax.all._
import domain.model.desk.CreateDesk
import domain.model.desk.Desk
import domain.model.desk.UpdateDesk
import domain.repository.desk.DeskRepository
import java.util.UUID
import util.FUUID

// TODO: Add unit tests
class DeskService[F[_]: FlatMap: FUUID](
  deskRepository: DeskRepository[F]
) {

  def createDesk(createDesk: CreateDesk): F[Desk] =
    for {
      deskId <- FUUID[F].randomUUID()
      desk = createDesk.toDesk(deskId)
      createdDesk <- deskRepository.create(desk)
    } yield createdDesk

  def readDesk(deskId: UUID): F[Desk] =
    deskRepository.read(deskId)

  def updateDesk(deskId: UUID, updateDesk: UpdateDesk): F[Desk] =
    deskRepository.update(deskId, updateDesk)

  def archiveDesk(deskId: UUID): F[Unit] =
    deskRepository.archive(deskId)
}

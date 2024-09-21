package io.github.avapl
package domain.service.desk

import cats.effect.IO
import cats.syntax.all._
import domain.model.desk.CreateDesk
import domain.model.desk.Desk
import domain.model.desk.UpdateDesk
import domain.model.error.desk.DeskNotFound
import domain.model.error.desk.DuplicateDeskNameForOffice
import domain.repository.desk.DeskRepository
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import util.FUUID
import weaver.SimpleIOSuite

object DeskServiceSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN a desk to create
      | WHEN createDesk is called
      | THEN a valid desk is created via deskRepository
      |""".stripMargin
  ) {
    val deskToCreate = anyCreateDesk

    val deskId = anyDeskId
    implicit val fuuid: FUUID[IO] = whenF(mock[FUUID[IO]].randomUUID()) thenReturn deskId
    val deskRepository = mock[DeskRepository[IO]]
    val desk = deskToCreate.toDesk(deskId)
    whenF(deskRepository.create(any)) thenReturn desk
    val deskService = new DeskService[IO](deskRepository)

    for {
      createdDesk <- deskService.createDesk(deskToCreate)
    } yield {
      verify(deskRepository, only).create(eqTo(desk))
      expect(createdDesk == desk)
    }
  }

  test(
    """GIVEN a desk to create
      | WHEN createDesk is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val deskToCreate = anyCreateDesk

    val duplicateDeskName = DuplicateDeskNameForOffice(name.some, deskToCreate.officeId.some)
    val deskRepository = whenF(mock[DeskRepository[IO]].create(any)) thenFailWith duplicateDeskName
    val deskService = new DeskService[IO](deskRepository)

    for {
      result <- deskService.createDesk(deskToCreate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == duplicateDeskName)
    }
  }

  test(
    """GIVEN a desk ID
      | WHEN readDesk is called
      | THEN the desk is read via deskRepository
      |""".stripMargin
  ) {
    val deskId = anyDeskId

    val deskRepository = mock[DeskRepository[IO]]
    val desk = anyDesk.copy(id = deskId)
    whenF(deskRepository.read(any)) thenReturn desk
    val deskService = new DeskService[IO](deskRepository)

    for {
      readDesk <- deskService.readDesk(deskId)
    } yield {
      verify(deskRepository, only).read(eqTo(deskId))
      expect(readDesk == desk)
    }
  }

  test(
    """GIVEN a desk ID
      | WHEN readDesk is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val deskId = anyDeskId

    val deskNotFound = DeskNotFound(deskId)
    val deskRepository = whenF(mock[DeskRepository[IO]].read(any)) thenFailWith deskNotFound
    val deskService = new DeskService[IO](deskRepository)

    for {
      result <- deskService.readDesk(deskId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == deskNotFound)
    }
  }

  test(
    """GIVEN a desk update
      | WHEN updateDesk is called
      | THEN the desk is updated via deskRepository
      |""".stripMargin
  ) {
    val deskId = anyDeskId
    val deskUpdate = anyUpdateDesk

    val deskRepository = mock[DeskRepository[IO]]
    val desk = Desk(
      id = deskId,
      name = deskUpdate.name.get,
      isAvailable = deskUpdate.isAvailable.get,
      notes = deskUpdate.notes.get,
      isStanding = deskUpdate.isStanding.get,
      monitorsCount = deskUpdate.monitorsCount.get,
      hasPhone = deskUpdate.hasPhone.get,
      officeId = deskUpdate.officeId.get
    )
    whenF(deskRepository.update(any, any)) thenReturn desk
    val deskService = new DeskService[IO](deskRepository)

    for {
      updatedDesk <- deskService.updateDesk(deskId, deskUpdate)
    } yield {
      verify(deskRepository, only).update(eqTo(deskId), eqTo(deskUpdate))
      expect(updatedDesk == desk)
    }
  }

  test(
    """GIVEN a desk update
      | WHEN updateDesk is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val deskId = anyDeskId
    val deskUpdate = anyUpdateDesk

    val deskNotFound = DeskNotFound(deskId)
    val deskRepository = whenF(mock[DeskRepository[IO]].update(any, any)) thenFailWith deskNotFound
    val deskService = new DeskService[IO](deskRepository)

    for {
      result <- deskService.updateDesk(deskId, deskUpdate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == deskNotFound)
    }
  }

  test(
    """GIVEN a desk ID
      | WHEN archiveDesk is called
      | THEN the desk is archived via deskRepository
      |""".stripMargin
  ) {
    val deskId = anyDeskId

    val deskRepository = mock[DeskRepository[IO]]
    whenF(deskRepository.archive(any)) thenReturn ()
    val deskService = new DeskService[IO](deskRepository)

    for {
      _ <- deskService.archiveDesk(deskId)
    } yield {
      verify(deskRepository, only).archive(eqTo(deskId))
      success
    }
  }

  test(
    """GIVEN a desk ID
      | WHEN archiveDesk is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val deskId = anyDeskId

    val repositoryException = new RuntimeException("intended exception")
    val deskRepository = whenF(mock[DeskRepository[IO]].archive(any)) thenFailWith repositoryException
    val deskService = new DeskService[IO](deskRepository)

    for {
      result <- deskService.archiveDesk(deskId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == repositoryException)
    }
  }

  private lazy val anyDesk = Desk(
    id = anyDeskId,
    name = "107.1",
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = anyOfficeId
  )

  private lazy val anyCreateDesk = CreateDesk(
    name = "107.1",
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = anyOfficeId
  )

  private lazy val anyUpdateDesk = UpdateDesk(
    name = Some("107.1"),
    isAvailable = Some(true),
    notes = Some(List("Rubik's Cube on the desk")),
    isStanding = Some(true),
    monitorsCount = Some(2),
    hasPhone = Some(false),
    officeId = Some(anyOfficeId)
  )

  private lazy val anyDeskId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")

  private lazy val anyOfficeId: UUID = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")
}

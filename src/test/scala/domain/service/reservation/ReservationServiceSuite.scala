package io.github.avapl
package domain.service.reservation

import cats.effect.IO
import domain.model.error.reservation.InvalidStateTransition
import domain.model.error.reservation.ReservationNotFound
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.repository.reservation.ReservationRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import weaver.SimpleIOSuite

object ReservationServiceSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN a desk reservation ID
      | WHEN readReservation is called
      | THEN the desk reservation is read via reservationRepository
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val reservationRepository = mock[ReservationRepository[IO, DeskReservation]]
    val reservation = anyDeskReservation.copy(id = reservationId)
    whenF(reservationRepository.readReservation(any)) thenReturn reservation
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      readReservation <- reservationService.readReservation(reservationId)
    } yield {
      verify(reservationRepository, only).readReservation(eqTo(reservationId))
      expect(readReservation == reservation)
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN readReservation is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val reservationNotFound = ReservationNotFound(reservationId)
    val reservationRepository =
      whenF(mock[ReservationRepository[IO, DeskReservation]].readReservation(any)) thenFailWith reservationNotFound
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      result <- reservationService.readReservation(reservationId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == reservationNotFound)
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN cancelReservation is called on a Pending reservation
      | THEN the operation should succeed
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val reservationRepository = mock[ReservationRepository[IO, DeskReservation]]
    whenF(reservationRepository.readReservationState(any)) thenReturn ReservationState.Pending
    whenF(reservationRepository.updateReservationState(any, any)) thenReturn ()
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      _ <- reservationService.cancelReservation(reservationId)
    } yield {
      verify(reservationRepository, times(1)).readReservationState(eqTo(reservationId))
      verify(reservationRepository, times(1))
        .updateReservationState(eqTo(reservationId), eqTo(ReservationState.Cancelled))
      success
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN cancelReservation is called on a Rejected reservation
      | THEN the operation should fail because of an invalid state transition
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val currentState = ReservationState.Rejected
    val reservationRepository =
      whenF(mock[ReservationRepository[IO, DeskReservation]].readReservationState(any)) thenReturn currentState
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      result <- reservationService.cancelReservation(reservationId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val invalidStateTransition = InvalidStateTransition(reservationId, currentState, ReservationState.Cancelled)
        expect(throwable == invalidStateTransition)
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN cancelReservation is called on a Pending reservation and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val reservationNotFound = ReservationNotFound(reservationId)
    val reservationRepository =
      whenF(mock[ReservationRepository[IO, DeskReservation]].readReservationState(any)) thenFailWith reservationNotFound
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      result <- reservationService.cancelReservation(reservationId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == reservationNotFound)
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN confirmReservation is called on a Pending reservation
      | THEN the operation should succeed
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val reservationRepository = mock[ReservationRepository[IO, DeskReservation]]
    whenF(reservationRepository.readReservationState(any)) thenReturn ReservationState.Pending
    whenF(reservationRepository.updateReservationState(any, any)) thenReturn ()
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      _ <- reservationService.confirmReservation(reservationId)
    } yield {
      verify(reservationRepository, times(1)).readReservationState(eqTo(reservationId))
      verify(reservationRepository, times(1))
        .updateReservationState(eqTo(reservationId), eqTo(ReservationState.Confirmed))
      success
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN confirmReservation is called on a Rejected reservation
      | THEN the operation should fail because of an invalid state transition
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val currentState = ReservationState.Rejected
    val reservationRepository =
      whenF(mock[ReservationRepository[IO, DeskReservation]].readReservationState(any)) thenReturn currentState
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      result <- reservationService.confirmReservation(reservationId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val invalidStateTransition = InvalidStateTransition(reservationId, currentState, ReservationState.Confirmed)
        expect(throwable == invalidStateTransition)
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN confirmReservation is called on a Pending reservation and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val reservationNotFound = ReservationNotFound(reservationId)
    val reservationRepository =
      whenF(mock[ReservationRepository[IO, DeskReservation]].readReservationState(any)) thenFailWith reservationNotFound
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      result <- reservationService.confirmReservation(reservationId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == reservationNotFound)
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN rejectReservation is called on a Pending reservation
      | THEN the operation should succeed
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val reservationRepository = mock[ReservationRepository[IO, DeskReservation]]
    whenF(reservationRepository.readReservationState(any)) thenReturn ReservationState.Pending
    whenF(reservationRepository.updateReservationState(any, any)) thenReturn ()
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      _ <- reservationService.rejectReservation(reservationId)
    } yield {
      verify(reservationRepository, times(1)).readReservationState(eqTo(reservationId))
      verify(reservationRepository, times(1))
        .updateReservationState(eqTo(reservationId), eqTo(ReservationState.Rejected))
      success
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN rejectReservation is called on a Cancelled reservation
      | THEN the operation should fail because of an invalid state transition
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val currentState = ReservationState.Cancelled
    val reservationRepository =
      whenF(mock[ReservationRepository[IO, DeskReservation]].readReservationState(any)) thenReturn currentState
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      result <- reservationService.rejectReservation(reservationId).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val invalidStateTransition = InvalidStateTransition(reservationId, currentState, ReservationState.Rejected)
        expect(throwable == invalidStateTransition)
    }
  }

  test(
    """GIVEN a desk reservation ID
      | WHEN rejectReservation is called on a Pending reservation and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val reservationId = anyReservationId

    val reservationNotFound = ReservationNotFound(reservationId)
    val reservationRepository =
      whenF(mock[ReservationRepository[IO, DeskReservation]].readReservationState(any)) thenFailWith reservationNotFound
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      result <- reservationService.rejectReservation(reservationId).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == reservationNotFound)
    }
  }

  private lazy val anyDeskReservation = DeskReservation(
    id = anyReservationId,
    userId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5"),
    createdAt = anyCreatedAt,
    reservedFromDate = LocalDate.parse("2023-07-19"),
    reservedToDate = LocalDate.parse("2023-07-20"),
    state = ReservationState.Pending,
    notes = "Please remove the duck from the desk, it scares me",
    deskId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")
  )

  private lazy val anyReservationId = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")

  private lazy val anyCreatedAt = LocalDateTime.parse("2023-07-18T20:41:00")
}

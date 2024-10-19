package io.github.avapl
package domain.service.reservation

import domain.model.error.reservation.{InvalidStateTransition, OverlappingReservations, ReservationNotFound}
import domain.model.reservation.{CreateDeskReservation, DeskReservation, ReservationState}
import domain.repository.reservation.ReservationRepository
import util.FUUID

import cats.effect.{Clock, IO}
import org.mockito.{ArgumentMatchersSugar, MockitoSugar}
import org.mockito.cats.MockitoCats
import weaver.SimpleIOSuite

import java.time.{LocalDate, LocalDateTime, ZoneOffset}
import java.util.UUID

object DeskReservationServiceSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN a desk reservation to create
      | WHEN createReservation is called
      | THEN a valid desk reservation is created via reservationRepository
      |""".stripMargin
  ) {
    val reservationToCreate = anyCreateDeskReservation

    val reservationId = anyReservationId
    implicit val fuuid: FUUID[IO] = whenF(mock[FUUID[IO]].randomUUID()) thenReturn reservationId
    val createdAt = anyCreatedAt
    implicit val clock: Clock[IO] =
      whenF(mock[Clock[IO]].realTimeInstant) thenReturn createdAt.toInstant(ZoneOffset.UTC)
    val reservationRepository = mock[ReservationRepository[IO, DeskReservation]]
    val reservation = reservationToCreate.toDeskReservation(reservationId, createdAt)
    whenF(reservationRepository.createReservation(any)) thenReturn reservation
    val reservationService = new DeskReservationService[IO](reservationRepository)(clock, fuuid, implicitly)

    for {
      createdReservation <- reservationService.reserve(reservationToCreate)
    } yield {
      verify(reservationRepository, only).createReservation(eqTo(reservation))
      expect(createdReservation == reservation)
    }
  }

  test(
    """GIVEN a desk reservation to create
      | WHEN createReservation is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val reservationToCreate = anyCreateDeskReservation

    val overlappingReservations = OverlappingReservations
    val reservationRepository =
      whenF(mock[ReservationRepository[IO, DeskReservation]].createReservation(any)) thenFailWith overlappingReservations
    val reservationService = new DeskReservationService[IO](reservationRepository)

    for {
      result <- reservationService.reserve(reservationToCreate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == overlappingReservations)
    }
  }

  private lazy val anyCreateDeskReservation = CreateDeskReservation(
    userId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5"),
    reservedFrom = LocalDate.parse("2023-07-19"),
    reservedTo = LocalDate.parse("2023-07-20"),
    notes = "Please remove the duck from the desk, it scares me",
    deskId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")
  )

  private lazy val anyReservationId = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")

  private lazy val anyCreatedAt = LocalDateTime.parse("2023-07-18T20:41:00")
}

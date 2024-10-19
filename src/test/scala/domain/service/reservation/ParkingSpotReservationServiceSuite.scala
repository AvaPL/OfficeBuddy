package io.github.avapl
package domain.service.reservation

import cats.effect.Clock
import cats.effect.IO
import domain.model.error.reservation.OverlappingReservations
import domain.model.reservation.CreateParkingSpotReservation
import domain.model.reservation.ParkingSpotReservation
import domain.repository.reservation.ReservationRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import util.FUUID
import weaver.SimpleIOSuite

object ParkingSpotReservationServiceSuite
  extends SimpleIOSuite
  with MockitoSugar
  with ArgumentMatchersSugar
  with MockitoCats {

  test(
    """GIVEN a parking spot reservation to create
      | WHEN createReservation is called
      | THEN a valid parking spot reservation is created via reservationRepository
      |""".stripMargin
  ) {
    val reservationToCreate = anyCreateParkingSpotReservation

    val reservationId = anyReservationId
    implicit val fuuid: FUUID[IO] = whenF(mock[FUUID[IO]].randomUUID()) thenReturn reservationId
    val createdAt = anyCreatedAt
    implicit val clock: Clock[IO] =
      whenF(mock[Clock[IO]].realTimeInstant) thenReturn createdAt.toInstant(ZoneOffset.UTC)
    val reservationRepository = mock[ReservationRepository[IO, ParkingSpotReservation]]
    val reservation = reservationToCreate.toParkingSpotReservation(reservationId, createdAt)
    whenF(reservationRepository.createReservation(any)) thenReturn reservation
    val reservationService = new ParkingSpotReservationService[IO](reservationRepository)(clock, fuuid, implicitly)

    for {
      createdReservation <- reservationService.reserve(reservationToCreate)
    } yield {
      verify(reservationRepository, only).createReservation(eqTo(reservation))
      expect(createdReservation == reservation)
    }
  }

  test(
    """GIVEN a parking spot reservation to create
      | WHEN createReservation is called and the repository fails
      | THEN the result should contain the failure
      |""".stripMargin
  ) {
    val reservationToCreate = anyCreateParkingSpotReservation

    val overlappingReservations = OverlappingReservations
    val reservationRepository =
      whenF(
        mock[ReservationRepository[IO, ParkingSpotReservation]].createReservation(any)
      ) thenFailWith overlappingReservations
    val reservationService = new ParkingSpotReservationService[IO](reservationRepository)

    for {
      result <- reservationService.reserve(reservationToCreate).attempt
    } yield matches(result) {
      case Left(throwable) => expect(throwable == overlappingReservations)
    }
  }

  private lazy val anyCreateParkingSpotReservation = CreateParkingSpotReservation(
    userId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5"),
    reservedFrom = LocalDate.parse("2023-07-19"),
    reservedTo = LocalDate.parse("2023-07-20"),
    notes = "Please reserve a spot close to the entrance",
    parkingSpotId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b"),
    plateNumber = "ABC 123"
  )

  private lazy val anyReservationId = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")

  private lazy val anyCreatedAt = LocalDateTime.parse("2023-07-18T20:41:00")
}

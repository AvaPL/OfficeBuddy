package io.github.avapl
package adapters.http.reservation

import cats.effect.IO
import domain.model.error.desk.DeskNotFound
import domain.model.error.reservation.InvalidStateTransition
import domain.model.error.reservation.OverlappingReservations
import domain.model.error.reservation.ReservationNotFound
import domain.model.error.user.UserNotFound
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.service.reservation.ReservationService
import io.circe.parser._
import io.circe.syntax._
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import sttp.client3._
import sttp.client3.circe._
import sttp.client3.testing.SttpBackendStub
import sttp.model.StatusCode
import sttp.tapir.integ.cats.effect.CatsMonadError
import sttp.tapir.server.stub.TapirStubInterpreter
import weaver.SimpleIOSuite

object ReservationEndpointsSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN reserve desk endpoint
      | WHEN a reservation is POSTed and created
      | THEN 201 Created and the created reservation is returned
      |""".stripMargin
  ) {
    val reservationToCreate = anyApiCreateDeskReservation
    val reservation = DeskReservation(
      id = anyReservationId,
      userId = reservationToCreate.userId,
      createdAt = anyCreatedAt,
      reservedFrom = reservationToCreate.reservedFrom.atStartOfDay(),
      reservedTo = reservationToCreate.reservedTo.atTime(LocalTime.MAX),
      state = ReservationState.Pending,
      notes = reservationToCreate.notes,
      deskId = reservationToCreate.deskId
    )
    val reservationService = whenF(mock[ReservationService[IO]].reserveDesk(any)) thenReturn reservation

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(reservationToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiDeskReservation.fromDomain(reservation).asJson
    )
  }

  test(
    """GIVEN reserve desk endpoint
      | WHEN creating the reservation fails with DeskNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationService =
      whenF(mock[ReservationService[IO]].reserveDesk(any)) thenFailWith DeskNotFound(anyDeskId)

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(anyApiCreateDeskReservation)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reserve desk endpoint
      | WHEN creating the reservation fails with UserNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationService =
      whenF(mock[ReservationService[IO]].reserveDesk(any)) thenFailWith UserNotFound(anyUserId)

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(anyApiCreateDeskReservation)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reserve desk endpoint
      | WHEN creating the reservation fails with OverlappingReservations error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val reservationService =
      whenF(mock[ReservationService[IO]].reserveDesk(any)) thenFailWith OverlappingReservations

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(anyApiCreateDeskReservation)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read desk reservation endpoint
      | WHEN an existing reservation is read
      | THEN 200 OK and the read reservation is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservation = anyDeskReservation.copy(id = reservationId)
    val reservationService = whenF(mock[ReservationService[IO]].readDeskReservation(any)) thenReturn reservation

    val response = sendRequest(reservationService) {
      basicRequest.get(uri"http://test.com/reservation/desk/$reservationId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiDeskReservation.fromDomain(reservation).asJson
    )
  }

  test(
    """GIVEN read desk reservation endpoint
      | WHEN the reservation is not found (ReservationNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].readDeskReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.get(uri"http://test.com/reservation/desk/$reservationId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN cancel reservation endpoint
      | WHEN the reservation is cancelled
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].cancelReservation(any)) thenReturn ()

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/cancel")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN cancel reservation endpoint
      | WHEN the state transition is invalid (InvalidStateTransition error)
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].cancelReservation(any)) thenFailWith
        InvalidStateTransition(reservationId, ReservationState.Rejected, ReservationState.Cancelled)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/cancel")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN cancel reservation endpoint
      | WHEN the reservation is not found (ReservationNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].cancelReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/cancel")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN confirm reservation endpoint
      | WHEN the reservation is confirmed
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].confirmReservation(any)) thenReturn ()

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/confirm")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN confirm reservation endpoint
      | WHEN the state transition is invalid (InvalidStateTransition error)
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].confirmReservation(any)) thenFailWith
        InvalidStateTransition(reservationId, ReservationState.Rejected, ReservationState.Confirmed)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/confirm")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN confirm reservation endpoint
      | WHEN the reservation is not found (ReservationNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].confirmReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/confirm")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN reject reservation endpoint
      | WHEN the reservation is rejected
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].rejectReservation(any)) thenReturn ()

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/reject")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN reject reservation endpoint
      | WHEN the state transition is invalid (InvalidStateTransition error)
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].rejectReservation(any)) thenFailWith
        InvalidStateTransition(reservationId, ReservationState.Cancelled, ReservationState.Rejected)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/reject")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reject reservation endpoint
      | WHEN the reservation is not found (ReservationNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].rejectReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/reject")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  private def sendRequest(reservationService: ReservationService[IO])(request: Request[Either[String, String], Any]) = {
    val reservationEndpoints = new ReservationEndpoints[IO](reservationService)
    val backendStub = TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]))
      .whenServerEndpointsRunLogic(reservationEndpoints.endpoints)
      .backend()
    request.send(backendStub)
  }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private lazy val anyDeskReservation = DeskReservation(
    id = anyReservationId,
    userId = anyUserId,
    createdAt = anyCreatedAt,
    reservedFrom = LocalDateTime.parse("2023-07-19T00:00:00"),
    reservedTo = LocalDateTime.parse("2023-07-20T23:59:59"),
    state = ReservationState.Pending,
    notes = "Please remove the duck from the desk, it scares me",
    deskId = anyDeskId
  )

  private lazy val anyApiCreateDeskReservation = ApiCreateDeskReservation(
    userId = anyUserId,
    reservedFrom = LocalDate.parse("2023-07-19"),
    reservedTo = LocalDate.parse("2023-07-20"),
    notes = "Please remove the duck from the desk, it scares me",
    deskId = anyDeskId
  )

  private lazy val anyUserId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5")

  private lazy val anyDeskId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")

  private lazy val anyReservationId = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")

  private lazy val anyCreatedAt = LocalDateTime.parse("2023-07-18T20:41:00")
}

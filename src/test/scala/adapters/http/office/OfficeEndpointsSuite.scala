package io.github.avapl
package adapters.http.office

import adapters.auth.model.PublicKey
import adapters.http.fixture.SecuredApiEndpointFixture
import cats.effect.IO
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.error.office.DuplicateOfficeName
import domain.model.error.office.OfficeNotFound
import domain.model.office.Address
import domain.model.office.Office
import domain.service.office.OfficeService
import io.circe.parser._
import io.circe.syntax._
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import sttp.client3._
import sttp.client3.circe._
import sttp.model.StatusCode
import weaver.SimpleIOSuite

object OfficeEndpointsSuite
  extends SimpleIOSuite
  with MockitoSugar
  with ArgumentMatchersSugar
  with MockitoCats
  with SecuredApiEndpointFixture {

  test(
    """GIVEN create office endpoint
      | WHEN an office is POSTed and created by an office manager
      | THEN 201 Created and the created office is returned
      |""".stripMargin
  ) {
    val officeToCreate = anyApiCreateOffice
    val office = Office(anyOfficeId, officeToCreate.name, officeToCreate.notes, officeToCreate.address.toDomain)
    val officeService = whenF(mock[OfficeService[IO]].createOffice(any)) thenReturn office

    val response = sendRequest(officeService, role = OfficeManager) {
      basicRequest
        .post(uri"http://test.com/office")
        .body(officeToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiOffice.fromDomain(office).asJson
    )
  }

  test(
    """GIVEN create office endpoint
      | WHEN there is an attempt to create an office by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val officeToCreate = anyApiCreateOffice
    val officeService = mock[OfficeService[IO]]

    val response = sendRequest(officeService, role = User) {
      basicRequest
        .post(uri"http://test.com/office")
        .body(officeToCreate)
    }

    for {
      response <- response
    } yield {
      verify(officeService, never).createOffice(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN create office endpoint
      | WHEN creating the office fails with DuplicateOfficeName error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val officeService = whenF(mock[OfficeService[IO]].createOffice(any)) thenFailWith DuplicateOfficeName(anyOfficeName)

    val response = sendRequest(officeService) {
      basicRequest
        .post(uri"http://test.com/office")
        .body(anyApiCreateOffice)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read office endpoint
      | WHEN an existing office is read by a user
      | THEN 200 OK and the read office is returned
      |""".stripMargin
  ) {
    val officeId = anyOfficeId
    val office = anyOffice.copy(id = officeId)
    val officeService = whenF(mock[OfficeService[IO]].readOffice(any)) thenReturn office

    val response = sendRequest(officeService, role = User) {
      basicRequest.get(uri"http://test.com/office/$officeId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiOffice.fromDomain(office).asJson
    )
  }

  test(
    """GIVEN read office endpoint
      | WHEN the office is not found (OfficeNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val officeId = anyOfficeId
    val officeService = whenF(mock[OfficeService[IO]].readOffice(any)) thenFailWith OfficeNotFound(officeId)

    val response = sendRequest(officeService) {
      basicRequest.get(uri"http://test.com/office/$officeId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update office endpoint
      | WHEN an office is PATCHed and updated by an office manager
      | THEN 200 OK and the updated office is returned
      |""".stripMargin
  ) {
    val officeToUpdate = anyApiUpdateOffice
    val officeId = anyOfficeId
    val office = Office(officeId, officeToUpdate.name, officeToUpdate.notes, officeToUpdate.address.toDomain)
    val officeService = whenF(mock[OfficeService[IO]].updateOffice(any, any)) thenReturn office

    val response = sendRequest(officeService, role = OfficeManager) {
      basicRequest
        .patch(uri"http://test.com/office/$officeId")
        .body(officeToUpdate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiOffice.fromDomain(office).asJson
    )
  }

  test(
    """GIVEN update office endpoint
      | WHEN there is an attempt to update an office by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val officeToUpdate = anyApiUpdateOffice
    val officeId = anyOfficeId
    val officeService = mock[OfficeService[IO]]

    val response = sendRequest(officeService, role = User) {
      basicRequest
        .patch(uri"http://test.com/office/$officeId")
        .body(officeToUpdate)
    }

    for {
      response <- response
    } yield {
      verify(officeService, never).updateOffice(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN update office endpoint
      | WHEN the office to update is not found (OfficeNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val officeId = anyOfficeId
    val officeService = whenF(mock[OfficeService[IO]].updateOffice(any, any)) thenFailWith OfficeNotFound(officeId)

    val response = sendRequest(officeService) {
      basicRequest
        .patch(uri"http://test.com/office/$officeId")
        .body(anyApiUpdateOffice)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update office endpoint
      | WHEN an office with the given name already exists
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val officeService =
      whenF(mock[OfficeService[IO]].updateOffice(any, any)) thenFailWith DuplicateOfficeName(anyOfficeName)

    val response = sendRequest(officeService) {
      basicRequest
        .patch(uri"http://test.com/office/$anyOfficeId")
        .body(anyApiUpdateOffice)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN archive office endpoint
      | WHEN an existing office is archived by an office manager
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val officeService = whenF(mock[OfficeService[IO]].archiveOffice(any)) thenReturn ()

    val response = sendRequest(officeService, role = OfficeManager) {
      basicRequest.delete(uri"http://test.com/office/$anyOfficeId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN archive office endpoint
      | WHEN there is an attempt to archive an office by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val officeService = mock[OfficeService[IO]]

    val response = sendRequest(officeService, role = User) {
      basicRequest.delete(uri"http://test.com/office/$anyOfficeId")
    }

    for {
      response <- response
    } yield {
      verify(officeService, never).archiveOffice(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  private def sendRequest(officeService: OfficeService[IO], role: Role = SuperAdmin)(
    request: Request[Either[String, String], Any]
  ) =
    sendSecuredApiEndpointRequest(request, role) { rolesExtractorService =>
      new OfficeEndpoints[IO](officeService, publicKeyRepository, rolesExtractorService).endpoints
    }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private lazy val anyOffice = Office(
    id = anyOfficeId,
    name = anyOfficeName,
    notes = anyOfficeNotes,
    address = anyOfficeAddress
  )

  private lazy val anyApiCreateOffice = ApiCreateOffice(
    name = anyOfficeName,
    notes = anyOfficeNotes,
    address = anyOfficeApiAddress
  )

  private lazy val anyApiUpdateOffice = ApiUpdateOffice(
    name = anyOfficeName,
    notes = anyOfficeNotes,
    address = anyOfficeApiAddress,
    isArchived = false
  )

  private lazy val anyOfficeId =
    UUID.fromString("4f99984c-e371-4b77-a184-7003f6281b8d")

  private lazy val anyOfficeName =
    "Test Office"

  private lazy val anyOfficeNotes =
    List("Test", "Notes")

  private lazy val anyOfficeAddress = Address(
    addressLine1 = "Test Street",
    addressLine2 = "Building 42",
    postalCode = "12-345",
    city = "Wroclaw",
    country = "Poland"
  )

  private lazy val anyOfficeApiAddress = ApiAddress(
    addressLine1 = "Test Street",
    addressLine2 = "Building 42",
    postalCode = "12-345",
    city = "Wroclaw",
    country = "Poland"
  )
}

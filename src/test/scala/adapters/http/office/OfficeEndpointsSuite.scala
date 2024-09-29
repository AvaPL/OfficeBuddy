package io.github.avapl
package adapters.http.office

import adapters.auth.model.PublicKey
import adapters.http.fixture.SecuredApiEndpointFixture
import adapters.http.office.model._
import adapters.http.office.model.view.ApiOfficeListView
import cats.effect.IO
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.error.office.DuplicateOfficeName
import domain.model.error.office.OfficeNotFound
import domain.model.office.Address
import domain.model.office.Office
import domain.model.office.view._
import domain.model.view.Pagination
import domain.repository.office.view.OfficeViewRepository
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
    val office = Office(anyOfficeId1, officeToCreate.name, officeToCreate.notes, officeToCreate.address.toDomain)
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
    val officeId = anyOfficeId1
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
    val officeId = anyOfficeId1
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
    val officeId = anyOfficeId1
    val address = Address(
      addressLine1 = officeToUpdate.address.addressLine1.get,
      addressLine2 = officeToUpdate.address.addressLine2.get,
      postalCode = officeToUpdate.address.postalCode.get,
      city = officeToUpdate.address.city.get,
      country = officeToUpdate.address.country.get
    )
    val office = Office(officeId, officeToUpdate.name.get, officeToUpdate.notes.get, address)
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
    val officeId = anyOfficeId1
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
    val officeId = anyOfficeId1
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
        .patch(uri"http://test.com/office/$anyOfficeId1")
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
      basicRequest.delete(uri"http://test.com/office/$anyOfficeId1")
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
      basicRequest.delete(uri"http://test.com/office/$anyOfficeId1")
    }

    for {
      response <- response
    } yield {
      verify(officeService, never).archiveOffice(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN view list office endpoint
      | WHEN the endpoint is called with limit and offset
      | THEN 200 OK and the list of offices is returned
      |""".stripMargin
  ) {
    val officeViewRepository = mock[OfficeViewRepository[IO]]
    val officeListView = OfficeListView(
      offices = List(
        anyOfficeView.copy(id = anyOfficeId1, name = "Office 1"),
        anyOfficeView.copy(id = anyOfficeId2, name = "Office 2")
      ),
      pagination = Pagination(
        limit = 10,
        offset = 0,
        hasMoreResults = false
      )
    )
    whenF(officeViewRepository.listOffices(any, any)) thenReturn officeListView

    val response = sendViewRequest(officeViewRepository) {
      basicRequest.get(
        uri"http://test.com/office/view/list"
          .withParams(
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiOfficeListView.fromDomain(officeListView).asJson
    )
  }

  private def sendRequest(
    officeService: OfficeService[IO],
    role: Role = SuperAdmin
  )(
    request: Request[Either[String, String], Any]
  ): IO[Response[Either[PublicKey, PublicKey]]] = {
    val officeViewRepository = mock[OfficeViewRepository[IO]]
    sendSecuredApiEndpointRequest(request, role) { rolesExtractorService =>
      new OfficeEndpoints[IO](officeService, officeViewRepository, publicKeyRepository, rolesExtractorService).endpoints
    }
  }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private def sendViewRequest(
    officeViewRepository: OfficeViewRepository[IO]
  )(
    request: Request[Either[String, String], Any]
  ): IO[Response[Either[PublicKey, PublicKey]]] = {
    val officeService = mock[OfficeService[IO]]
    sendSecuredApiEndpointRequest(request, role = User) { rolesExtractorService =>
      new OfficeEndpoints[IO](officeService, officeViewRepository, publicKeyRepository, rolesExtractorService).endpoints
    }
  }

  private lazy val anyOffice = Office(
    id = anyOfficeId1,
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
    name = Some(anyOfficeName),
    notes = Some(anyOfficeNotes),
    address = anyOfficeApiUpdateAddress
  )

  private lazy val anyOfficeId1 = UUID.fromString("4f99984c-e371-4b77-a184-7003f6281b8d")
  private lazy val anyOfficeId2 = UUID.fromString("b278adc4-0dd3-44b0-9cec-6b5338b480c3")

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

  private lazy val anyOfficeApiUpdateAddress = ApiUpdateAddress(
    addressLine1 = Some("Test Street"),
    addressLine2 = Some("Building 42"),
    postalCode = Some("12-345"),
    city = Some("Wroclaw"),
    country = Some("Poland")
  )

  private lazy val anyOfficeView = OfficeView(
    id = anyOfficeId1,
    name = anyOfficeName,
    notes = List("Everyone's favorite", "The funniest one"),
    address = AddressView(
      addressLine1 = "Powstancow Slaskich 9",
      addressLine2 = "1st floor",
      postalCode = "53-332",
      city = "Wroclaw",
      country = "Poland"
    ),
    officeManagers = List(
      OfficeManagerView(
        id = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227297"),
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com"
      ),
      OfficeManagerView(
        id = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227296"),
        firstName = "Jane",
        lastName = "Doe",
        email = "jane.doe@example.com"
      )
    ),
    assignedAccountsCount = 20,
    desksCount = 10,
    parkingSpotsCount = 2,
    roomsCount = 1,
    activeReservationsCount = 5
  )
}

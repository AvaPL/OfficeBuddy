package io.github.avapl
package adapters.http.account

import adapters.auth.model.PublicKey
import adapters.http.fixture.SecuredApiEndpointFixture
import cats.effect.IO
import domain.model.account.OfficeManagerAccount
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.office.OfficeNotFound
import domain.service.account.AccountService
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

object AccountEndpointsSuite
  extends SimpleIOSuite
  with MockitoSugar
  with ArgumentMatchersSugar
  with MockitoCats
  with SecuredApiEndpointFixture {

  test(
    """GIVEN create user endpoint
      | WHEN a user is POSTed and created by an office manager
      | THEN 201 Created and the created user is returned
      |""".stripMargin
  ) {
    val userToCreate = anyApiCreateUserAccount
    val user = UserAccount(
      id = anyAccountId,
      firstName = userToCreate.firstName,
      lastName = userToCreate.lastName,
      email = userToCreate.email,
      assignedOfficeId = userToCreate.assignedOfficeId
    )
    val accountService = whenF(mock[AccountService[IO]].createUser(any)) thenReturn user

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .post(uri"http://test.com/account/user")
        .body(userToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiUserAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN create user endpoint
      | WHEN there is an attempt to create a user by another user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val userToCreate = anyApiCreateUserAccount
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = User) {
      basicRequest
        .post(uri"http://test.com/account/user")
        .body(userToCreate)
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).createUser(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN create user endpoint
      | WHEN creating the user fails with OfficeNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountService = whenF(mock[AccountService[IO]].createUser(any)) thenFailWith OfficeNotFound(anyOfficeId)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/user")
        .body(anyApiCreateUserAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN create user endpoint
      | WHEN creating the user fails with DuplicateAccountEmail error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val accountService = whenF(mock[AccountService[IO]].createUser(any)) thenFailWith DuplicateAccountEmail(anyEmail)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/user")
        .body(anyApiCreateUserAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read user endpoint
      | WHEN an existing user is read by another user
      | THEN 200 OK and the read user is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val user = anyUserAccount.copy(id = userId)
    val accountService = whenF(mock[AccountService[IO]].readUser(any)) thenReturn user

    val response = sendRequest(accountService, role = User) {
      basicRequest.get(uri"http://test.com/account/user/$userId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiUserAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN read user endpoint
      | WHEN the user is not found (AccountNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val accountService = whenF(mock[AccountService[IO]].readUser(any)) thenFailWith AccountNotFound(userId)

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/user/$userId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN assign office to user endpoint
      | WHEN the office is assigned by an office manager to a user
      | THEN 200 OK and the updated user is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val officeId = anyOfficeId
    val user = anyUserAccount.copy(id = userId, assignedOfficeId = Some(officeId))
    val accountService =
      whenF(mock[AccountService[IO]].updateUserAssignedOffice(any, any)) thenReturn user

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.put(uri"http://test.com/account/user/$userId/assigned-office-id/$officeId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiUserAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN assign office to user endpoint
      | WHEN there is an attempt to assign the office by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val officeId = anyOfficeId
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = User) {
      basicRequest.put(uri"http://test.com/account/user/$userId/assigned-office-id/$officeId")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).updateUserAssignedOffice(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN assign office to user endpoint
      | WHEN assigning the office fails with OfficeNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val officeId = anyOfficeId
    val accountService =
      whenF(mock[AccountService[IO]].updateUserAssignedOffice(any, any)) thenFailWith OfficeNotFound(officeId)

    val response = sendRequest(accountService) {
      basicRequest.put(uri"http://test.com/account/user/$anyAccountId/assigned-office-id/$officeId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN assign office to user endpoint
      | WHEN assigning the office fails with AccountNotFound error
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].updateUserAssignedOffice(any, any)) thenFailWith AccountNotFound(userId)

    val response = sendRequest(accountService) {
      basicRequest.put(uri"http://test.com/account/user/$userId/assigned-office-id/$anyOfficeId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN unassign user office endpoint
      | WHEN the office is successfully unassigned by an office manager
      | THEN 200 OK and the updated user is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val user = anyUserAccount.copy(id = userId, assignedOfficeId = None)
    val accountService =
      whenF(mock[AccountService[IO]].updateUserAssignedOffice(any, any)) thenReturn user

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.delete(uri"http://test.com/account/user/$userId/assigned-office-id")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiUserAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN unassign user office endpoint
      | WHEN there is an attempt to unassign the office by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = User) {
      basicRequest.delete(uri"http://test.com/account/user/$userId/assigned-office-id")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).updateUserAssignedOffice(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN unassign user office endpoint
      | WHEN unassigning the office fails with AccountNotFound error
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].updateUserAssignedOffice(any, any)) thenFailWith AccountNotFound(userId)

    val response = sendRequest(accountService) {
      basicRequest.delete(uri"http://test.com/account/user/$userId/assigned-office-id")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN create office manager endpoint
      | WHEN a office manager is POSTed and created by a super admin
      | THEN 201 Created and the created office manager is returned
      |""".stripMargin
  ) {
    val officeManagerToCreate = anyApiCreateOfficeManagerAccount
    val officeManager = OfficeManagerAccount(
      id = anyAccountId,
      firstName = officeManagerToCreate.firstName,
      lastName = officeManagerToCreate.lastName,
      email = officeManagerToCreate.email,
      managedOfficeIds = officeManagerToCreate.managedOfficeIds
    )
    val accountService = whenF(mock[AccountService[IO]].createOfficeManager(any)) thenReturn officeManager

    val response = sendRequest(accountService, role = SuperAdmin) {
      basicRequest
        .post(uri"http://test.com/account/office-manager")
        .body(officeManagerToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiOfficeManagerAccount.fromDomain(officeManager).asJson
    )
  }

  test(
    """GIVEN create office manager endpoint
      | WHEN there is an attempt to create an office manager by another office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val officeManagerToCreate = anyApiCreateOfficeManagerAccount
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .post(uri"http://test.com/account/office-manager")
        .body(officeManagerToCreate)
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).createOfficeManager(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN create office manager endpoint
      | WHEN creating the office manager fails with DuplicateAccountEmail error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val accountService =
      whenF(mock[AccountService[IO]].createOfficeManager(any)) thenFailWith DuplicateAccountEmail(anyEmail)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/office-manager")
        .body(anyApiCreateOfficeManagerAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read office manager endpoint
      | WHEN an existing office manager is read by an office manager
      | THEN 200 OK and the read office manager is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val officeManager = anyOfficeManagerAccount.copy(id = officeManagerId)
    val accountService = whenF(mock[AccountService[IO]].readOfficeManager(any)) thenReturn officeManager

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.get(uri"http://test.com/account/office-manager/$officeManagerId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiOfficeManagerAccount.fromDomain(officeManager).asJson
    )
  }

  test(
    """GIVEN read office manager endpoint
      | WHEN there is an attempt to read the office manager by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = User) {
      basicRequest.get(uri"http://test.com/account/office-manager/$officeManagerId")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).readOfficeManager(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN read office manager endpoint
      | WHEN the office manager is not found (AccountNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].readOfficeManager(any)) thenFailWith AccountNotFound(officeManagerId)

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/office-manager/$officeManagerId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update office manager managed offices endpoint
      | WHEN the offices are successfully assigned by a super admin
      | THEN 200 OK and the updated office manager is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val managedOfficeIds = List(anyOfficeId)
    val officeManager = anyOfficeManagerAccount.copy(id = officeManagerId, managedOfficeIds = managedOfficeIds)
    val accountService =
      whenF(mock[AccountService[IO]].updateOfficeManagerManagedOffices(any, any)) thenReturn officeManager

    val response = sendRequest(accountService, role = SuperAdmin) {
      basicRequest
        .put(uri"http://test.com/account/office-manager/$officeManagerId/managed-office-ids")
        .body(managedOfficeIds)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiOfficeManagerAccount.fromDomain(officeManager).asJson
    )
  }

  test(
    """GIVEN update office manager managed offices endpoint
      | WHEN there is an attempt to assign the offices by an office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val managedOfficeIds = List(anyOfficeId)
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .put(uri"http://test.com/account/office-manager/$anyAccountId/managed-office-ids")
        .body(managedOfficeIds)
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).updateOfficeManagerManagedOffices(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN update office manager managed offices endpoint
      | WHEN assigning the offices fails with AccountNotFound error
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].updateOfficeManagerManagedOffices(any, any)) thenFailWith
        AccountNotFound(officeManagerId)

    val response = sendRequest(accountService) {
      basicRequest
        .put(uri"http://test.com/account/office-manager/$officeManagerId/managed-office-ids")
        .body(List(anyOfficeId))
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN create super admin endpoint
      | WHEN a super admin is POSTed and created by another super admin
      | THEN 201 Created and the created super admin is returned
      |""".stripMargin
  ) {
    val superAdminToCreate = anyApiCreateSuperAdminAccount
    val superAdmin = SuperAdminAccount(
      id = anyAccountId,
      firstName = superAdminToCreate.firstName,
      lastName = superAdminToCreate.lastName,
      email = superAdminToCreate.email
    )
    val accountService = whenF(mock[AccountService[IO]].createSuperAdmin(any)) thenReturn superAdmin

    val response = sendRequest(accountService, role = SuperAdmin) {
      basicRequest
        .post(uri"http://test.com/account/super-admin")
        .body(superAdminToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiSuperAdminAccount.fromDomain(superAdmin).asJson
    )
  }

  test(
    """GIVEN create super admin endpoint
      | WHEN there is an attempt to create a super admin by an office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val superAdminToCreate = anyApiCreateSuperAdminAccount
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .post(uri"http://test.com/account/super-admin")
        .body(superAdminToCreate)
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).createSuperAdmin(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN create super admin endpoint
      | WHEN creating the super admin fails with DuplicateAccountEmail error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val accountService =
      whenF(mock[AccountService[IO]].createSuperAdmin(any)) thenFailWith DuplicateAccountEmail(anyEmail)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/super-admin")
        .body(anyApiCreateSuperAdminAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read super admin endpoint
      | WHEN an existing super admin is read by a super admin
      | THEN 200 OK and the read super admin is returned
      |""".stripMargin
  ) {
    val superAdminId = anyAccountId
    val superAdmin = anySuperAdminAccount.copy(id = superAdminId)
    val accountService = whenF(mock[AccountService[IO]].readSuperAdmin(any)) thenReturn superAdmin

    val response = sendRequest(accountService, role = SuperAdmin) {
      basicRequest.get(uri"http://test.com/account/super-admin/$superAdminId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiSuperAdminAccount.fromDomain(superAdmin).asJson
    )
  }

  test(
    """GIVEN read super admin endpoint
      | WHEN there is an attempt to read the super admin by an office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val superAdminId = anyAccountId
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.get(uri"http://test.com/account/super-admin/$superAdminId")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).readSuperAdmin(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN read super admin endpoint
      | WHEN the super admin is not found (AccountNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val superAdminId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].readSuperAdmin(any)) thenFailWith AccountNotFound(superAdminId)

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/super-admin/$superAdminId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update roles endpoint
      | WHEN the roles are successfully updated by a super admin
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val accountId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].updateRoles(any, any)) thenReturn anyUserAccount

    val response = sendRequest(accountService, role = SuperAdmin) {
      basicRequest
        .put(uri"http://test.com/account/$accountId/roles")
        .body(anyApiAccountRoles)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN update roles endpoint
      | WHEN there is an attempt to update the roles by an office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .put(uri"http://test.com/account/$anyAccountId/roles")
        .body(anyApiAccountRoles)
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).updateRoles(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN update roles endpoint
      | WHEN an empty list of roles is supplied
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].updateRoles(any, any)) thenReturn anyUserAccount

    val response = sendRequest(accountService) {
      basicRequest
        .put(uri"http://test.com/account/$accountId/roles")
        .body(List.empty[ApiRole])
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN update roles endpoint
      | WHEN the account is not found (AccountNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val accountId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].updateRoles(any, any)) thenFailWith AccountNotFound(accountId)

    val response = sendRequest(accountService) {
      basicRequest
        .put(uri"http://test.com/account/$accountId/roles")
        .body(anyApiAccountRoles)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN archive account endpoint
      | WHEN an existing account is archived by an office manager
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val accountId = anyAccountId
    val accountService = mock[AccountService[IO]]
    whenF(accountService.readSuperAdmin(any)) thenFailWith AccountNotFound(accountId)
    whenF(accountService.readOfficeManager(any)) thenFailWith AccountNotFound(accountId)
    whenF(accountService.archive(any)) thenReturn ()

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.delete(uri"http://test.com/account/$accountId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN archive account endpoint
      | WHEN there is an attempt to archive the account by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = User) {
      basicRequest.delete(uri"http://test.com/account/$anyAccountId")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).archive(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN archive account endpoint
      | WHEN there is an attempt to archive an office manager account by another office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val officeManagerAccountId = anyAccountId
    val accountService = mock[AccountService[IO]]
    whenF(accountService.readSuperAdmin(officeManagerAccountId)) thenFailWith AccountNotFound(officeManagerAccountId)
    whenF(accountService.readOfficeManager(officeManagerAccountId)) thenReturn anyOfficeManagerAccount

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.delete(uri"http://test.com/account/$officeManagerAccountId")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).archive(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN archive account endpoint
      | WHEN there is an attempt to archive a super admin account by an office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val superAdminAccountId = anyAccountId
    val accountService = mock[AccountService[IO]]
    whenF(accountService.readSuperAdmin(superAdminAccountId)) thenReturn anySuperAdminAccount
    whenF(accountService.readOfficeManager(superAdminAccountId)) thenFailWith AccountNotFound(superAdminAccountId)

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.delete(uri"http://test.com/account/$superAdminAccountId")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).archive(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  private def sendRequest(accountService: AccountService[IO], role: Role = SuperAdmin)(
    request: Request[Either[String, String], Any]
  ) =
    sendSecuredApiEndpointRequest(request, role) { claimsExtractorService =>
      new AccountEndpoints[IO](accountService, publicKeyRepository, claimsExtractorService).endpoints
    }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private lazy val anyUserAccount = UserAccount(
    id = anyAccountId,
    firstName = "John",
    lastName = "Doe",
    email = anyEmail,
    assignedOfficeId = Some(anyOfficeId)
  )

  private lazy val anyApiCreateUserAccount = ApiCreateUserAccount(
    firstName = "John",
    lastName = "Doe",
    email = anyEmail,
    assignedOfficeId = Some(anyOfficeId)
  )

  private lazy val anyOfficeManagerAccount = OfficeManagerAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    managedOfficeIds = List(
      UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
      UUID.fromString("305bfad9-9354-4d7f-93ef-c1bab1f8dd7b")
    )
  )

  private lazy val anyApiCreateOfficeManagerAccount = ApiCreateOfficeManagerAccount(
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    managedOfficeIds = List(
      UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
      UUID.fromString("305bfad9-9354-4d7f-93ef-c1bab1f8dd7b")
    )
  )

  private lazy val anySuperAdminAccount = SuperAdminAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com"
  )

  private lazy val anyApiCreateSuperAdminAccount = ApiCreateSuperAdminAccount(
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com"
  )

  private lazy val anyAccountId: UUID = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40")

  private lazy val anyEmail = "john.doe@example.com"

  private lazy val anyOfficeId: UUID = UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed")

  private lazy val anyApiAccountRoles = List[ApiRole](ApiRole.OfficeManager)
}

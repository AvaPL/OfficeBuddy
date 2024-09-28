package io.github.avapl
package adapters.http.account

import adapters.auth.model.PublicKey
import adapters.http.account.model.ApiRole
import adapters.http.account.model.view.ApiAccountListView
import adapters.http.fixture.SecuredApiEndpointFixture
import cats.effect.IO
import domain.model.account.OfficeManagerAccount
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import domain.model.account.view.AccountListView
import domain.model.account.view.AccountView
import domain.model.account.view.OfficeView
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.office.OfficeNotFound
import domain.model.view.Pagination
import domain.repository.account.view.AccountViewRepository
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
    """GIVEN create account endpoint
      | WHEN a user is POSTed and created by an office manager
      | THEN 201 Created and the created user is returned
      |""".stripMargin
  ) {
    val userToCreate = anyApiCreateUserAccount
    val user = UserAccount(
      id = anyAccountId1,
      firstName = userToCreate.firstName,
      lastName = userToCreate.lastName,
      email = userToCreate.email,
      assignedOfficeId = userToCreate.assignedOfficeId
    )
    val accountService = whenF(mock[AccountService[IO]].create(any)) thenReturn user

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .post(uri"http://test.com/account")
        .body(userToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN create account endpoint
      | WHEN a office manager is POSTed and created by a super admin
      | THEN 201 Created and the created office manager is returned
      |""".stripMargin
  ) {
    val officeManagerToCreate = anyApiCreateOfficeManagerAccount
    val officeManager = OfficeManagerAccount(
      id = anyAccountId1,
      firstName = officeManagerToCreate.firstName,
      lastName = officeManagerToCreate.lastName,
      email = officeManagerToCreate.email,
      managedOfficeIds = officeManagerToCreate.managedOfficeIds
    )
    val accountService = whenF(mock[AccountService[IO]].create(any)) thenReturn officeManager

    val response = sendRequest(accountService, role = SuperAdmin) {
      basicRequest
        .post(uri"http://test.com/account")
        .body(officeManagerToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiAccount.fromDomain(officeManager).asJson
    )
  }

  test(
    """GIVEN create account endpoint
      | WHEN a super admin is POSTed and created by another super admin
      | THEN 201 Created and the created super admin is returned
      |""".stripMargin
  ) {
    val superAdminToCreate = anyApiCreateSuperAdminAccount
    val superAdmin = SuperAdminAccount(
      id = anyAccountId1,
      firstName = superAdminToCreate.firstName,
      lastName = superAdminToCreate.lastName,
      email = superAdminToCreate.email
    )
    val accountService = whenF(mock[AccountService[IO]].create(any)) thenReturn superAdmin

    val response = sendRequest(accountService, role = SuperAdmin) {
      basicRequest
        .post(uri"http://test.com/account")
        .body(superAdminToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiAccount.fromDomain(superAdmin).asJson
    )
  }

  test(
    """GIVEN create account endpoint
      | WHEN there is an attempt to create a user by another user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val userToCreate = anyApiCreateUserAccount
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = User) {
      basicRequest
        .post(uri"http://test.com/account")
        .body(userToCreate)
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).create(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN create account endpoint
      | WHEN there is an attempt to create an office manager by another office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val officeManagerToCreate = anyApiCreateOfficeManagerAccount
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .post(uri"http://test.com/account")
        .body(officeManagerToCreate)
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).create(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN create account endpoint
      | WHEN there is an attempt to create a super admin by an office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val superAdminToCreate = anyApiCreateSuperAdminAccount
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .post(uri"http://test.com/account")
        .body(superAdminToCreate)
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).create(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN create account endpoint
      | WHEN creating the user fails with OfficeNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountService = whenF(mock[AccountService[IO]].create(any)) thenFailWith OfficeNotFound(anyOfficeId)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account")
        .body(anyApiCreateUserAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN create account endpoint
      | WHEN creating the user fails with DuplicateAccountEmail error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val accountService = whenF(mock[AccountService[IO]].create(any)) thenFailWith DuplicateAccountEmail(anyEmail)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account")
        .body(anyApiCreateUserAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read account endpoint
      | WHEN an existing user is read by another user
      | THEN 200 OK and the read user is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId1
    val user = anyUserAccount.copy(id = userId)
    val accountService = whenF(mock[AccountService[IO]].read(any)) thenReturn user

    val response = sendRequest(accountService, role = User) {
      basicRequest.get(uri"http://test.com/account/$userId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN read account endpoint
      | WHEN an existing office manager is read by a user
      | THEN 200 OK and the read office manager is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId1
    val officeManager = anyOfficeManagerAccount.copy(id = officeManagerId)
    val accountService = whenF(mock[AccountService[IO]].read(any)) thenReturn officeManager

    val response = sendRequest(accountService, role = User) {
      basicRequest.get(uri"http://test.com/account/$officeManagerId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiAccount.fromDomain(officeManager).asJson
    )
  }

  test(
    """GIVEN read account endpoint
      | WHEN an existing super admin is read by a user
      | THEN 200 OK and the read super admin is returned
      |""".stripMargin
  ) {
    val superAdminId = anyAccountId1
    val superAdmin = anySuperAdminAccount.copy(id = superAdminId)
    val accountService = whenF(mock[AccountService[IO]].read(any)) thenReturn superAdmin

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.get(uri"http://test.com/account/$superAdminId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiAccount.fromDomain(superAdmin).asJson
    )
  }

  test(
    """GIVEN read account endpoint
      | WHEN the account is not found (AccountNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val accountId = anyAccountId1
    val accountService = whenF(mock[AccountService[IO]].read(any)) thenFailWith AccountNotFound(accountId)

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/$accountId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN assign office endpoint
      | WHEN the office is assigned by an office manager to a user
      | THEN 200 OK and the updated user is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId1
    val officeId = anyOfficeId
    val user = anyUserAccount.copy(id = userId, assignedOfficeId = Some(officeId))
    val accountService =
      whenF(mock[AccountService[IO]].updateAssignedOffice(any, any)) thenReturn user

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.put(uri"http://test.com/account/$userId/assigned-office-id/$officeId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN assign office endpoint
      | WHEN there is an attempt to assign the office by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId1
    val officeId = anyOfficeId
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = User) {
      basicRequest.put(uri"http://test.com/account/$userId/assigned-office-id/$officeId")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).updateAssignedOffice(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN assign office endpoint
      | WHEN assigning the office fails with OfficeNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val officeId = anyOfficeId
    val accountService =
      whenF(mock[AccountService[IO]].updateAssignedOffice(any, any)) thenFailWith OfficeNotFound(officeId)

    val response = sendRequest(accountService) {
      basicRequest.put(uri"http://test.com/account/$anyAccountId1/assigned-office-id/$officeId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN assign office endpoint
      | WHEN assigning the office fails with AccountNotFound error
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId1
    val accountService =
      whenF(mock[AccountService[IO]].updateAssignedOffice(any, any)) thenFailWith AccountNotFound(userId)

    val response = sendRequest(accountService) {
      basicRequest.put(uri"http://test.com/account/$userId/assigned-office-id/$anyOfficeId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN unassign office endpoint
      | WHEN the office is successfully unassigned by an office manager
      | THEN 200 OK and the updated user is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId1
    val user = anyUserAccount.copy(id = userId, assignedOfficeId = None)
    val accountService =
      whenF(mock[AccountService[IO]].updateAssignedOffice(any, any)) thenReturn user

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest.delete(uri"http://test.com/account/$userId/assigned-office-id")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN unassign office endpoint
      | WHEN there is an attempt to unassign the office by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId1
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = User) {
      basicRequest.delete(uri"http://test.com/account/$userId/assigned-office-id")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).updateAssignedOffice(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN unassign office endpoint
      | WHEN unassigning the office fails with AccountNotFound error
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId1
    val accountService =
      whenF(mock[AccountService[IO]].updateAssignedOffice(any, any)) thenFailWith AccountNotFound(userId)

    val response = sendRequest(accountService) {
      basicRequest.delete(uri"http://test.com/account/$userId/assigned-office-id")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update managed offices endpoint
      | WHEN the offices are successfully assigned by a super admin to an office manager
      | THEN 200 OK and the updated office manager is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId1
    val managedOfficeIds = List(anyOfficeId)
    val officeManager = anyOfficeManagerAccount.copy(id = officeManagerId, managedOfficeIds = managedOfficeIds)
    val accountService =
      whenF(mock[AccountService[IO]].updateManagedOffices(any, any)) thenReturn officeManager

    val response = sendRequest(accountService, role = SuperAdmin) {
      basicRequest
        .put(uri"http://test.com/account/$officeManagerId/managed-office-ids")
        .body(managedOfficeIds)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiAccount.fromDomain(officeManager).asJson
    )
  }

  test(
    """GIVEN update managed offices endpoint
      | WHEN there is an attempt to assign the offices by an office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val managedOfficeIds = List(anyOfficeId)
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .put(uri"http://test.com/account/$anyAccountId1/managed-office-ids")
        .body(managedOfficeIds)
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).updateManagedOffices(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN update managed offices endpoint
      | WHEN assigning the offices fails with AccountNotFound error
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId1
    val accountService =
      whenF(mock[AccountService[IO]].updateManagedOffices(any, any)) thenFailWith
        AccountNotFound(officeManagerId)

    val response = sendRequest(accountService) {
      basicRequest
        .put(uri"http://test.com/account/$officeManagerId/managed-office-ids")
        .body(List(anyOfficeId))
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update role endpoint
      | WHEN the role is successfully updated by a super admin
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val accountId = anyAccountId1
    val accountService =
      whenF(mock[AccountService[IO]].updateRole(any, any)) thenReturn anyUserAccount

    val response = sendRequest(accountService, role = SuperAdmin) {
      basicRequest
        .put(uri"http://test.com/account/$accountId/role/${anyApiAccountRole.entryName}")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN update role endpoint
      | WHEN there is an attempt to update the role by an office manager
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val accountService = mock[AccountService[IO]]

    val response = sendRequest(accountService, role = OfficeManager) {
      basicRequest
        .put(uri"http://test.com/account/$anyAccountId1/role/${anyApiAccountRole.entryName}")
    }

    for {
      response <- response
    } yield {
      verify(accountService, never).updateRole(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN update role endpoint
      | WHEN the account is not found (AccountNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val accountId = anyAccountId1
    val accountService =
      whenF(mock[AccountService[IO]].updateRole(any, any)) thenFailWith AccountNotFound(accountId)

    val response = sendRequest(accountService) {
      basicRequest
        .put(uri"http://test.com/account/$accountId/role/${anyApiAccountRole.entryName}")
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
    val accountId = anyAccountId1
    val accountService = mock[AccountService[IO]]
    whenF(accountService.read(any)) thenReturn anyUserAccount
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
      basicRequest.delete(uri"http://test.com/account/$anyAccountId1")
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
    val officeManagerAccountId = anyAccountId1
    val accountService = mock[AccountService[IO]]
    whenF(accountService.read(officeManagerAccountId)) thenReturn anyOfficeManagerAccount

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
    val superAdminAccountId = anyAccountId1
    val accountService = mock[AccountService[IO]]
    whenF(accountService.read(superAdminAccountId)) thenReturn anySuperAdminAccount

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

  test(
    """GIVEN view list account endpoint
      | WHEN the endpoint is called with filters, limit and offset
      | THEN 200 OK and the list of accounts is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val accountListView = AccountListView(
      accounts = List(
        anyAccountView.copy(id = anyAccountId1, firstName = "Adam", lastName = "Doe"),
        anyAccountView.copy(id = anyAccountId2, firstName = "Jane", lastName = "Doe")
      ),
      pagination = Pagination(
        limit = 10,
        offset = 0,
        hasMoreResults = false
      )
    )
    whenF(accountViewRepository.listAccounts(any, any, any, any, any)) thenReturn accountListView

    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
          .withParams(
            "text_search_query" -> "doe",
            "office_id" -> anyOfficeId.toString,
            "roles" -> List(ApiRole.User, ApiRole.OfficeManager).map(_.entryName).mkString(","),
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiAccountListView.fromDomain(accountListView).asJson
    )
  }

  test(
    """GIVEN view list account endpoint
      | WHEN the endpoint is called with limit, offset, and no filters
      | THEN 200 OK and the list of accounts is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val accountListView = AccountListView(
      accounts = List(
        anyAccountView.copy(id = anyAccountId1, firstName = "Adam", lastName = "Doe"),
        anyAccountView.copy(id = anyAccountId2, firstName = "Jane", lastName = "Doe")
      ),
      pagination = Pagination(
        limit = 10,
        offset = 0,
        hasMoreResults = false
      )
    )
    whenF(accountViewRepository.listAccounts(any, any, any, any, any)) thenReturn accountListView

    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
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
      bodyJson(response) == ApiAccountListView.fromDomain(accountListView).asJson
    )
  }

  test(
    """GIVEN view list account endpoint
      | WHEN office_id is not a valid UUID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
          .withParams(
            "office_id" -> "not a UUID",
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list account endpoint
      | WHEN roles contains an invalid role
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
          .withParams(
            "roles" -> s"${ApiRole.User},InvalidRole",
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list account endpoint
      | WHEN the endpoint is called with limit less than 1
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
          .withParams(
            "limit" -> "0",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list account endpoint
      | WHEN the endpoint is called with negative offset
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
          .withParams(
            "limit" -> "10",
            "offset" -> "-1"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list account endpoint
      | WHEN the endpoint is called with limit that is not a number
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
          .withParams(
            "limit" -> "notANumber",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list account endpoint
      | WHEN the endpoint is called with offset that is not a number
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
          .withParams(
            "limit" -> "10",
            "offset" -> "notANumber"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list account endpoint
      | WHEN the endpoint is called without limit
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
          .withParams(
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list account endpoint
      | WHEN the endpoint is called without offset
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    val response = sendViewRequest(accountViewRepository) {
      basicRequest.get(
        uri"http://test.com/account/view/list"
          .withParams(
            "limit" -> "10"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  private def sendRequest(accountService: AccountService[IO], role: Role = SuperAdmin)(
    request: Request[Either[String, String], Any]
  ) = {
    val accountViewRepository = mock[AccountViewRepository[IO]]
    sendSecuredApiEndpointRequest(request, role) { claimsExtractorService =>
      new AccountEndpoints[IO](
        accountService,
        accountViewRepository,
        publicKeyRepository,
        claimsExtractorService
      ).endpoints
    }
  }

  private def sendViewRequest(
    accountViewRepository: AccountViewRepository[IO]
  )(
    request: Request[Either[String, String], Any]
  ): IO[Response[Either[PublicKey, PublicKey]]] = {
    val accountService = mock[AccountService[IO]]
    sendSecuredApiEndpointRequest(request, role = User) { claimsExtractorService =>
      new AccountEndpoints[IO](
        accountService,
        accountViewRepository,
        publicKeyRepository,
        claimsExtractorService
      ).endpoints
    }
  }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private lazy val anyUserAccount = UserAccount(
    id = anyAccountId1,
    firstName = "John",
    lastName = "Doe",
    email = anyEmail,
    assignedOfficeId = Some(anyOfficeId)
  )

  private lazy val anyApiCreateUserAccount = ApiCreateAccount(
    role = ApiRole.User,
    firstName = "John",
    lastName = "Doe",
    email = anyEmail,
    assignedOfficeId = Some(anyOfficeId),
    managedOfficeIds = Nil
  )

  private lazy val anyOfficeManagerAccount = OfficeManagerAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    managedOfficeIds = anyManagedOfficeIds
  )

  private lazy val anyApiCreateOfficeManagerAccount = ApiCreateAccount(
    role = ApiRole.OfficeManager,
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    assignedOfficeId = Some(anyOfficeId),
    managedOfficeIds = anyManagedOfficeIds
  )

  private lazy val anySuperAdminAccount = SuperAdminAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com"
  )

  private lazy val anyApiCreateSuperAdminAccount = ApiCreateAccount(
    role = ApiRole.SuperAdmin,
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    assignedOfficeId = None,
    managedOfficeIds = Nil
  )

  private lazy val anyAccountId1 = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40")
  private lazy val anyAccountId2 = UUID.fromString("8539b4f6-b3ea-4771-aec9-0a6e18431bff")

  private lazy val anyEmail = "john.doe@example.com"

  private lazy val anyOfficeId = UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed")

  private lazy val anyManagedOfficeIds = List(
    UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
    UUID.fromString("305bfad9-9354-4d7f-93ef-c1bab1f8dd7b")
  )

  private lazy val anyApiAccountRole = ApiRole.OfficeManager

  private lazy val anyAccountView = AccountView(
    id = anyAccountId1,
    firstName = "Test",
    lastName = "OfficeManager",
    email = "test.office.manager@postgres.localhost",
    role = Role.OfficeManager,
    assignedOffice = Some(anyOfficeView),
    managedOffices = List(anyOfficeView)
  )

  private lazy val anyOfficeView = OfficeView(
    id = UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
    name = "Test Office"
  )
}

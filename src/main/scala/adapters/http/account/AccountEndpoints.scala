package io.github.avapl
package adapters.http.account

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import adapters.http.ApiError
import adapters.http.SecuredApiEndpoint
import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.office.OfficeNotFound
import domain.service.account.AccountService
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

class AccountEndpoints[F[_]: Clock: MonadThrow](
  accountService: AccountService[F],
  override val publicKeyRepository: PublicKeyRepository[F],
  override val rolesExtractor: ClaimsExtractorService
) extends SecuredApiEndpoint[F] {

  override protected def apiEndpointName: String = "account"

  val endpoints: List[ServerEndpoint[Any, F]] =
    createAccountEndpoint ::
      readAccountEndpoint ::
      assignOfficeEndpoint ::
      unassignOfficeEndpoint ::
      updateManagedOfficesEndpoint ::
      updateRolesEndpoint ::
      archiveAccountEndpoint ::
      Nil

  // TODO: Office managers and super admins should be creatable only by super admins
  private lazy val createAccountEndpoint =
    securedEndpoint(requiredRole = OfficeManager).post
      .summary("Create an account")
      .description("Required role: office manager")
      .in(
        jsonBody[ApiCreateAccount]
          .example(apiCreateAccountExample)
      )
      .out(
        statusCode(StatusCode.Created) and jsonBody[ApiAccount]
          .description("Account created")
          .example(apiOfficeManagerAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Office with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Account with the given email already exists")
        )
      )
      .serverLogic(_ => createAccount)

  private def createAccount(apiCreateAccount: ApiCreateAccount) =
    accountService
      .create(apiCreateAccount.toDomain)
      .map(ApiAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        // TODO: Recover on non-existent managed office IDs
        case OfficeNotFound(officeId) =>
          ApiError.BadRequest(s"Office [id: $officeId] was not found").asLeft
        case DuplicateAccountEmail(email) =>
          ApiError.Conflict(s"Account with email '$email' is already defined").asLeft
      }

  private lazy val readAccountEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Find a account by ID")
      .description("Required role: user")
      .in(path[UUID]("accountId"))
      .out(
        jsonBody[ApiAccount]
          .description("Found account")
          .example(apiOfficeManagerAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Account with the given ID was not found")
        )
      )
      .serverLogic(_ => readAccount)

  private def readAccount(accountId: UUID) =
    accountService
      .read(accountId)
      .map(ApiAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Account [id: $accountId] was not found").asLeft
      }

  private lazy val assignOfficeEndpoint =
    securedEndpoint(requiredRole = OfficeManager).put
      .summary("Assign office to an account")
      .description("Required role: office manager")
      .in(path[UUID]("accountId") / "assigned-office-id" / path[UUID]("assignedOfficeId"))
      .out(
        jsonBody[ApiAccount]
          .description("Updated account")
          .example(apiOfficeManagerAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Office with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Account with the given ID was not found")
        )
      )
      .serverLogic(_ => (assignOffice _).tupled)

  private def assignOffice(accountId: UUID, assignedOfficeId: UUID) =
    accountService
      .updateAssignedOffice(accountId, Some(assignedOfficeId))
      .map(ApiAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(officeId) =>
          ApiError.BadRequest(s"Office [id: $officeId] was not found").asLeft
        case AccountNotFound(accountId) =>
          ApiError.NotFound(s"Account [id: $accountId] was not found").asLeft
      }

  private lazy val unassignOfficeEndpoint =
    securedEndpoint(requiredRole = OfficeManager).delete
      .summary("Unassign account office")
      .description("Required role: office manager")
      .in(path[UUID]("accountId") / "assigned-office-id")
      .out(
        jsonBody[ApiAccount]
          .description("Updated account")
          .example(apiOfficeManagerAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Account with the given ID was not found")
        )
      )
      .serverLogic(_ => unassignOffice)

  private def unassignOffice(accountId: UUID) =
    accountService
      .updateAssignedOffice(accountId, officeId = None)
      .map(ApiAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) =>
          ApiError.NotFound(s"Account [id: $accountId] was not found").asLeft
      }

  private lazy val updateManagedOfficesEndpoint =
    securedEndpoint(requiredRole = SuperAdmin).put
      .summary("Assign managed offices to an account")
      .description("Required role: super admin")
      .in(path[UUID]("accountId") / "managed-office-ids")
      .in(
        jsonBody[List[UUID]]
          .description("Managed office IDs")
          .example(managedOfficeIdsExample)
      )
      .out(
        jsonBody[ApiAccount]
          .description("Updated account")
          .example(apiOfficeManagerAccountExample)
      )
      // TODO: Validate managed office IDs
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Account with the given ID was not found")
        )
      )
      .serverLogic(_ => (updateManagedOffices _).tupled)

  private def updateManagedOffices(accountId: UUID, managedOfficeIds: List[UUID]) =
    accountService
      .updateManagedOffices(accountId, managedOfficeIds)
      .map(ApiAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Account [id: $accountId] was not found").asLeft
      }

  private lazy val updateRolesEndpoint =
    securedEndpoint(requiredRole = SuperAdmin).put
      .summary("Update account roles")
      .description(
        """Updates account roles. The account can be promoted or demoted depending on the roles provided.
          |
          |Required role: super admin
          |""".stripMargin
      )
      .in(path[UUID]("accountId") / "role" / path[ApiRole]("role"))
      .out(
        statusCode(StatusCode.NoContent)
          .description("Roles updated")
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Roles list cannot be empty")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Account with the given ID was not found")
        )
      )
      .serverLogic(_ => (updateRole _).tupled)

  private def updateRole(accountId: UUID, apiRole: ApiRole): F[Either[ApiError, Unit]] =
    accountService
      .updateRole(accountId, apiRole.toDomain)
      .as(().asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Account [id: $accountId] was not found").asLeft
      }

  private lazy val archiveAccountEndpoint =
    securedEndpoint(requiredRole = OfficeManager).delete
      .summary("Archive an account")
      .description(
        """Archives an account. The account is NOT deleted. The operation is idempotent ie. if the account doesn't exist, the operation doesn't fail.
          |
          |Required role: office manager
          |Access note: The requester can only modify accounts with lower privileges.
          |""".stripMargin
      )
      .in(path[UUID]("accountId"))
      .out(
        statusCode(StatusCode.NoContent)
          .description("Account archived or not found")
      )
      .serverLogic(accessToken => archiveAccount(requesterRoles = accessToken.roles))

  private def archiveAccount(requesterRoles: List[Role])(accountId: UUID): F[Either[ApiError, Unit]] =
    for {
      accountToArchiveRole <- accountService.read(accountId).map(_.role)
      hasPermission =
        if (accountToArchiveRole == SuperAdmin || accountToArchiveRole == OfficeManager)
          requesterRoles.exists(_.hasAccess(SuperAdmin))
        else requesterRoles.exists(_.hasAccess(OfficeManager))
      result <-
        if (hasPermission) accountService.archive(accountId).as(().asRight[ApiError])
        else ApiError.Forbidden.asLeft.pure[F]
    } yield result

  private lazy val apiOfficeManagerAccountExample = ApiOfficeManagerAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    isArchived = false,
    assignedOfficeId = Some(managedOfficeIdsExample.head),
    managedOfficeIds = managedOfficeIdsExample
  )

  private lazy val apiCreateAccountExample = ApiCreateAccount(
    role = ApiRole.OfficeManager,
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    assignedOfficeId = Some(managedOfficeIdsExample.head),
    managedOfficeIds = managedOfficeIdsExample
  )

  private lazy val managedOfficeIdsExample = List(
    UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
    UUID.fromString("305bfad9-9354-4d7f-93ef-c1bab1f8dd7b")
  )
}

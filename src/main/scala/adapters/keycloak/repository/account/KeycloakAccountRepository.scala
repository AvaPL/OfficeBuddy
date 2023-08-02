package io.github.avapl
package adapters.keycloak.repository.account

import cats.effect.Sync
import cats.syntax.all._
import domain.model.error.account.DuplicateAccountEmail
import jakarta.ws.rs.core.Response
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.UserRepresentation
import scala.jdk.CollectionConverters._
import sttp.model.StatusCode

// TODO: Add a composite repository that will combine IAM and DB data
class KeycloakAccountRepository[F[_]: Sync](
  keycloak: Keycloak,
  realmName: String
) {

  def createUser(user: KeycloakUser): F[KeycloakUser] =
    safeCreateUser(user.toUserRepresentation)
      .flatMap(validateCreateUserResponse(user))

  private def safeCreateUser(userRepresentation: UserRepresentation) =
    Sync[F].delay {
      keycloak.realm(realmName).users().create(userRepresentation)
    }

  private def validateCreateUserResponse(user: KeycloakUser)(response: Response): F[KeycloakUser] =
    StatusCode(response.getStatus) match {
      case StatusCode.Conflict => DuplicateAccountEmail(user.email).raiseError
      case statusCode if !statusCode.isSuccess =>
        new RuntimeException(
          s"Failed to create user [status: $statusCode]: ${response.readEntity(classOf[String])}"
        ).raiseError
      case _ => user.pure
    }

  def findUserByEmail(email: String): F[KeycloakUser] =
    safeFindUserByEmail(email)
      .flatMap(toSingleUser(email))

  private def toSingleUser(email: String)(userRepresentations: List[UserRepresentation]): F[KeycloakUser] =
    userRepresentations match {
      case List(userRepresentation) => KeycloakUser.fromUserRepresentation(userRepresentation).pure
      case Nil                      => KeycloakUserNotFound(email).raiseError
      case userRepresentations =>
        new RuntimeException(
          s"Found ${userRepresentations.size} users with email: $email"
        ).raiseError
    }

  private def safeFindUserByEmail(email: String) =
    Sync[F].delay {
      keycloak.realm(realmName).users().search(email).asScala.toList
    }

  // TODO: Remove commented out code
//  override def updateUserAssignedOffice(userId: UUID, officeId: Option[UUID]): F[UserAccount] = ???
//
//  override def createOfficeManager(officeManager: OfficeManagerAccount): F[OfficeManagerAccount] = ???
//
//  override def readOfficeManager(officeManagerId: UUID): F[OfficeManagerAccount] = ???
//
//  override def updateOfficeManagerManagedOffices(
//    officeManagerId: UUID,
//    officeIds: List[UUID]
//  ): F[OfficeManagerAccount] = ???
//
//  override def createSuperAdmin(superAdmin: SuperAdminAccount): F[SuperAdminAccount] = ???
//
//  override def readSuperAdmin(superAdminId: UUID): F[SuperAdminAccount] = ???
//
//  override def updateAccountRoles(accountId: UUID, roles: List[Role]): F[Account] = ???
//
//  override def archiveAccount(accountId: UUID): F[Unit] = ???
}

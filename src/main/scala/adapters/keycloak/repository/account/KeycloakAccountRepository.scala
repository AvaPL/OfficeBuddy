package io.github.avapl
package adapters.keycloak.repository.account

import cats.effect.Sync
import cats.syntax.all._
import domain.model.error.account.DuplicateAccountEmail
import jakarta.ws.rs.core.Response
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.UserResource
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
      .map(KeycloakUser.fromUserRepresentation)

  private def safeFindUserByEmail(email: String) =
    Sync[F]
      .delay {
        keycloak.realm(realmName).users().search(email).asScala.toList
      }
      .flatMap(toSingleUser(email))

  private def toSingleUser(email: String)(userRepresentations: List[UserRepresentation]): F[UserRepresentation] =
    userRepresentations match {
      case List(userRepresentation) => userRepresentation.pure
      case Nil                      => KeycloakUserNotFound(email).raiseError
      case userRepresentations =>
        new RuntimeException(
          s"Found ${userRepresentations.size} users with email: $email"
        ).raiseError
    }

  def updateUserAttributes(email: String, newAttributes: Map[String, List[String]]): F[KeycloakUser] =
    for {
      userRepresentation <- safeFindUserByEmail(email)
      userResource <- safeGetUserResource(userRepresentation)
      _ = userRepresentation.setAttributes(newAttributes)
      _ <- safeUpdateUser(userResource, userRepresentation)
    } yield KeycloakUser.fromUserRepresentation(userRepresentation)

  private def safeGetUserResource(userRepresentation: UserRepresentation) =
    Sync[F].delay {
      keycloak.realm(realmName).users().get(userRepresentation.getId)
    }

  private def safeUpdateUser(userResource: UserResource, userRepresentation: UserRepresentation) =
    Sync[F].delay {
      userResource.update(userRepresentation)
    }

  // TODO: Remove commented out code
//  override def updateAccountRoles(accountId: UUID, roles: List[Role]): F[Account] = ???
//
//  override def archiveAccount(accountId: UUID): F[Unit] = ???
}

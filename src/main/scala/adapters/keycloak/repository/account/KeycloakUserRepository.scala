package io.github.avapl
package adapters.keycloak.repository.account

import cats.ApplicativeThrow
import cats.effect.Sync
import cats.syntax.all._
import domain.model.error.account.DuplicateAccountEmail
import jakarta.ws.rs.core.Response
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import scala.jdk.CollectionConverters._
import sttp.model.StatusCode

class KeycloakUserRepository[F[_]: Sync](
  keycloak: Keycloak,
  realmName: String
) {

  def createUser(user: KeycloakUser): F[KeycloakUser] =
    for {
      response <- safeCreateUser(user.toUserRepresentation)
      _ <- validateCreateUserResponse(response, user)
      _ <- updateUserRoles(user.email, user.roles)
    } yield user

  private def safeCreateUser(userRepresentation: UserRepresentation) =
    Sync[F].delay {
      keycloak.realm(realmName).users().create(userRepresentation)
    }

  private def validateCreateUserResponse(response: Response, user: KeycloakUser): F[KeycloakUser] =
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
    for {
      foundUserRepresentations <- safeGetUserRepresentations(email)
      userRepresentation <- toSingleUser(email, foundUserRepresentations)
      userRealmRoles <- safeGetUserRealmRoles(userRepresentation.getId)
    } yield {
      userRepresentation.setRealmRoles(userRealmRoles.asJava)
      userRepresentation
    }

  private def safeGetUserRepresentations(email: String) =
    Sync[F].delay {
      keycloak.realm(realmName).users().search(email).asScala.toList
    }

  private def toSingleUser(email: String, userRepresentations: List[UserRepresentation]): F[UserRepresentation] =
    userRepresentations match {
      case List(userRepresentation) => userRepresentation.pure
      case Nil                      => KeycloakUserNotFound(email).raiseError
      case userRepresentations =>
        new RuntimeException(
          s"Found ${userRepresentations.size} users with email: $email"
        ).raiseError
    }

  private def safeGetUserRealmRoles(userId: String) =
    Sync[F].delay {
      keycloak
        .realm(realmName)
        .users()
        .get(userId)
        .roles()
        .realmLevel()
        .listAll()
        .asScala
        .toList
        .map(_.getName)
    }

  def getUserAttributes(email: String): F[List[KeycloakAttribute]] =
    safeFindUserByEmail(email).map { userRepresentation =>
      KeycloakAttribute.fromAttributesMap(javaMapListToScala(userRepresentation.getAttributes))
    }

  def updateUserAttributes(email: String, newAttributes: List[KeycloakAttribute]): F[KeycloakUser] =
    for {
      userRepresentation <- safeFindUserByEmail(email)
      userResource <- safeGetUserResource(userRepresentation)
      _ = userRepresentation.setAttributes(KeycloakAttribute.toAttributesMap(newAttributes))
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

  def updateUserRoles(email: String, newRoles: List[KeycloakRole]): F[KeycloakUser] =
    for {
      newRoleRepresentations <- safeGetRoleRepresentations(newRoles)
      _ <- validateRoleRepresentations(newRoleRepresentations, newRoles)
      userRepresentation <- safeFindUserByEmail(email)
      userResource <- safeGetUserResource(userRepresentation)
      _ <- safeUpdateRoles(userResource, newRoleRepresentations)
    } yield {
      userRepresentation.setRealmRoles(newRoles.map(_.value).asJava)
      KeycloakUser.fromUserRepresentation(userRepresentation)
    }

  private def safeGetRoleRepresentations(roles: List[KeycloakRole]) =
    Sync[F].delay {
      val allRoleRepresentations = keycloak.realm(realmName).roles().list().asScala.toList
      allRoleRepresentations.filter { representation =>
        roles.exists(_.value == representation.getName)
      }
    }

  private def validateRoleRepresentations(roleRepresentations: List[RoleRepresentation], roles: List[KeycloakRole]) = {
    val roleRepresentationsNames = roleRepresentations.map(_.getName)
    val missingRoles = roles.filterNot(role => roleRepresentationsNames.contains(role.value))
    ApplicativeThrow[F].raiseWhen(missingRoles.nonEmpty) {
      new RuntimeException(s"Roles not found: ${missingRoles.mkString(", ")}")
    }
  }

  private def safeUpdateRoles(userResource: UserResource, newRoleRepresentations: List[RoleRepresentation]) =
    Sync[F].delay {
      userResource.roles().realmLevel().add(newRoleRepresentations.asJava)
    }

  def disableUser(email: String): F[Unit] = {
    for {
      userRepresentation <- safeFindUserByEmail(email)
      userResource <- safeGetUserResource(userRepresentation)
      _ = userRepresentation.setEnabled(false)
      _ <- safeUpdateUser(userResource, userRepresentation)
    } yield KeycloakUser.fromUserRepresentation(userRepresentation)
  }.void.recover {
    case KeycloakUserNotFound(_) => ()
  }
}

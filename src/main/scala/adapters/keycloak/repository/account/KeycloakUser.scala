package io.github.avapl
package adapters.keycloak.repository.account

import adapters.keycloak.repository.account.KeycloakAttribute.AccountId
import adapters.keycloak.repository.account.KeycloakAttribute.ManagedOfficeIds
import adapters.keycloak.repository.account.KeycloakAttributeKey.AccountIdKey
import adapters.keycloak.repository.account.KeycloakAttributeKey.ManagedOfficeIdsKey
import adapters.keycloak.repository.account.KeycloakRole.OfficeManager
import adapters.keycloak.repository.account.KeycloakRole.SuperAdmin
import adapters.keycloak.repository.account.KeycloakRole.User
import cats.implicits._
import domain.model.account.OfficeManagerAccount
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import java.util.UUID
import org.keycloak.representations.idm.UserRepresentation
import scala.jdk.CollectionConverters._

case class KeycloakUser(
  email: String,
  firstName: String,
  lastName: String,
  roles: List[KeycloakRole] = Nil,
  attributes: List[KeycloakAttribute] = Nil,
  isEnabled: Boolean = true
) {

  lazy val toUserRepresentation: UserRepresentation = {
    val userRepresentation = new UserRepresentation
    userRepresentation.setUsername(email)
    userRepresentation.setEmail(email)
    userRepresentation.setFirstName(firstName)
    userRepresentation.setLastName(lastName)
    userRepresentation.setRealmRoles(roles.map(_.value).asJava)
    userRepresentation.setAttributes(KeycloakAttribute.toAttributesMap(attributes))
    userRepresentation.setEnabled(isEnabled)
    userRepresentation
  }
}

object KeycloakUser {

  def fromUserAccount(userAccount: UserAccount): KeycloakUser =
    KeycloakUser(
      email = userAccount.email,
      firstName = userAccount.firstName,
      lastName = userAccount.lastName,
      roles = List(User),
      attributes = List(
        AccountId(userAccount.id)
      )
    )

  def fromOfficeManagerAccount(officeManagerAccount: OfficeManagerAccount): KeycloakUser =
    KeycloakUser(
      email = officeManagerAccount.email,
      firstName = officeManagerAccount.firstName,
      lastName = officeManagerAccount.lastName,
      roles = List(OfficeManager),
      attributes = List(
        AccountId(officeManagerAccount.id),
        ManagedOfficeIds(officeManagerAccount.managedOfficeIds)
      )
    )

  def fromSuperAdminAccount(superAdminAccount: SuperAdminAccount): KeycloakUser =
    KeycloakUser(
      email = superAdminAccount.email,
      firstName = superAdminAccount.firstName,
      lastName = superAdminAccount.lastName,
      roles = List(SuperAdmin),
      attributes = List(
        AccountId(superAdminAccount.id)
      )
    )

  def fromUserRepresentation(userRepresentation: UserRepresentation): KeycloakUser = {
    val roles = parseRoles(userRepresentation)
    val attributes = parseAttributes(userRepresentation)
    KeycloakUser(
      email = userRepresentation.getEmail,
      firstName = userRepresentation.getFirstName,
      lastName = userRepresentation.getLastName,
      roles = roles.flatMap(KeycloakRole.withValueOpt),
      attributes = attributes,
      isEnabled = userRepresentation.isEnabled
    )
  }

  private def parseRoles(userRepresentation: UserRepresentation) =
    Option(userRepresentation.getRealmRoles)
      .map(_.asScala.toList)
      .getOrElse(Nil)

  private def parseAttributes(userRepresentation: UserRepresentation) =
    Option(userRepresentation.getAttributes)
      .map(javaMapListToScala)
      .getOrElse(Map.empty)
      .flatMap {
        case (key, values) =>
          KeycloakAttributeKey.withValueOpt(key).flatMap {
            case AccountIdKey        => values.headOption.map(UUID.fromString).map(AccountId)
            case ManagedOfficeIdsKey => ManagedOfficeIds(values.map(UUID.fromString)).some
          }
      }
      .toList
}

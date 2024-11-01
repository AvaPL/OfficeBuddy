package io.github.avapl
package adapters.keycloak.repository.account

import adapters.keycloak.repository.account.KeycloakAttribute.AccountId
import adapters.keycloak.repository.account.KeycloakAttributeKey.AccountIdKey
import domain.model.account.Account
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

  def fromDomainAccount(account: Account): KeycloakUser =
    KeycloakUser(
      email = account.email,
      firstName = account.firstName,
      lastName = account.lastName,
      roles = List(KeycloakRole.fromDomain(account.role)),
      attributes = List(
        AccountId(account.id)
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
            case AccountIdKey => values.headOption.map(UUID.fromString).map(AccountId)
          }
      }
      .toList
}

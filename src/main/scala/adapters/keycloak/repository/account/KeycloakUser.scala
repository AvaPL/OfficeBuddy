package io.github.avapl
package adapters.keycloak.repository.account

import domain.model.account.UserAccount
import org.keycloak.representations.idm.UserRepresentation

case class KeycloakUser(
  email: String,
  firstName: String,
  lastName: String
) {

  lazy val toUserRepresentation: UserRepresentation = {
    val userRepresentation = new UserRepresentation
    userRepresentation.setUsername(email)
    userRepresentation.setEmail(email)
    userRepresentation.setFirstName(firstName)
    userRepresentation.setLastName(lastName)
    userRepresentation
  }
}

object KeycloakUser {

  def fromUserAccount(userAccount: UserAccount): KeycloakUser =
    KeycloakUser(
      email = userAccount.email,
      firstName = userAccount.firstName,
      lastName = userAccount.lastName
    )

  def fromUserRepresentation(userRepresentation: UserRepresentation): KeycloakUser =
    KeycloakUser(
      email = userRepresentation.getEmail,
      firstName = userRepresentation.getFirstName,
      lastName = userRepresentation.getLastName
    )
}

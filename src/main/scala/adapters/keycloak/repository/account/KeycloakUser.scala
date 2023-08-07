package io.github.avapl
package adapters.keycloak.repository.account

import domain.model.account.OfficeManagerAccount
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import java.util.UUID
import org.keycloak.representations.idm.UserRepresentation

case class KeycloakUser(
  email: String,
  firstName: String,
  lastName: String,
  // TODO: Add roles
  attributes: Map[String, List[String]] = Map.empty
) {

  lazy val toUserRepresentation: UserRepresentation = {
    val userRepresentation = new UserRepresentation
    userRepresentation.setUsername(email)
    userRepresentation.setEmail(email)
    userRepresentation.setFirstName(firstName)
    userRepresentation.setLastName(lastName)
    userRepresentation.setAttributes(attributes)
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

  def fromOfficeManagerAccount(officeManagerAccount: OfficeManagerAccount): KeycloakUser =
    KeycloakUser(
      email = officeManagerAccount.email,
      firstName = officeManagerAccount.firstName,
      lastName = officeManagerAccount.lastName,
      attributes = managedOfficeIdsToAttributes(officeManagerAccount.managedOfficeIds)
    )

  def fromSuperAdminAccount(superAdminAccount: SuperAdminAccount): KeycloakUser =
    KeycloakUser(
      email = superAdminAccount.email,
      firstName = superAdminAccount.firstName,
      lastName = superAdminAccount.lastName
    )

  def fromUserRepresentation(userRepresentation: UserRepresentation): KeycloakUser =
    KeycloakUser(
      email = userRepresentation.getEmail,
      firstName = userRepresentation.getFirstName,
      lastName = userRepresentation.getLastName,
      attributes = userRepresentation.getAttributes
    )

  private val managedOfficeIdsAttributeKey = "managed_office_ids"

  def managedOfficeIdsToAttributes(managedOfficeIds: List[UUID]): Map[String, List[String]] =
    Map(
      managedOfficeIdsAttributeKey -> managedOfficeIds.map(_.toString)
    )
}

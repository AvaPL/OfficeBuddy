package io.github.avapl
package adapters.keycloak.repository.account

import domain.model.account.Role
import enumeratum.values.StringEnum
import enumeratum.values.StringEnumEntry
import io.scalaland.chimney.dsl._

sealed abstract class KeycloakRole(override val value: String) extends StringEnumEntry {

  lazy val toDomain: Role =
    this.transformInto[Role]
}

object KeycloakRole extends StringEnum[KeycloakRole] {

  def fromDomain(role: Role): KeycloakRole =
    role.transformInto[KeycloakRole]

  case object User extends KeycloakRole("office_buddy_user")
  case object OfficeManager extends KeycloakRole("office_buddy_office_manager")
  case object SuperAdmin extends KeycloakRole("office_buddy_super_admin")

  override val values: IndexedSeq[KeycloakRole] = findValues
}

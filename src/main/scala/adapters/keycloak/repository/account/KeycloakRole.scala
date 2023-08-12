package io.github.avapl
package adapters.keycloak.repository.account

import enumeratum.values.StringEnum
import enumeratum.values.StringEnumEntry

sealed abstract class KeycloakRole(override val value: String) extends StringEnumEntry

object KeycloakRole extends StringEnum[KeycloakRole] {

  case object User extends KeycloakRole("office_buddy_user")
  case object OfficeManager extends KeycloakRole("office_buddy_office_manager")
  case object SuperAdmin extends KeycloakRole("office_buddy_super_admin")

  override val values: IndexedSeq[KeycloakRole] = findValues
}

package io.github.avapl
package adapters.keycloak.repository.account

import adapters.keycloak.repository.account.KeycloakAttributeKey.ManagedOfficeIdsKey
import enumeratum.values.StringEnum
import enumeratum.values.StringEnumEntry
import java.util.UUID

sealed abstract class KeycloakAttribute(val key: KeycloakAttributeKey) {
  def values: List[String]
}

object KeycloakAttribute {

  case class ManagedOfficeIds(officeIds: List[UUID]) extends KeycloakAttribute(ManagedOfficeIdsKey) {
    override val values: List[String] = officeIds.map(_.toString)
  }

  def toAttributesMap(attributes: List[KeycloakAttribute]): Map[String, List[String]] =
    attributes.map { attribute =>
      attribute.key.value -> attribute.values
    }.toMap
}

sealed abstract class KeycloakAttributeKey(override val value: String) extends StringEnumEntry

object KeycloakAttributeKey extends StringEnum[KeycloakAttributeKey] {

  case object ManagedOfficeIdsKey extends KeycloakAttributeKey("managed_office_ids")

  override val values: IndexedSeq[KeycloakAttributeKey] = findValues
}

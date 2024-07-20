package io.github.avapl
package adapters.keycloak.repository.account

import adapters.keycloak.repository.account.KeycloakAttributeKey.AccountIdKey
import adapters.keycloak.repository.account.KeycloakAttributeKey.ManagedOfficeIdsKey
import enumeratum.values.StringEnum
import enumeratum.values.StringEnumEntry
import java.util.UUID

sealed abstract class KeycloakAttribute(val key: KeycloakAttributeKey) {
  def values: List[String]
}

object KeycloakAttribute {

  case class AccountId(accountId: UUID) extends KeycloakAttribute(AccountIdKey) {
    override val values: List[String] = List(accountId.toString)
  }

  case class ManagedOfficeIds(officeIds: List[UUID]) extends KeycloakAttribute(ManagedOfficeIdsKey) {
    override val values: List[String] = officeIds.map(_.toString)
  }

  def toAttributesMap(attributes: List[KeycloakAttribute]): Map[String, List[String]] =
    attributes.map { attribute =>
      attribute.key.value -> attribute.values
    }.toMap

  def fromAttributesMap(attributes: Map[String, List[String]]): List[KeycloakAttribute] =
    attributes.flatMap {
      case (key, values) =>
        KeycloakAttributeKey.withValueOpt(key).collect {
          case AccountIdKey        => AccountId(UUID.fromString(values.head))
          case ManagedOfficeIdsKey => ManagedOfficeIds(values.map(UUID.fromString))
        }
    }.toList
}

sealed abstract class KeycloakAttributeKey(override val value: String) extends StringEnumEntry

object KeycloakAttributeKey extends StringEnum[KeycloakAttributeKey] {

  case object AccountIdKey extends KeycloakAttributeKey("account_id")
  case object ManagedOfficeIdsKey extends KeycloakAttributeKey("managed_office_ids")

  override val values: IndexedSeq[KeycloakAttributeKey] = findValues
}

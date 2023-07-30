package io.github.avapl
package domain.model.account

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait Role extends EnumEntry

object Role extends Enum[Role] {

  case object User extends Role
  case object OfficeManager extends Role
  case object SuperAdmin extends Role

  override val values: IndexedSeq[Role] = findValues
}

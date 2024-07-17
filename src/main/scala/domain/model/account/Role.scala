package io.github.avapl
package domain.model.account

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait Role extends EnumEntry {

  def inheritedRoles: List[Role] = Nil

  def hasAccess(requiredRole: Role): Boolean =
    this == requiredRole || inheritedRoles.exists(_.hasAccess(requiredRole))
}

object Role extends Enum[Role] {

  case object User extends Role

  case object OfficeManager extends Role {
    override val inheritedRoles: List[Role] = List(User)
  }

  case object SuperAdmin extends Role {
    override val inheritedRoles: List[Role] = List(OfficeManager)
  }

  override val values: IndexedSeq[Role] = findValues
}

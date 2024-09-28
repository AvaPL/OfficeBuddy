package io.github.avapl
package adapters.http.account.model

import enumeratum.CirceEnum
import enumeratum.Enum
import enumeratum.EnumEntry
import io.github.avapl.domain.model.account.Role
import sttp.tapir.codec.enumeratum.TapirCodecEnumeratum

sealed abstract class ApiRole(val toDomain: Role) extends EnumEntry

object ApiRole extends Enum[ApiRole] with CirceEnum[ApiRole] with TapirCodecEnumeratum {

  case object User extends ApiRole(toDomain = Role.User)
  case object OfficeManager extends ApiRole(toDomain = Role.OfficeManager)
  case object SuperAdmin extends ApiRole(toDomain = Role.SuperAdmin)

  override val values: IndexedSeq[ApiRole] = findValues
}

package io.github.avapl
package adapters.http.account

import derevo.derive
import domain.model.account._
import io.scalaland.chimney.dsl._
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
sealed trait ApiAccount {

  lazy val toDomain: Account =
    this match {
      case user: ApiUserAccount                   => user.transformInto[UserAccount]
      case officeManager: ApiOfficeManagerAccount => officeManager.transformInto[OfficeManagerAccount]
      case superAdmin: ApiSuperAdminAccount       => superAdmin.transformInto[SuperAdminAccount]
    }
}

object ApiAccount {

  def fromDomain(account: Account): ApiAccount =
    account match {
      case user: UserAccount                   => user.transformInto[ApiUserAccount]
      case officeManager: OfficeManagerAccount => officeManager.transformInto[ApiOfficeManagerAccount]
      case superAdmin: SuperAdminAccount       => superAdmin.transformInto[ApiSuperAdminAccount]
    }
}

@encodedName("User account")
case class ApiUserAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean,
  //
  assignedOfficeId: Option[UUID]
) extends ApiAccount

@encodedName("Office manager account")
case class ApiOfficeManagerAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean,
  //
  assignedOfficeId: Option[UUID],
  managedOfficeIds: List[UUID]
) extends ApiAccount

@encodedName("Super admin account")
case class ApiSuperAdminAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean,
  //
  assignedOfficeId: Option[UUID],
  managedOfficeIds: List[UUID]
) extends ApiAccount

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Account (create)")
case class ApiCreateAccount(
  role: ApiRole,
  firstName: String,
  lastName: String,
  email: String,
  //
  assignedOfficeId: Option[UUID],
  managedOfficeIds: List[UUID]
) {

  lazy val toDomain: CreateAccount =
    this.transformInto[CreateAccount]
}

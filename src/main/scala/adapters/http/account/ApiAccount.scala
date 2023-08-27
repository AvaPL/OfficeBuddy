package io.github.avapl
package adapters.http.account

import derevo.derive
import domain.model.account.{CreateOfficeManagerAccount, CreateSuperAdminAccount, CreateUserAccount, OfficeManagerAccount, SuperAdminAccount, UserAccount}

import io.scalaland.chimney.dsl._

import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
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
) {

  lazy val toDomain: UserAccount =
    this.transformInto[UserAccount]
}

object ApiUserAccount {

  def fromDomain(userAccount: UserAccount): ApiUserAccount =
    userAccount.transformInto[ApiUserAccount]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("User account (create)")
case class ApiCreateUserAccount(
  firstName: String,
  lastName: String,
  email: String,
  //
  assignedOfficeId: Option[UUID]
) {

  lazy val toDomain: CreateUserAccount =
    this.transformInto[CreateUserAccount]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Office manager account")
case class ApiOfficeManagerAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean,
  //
  managedOfficeIds: List[UUID]
) {

  lazy val toDomain: OfficeManagerAccount =
    this.transformInto[OfficeManagerAccount]
}

object ApiOfficeManagerAccount {

  def fromDomain(officeManagerAccount: OfficeManagerAccount): ApiOfficeManagerAccount =
    officeManagerAccount.transformInto[ApiOfficeManagerAccount]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Office manager account (create)")
case class ApiCreateOfficeManagerAccount(
  firstName: String,
  lastName: String,
  email: String,
  //
  managedOfficeIds: List[UUID]
) {

  lazy val toDomain: CreateOfficeManagerAccount =
    this.transformInto[CreateOfficeManagerAccount]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Super admin account")
case class ApiSuperAdminAccount(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  //
  isArchived: Boolean
) {

  lazy val toDomain: SuperAdminAccount =
    this.transformInto[SuperAdminAccount]
}

object ApiSuperAdminAccount {

  def fromDomain(superAdminAccount: SuperAdminAccount): ApiSuperAdminAccount =
    superAdminAccount.transformInto[ApiSuperAdminAccount]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Super admin account (create)")
case class ApiCreateSuperAdminAccount(
  firstName: String,
  lastName: String,
  email: String,
) {

  lazy val toDomain: CreateSuperAdminAccount =
    this.transformInto[CreateSuperAdminAccount]
}

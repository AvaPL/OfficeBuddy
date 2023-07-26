// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.app.v1.app

sealed abstract class OIDCGrantType(val value: _root_.scala.Int) extends _root_.scalapb.GeneratedEnum {
  type EnumType = OIDCGrantType
  def isOidcGrantTypeAuthorizationCode: _root_.scala.Boolean = false
  def isOidcGrantTypeImplicit: _root_.scala.Boolean = false
  def isOidcGrantTypeRefreshToken: _root_.scala.Boolean = false
  def isOidcGrantTypeDeviceCode: _root_.scala.Boolean = false
  def companion: _root_.scalapb.GeneratedEnumCompanion[OIDCGrantType] = com.zitadel.app.v1.app.OIDCGrantType
  final def asRecognized: _root_.scala.Option[com.zitadel.app.v1.app.OIDCGrantType.Recognized] = if (isUnrecognized) _root_.scala.None else _root_.scala.Some(this.asInstanceOf[com.zitadel.app.v1.app.OIDCGrantType.Recognized])
}

object OIDCGrantType extends _root_.scalapb.GeneratedEnumCompanion[OIDCGrantType] {
  sealed trait Recognized extends OIDCGrantType
  implicit def enumCompanion: _root_.scalapb.GeneratedEnumCompanion[OIDCGrantType] = this
  
  @SerialVersionUID(0L)
  case object OIDC_GRANT_TYPE_AUTHORIZATION_CODE extends OIDCGrantType(0) with OIDCGrantType.Recognized {
    val index = 0
    val name = "OIDC_GRANT_TYPE_AUTHORIZATION_CODE"
    override def isOidcGrantTypeAuthorizationCode: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object OIDC_GRANT_TYPE_IMPLICIT extends OIDCGrantType(1) with OIDCGrantType.Recognized {
    val index = 1
    val name = "OIDC_GRANT_TYPE_IMPLICIT"
    override def isOidcGrantTypeImplicit: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object OIDC_GRANT_TYPE_REFRESH_TOKEN extends OIDCGrantType(2) with OIDCGrantType.Recognized {
    val index = 2
    val name = "OIDC_GRANT_TYPE_REFRESH_TOKEN"
    override def isOidcGrantTypeRefreshToken: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object OIDC_GRANT_TYPE_DEVICE_CODE extends OIDCGrantType(3) with OIDCGrantType.Recognized {
    val index = 3
    val name = "OIDC_GRANT_TYPE_DEVICE_CODE"
    override def isOidcGrantTypeDeviceCode: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  final case class Unrecognized(unrecognizedValue: _root_.scala.Int) extends OIDCGrantType(unrecognizedValue) with _root_.scalapb.UnrecognizedEnum
  lazy val values = scala.collection.immutable.Seq(OIDC_GRANT_TYPE_AUTHORIZATION_CODE, OIDC_GRANT_TYPE_IMPLICIT, OIDC_GRANT_TYPE_REFRESH_TOKEN, OIDC_GRANT_TYPE_DEVICE_CODE)
  def fromValue(__value: _root_.scala.Int): OIDCGrantType = __value match {
    case 0 => OIDC_GRANT_TYPE_AUTHORIZATION_CODE
    case 1 => OIDC_GRANT_TYPE_IMPLICIT
    case 2 => OIDC_GRANT_TYPE_REFRESH_TOKEN
    case 3 => OIDC_GRANT_TYPE_DEVICE_CODE
    case __other => Unrecognized(__other)
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.EnumDescriptor = AppProto.javaDescriptor.getEnumTypes().get(2)
  def scalaDescriptor: _root_.scalapb.descriptors.EnumDescriptor = AppProto.scalaDescriptor.enums(2)
}
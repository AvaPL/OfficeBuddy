// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.user.v2alpha.password

sealed abstract class NotificationType(val value: _root_.scala.Int) extends _root_.scalapb.GeneratedEnum {
  type EnumType = NotificationType
  def isNotificationTypeUnspecified: _root_.scala.Boolean = false
  def isNotificationTypeEmail: _root_.scala.Boolean = false
  def isNotificationTypeSms: _root_.scala.Boolean = false
  def companion: _root_.scalapb.GeneratedEnumCompanion[NotificationType] = com.zitadel.user.v2alpha.password.NotificationType
  final def asRecognized: _root_.scala.Option[com.zitadel.user.v2alpha.password.NotificationType.Recognized] = if (isUnrecognized) _root_.scala.None else _root_.scala.Some(this.asInstanceOf[com.zitadel.user.v2alpha.password.NotificationType.Recognized])
}

object NotificationType extends _root_.scalapb.GeneratedEnumCompanion[NotificationType] {
  sealed trait Recognized extends NotificationType
  implicit def enumCompanion: _root_.scalapb.GeneratedEnumCompanion[NotificationType] = this
  
  @SerialVersionUID(0L)
  case object NOTIFICATION_TYPE_Unspecified extends NotificationType(0) with NotificationType.Recognized {
    val index = 0
    val name = "NOTIFICATION_TYPE_Unspecified"
    override def isNotificationTypeUnspecified: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object NOTIFICATION_TYPE_Email extends NotificationType(1) with NotificationType.Recognized {
    val index = 1
    val name = "NOTIFICATION_TYPE_Email"
    override def isNotificationTypeEmail: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object NOTIFICATION_TYPE_SMS extends NotificationType(2) with NotificationType.Recognized {
    val index = 2
    val name = "NOTIFICATION_TYPE_SMS"
    override def isNotificationTypeSms: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  final case class Unrecognized(unrecognizedValue: _root_.scala.Int) extends NotificationType(unrecognizedValue) with _root_.scalapb.UnrecognizedEnum
  lazy val values = scala.collection.immutable.Seq(NOTIFICATION_TYPE_Unspecified, NOTIFICATION_TYPE_Email, NOTIFICATION_TYPE_SMS)
  def fromValue(__value: _root_.scala.Int): NotificationType = __value match {
    case 0 => NOTIFICATION_TYPE_Unspecified
    case 1 => NOTIFICATION_TYPE_Email
    case 2 => NOTIFICATION_TYPE_SMS
    case __other => Unrecognized(__other)
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.EnumDescriptor = PasswordProto.javaDescriptor.getEnumTypes().get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.EnumDescriptor = PasswordProto.scalaDescriptor.enums(0)
}
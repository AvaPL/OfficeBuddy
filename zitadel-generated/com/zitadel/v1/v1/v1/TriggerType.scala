// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.v1.v1.v1

sealed abstract class TriggerType(val value: _root_.scala.Int) extends _root_.scalapb.GeneratedEnum {
  type EnumType = TriggerType
  def isTriggerTypeUnspecified: _root_.scala.Boolean = false
  def isTriggerTypePostAuthentication: _root_.scala.Boolean = false
  def isTriggerTypePreCreation: _root_.scala.Boolean = false
  def isTriggerTypePostCreation: _root_.scala.Boolean = false
  def companion: _root_.scalapb.GeneratedEnumCompanion[TriggerType] = com.zitadel.v1.v1.v1.TriggerType
  final def asRecognized: _root_.scala.Option[com.zitadel.v1.v1.v1.TriggerType.Recognized] = if (isUnrecognized) _root_.scala.None else _root_.scala.Some(this.asInstanceOf[com.zitadel.v1.v1.v1.TriggerType.Recognized])
}

object TriggerType extends _root_.scalapb.GeneratedEnumCompanion[TriggerType] {
  sealed trait Recognized extends TriggerType
  implicit def enumCompanion: _root_.scalapb.GeneratedEnumCompanion[TriggerType] = this
  
  @SerialVersionUID(0L)
  case object TRIGGER_TYPE_UNSPECIFIED extends TriggerType(0) with TriggerType.Recognized {
    val index = 0
    val name = "TRIGGER_TYPE_UNSPECIFIED"
    override def isTriggerTypeUnspecified: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object TRIGGER_TYPE_POST_AUTHENTICATION extends TriggerType(1) with TriggerType.Recognized {
    val index = 1
    val name = "TRIGGER_TYPE_POST_AUTHENTICATION"
    override def isTriggerTypePostAuthentication: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object TRIGGER_TYPE_PRE_CREATION extends TriggerType(2) with TriggerType.Recognized {
    val index = 2
    val name = "TRIGGER_TYPE_PRE_CREATION"
    override def isTriggerTypePreCreation: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object TRIGGER_TYPE_POST_CREATION extends TriggerType(3) with TriggerType.Recognized {
    val index = 3
    val name = "TRIGGER_TYPE_POST_CREATION"
    override def isTriggerTypePostCreation: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  final case class Unrecognized(unrecognizedValue: _root_.scala.Int) extends TriggerType(unrecognizedValue) with _root_.scalapb.UnrecognizedEnum
  lazy val values = scala.collection.immutable.Seq(TRIGGER_TYPE_UNSPECIFIED, TRIGGER_TYPE_POST_AUTHENTICATION, TRIGGER_TYPE_PRE_CREATION, TRIGGER_TYPE_POST_CREATION)
  def fromValue(__value: _root_.scala.Int): TriggerType = __value match {
    case 0 => TRIGGER_TYPE_UNSPECIFIED
    case 1 => TRIGGER_TYPE_POST_AUTHENTICATION
    case 2 => TRIGGER_TYPE_PRE_CREATION
    case 3 => TRIGGER_TYPE_POST_CREATION
    case __other => Unrecognized(__other)
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.EnumDescriptor = V1Proto.javaDescriptor.getEnumTypes().get(1)
  def scalaDescriptor: _root_.scalapb.descriptors.EnumDescriptor = V1Proto.scalaDescriptor.enums(1)
}
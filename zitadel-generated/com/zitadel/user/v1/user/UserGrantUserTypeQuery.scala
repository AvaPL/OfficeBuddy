// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.user.v1.user

@SerialVersionUID(0L)
final case class UserGrantUserTypeQuery(
    `type`: com.zitadel.user.v1.user.Type = com.zitadel.user.v1.user.Type.TYPE_UNSPECIFIED,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[UserGrantUserTypeQuery] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = `type`.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(1, __value)
        }
      };
      __size += unknownFields.serializedSize
      __size
    }
    override def serializedSize: _root_.scala.Int = {
      var __size = __serializedSizeMemoized
      if (__size == 0) {
        __size = __computeSerializedSize() + 1
        __serializedSizeMemoized = __size
      }
      __size - 1
      
    }
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): _root_.scala.Unit = {
      {
        val __v = `type`.value
        if (__v != 0) {
          _output__.writeEnum(1, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withType(__v: com.zitadel.user.v1.user.Type): UserGrantUserTypeQuery = copy(`type` = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = `type`.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PEnum(`type`.scalaValueDescriptor)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.user.v1.user.UserGrantUserTypeQuery.type = com.zitadel.user.v1.user.UserGrantUserTypeQuery
    // @@protoc_insertion_point(GeneratedMessage[zitadel.user.v1.UserGrantUserTypeQuery])
}

object UserGrantUserTypeQuery extends scalapb.GeneratedMessageCompanion[com.zitadel.user.v1.user.UserGrantUserTypeQuery] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.user.v1.user.UserGrantUserTypeQuery] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.user.v1.user.UserGrantUserTypeQuery = {
    var __type: com.zitadel.user.v1.user.Type = com.zitadel.user.v1.user.Type.TYPE_UNSPECIFIED
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 8 =>
          __type = com.zitadel.user.v1.user.Type.fromValue(_input__.readEnum())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.user.v1.user.UserGrantUserTypeQuery(
        `type` = __type,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.user.v1.user.UserGrantUserTypeQuery] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.user.v1.user.UserGrantUserTypeQuery(
        `type` = com.zitadel.user.v1.user.Type.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.user.v1.user.Type.TYPE_UNSPECIFIED.scalaValueDescriptor).number)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = UserProto.javaDescriptor.getMessageTypes().get(46)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = UserProto.scalaDescriptor.messages(46)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 1 => com.zitadel.user.v1.user.Type
    }
  }
  lazy val defaultInstance = com.zitadel.user.v1.user.UserGrantUserTypeQuery(
    `type` = com.zitadel.user.v1.user.Type.TYPE_UNSPECIFIED
  )
  implicit class UserGrantUserTypeQueryLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v1.user.UserGrantUserTypeQuery]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.user.v1.user.UserGrantUserTypeQuery](_l) {
    def `type`: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v1.user.Type] = field(_.`type`)((c_, f_) => c_.copy(`type` = f_))
  }
  final val TYPE_FIELD_NUMBER = 1
  def of(
    `type`: com.zitadel.user.v1.user.Type
  ): _root_.com.zitadel.user.v1.user.UserGrantUserTypeQuery = _root_.com.zitadel.user.v1.user.UserGrantUserTypeQuery(
    `type`
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.user.v1.UserGrantUserTypeQuery])
}

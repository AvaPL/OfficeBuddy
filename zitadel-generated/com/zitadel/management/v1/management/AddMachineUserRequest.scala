// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

@SerialVersionUID(0L)
final case class AddMachineUserRequest(
    userName: _root_.scala.Predef.String = "",
    name: _root_.scala.Predef.String = "",
    description: _root_.scala.Predef.String = "",
    accessTokenType: com.zitadel.user.v1.user.AccessTokenType = com.zitadel.user.v1.user.AccessTokenType.ACCESS_TOKEN_TYPE_BEARER,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[AddMachineUserRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = userName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = name
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      
      {
        val __value = description
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
        }
      };
      
      {
        val __value = accessTokenType.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(4, __value)
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
        val __v = userName
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = name
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      {
        val __v = description
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
      {
        val __v = accessTokenType.value
        if (__v != 0) {
          _output__.writeEnum(4, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withUserName(__v: _root_.scala.Predef.String): AddMachineUserRequest = copy(userName = __v)
    def withName(__v: _root_.scala.Predef.String): AddMachineUserRequest = copy(name = __v)
    def withDescription(__v: _root_.scala.Predef.String): AddMachineUserRequest = copy(description = __v)
    def withAccessTokenType(__v: com.zitadel.user.v1.user.AccessTokenType): AddMachineUserRequest = copy(accessTokenType = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = userName
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = name
          if (__t != "") __t else null
        }
        case 3 => {
          val __t = description
          if (__t != "") __t else null
        }
        case 4 => {
          val __t = accessTokenType.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(userName)
        case 2 => _root_.scalapb.descriptors.PString(name)
        case 3 => _root_.scalapb.descriptors.PString(description)
        case 4 => _root_.scalapb.descriptors.PEnum(accessTokenType.scalaValueDescriptor)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.AddMachineUserRequest.type = com.zitadel.management.v1.management.AddMachineUserRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.AddMachineUserRequest])
}

object AddMachineUserRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.AddMachineUserRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.AddMachineUserRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.AddMachineUserRequest = {
    var __userName: _root_.scala.Predef.String = ""
    var __name: _root_.scala.Predef.String = ""
    var __description: _root_.scala.Predef.String = ""
    var __accessTokenType: com.zitadel.user.v1.user.AccessTokenType = com.zitadel.user.v1.user.AccessTokenType.ACCESS_TOKEN_TYPE_BEARER
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __userName = _input__.readStringRequireUtf8()
        case 18 =>
          __name = _input__.readStringRequireUtf8()
        case 26 =>
          __description = _input__.readStringRequireUtf8()
        case 32 =>
          __accessTokenType = com.zitadel.user.v1.user.AccessTokenType.fromValue(_input__.readEnum())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.AddMachineUserRequest(
        userName = __userName,
        name = __name,
        description = __description,
        accessTokenType = __accessTokenType,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.AddMachineUserRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.AddMachineUserRequest(
        userName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        name = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        description = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        accessTokenType = com.zitadel.user.v1.user.AccessTokenType.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.user.v1.user.AccessTokenType.ACCESS_TOKEN_TYPE_BEARER.scalaValueDescriptor).number)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(22)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(22)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 4 => com.zitadel.user.v1.user.AccessTokenType
    }
  }
  lazy val defaultInstance = com.zitadel.management.v1.management.AddMachineUserRequest(
    userName = "",
    name = "",
    description = "",
    accessTokenType = com.zitadel.user.v1.user.AccessTokenType.ACCESS_TOKEN_TYPE_BEARER
  )
  implicit class AddMachineUserRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.AddMachineUserRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.AddMachineUserRequest](_l) {
    def userName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.userName)((c_, f_) => c_.copy(userName = f_))
    def name: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.name)((c_, f_) => c_.copy(name = f_))
    def description: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.description)((c_, f_) => c_.copy(description = f_))
    def accessTokenType: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v1.user.AccessTokenType] = field(_.accessTokenType)((c_, f_) => c_.copy(accessTokenType = f_))
  }
  final val USER_NAME_FIELD_NUMBER = 1
  final val NAME_FIELD_NUMBER = 2
  final val DESCRIPTION_FIELD_NUMBER = 3
  final val ACCESS_TOKEN_TYPE_FIELD_NUMBER = 4
  def of(
    userName: _root_.scala.Predef.String,
    name: _root_.scala.Predef.String,
    description: _root_.scala.Predef.String,
    accessTokenType: com.zitadel.user.v1.user.AccessTokenType
  ): _root_.com.zitadel.management.v1.management.AddMachineUserRequest = _root_.com.zitadel.management.v1.management.AddMachineUserRequest(
    userName,
    name,
    description,
    accessTokenType
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.AddMachineUserRequest])
}

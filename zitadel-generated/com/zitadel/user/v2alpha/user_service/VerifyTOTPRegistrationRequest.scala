// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.user.v2alpha.user_service

@SerialVersionUID(0L)
final case class VerifyTOTPRegistrationRequest(
    userId: _root_.scala.Predef.String = "",
    code: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[VerifyTOTPRegistrationRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = userId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = code
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
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
        val __v = userId
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = code
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withUserId(__v: _root_.scala.Predef.String): VerifyTOTPRegistrationRequest = copy(userId = __v)
    def withCode(__v: _root_.scala.Predef.String): VerifyTOTPRegistrationRequest = copy(code = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = userId
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = code
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(userId)
        case 2 => _root_.scalapb.descriptors.PString(code)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest.type = com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.user.v2alpha.VerifyTOTPRegistrationRequest])
}

object VerifyTOTPRegistrationRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest = {
    var __userId: _root_.scala.Predef.String = ""
    var __code: _root_.scala.Predef.String = ""
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __userId = _input__.readStringRequireUtf8()
        case 18 =>
          __code = _input__.readStringRequireUtf8()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest(
        userId = __userId,
        code = __code,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest(
        userId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        code = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = UserServiceProto.javaDescriptor.getMessageTypes().get(16)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = UserServiceProto.scalaDescriptor.messages(16)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest(
    userId = "",
    code = ""
  )
  implicit class VerifyTOTPRegistrationRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest](_l) {
    def userId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.userId)((c_, f_) => c_.copy(userId = f_))
    def code: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.code)((c_, f_) => c_.copy(code = f_))
  }
  final val USER_ID_FIELD_NUMBER = 1
  final val CODE_FIELD_NUMBER = 2
  def of(
    userId: _root_.scala.Predef.String,
    code: _root_.scala.Predef.String
  ): _root_.com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest = _root_.com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest(
    userId,
    code
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.user.v2alpha.VerifyTOTPRegistrationRequest])
}

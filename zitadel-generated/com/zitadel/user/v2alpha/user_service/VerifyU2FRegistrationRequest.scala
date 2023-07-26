// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.user.v2alpha.user_service

@SerialVersionUID(0L)
final case class VerifyU2FRegistrationRequest(
    userId: _root_.scala.Predef.String = "",
    u2FId: _root_.scala.Predef.String = "",
    publicKeyCredential: _root_.scala.Option[com.google.protobuf.struct.Struct] = _root_.scala.None,
    tokenName: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[VerifyU2FRegistrationRequest] {
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
        val __value = u2FId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      if (publicKeyCredential.isDefined) {
        val __value = publicKeyCredential.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      
      {
        val __value = tokenName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
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
        val __v = u2FId
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      publicKeyCredential.foreach { __v =>
        val __m = __v
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      {
        val __v = tokenName
        if (!__v.isEmpty) {
          _output__.writeString(4, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withUserId(__v: _root_.scala.Predef.String): VerifyU2FRegistrationRequest = copy(userId = __v)
    def withU2FId(__v: _root_.scala.Predef.String): VerifyU2FRegistrationRequest = copy(u2FId = __v)
    def getPublicKeyCredential: com.google.protobuf.struct.Struct = publicKeyCredential.getOrElse(com.google.protobuf.struct.Struct.defaultInstance)
    def clearPublicKeyCredential: VerifyU2FRegistrationRequest = copy(publicKeyCredential = _root_.scala.None)
    def withPublicKeyCredential(__v: com.google.protobuf.struct.Struct): VerifyU2FRegistrationRequest = copy(publicKeyCredential = Option(__v))
    def withTokenName(__v: _root_.scala.Predef.String): VerifyU2FRegistrationRequest = copy(tokenName = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = userId
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = u2FId
          if (__t != "") __t else null
        }
        case 3 => publicKeyCredential.orNull
        case 4 => {
          val __t = tokenName
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(userId)
        case 2 => _root_.scalapb.descriptors.PString(u2FId)
        case 3 => publicKeyCredential.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 4 => _root_.scalapb.descriptors.PString(tokenName)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest.type = com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.user.v2alpha.VerifyU2FRegistrationRequest])
}

object VerifyU2FRegistrationRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest = {
    var __userId: _root_.scala.Predef.String = ""
    var __u2FId: _root_.scala.Predef.String = ""
    var __publicKeyCredential: _root_.scala.Option[com.google.protobuf.struct.Struct] = _root_.scala.None
    var __tokenName: _root_.scala.Predef.String = ""
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __userId = _input__.readStringRequireUtf8()
        case 18 =>
          __u2FId = _input__.readStringRequireUtf8()
        case 26 =>
          __publicKeyCredential = Option(__publicKeyCredential.fold(_root_.scalapb.LiteParser.readMessage[com.google.protobuf.struct.Struct](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 34 =>
          __tokenName = _input__.readStringRequireUtf8()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest(
        userId = __userId,
        u2FId = __u2FId,
        publicKeyCredential = __publicKeyCredential,
        tokenName = __tokenName,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest(
        userId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        u2FId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        publicKeyCredential = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[_root_.scala.Option[com.google.protobuf.struct.Struct]]),
        tokenName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = UserServiceProto.javaDescriptor.getMessageTypes().get(12)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = UserServiceProto.scalaDescriptor.messages(12)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 3 => __out = com.google.protobuf.struct.Struct
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest(
    userId = "",
    u2FId = "",
    publicKeyCredential = _root_.scala.None,
    tokenName = ""
  )
  implicit class VerifyU2FRegistrationRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest](_l) {
    def userId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.userId)((c_, f_) => c_.copy(userId = f_))
    def u2FId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.u2FId)((c_, f_) => c_.copy(u2FId = f_))
    def publicKeyCredential: _root_.scalapb.lenses.Lens[UpperPB, com.google.protobuf.struct.Struct] = field(_.getPublicKeyCredential)((c_, f_) => c_.copy(publicKeyCredential = Option(f_)))
    def optionalPublicKeyCredential: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.google.protobuf.struct.Struct]] = field(_.publicKeyCredential)((c_, f_) => c_.copy(publicKeyCredential = f_))
    def tokenName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.tokenName)((c_, f_) => c_.copy(tokenName = f_))
  }
  final val USER_ID_FIELD_NUMBER = 1
  final val U2F_ID_FIELD_NUMBER = 2
  final val PUBLIC_KEY_CREDENTIAL_FIELD_NUMBER = 3
  final val TOKEN_NAME_FIELD_NUMBER = 4
  def of(
    userId: _root_.scala.Predef.String,
    u2FId: _root_.scala.Predef.String,
    publicKeyCredential: _root_.scala.Option[com.google.protobuf.struct.Struct],
    tokenName: _root_.scala.Predef.String
  ): _root_.com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest = _root_.com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest(
    userId,
    u2FId,
    publicKeyCredential,
    tokenName
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.user.v2alpha.VerifyU2FRegistrationRequest])
}

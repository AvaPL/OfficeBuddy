// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.user.v2alpha.user_service

@SerialVersionUID(0L)
final case class PasswordResetRequest(
    userId: _root_.scala.Predef.String = "",
    medium: com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.Empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[PasswordResetRequest] {
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
      if (medium.sendLink.isDefined) {
        val __value = medium.sendLink.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (medium.returnCode.isDefined) {
        val __value = medium.returnCode.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
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
      medium.sendLink.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      medium.returnCode.foreach { __v =>
        val __m = __v
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def withUserId(__v: _root_.scala.Predef.String): PasswordResetRequest = copy(userId = __v)
    def getSendLink: com.zitadel.user.v2alpha.password.SendPasswordResetLink = medium.sendLink.getOrElse(com.zitadel.user.v2alpha.password.SendPasswordResetLink.defaultInstance)
    def withSendLink(__v: com.zitadel.user.v2alpha.password.SendPasswordResetLink): PasswordResetRequest = copy(medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.SendLink(__v))
    def getReturnCode: com.zitadel.user.v2alpha.password.ReturnPasswordResetCode = medium.returnCode.getOrElse(com.zitadel.user.v2alpha.password.ReturnPasswordResetCode.defaultInstance)
    def withReturnCode(__v: com.zitadel.user.v2alpha.password.ReturnPasswordResetCode): PasswordResetRequest = copy(medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.ReturnCode(__v))
    def clearMedium: PasswordResetRequest = copy(medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.Empty)
    def withMedium(__v: com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium): PasswordResetRequest = copy(medium = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = userId
          if (__t != "") __t else null
        }
        case 2 => medium.sendLink.orNull
        case 3 => medium.returnCode.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(userId)
        case 2 => medium.sendLink.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => medium.returnCode.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.user.v2alpha.user_service.PasswordResetRequest.type = com.zitadel.user.v2alpha.user_service.PasswordResetRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.user.v2alpha.PasswordResetRequest])
}

object PasswordResetRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.PasswordResetRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.PasswordResetRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.user.v2alpha.user_service.PasswordResetRequest = {
    var __userId: _root_.scala.Predef.String = ""
    var __medium: com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.Empty
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __userId = _input__.readStringRequireUtf8()
        case 18 =>
          __medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.SendLink(__medium.sendLink.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.user.v2alpha.password.SendPasswordResetLink](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 26 =>
          __medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.ReturnCode(__medium.returnCode.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.user.v2alpha.password.ReturnPasswordResetCode](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.user.v2alpha.user_service.PasswordResetRequest(
        userId = __userId,
        medium = __medium,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.user.v2alpha.user_service.PasswordResetRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.user.v2alpha.user_service.PasswordResetRequest(
        userId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        medium = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.user.v2alpha.password.SendPasswordResetLink]]).map(com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.SendLink(_))
            .orElse[com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium](__fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[_root_.scala.Option[com.zitadel.user.v2alpha.password.ReturnPasswordResetCode]]).map(com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.ReturnCode(_)))
            .getOrElse(com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = UserServiceProto.javaDescriptor.getMessageTypes().get(26)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = UserServiceProto.scalaDescriptor.messages(26)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 2 => __out = com.zitadel.user.v2alpha.password.SendPasswordResetLink
      case 3 => __out = com.zitadel.user.v2alpha.password.ReturnPasswordResetCode
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.user.v2alpha.user_service.PasswordResetRequest(
    userId = "",
    medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.Empty
  )
  sealed trait Medium extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isSendLink: _root_.scala.Boolean = false
    def isReturnCode: _root_.scala.Boolean = false
    def sendLink: _root_.scala.Option[com.zitadel.user.v2alpha.password.SendPasswordResetLink] = _root_.scala.None
    def returnCode: _root_.scala.Option[com.zitadel.user.v2alpha.password.ReturnPasswordResetCode] = _root_.scala.None
  }
  object Medium {
    @SerialVersionUID(0L)
    case object Empty extends com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class SendLink(value: com.zitadel.user.v2alpha.password.SendPasswordResetLink) extends com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium {
      type ValueType = com.zitadel.user.v2alpha.password.SendPasswordResetLink
      override def isSendLink: _root_.scala.Boolean = true
      override def sendLink: _root_.scala.Option[com.zitadel.user.v2alpha.password.SendPasswordResetLink] = Some(value)
      override def number: _root_.scala.Int = 2
    }
    @SerialVersionUID(0L)
    final case class ReturnCode(value: com.zitadel.user.v2alpha.password.ReturnPasswordResetCode) extends com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium {
      type ValueType = com.zitadel.user.v2alpha.password.ReturnPasswordResetCode
      override def isReturnCode: _root_.scala.Boolean = true
      override def returnCode: _root_.scala.Option[com.zitadel.user.v2alpha.password.ReturnPasswordResetCode] = Some(value)
      override def number: _root_.scala.Int = 3
    }
  }
  implicit class PasswordResetRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.user_service.PasswordResetRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.user.v2alpha.user_service.PasswordResetRequest](_l) {
    def userId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.userId)((c_, f_) => c_.copy(userId = f_))
    def sendLink: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.password.SendPasswordResetLink] = field(_.getSendLink)((c_, f_) => c_.copy(medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.SendLink(f_)))
    def returnCode: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.password.ReturnPasswordResetCode] = field(_.getReturnCode)((c_, f_) => c_.copy(medium = com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium.ReturnCode(f_)))
    def medium: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium] = field(_.medium)((c_, f_) => c_.copy(medium = f_))
  }
  final val USER_ID_FIELD_NUMBER = 1
  final val SEND_LINK_FIELD_NUMBER = 2
  final val RETURN_CODE_FIELD_NUMBER = 3
  def of(
    userId: _root_.scala.Predef.String,
    medium: com.zitadel.user.v2alpha.user_service.PasswordResetRequest.Medium
  ): _root_.com.zitadel.user.v2alpha.user_service.PasswordResetRequest = _root_.com.zitadel.user.v2alpha.user_service.PasswordResetRequest(
    userId,
    medium
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.user.v2alpha.PasswordResetRequest])
}

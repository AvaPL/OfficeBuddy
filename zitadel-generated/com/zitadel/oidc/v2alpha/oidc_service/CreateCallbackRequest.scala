// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.oidc.v2alpha.oidc_service

@SerialVersionUID(0L)
final case class CreateCallbackRequest(
    authRequestId: _root_.scala.Predef.String = "",
    callbackKind: com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[CreateCallbackRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = authRequestId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      if (callbackKind.session.isDefined) {
        val __value = callbackKind.session.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (callbackKind.error.isDefined) {
        val __value = callbackKind.error.get
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
        val __v = authRequestId
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      callbackKind.session.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      callbackKind.error.foreach { __v =>
        val __m = __v
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def withAuthRequestId(__v: _root_.scala.Predef.String): CreateCallbackRequest = copy(authRequestId = __v)
    def getSession: com.zitadel.oidc.v2alpha.oidc_service.Session = callbackKind.session.getOrElse(com.zitadel.oidc.v2alpha.oidc_service.Session.defaultInstance)
    def withSession(__v: com.zitadel.oidc.v2alpha.oidc_service.Session): CreateCallbackRequest = copy(callbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Session(__v))
    def getError: com.zitadel.oidc.v2alpha.authorization.AuthorizationError = callbackKind.error.getOrElse(com.zitadel.oidc.v2alpha.authorization.AuthorizationError.defaultInstance)
    def withError(__v: com.zitadel.oidc.v2alpha.authorization.AuthorizationError): CreateCallbackRequest = copy(callbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Error(__v))
    def clearCallbackKind: CreateCallbackRequest = copy(callbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Empty)
    def withCallbackKind(__v: com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind): CreateCallbackRequest = copy(callbackKind = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = authRequestId
          if (__t != "") __t else null
        }
        case 2 => callbackKind.session.orNull
        case 3 => callbackKind.error.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(authRequestId)
        case 2 => callbackKind.session.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => callbackKind.error.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.type = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.oidc.v2alpha.CreateCallbackRequest])
}

object CreateCallbackRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest = {
    var __authRequestId: _root_.scala.Predef.String = ""
    var __callbackKind: com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Empty
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __authRequestId = _input__.readStringRequireUtf8()
        case 18 =>
          __callbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Session(__callbackKind.session.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.oidc.v2alpha.oidc_service.Session](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 26 =>
          __callbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Error(__callbackKind.error.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.oidc.v2alpha.authorization.AuthorizationError](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest(
        authRequestId = __authRequestId,
        callbackKind = __callbackKind,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest(
        authRequestId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        callbackKind = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.oidc.v2alpha.oidc_service.Session]]).map(com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Session(_))
            .orElse[com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind](__fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[_root_.scala.Option[com.zitadel.oidc.v2alpha.authorization.AuthorizationError]]).map(com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Error(_)))
            .getOrElse(com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = OidcServiceProto.javaDescriptor.getMessageTypes().get(2)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = OidcServiceProto.scalaDescriptor.messages(2)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 2 => __out = com.zitadel.oidc.v2alpha.oidc_service.Session
      case 3 => __out = com.zitadel.oidc.v2alpha.authorization.AuthorizationError
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest(
    authRequestId = "",
    callbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Empty
  )
  sealed trait CallbackKind extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isSession: _root_.scala.Boolean = false
    def isError: _root_.scala.Boolean = false
    def session: _root_.scala.Option[com.zitadel.oidc.v2alpha.oidc_service.Session] = _root_.scala.None
    def error: _root_.scala.Option[com.zitadel.oidc.v2alpha.authorization.AuthorizationError] = _root_.scala.None
  }
  object CallbackKind {
    @SerialVersionUID(0L)
    case object Empty extends com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class Session(value: com.zitadel.oidc.v2alpha.oidc_service.Session) extends com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind {
      type ValueType = com.zitadel.oidc.v2alpha.oidc_service.Session
      override def isSession: _root_.scala.Boolean = true
      override def session: _root_.scala.Option[com.zitadel.oidc.v2alpha.oidc_service.Session] = Some(value)
      override def number: _root_.scala.Int = 2
    }
    @SerialVersionUID(0L)
    final case class Error(value: com.zitadel.oidc.v2alpha.authorization.AuthorizationError) extends com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind {
      type ValueType = com.zitadel.oidc.v2alpha.authorization.AuthorizationError
      override def isError: _root_.scala.Boolean = true
      override def error: _root_.scala.Option[com.zitadel.oidc.v2alpha.authorization.AuthorizationError] = Some(value)
      override def number: _root_.scala.Int = 3
    }
  }
  implicit class CreateCallbackRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest](_l) {
    def authRequestId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.authRequestId)((c_, f_) => c_.copy(authRequestId = f_))
    def session: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.oidc.v2alpha.oidc_service.Session] = field(_.getSession)((c_, f_) => c_.copy(callbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Session(f_)))
    def error: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.oidc.v2alpha.authorization.AuthorizationError] = field(_.getError)((c_, f_) => c_.copy(callbackKind = com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind.Error(f_)))
    def callbackKind: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind] = field(_.callbackKind)((c_, f_) => c_.copy(callbackKind = f_))
  }
  final val AUTH_REQUEST_ID_FIELD_NUMBER = 1
  final val SESSION_FIELD_NUMBER = 2
  final val ERROR_FIELD_NUMBER = 3
  def of(
    authRequestId: _root_.scala.Predef.String,
    callbackKind: com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest.CallbackKind
  ): _root_.com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest = _root_.com.zitadel.oidc.v2alpha.oidc_service.CreateCallbackRequest(
    authRequestId,
    callbackKind
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.oidc.v2alpha.CreateCallbackRequest])
}

// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.settings.v2alpha.settings_service

@SerialVersionUID(0L)
final case class GetActiveIdentityProvidersRequest(
    ctx: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.RequestContext] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[GetActiveIdentityProvidersRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (ctx.isDefined) {
        val __value = ctx.get
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
      ctx.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getCtx: com.zitadel.`object`.v2alpha.`object`.RequestContext = ctx.getOrElse(com.zitadel.`object`.v2alpha.`object`.RequestContext.defaultInstance)
    def clearCtx: GetActiveIdentityProvidersRequest = copy(ctx = _root_.scala.None)
    def withCtx(__v: com.zitadel.`object`.v2alpha.`object`.RequestContext): GetActiveIdentityProvidersRequest = copy(ctx = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => ctx.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => ctx.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest.type = com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.settings.v2alpha.GetActiveIdentityProvidersRequest])
}

object GetActiveIdentityProvidersRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest = {
    var __ctx: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.RequestContext] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __ctx = Option(__ctx.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.`object`.v2alpha.`object`.RequestContext](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest(
        ctx = __ctx,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest(
        ctx = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.RequestContext]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SettingsServiceProto.javaDescriptor.getMessageTypes().get(12)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SettingsServiceProto.scalaDescriptor.messages(12)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.`object`.v2alpha.`object`.RequestContext
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest(
    ctx = _root_.scala.None
  )
  implicit class GetActiveIdentityProvidersRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest](_l) {
    def ctx: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.`object`.v2alpha.`object`.RequestContext] = field(_.getCtx)((c_, f_) => c_.copy(ctx = Option(f_)))
    def optionalCtx: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.RequestContext]] = field(_.ctx)((c_, f_) => c_.copy(ctx = f_))
  }
  final val CTX_FIELD_NUMBER = 1
  def of(
    ctx: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.RequestContext]
  ): _root_.com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest = _root_.com.zitadel.settings.v2alpha.settings_service.GetActiveIdentityProvidersRequest(
    ctx
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.settings.v2alpha.GetActiveIdentityProvidersRequest])
}

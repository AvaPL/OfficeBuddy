// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.user.v2alpha.user_service

@SerialVersionUID(0L)
final case class SetPasswordResponse(
    details: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[SetPasswordResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (details.isDefined) {
        val __value = details.get
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
      details.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getDetails: com.zitadel.`object`.v2alpha.`object`.Details = details.getOrElse(com.zitadel.`object`.v2alpha.`object`.Details.defaultInstance)
    def clearDetails: SetPasswordResponse = copy(details = _root_.scala.None)
    def withDetails(__v: com.zitadel.`object`.v2alpha.`object`.Details): SetPasswordResponse = copy(details = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => details.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => details.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.user.v2alpha.user_service.SetPasswordResponse.type = com.zitadel.user.v2alpha.user_service.SetPasswordResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.user.v2alpha.SetPasswordResponse])
}

object SetPasswordResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.SetPasswordResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.SetPasswordResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.user.v2alpha.user_service.SetPasswordResponse = {
    var __details: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __details = Option(__details.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.`object`.v2alpha.`object`.Details](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.user.v2alpha.user_service.SetPasswordResponse(
        details = __details,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.user.v2alpha.user_service.SetPasswordResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.user.v2alpha.user_service.SetPasswordResponse(
        details = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = UserServiceProto.javaDescriptor.getMessageTypes().get(29)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = UserServiceProto.scalaDescriptor.messages(29)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.`object`.v2alpha.`object`.Details
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.user.v2alpha.user_service.SetPasswordResponse(
    details = _root_.scala.None
  )
  implicit class SetPasswordResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.user_service.SetPasswordResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.user.v2alpha.user_service.SetPasswordResponse](_l) {
    def details: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.`object`.v2alpha.`object`.Details] = field(_.getDetails)((c_, f_) => c_.copy(details = Option(f_)))
    def optionalDetails: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details]] = field(_.details)((c_, f_) => c_.copy(details = f_))
  }
  final val DETAILS_FIELD_NUMBER = 1
  def of(
    details: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details]
  ): _root_.com.zitadel.user.v2alpha.user_service.SetPasswordResponse = _root_.com.zitadel.user.v2alpha.user_service.SetPasswordResponse(
    details
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.user.v2alpha.SetPasswordResponse])
}

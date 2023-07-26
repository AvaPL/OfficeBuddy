// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

@SerialVersionUID(0L)
final case class GetDefaultPasswordChangeMessageTextResponse(
    customText: _root_.scala.Option[com.zitadel.text.v1.text.MessageCustomText] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[GetDefaultPasswordChangeMessageTextResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (customText.isDefined) {
        val __value = customText.get
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
      customText.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getCustomText: com.zitadel.text.v1.text.MessageCustomText = customText.getOrElse(com.zitadel.text.v1.text.MessageCustomText.defaultInstance)
    def clearCustomText: GetDefaultPasswordChangeMessageTextResponse = copy(customText = _root_.scala.None)
    def withCustomText(__v: com.zitadel.text.v1.text.MessageCustomText): GetDefaultPasswordChangeMessageTextResponse = copy(customText = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => customText.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => customText.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse.type = com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.GetDefaultPasswordChangeMessageTextResponse])
}

object GetDefaultPasswordChangeMessageTextResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse = {
    var __customText: _root_.scala.Option[com.zitadel.text.v1.text.MessageCustomText] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __customText = Option(__customText.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.text.v1.text.MessageCustomText](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse(
        customText = __customText,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse(
        customText = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.text.v1.text.MessageCustomText]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(457)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(457)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.text.v1.text.MessageCustomText
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse(
    customText = _root_.scala.None
  )
  implicit class GetDefaultPasswordChangeMessageTextResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse](_l) {
    def customText: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.text.v1.text.MessageCustomText] = field(_.getCustomText)((c_, f_) => c_.copy(customText = Option(f_)))
    def optionalCustomText: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.text.v1.text.MessageCustomText]] = field(_.customText)((c_, f_) => c_.copy(customText = f_))
  }
  final val CUSTOM_TEXT_FIELD_NUMBER = 1
  def of(
    customText: _root_.scala.Option[com.zitadel.text.v1.text.MessageCustomText]
  ): _root_.com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse = _root_.com.zitadel.management.v1.management.GetDefaultPasswordChangeMessageTextResponse(
    customText
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.GetDefaultPasswordChangeMessageTextResponse])
}

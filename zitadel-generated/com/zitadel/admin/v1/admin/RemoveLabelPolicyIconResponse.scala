// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.admin.v1.admin

@SerialVersionUID(0L)
final case class RemoveLabelPolicyIconResponse(
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[RemoveLabelPolicyIconResponse] {
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
    def getDetails: com.zitadel.v1.`object`.ObjectDetails = details.getOrElse(com.zitadel.v1.`object`.ObjectDetails.defaultInstance)
    def clearDetails: RemoveLabelPolicyIconResponse = copy(details = _root_.scala.None)
    def withDetails(__v: com.zitadel.v1.`object`.ObjectDetails): RemoveLabelPolicyIconResponse = copy(details = Option(__v))
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
    def companion: com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse.type = com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.admin.v1.RemoveLabelPolicyIconResponse])
}

object RemoveLabelPolicyIconResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse = {
    var __details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __details = Option(__details.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.v1.`object`.ObjectDetails](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse(
        details = __details,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse(
        details = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = AdminProto.javaDescriptor.getMessageTypes().get(179)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = AdminProto.scalaDescriptor.messages(179)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.v1.`object`.ObjectDetails
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse(
    details = _root_.scala.None
  )
  implicit class RemoveLabelPolicyIconResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse](_l) {
    def details: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.`object`.ObjectDetails] = field(_.getDetails)((c_, f_) => c_.copy(details = Option(f_)))
    def optionalDetails: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]] = field(_.details)((c_, f_) => c_.copy(details = f_))
  }
  final val DETAILS_FIELD_NUMBER = 1
  def of(
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]
  ): _root_.com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse = _root_.com.zitadel.admin.v1.admin.RemoveLabelPolicyIconResponse(
    details
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.admin.v1.RemoveLabelPolicyIconResponse])
}

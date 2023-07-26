// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.admin.v1.admin

/** @param result
  *  TODO: list details
  */
@SerialVersionUID(0L)
final case class ListFailedEventsResponse(
    result: _root_.scala.Seq[com.zitadel.admin.v1.admin.FailedEvent] = _root_.scala.Seq.empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[ListFailedEventsResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      result.foreach { __item =>
        val __value = __item
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      }
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
      result.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def clearResult = copy(result = _root_.scala.Seq.empty)
    def addResult(__vs: com.zitadel.admin.v1.admin.FailedEvent *): ListFailedEventsResponse = addAllResult(__vs)
    def addAllResult(__vs: Iterable[com.zitadel.admin.v1.admin.FailedEvent]): ListFailedEventsResponse = copy(result = result ++ __vs)
    def withResult(__v: _root_.scala.Seq[com.zitadel.admin.v1.admin.FailedEvent]): ListFailedEventsResponse = copy(result = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => result
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PRepeated(result.iterator.map(_.toPMessage).toVector)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.admin.v1.admin.ListFailedEventsResponse.type = com.zitadel.admin.v1.admin.ListFailedEventsResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.admin.v1.ListFailedEventsResponse])
}

object ListFailedEventsResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.ListFailedEventsResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.ListFailedEventsResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.admin.v1.admin.ListFailedEventsResponse = {
    val __result: _root_.scala.collection.immutable.VectorBuilder[com.zitadel.admin.v1.admin.FailedEvent] = new _root_.scala.collection.immutable.VectorBuilder[com.zitadel.admin.v1.admin.FailedEvent]
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __result += _root_.scalapb.LiteParser.readMessage[com.zitadel.admin.v1.admin.FailedEvent](_input__)
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.admin.v1.admin.ListFailedEventsResponse(
        result = __result.result(),
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.admin.v1.admin.ListFailedEventsResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.admin.v1.admin.ListFailedEventsResponse(
        result = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Seq[com.zitadel.admin.v1.admin.FailedEvent]]).getOrElse(_root_.scala.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = AdminProto.javaDescriptor.getMessageTypes().get(305)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = AdminProto.scalaDescriptor.messages(305)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.admin.v1.admin.FailedEvent
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.admin.v1.admin.ListFailedEventsResponse(
    result = _root_.scala.Seq.empty
  )
  implicit class ListFailedEventsResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.admin.v1.admin.ListFailedEventsResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.admin.v1.admin.ListFailedEventsResponse](_l) {
    def result: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[com.zitadel.admin.v1.admin.FailedEvent]] = field(_.result)((c_, f_) => c_.copy(result = f_))
  }
  final val RESULT_FIELD_NUMBER = 1
  def of(
    result: _root_.scala.Seq[com.zitadel.admin.v1.admin.FailedEvent]
  ): _root_.com.zitadel.admin.v1.admin.ListFailedEventsResponse = _root_.com.zitadel.admin.v1.admin.ListFailedEventsResponse(
    result
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.admin.v1.ListFailedEventsResponse])
}

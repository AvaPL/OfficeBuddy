// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.event.v1.event

@SerialVersionUID(0L)
final case class Event(
    editor: _root_.scala.Option[com.zitadel.event.v1.event.Editor] = _root_.scala.None,
    aggregate: _root_.scala.Option[com.zitadel.event.v1.event.Aggregate] = _root_.scala.None,
    sequence: _root_.scala.Long = 0L,
    creationDate: _root_.scala.Option[com.google.protobuf.timestamp.Timestamp] = _root_.scala.None,
    payload: _root_.scala.Option[com.google.protobuf.struct.Struct] = _root_.scala.None,
    `type`: _root_.scala.Option[com.zitadel.event.v1.event.EventType] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[Event] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (editor.isDefined) {
        val __value = editor.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (aggregate.isDefined) {
        val __value = aggregate.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      
      {
        val __value = sequence
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeUInt64Size(3, __value)
        }
      };
      if (creationDate.isDefined) {
        val __value = creationDate.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (payload.isDefined) {
        val __value = payload.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (`type`.isDefined) {
        val __value = `type`.get
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
      editor.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      aggregate.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      {
        val __v = sequence
        if (__v != 0L) {
          _output__.writeUInt64(3, __v)
        }
      };
      creationDate.foreach { __v =>
        val __m = __v
        _output__.writeTag(4, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      payload.foreach { __v =>
        val __m = __v
        _output__.writeTag(5, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      `type`.foreach { __v =>
        val __m = __v
        _output__.writeTag(6, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getEditor: com.zitadel.event.v1.event.Editor = editor.getOrElse(com.zitadel.event.v1.event.Editor.defaultInstance)
    def clearEditor: Event = copy(editor = _root_.scala.None)
    def withEditor(__v: com.zitadel.event.v1.event.Editor): Event = copy(editor = Option(__v))
    def getAggregate: com.zitadel.event.v1.event.Aggregate = aggregate.getOrElse(com.zitadel.event.v1.event.Aggregate.defaultInstance)
    def clearAggregate: Event = copy(aggregate = _root_.scala.None)
    def withAggregate(__v: com.zitadel.event.v1.event.Aggregate): Event = copy(aggregate = Option(__v))
    def withSequence(__v: _root_.scala.Long): Event = copy(sequence = __v)
    def getCreationDate: com.google.protobuf.timestamp.Timestamp = creationDate.getOrElse(com.google.protobuf.timestamp.Timestamp.defaultInstance)
    def clearCreationDate: Event = copy(creationDate = _root_.scala.None)
    def withCreationDate(__v: com.google.protobuf.timestamp.Timestamp): Event = copy(creationDate = Option(__v))
    def getPayload: com.google.protobuf.struct.Struct = payload.getOrElse(com.google.protobuf.struct.Struct.defaultInstance)
    def clearPayload: Event = copy(payload = _root_.scala.None)
    def withPayload(__v: com.google.protobuf.struct.Struct): Event = copy(payload = Option(__v))
    def getType: com.zitadel.event.v1.event.EventType = `type`.getOrElse(com.zitadel.event.v1.event.EventType.defaultInstance)
    def clearType: Event = copy(`type` = _root_.scala.None)
    def withType(__v: com.zitadel.event.v1.event.EventType): Event = copy(`type` = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => editor.orNull
        case 2 => aggregate.orNull
        case 3 => {
          val __t = sequence
          if (__t != 0L) __t else null
        }
        case 4 => creationDate.orNull
        case 5 => payload.orNull
        case 6 => `type`.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => editor.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => aggregate.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => _root_.scalapb.descriptors.PLong(sequence)
        case 4 => creationDate.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 5 => payload.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 6 => `type`.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.event.v1.event.Event.type = com.zitadel.event.v1.event.Event
    // @@protoc_insertion_point(GeneratedMessage[zitadel.event.v1.Event])
}

object Event extends scalapb.GeneratedMessageCompanion[com.zitadel.event.v1.event.Event] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.event.v1.event.Event] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.event.v1.event.Event = {
    var __editor: _root_.scala.Option[com.zitadel.event.v1.event.Editor] = _root_.scala.None
    var __aggregate: _root_.scala.Option[com.zitadel.event.v1.event.Aggregate] = _root_.scala.None
    var __sequence: _root_.scala.Long = 0L
    var __creationDate: _root_.scala.Option[com.google.protobuf.timestamp.Timestamp] = _root_.scala.None
    var __payload: _root_.scala.Option[com.google.protobuf.struct.Struct] = _root_.scala.None
    var __type: _root_.scala.Option[com.zitadel.event.v1.event.EventType] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __editor = Option(__editor.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.event.v1.event.Editor](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 18 =>
          __aggregate = Option(__aggregate.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.event.v1.event.Aggregate](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 24 =>
          __sequence = _input__.readUInt64()
        case 34 =>
          __creationDate = Option(__creationDate.fold(_root_.scalapb.LiteParser.readMessage[com.google.protobuf.timestamp.Timestamp](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 42 =>
          __payload = Option(__payload.fold(_root_.scalapb.LiteParser.readMessage[com.google.protobuf.struct.Struct](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 50 =>
          __type = Option(__type.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.event.v1.event.EventType](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.event.v1.event.Event(
        editor = __editor,
        aggregate = __aggregate,
        sequence = __sequence,
        creationDate = __creationDate,
        payload = __payload,
        `type` = __type,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.event.v1.event.Event] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.event.v1.event.Event(
        editor = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.event.v1.event.Editor]]),
        aggregate = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.event.v1.event.Aggregate]]),
        sequence = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Long]).getOrElse(0L),
        creationDate = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).flatMap(_.as[_root_.scala.Option[com.google.protobuf.timestamp.Timestamp]]),
        payload = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).flatMap(_.as[_root_.scala.Option[com.google.protobuf.struct.Struct]]),
        `type` = __fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).flatMap(_.as[_root_.scala.Option[com.zitadel.event.v1.event.EventType]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = EventProto.javaDescriptor.getMessageTypes().get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = EventProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.event.v1.event.Editor
      case 2 => __out = com.zitadel.event.v1.event.Aggregate
      case 4 => __out = com.google.protobuf.timestamp.Timestamp
      case 5 => __out = com.google.protobuf.struct.Struct
      case 6 => __out = com.zitadel.event.v1.event.EventType
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.event.v1.event.Event(
    editor = _root_.scala.None,
    aggregate = _root_.scala.None,
    sequence = 0L,
    creationDate = _root_.scala.None,
    payload = _root_.scala.None,
    `type` = _root_.scala.None
  )
  implicit class EventLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.event.v1.event.Event]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.event.v1.event.Event](_l) {
    def editor: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.event.v1.event.Editor] = field(_.getEditor)((c_, f_) => c_.copy(editor = Option(f_)))
    def optionalEditor: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.event.v1.event.Editor]] = field(_.editor)((c_, f_) => c_.copy(editor = f_))
    def aggregate: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.event.v1.event.Aggregate] = field(_.getAggregate)((c_, f_) => c_.copy(aggregate = Option(f_)))
    def optionalAggregate: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.event.v1.event.Aggregate]] = field(_.aggregate)((c_, f_) => c_.copy(aggregate = f_))
    def sequence: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.sequence)((c_, f_) => c_.copy(sequence = f_))
    def creationDate: _root_.scalapb.lenses.Lens[UpperPB, com.google.protobuf.timestamp.Timestamp] = field(_.getCreationDate)((c_, f_) => c_.copy(creationDate = Option(f_)))
    def optionalCreationDate: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.google.protobuf.timestamp.Timestamp]] = field(_.creationDate)((c_, f_) => c_.copy(creationDate = f_))
    def payload: _root_.scalapb.lenses.Lens[UpperPB, com.google.protobuf.struct.Struct] = field(_.getPayload)((c_, f_) => c_.copy(payload = Option(f_)))
    def optionalPayload: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.google.protobuf.struct.Struct]] = field(_.payload)((c_, f_) => c_.copy(payload = f_))
    def `type`: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.event.v1.event.EventType] = field(_.getType)((c_, f_) => c_.copy(`type` = Option(f_)))
    def optionalType: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.event.v1.event.EventType]] = field(_.`type`)((c_, f_) => c_.copy(`type` = f_))
  }
  final val EDITOR_FIELD_NUMBER = 1
  final val AGGREGATE_FIELD_NUMBER = 2
  final val SEQUENCE_FIELD_NUMBER = 3
  final val CREATION_DATE_FIELD_NUMBER = 4
  final val PAYLOAD_FIELD_NUMBER = 5
  final val TYPE_FIELD_NUMBER = 6
  def of(
    editor: _root_.scala.Option[com.zitadel.event.v1.event.Editor],
    aggregate: _root_.scala.Option[com.zitadel.event.v1.event.Aggregate],
    sequence: _root_.scala.Long,
    creationDate: _root_.scala.Option[com.google.protobuf.timestamp.Timestamp],
    payload: _root_.scala.Option[com.google.protobuf.struct.Struct],
    `type`: _root_.scala.Option[com.zitadel.event.v1.event.EventType]
  ): _root_.com.zitadel.event.v1.event.Event = _root_.com.zitadel.event.v1.event.Event(
    editor,
    aggregate,
    sequence,
    creationDate,
    payload,
    `type`
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.event.v1.Event])
}

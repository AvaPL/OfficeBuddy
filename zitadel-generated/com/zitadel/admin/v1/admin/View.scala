// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.admin.v1.admin

/** @param eventTimestamp
  *   The timestamp the event occurred
  */
@SerialVersionUID(0L)
final case class View(
    database: _root_.scala.Predef.String = "",
    viewName: _root_.scala.Predef.String = "",
    processedSequence: _root_.scala.Long = 0L,
    eventTimestamp: _root_.scala.Option[com.google.protobuf.timestamp.Timestamp] = _root_.scala.None,
    lastSuccessfulSpoolerRun: _root_.scala.Option[com.google.protobuf.timestamp.Timestamp] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[View] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = database
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = viewName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      
      {
        val __value = processedSequence
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeUInt64Size(3, __value)
        }
      };
      if (eventTimestamp.isDefined) {
        val __value = eventTimestamp.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (lastSuccessfulSpoolerRun.isDefined) {
        val __value = lastSuccessfulSpoolerRun.get
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
        val __v = database
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = viewName
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      {
        val __v = processedSequence
        if (__v != 0L) {
          _output__.writeUInt64(3, __v)
        }
      };
      eventTimestamp.foreach { __v =>
        val __m = __v
        _output__.writeTag(4, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      lastSuccessfulSpoolerRun.foreach { __v =>
        val __m = __v
        _output__.writeTag(5, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def withDatabase(__v: _root_.scala.Predef.String): View = copy(database = __v)
    def withViewName(__v: _root_.scala.Predef.String): View = copy(viewName = __v)
    def withProcessedSequence(__v: _root_.scala.Long): View = copy(processedSequence = __v)
    def getEventTimestamp: com.google.protobuf.timestamp.Timestamp = eventTimestamp.getOrElse(com.google.protobuf.timestamp.Timestamp.defaultInstance)
    def clearEventTimestamp: View = copy(eventTimestamp = _root_.scala.None)
    def withEventTimestamp(__v: com.google.protobuf.timestamp.Timestamp): View = copy(eventTimestamp = Option(__v))
    def getLastSuccessfulSpoolerRun: com.google.protobuf.timestamp.Timestamp = lastSuccessfulSpoolerRun.getOrElse(com.google.protobuf.timestamp.Timestamp.defaultInstance)
    def clearLastSuccessfulSpoolerRun: View = copy(lastSuccessfulSpoolerRun = _root_.scala.None)
    def withLastSuccessfulSpoolerRun(__v: com.google.protobuf.timestamp.Timestamp): View = copy(lastSuccessfulSpoolerRun = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = database
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = viewName
          if (__t != "") __t else null
        }
        case 3 => {
          val __t = processedSequence
          if (__t != 0L) __t else null
        }
        case 4 => eventTimestamp.orNull
        case 5 => lastSuccessfulSpoolerRun.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(database)
        case 2 => _root_.scalapb.descriptors.PString(viewName)
        case 3 => _root_.scalapb.descriptors.PLong(processedSequence)
        case 4 => eventTimestamp.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 5 => lastSuccessfulSpoolerRun.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.admin.v1.admin.View.type = com.zitadel.admin.v1.admin.View
    // @@protoc_insertion_point(GeneratedMessage[zitadel.admin.v1.View])
}

object View extends scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.View] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.View] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.admin.v1.admin.View = {
    var __database: _root_.scala.Predef.String = ""
    var __viewName: _root_.scala.Predef.String = ""
    var __processedSequence: _root_.scala.Long = 0L
    var __eventTimestamp: _root_.scala.Option[com.google.protobuf.timestamp.Timestamp] = _root_.scala.None
    var __lastSuccessfulSpoolerRun: _root_.scala.Option[com.google.protobuf.timestamp.Timestamp] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __database = _input__.readStringRequireUtf8()
        case 18 =>
          __viewName = _input__.readStringRequireUtf8()
        case 24 =>
          __processedSequence = _input__.readUInt64()
        case 34 =>
          __eventTimestamp = Option(__eventTimestamp.fold(_root_.scalapb.LiteParser.readMessage[com.google.protobuf.timestamp.Timestamp](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 42 =>
          __lastSuccessfulSpoolerRun = Option(__lastSuccessfulSpoolerRun.fold(_root_.scalapb.LiteParser.readMessage[com.google.protobuf.timestamp.Timestamp](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.admin.v1.admin.View(
        database = __database,
        viewName = __viewName,
        processedSequence = __processedSequence,
        eventTimestamp = __eventTimestamp,
        lastSuccessfulSpoolerRun = __lastSuccessfulSpoolerRun,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.admin.v1.admin.View] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.admin.v1.admin.View(
        database = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        viewName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        processedSequence = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Long]).getOrElse(0L),
        eventTimestamp = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).flatMap(_.as[_root_.scala.Option[com.google.protobuf.timestamp.Timestamp]]),
        lastSuccessfulSpoolerRun = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).flatMap(_.as[_root_.scala.Option[com.google.protobuf.timestamp.Timestamp]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = AdminProto.javaDescriptor.getMessageTypes().get(308)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = AdminProto.scalaDescriptor.messages(308)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 4 => __out = com.google.protobuf.timestamp.Timestamp
      case 5 => __out = com.google.protobuf.timestamp.Timestamp
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.admin.v1.admin.View(
    database = "",
    viewName = "",
    processedSequence = 0L,
    eventTimestamp = _root_.scala.None,
    lastSuccessfulSpoolerRun = _root_.scala.None
  )
  implicit class ViewLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.admin.v1.admin.View]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.admin.v1.admin.View](_l) {
    def database: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.database)((c_, f_) => c_.copy(database = f_))
    def viewName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.viewName)((c_, f_) => c_.copy(viewName = f_))
    def processedSequence: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.processedSequence)((c_, f_) => c_.copy(processedSequence = f_))
    def eventTimestamp: _root_.scalapb.lenses.Lens[UpperPB, com.google.protobuf.timestamp.Timestamp] = field(_.getEventTimestamp)((c_, f_) => c_.copy(eventTimestamp = Option(f_)))
    def optionalEventTimestamp: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.google.protobuf.timestamp.Timestamp]] = field(_.eventTimestamp)((c_, f_) => c_.copy(eventTimestamp = f_))
    def lastSuccessfulSpoolerRun: _root_.scalapb.lenses.Lens[UpperPB, com.google.protobuf.timestamp.Timestamp] = field(_.getLastSuccessfulSpoolerRun)((c_, f_) => c_.copy(lastSuccessfulSpoolerRun = Option(f_)))
    def optionalLastSuccessfulSpoolerRun: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.google.protobuf.timestamp.Timestamp]] = field(_.lastSuccessfulSpoolerRun)((c_, f_) => c_.copy(lastSuccessfulSpoolerRun = f_))
  }
  final val DATABASE_FIELD_NUMBER = 1
  final val VIEW_NAME_FIELD_NUMBER = 2
  final val PROCESSED_SEQUENCE_FIELD_NUMBER = 3
  final val EVENT_TIMESTAMP_FIELD_NUMBER = 4
  final val LAST_SUCCESSFUL_SPOOLER_RUN_FIELD_NUMBER = 5
  def of(
    database: _root_.scala.Predef.String,
    viewName: _root_.scala.Predef.String,
    processedSequence: _root_.scala.Long,
    eventTimestamp: _root_.scala.Option[com.google.protobuf.timestamp.Timestamp],
    lastSuccessfulSpoolerRun: _root_.scala.Option[com.google.protobuf.timestamp.Timestamp]
  ): _root_.com.zitadel.admin.v1.admin.View = _root_.com.zitadel.admin.v1.admin.View(
    database,
    viewName,
    processedSequence,
    eventTimestamp,
    lastSuccessfulSpoolerRun
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.admin.v1.View])
}

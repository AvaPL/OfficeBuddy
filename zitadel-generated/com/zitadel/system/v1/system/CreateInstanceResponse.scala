// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.system.v1.system

@SerialVersionUID(0L)
final case class CreateInstanceResponse(
    instanceId: _root_.scala.Predef.String = "",
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None,
    pat: _root_.scala.Predef.String = "",
    machineKey: _root_.com.google.protobuf.ByteString = _root_.com.google.protobuf.ByteString.EMPTY,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[CreateInstanceResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = instanceId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      if (details.isDefined) {
        val __value = details.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      
      {
        val __value = pat
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
        }
      };
      
      {
        val __value = machineKey
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(4, __value)
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
        val __v = instanceId
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      details.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      {
        val __v = pat
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
      {
        val __v = machineKey
        if (!__v.isEmpty) {
          _output__.writeBytes(4, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withInstanceId(__v: _root_.scala.Predef.String): CreateInstanceResponse = copy(instanceId = __v)
    def getDetails: com.zitadel.v1.`object`.ObjectDetails = details.getOrElse(com.zitadel.v1.`object`.ObjectDetails.defaultInstance)
    def clearDetails: CreateInstanceResponse = copy(details = _root_.scala.None)
    def withDetails(__v: com.zitadel.v1.`object`.ObjectDetails): CreateInstanceResponse = copy(details = Option(__v))
    def withPat(__v: _root_.scala.Predef.String): CreateInstanceResponse = copy(pat = __v)
    def withMachineKey(__v: _root_.com.google.protobuf.ByteString): CreateInstanceResponse = copy(machineKey = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = instanceId
          if (__t != "") __t else null
        }
        case 2 => details.orNull
        case 3 => {
          val __t = pat
          if (__t != "") __t else null
        }
        case 4 => {
          val __t = machineKey
          if (__t != _root_.com.google.protobuf.ByteString.EMPTY) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(instanceId)
        case 2 => details.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => _root_.scalapb.descriptors.PString(pat)
        case 4 => _root_.scalapb.descriptors.PByteString(machineKey)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.system.v1.system.CreateInstanceResponse.type = com.zitadel.system.v1.system.CreateInstanceResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.system.v1.CreateInstanceResponse])
}

object CreateInstanceResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.system.v1.system.CreateInstanceResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.system.v1.system.CreateInstanceResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.system.v1.system.CreateInstanceResponse = {
    var __instanceId: _root_.scala.Predef.String = ""
    var __details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None
    var __pat: _root_.scala.Predef.String = ""
    var __machineKey: _root_.com.google.protobuf.ByteString = _root_.com.google.protobuf.ByteString.EMPTY
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __instanceId = _input__.readStringRequireUtf8()
        case 18 =>
          __details = Option(__details.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.v1.`object`.ObjectDetails](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 26 =>
          __pat = _input__.readStringRequireUtf8()
        case 34 =>
          __machineKey = _input__.readBytes()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.system.v1.system.CreateInstanceResponse(
        instanceId = __instanceId,
        details = __details,
        pat = __pat,
        machineKey = __machineKey,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.system.v1.system.CreateInstanceResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.system.v1.system.CreateInstanceResponse(
        instanceId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        details = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]]),
        pat = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        machineKey = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.com.google.protobuf.ByteString]).getOrElse(_root_.com.google.protobuf.ByteString.EMPTY)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SystemProto.javaDescriptor.getMessageTypes().get(9)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SystemProto.scalaDescriptor.messages(9)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 2 => __out = com.zitadel.v1.`object`.ObjectDetails
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.system.v1.system.CreateInstanceResponse(
    instanceId = "",
    details = _root_.scala.None,
    pat = "",
    machineKey = _root_.com.google.protobuf.ByteString.EMPTY
  )
  implicit class CreateInstanceResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.system.v1.system.CreateInstanceResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.system.v1.system.CreateInstanceResponse](_l) {
    def instanceId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.instanceId)((c_, f_) => c_.copy(instanceId = f_))
    def details: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.`object`.ObjectDetails] = field(_.getDetails)((c_, f_) => c_.copy(details = Option(f_)))
    def optionalDetails: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]] = field(_.details)((c_, f_) => c_.copy(details = f_))
    def pat: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.pat)((c_, f_) => c_.copy(pat = f_))
    def machineKey: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.machineKey)((c_, f_) => c_.copy(machineKey = f_))
  }
  final val INSTANCE_ID_FIELD_NUMBER = 1
  final val DETAILS_FIELD_NUMBER = 2
  final val PAT_FIELD_NUMBER = 3
  final val MACHINE_KEY_FIELD_NUMBER = 4
  def of(
    instanceId: _root_.scala.Predef.String,
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails],
    pat: _root_.scala.Predef.String,
    machineKey: _root_.com.google.protobuf.ByteString
  ): _root_.com.zitadel.system.v1.system.CreateInstanceResponse = _root_.com.zitadel.system.v1.system.CreateInstanceResponse(
    instanceId,
    details,
    pat,
    machineKey
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.system.v1.CreateInstanceResponse])
}

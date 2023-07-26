// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.system.v1.system

@SerialVersionUID(0L)
final case class GetInstanceResponse(
    instance: _root_.scala.Option[com.zitadel.instance.v1.instance.InstanceDetail] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[GetInstanceResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (instance.isDefined) {
        val __value = instance.get
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
      instance.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getInstance: com.zitadel.instance.v1.instance.InstanceDetail = instance.getOrElse(com.zitadel.instance.v1.instance.InstanceDetail.defaultInstance)
    def clearInstance: GetInstanceResponse = copy(instance = _root_.scala.None)
    def withInstance(__v: com.zitadel.instance.v1.instance.InstanceDetail): GetInstanceResponse = copy(instance = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => instance.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => instance.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.system.v1.system.GetInstanceResponse.type = com.zitadel.system.v1.system.GetInstanceResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.system.v1.GetInstanceResponse])
}

object GetInstanceResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.system.v1.system.GetInstanceResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.system.v1.system.GetInstanceResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.system.v1.system.GetInstanceResponse = {
    var __instance: _root_.scala.Option[com.zitadel.instance.v1.instance.InstanceDetail] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __instance = Option(__instance.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.instance.v1.instance.InstanceDetail](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.system.v1.system.GetInstanceResponse(
        instance = __instance,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.system.v1.system.GetInstanceResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.system.v1.system.GetInstanceResponse(
        instance = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.instance.v1.instance.InstanceDetail]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SystemProto.javaDescriptor.getMessageTypes().get(5)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SystemProto.scalaDescriptor.messages(5)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.instance.v1.instance.InstanceDetail
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.system.v1.system.GetInstanceResponse(
    instance = _root_.scala.None
  )
  implicit class GetInstanceResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.system.v1.system.GetInstanceResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.system.v1.system.GetInstanceResponse](_l) {
    def instance: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.instance.v1.instance.InstanceDetail] = field(_.getInstance)((c_, f_) => c_.copy(instance = Option(f_)))
    def optionalInstance: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.instance.v1.instance.InstanceDetail]] = field(_.instance)((c_, f_) => c_.copy(instance = f_))
  }
  final val INSTANCE_FIELD_NUMBER = 1
  def of(
    instance: _root_.scala.Option[com.zitadel.instance.v1.instance.InstanceDetail]
  ): _root_.com.zitadel.system.v1.system.GetInstanceResponse = _root_.com.zitadel.system.v1.system.GetInstanceResponse(
    instance
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.system.v1.GetInstanceResponse])
}

// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

@SerialVersionUID(0L)
final case class UpdateCustomPasswordAgePolicyRequest(
    maxAgeDays: _root_.scala.Int = 0,
    expireWarnDays: _root_.scala.Int = 0,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[UpdateCustomPasswordAgePolicyRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = maxAgeDays
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeUInt32Size(1, __value)
        }
      };
      
      {
        val __value = expireWarnDays
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeUInt32Size(2, __value)
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
        val __v = maxAgeDays
        if (__v != 0) {
          _output__.writeUInt32(1, __v)
        }
      };
      {
        val __v = expireWarnDays
        if (__v != 0) {
          _output__.writeUInt32(2, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withMaxAgeDays(__v: _root_.scala.Int): UpdateCustomPasswordAgePolicyRequest = copy(maxAgeDays = __v)
    def withExpireWarnDays(__v: _root_.scala.Int): UpdateCustomPasswordAgePolicyRequest = copy(expireWarnDays = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = maxAgeDays
          if (__t != 0) __t else null
        }
        case 2 => {
          val __t = expireWarnDays
          if (__t != 0) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(maxAgeDays)
        case 2 => _root_.scalapb.descriptors.PInt(expireWarnDays)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest.type = com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.UpdateCustomPasswordAgePolicyRequest])
}

object UpdateCustomPasswordAgePolicyRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest = {
    var __maxAgeDays: _root_.scala.Int = 0
    var __expireWarnDays: _root_.scala.Int = 0
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 8 =>
          __maxAgeDays = _input__.readUInt32()
        case 16 =>
          __expireWarnDays = _input__.readUInt32()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest(
        maxAgeDays = __maxAgeDays,
        expireWarnDays = __expireWarnDays,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest(
        maxAgeDays = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Int]).getOrElse(0),
        expireWarnDays = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Int]).getOrElse(0)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(340)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(340)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest(
    maxAgeDays = 0,
    expireWarnDays = 0
  )
  implicit class UpdateCustomPasswordAgePolicyRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest](_l) {
    def maxAgeDays: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.maxAgeDays)((c_, f_) => c_.copy(maxAgeDays = f_))
    def expireWarnDays: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.expireWarnDays)((c_, f_) => c_.copy(expireWarnDays = f_))
  }
  final val MAX_AGE_DAYS_FIELD_NUMBER = 1
  final val EXPIRE_WARN_DAYS_FIELD_NUMBER = 2
  def of(
    maxAgeDays: _root_.scala.Int,
    expireWarnDays: _root_.scala.Int
  ): _root_.com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest = _root_.com.zitadel.management.v1.management.UpdateCustomPasswordAgePolicyRequest(
    maxAgeDays,
    expireWarnDays
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.UpdateCustomPasswordAgePolicyRequest])
}

// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

/** This is an empty request
  */
@SerialVersionUID(0L)
final case class ResetLockoutPolicyToDefaultRequest(
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[ResetLockoutPolicyToDefaultRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
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
      unknownFields.writeTo(_output__)
    }
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = throw new MatchError(__fieldNumber)
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = throw new MatchError(__field)
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest.type = com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.ResetLockoutPolicyToDefaultRequest])
}

object ResetLockoutPolicyToDefaultRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest = {
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest(
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest(
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(352)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(352)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest(
  )
  implicit class ResetLockoutPolicyToDefaultRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest](_l) {
  }
  def of(
  ): _root_.com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest = _root_.com.zitadel.management.v1.management.ResetLockoutPolicyToDefaultRequest(
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.ResetLockoutPolicyToDefaultRequest])
}

// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.auth.v1.auth

/** This is an empty request
  */
@SerialVersionUID(0L)
final case class AddMyAuthFactorOTPRequest(
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[AddMyAuthFactorOTPRequest] {
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
    def companion: com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest.type = com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.auth.v1.AddMyAuthFactorOTPRequest])
}

object AddMyAuthFactorOTPRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest = {
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
    com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest(
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest(
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = AuthProto.javaDescriptor.getMessageTypes().get(68)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = AuthProto.scalaDescriptor.messages(68)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest(
  )
  implicit class AddMyAuthFactorOTPRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest](_l) {
  }
  def of(
  ): _root_.com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest = _root_.com.zitadel.auth.v1.auth.AddMyAuthFactorOTPRequest(
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.auth.v1.AddMyAuthFactorOTPRequest])
}

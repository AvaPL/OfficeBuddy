// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

/** @param globalOrgId
  *  deprecated: use default_org_id instead
  */
@SerialVersionUID(0L)
final case class GetIAMResponse(
    globalOrgId: _root_.scala.Predef.String = "",
    iamProjectId: _root_.scala.Predef.String = "",
    defaultOrgId: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[GetIAMResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = globalOrgId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = iamProjectId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      
      {
        val __value = defaultOrgId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
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
        val __v = globalOrgId
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = iamProjectId
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      {
        val __v = defaultOrgId
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withGlobalOrgId(__v: _root_.scala.Predef.String): GetIAMResponse = copy(globalOrgId = __v)
    def withIamProjectId(__v: _root_.scala.Predef.String): GetIAMResponse = copy(iamProjectId = __v)
    def withDefaultOrgId(__v: _root_.scala.Predef.String): GetIAMResponse = copy(defaultOrgId = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = globalOrgId
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = iamProjectId
          if (__t != "") __t else null
        }
        case 3 => {
          val __t = defaultOrgId
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(globalOrgId)
        case 2 => _root_.scalapb.descriptors.PString(iamProjectId)
        case 3 => _root_.scalapb.descriptors.PString(defaultOrgId)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.GetIAMResponse.type = com.zitadel.management.v1.management.GetIAMResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.GetIAMResponse])
}

object GetIAMResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.GetIAMResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.GetIAMResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.GetIAMResponse = {
    var __globalOrgId: _root_.scala.Predef.String = ""
    var __iamProjectId: _root_.scala.Predef.String = ""
    var __defaultOrgId: _root_.scala.Predef.String = ""
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __globalOrgId = _input__.readStringRequireUtf8()
        case 18 =>
          __iamProjectId = _input__.readStringRequireUtf8()
        case 26 =>
          __defaultOrgId = _input__.readStringRequireUtf8()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.GetIAMResponse(
        globalOrgId = __globalOrgId,
        iamProjectId = __iamProjectId,
        defaultOrgId = __defaultOrgId,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.GetIAMResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.GetIAMResponse(
        globalOrgId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        iamProjectId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        defaultOrgId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(5)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(5)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.GetIAMResponse(
    globalOrgId = "",
    iamProjectId = "",
    defaultOrgId = ""
  )
  implicit class GetIAMResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.GetIAMResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.GetIAMResponse](_l) {
    def globalOrgId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.globalOrgId)((c_, f_) => c_.copy(globalOrgId = f_))
    def iamProjectId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.iamProjectId)((c_, f_) => c_.copy(iamProjectId = f_))
    def defaultOrgId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.defaultOrgId)((c_, f_) => c_.copy(defaultOrgId = f_))
  }
  final val GLOBAL_ORG_ID_FIELD_NUMBER = 1
  final val IAM_PROJECT_ID_FIELD_NUMBER = 2
  final val DEFAULT_ORG_ID_FIELD_NUMBER = 3
  def of(
    globalOrgId: _root_.scala.Predef.String,
    iamProjectId: _root_.scala.Predef.String,
    defaultOrgId: _root_.scala.Predef.String
  ): _root_.com.zitadel.management.v1.management.GetIAMResponse = _root_.com.zitadel.management.v1.management.GetIAMResponse(
    globalOrgId,
    iamProjectId,
    defaultOrgId
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.GetIAMResponse])
}

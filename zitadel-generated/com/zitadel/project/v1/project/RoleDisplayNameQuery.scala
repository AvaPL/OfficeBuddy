// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.project.v1.project

@SerialVersionUID(0L)
final case class RoleDisplayNameQuery(
    displayName: _root_.scala.Predef.String = "",
    method: com.zitadel.v1.`object`.TextQueryMethod = com.zitadel.v1.`object`.TextQueryMethod.TEXT_QUERY_METHOD_EQUALS,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[RoleDisplayNameQuery] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = displayName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = method.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(2, __value)
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
        val __v = displayName
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = method.value
        if (__v != 0) {
          _output__.writeEnum(2, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withDisplayName(__v: _root_.scala.Predef.String): RoleDisplayNameQuery = copy(displayName = __v)
    def withMethod(__v: com.zitadel.v1.`object`.TextQueryMethod): RoleDisplayNameQuery = copy(method = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = displayName
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = method.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(displayName)
        case 2 => _root_.scalapb.descriptors.PEnum(method.scalaValueDescriptor)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.project.v1.project.RoleDisplayNameQuery.type = com.zitadel.project.v1.project.RoleDisplayNameQuery
    // @@protoc_insertion_point(GeneratedMessage[zitadel.project.v1.RoleDisplayNameQuery])
}

object RoleDisplayNameQuery extends scalapb.GeneratedMessageCompanion[com.zitadel.project.v1.project.RoleDisplayNameQuery] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.project.v1.project.RoleDisplayNameQuery] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.project.v1.project.RoleDisplayNameQuery = {
    var __displayName: _root_.scala.Predef.String = ""
    var __method: com.zitadel.v1.`object`.TextQueryMethod = com.zitadel.v1.`object`.TextQueryMethod.TEXT_QUERY_METHOD_EQUALS
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __displayName = _input__.readStringRequireUtf8()
        case 16 =>
          __method = com.zitadel.v1.`object`.TextQueryMethod.fromValue(_input__.readEnum())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.project.v1.project.RoleDisplayNameQuery(
        displayName = __displayName,
        method = __method,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.project.v1.project.RoleDisplayNameQuery] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.project.v1.project.RoleDisplayNameQuery(
        displayName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        method = com.zitadel.v1.`object`.TextQueryMethod.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.v1.`object`.TextQueryMethod.TEXT_QUERY_METHOD_EQUALS.scalaValueDescriptor).number)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ProjectProto.javaDescriptor.getMessageTypes().get(8)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ProjectProto.scalaDescriptor.messages(8)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 2 => com.zitadel.v1.`object`.TextQueryMethod
    }
  }
  lazy val defaultInstance = com.zitadel.project.v1.project.RoleDisplayNameQuery(
    displayName = "",
    method = com.zitadel.v1.`object`.TextQueryMethod.TEXT_QUERY_METHOD_EQUALS
  )
  implicit class RoleDisplayNameQueryLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.RoleDisplayNameQuery]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.project.v1.project.RoleDisplayNameQuery](_l) {
    def displayName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.displayName)((c_, f_) => c_.copy(displayName = f_))
    def method: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.`object`.TextQueryMethod] = field(_.method)((c_, f_) => c_.copy(method = f_))
  }
  final val DISPLAY_NAME_FIELD_NUMBER = 1
  final val METHOD_FIELD_NUMBER = 2
  def of(
    displayName: _root_.scala.Predef.String,
    method: com.zitadel.v1.`object`.TextQueryMethod
  ): _root_.com.zitadel.project.v1.project.RoleDisplayNameQuery = _root_.com.zitadel.project.v1.project.RoleDisplayNameQuery(
    displayName,
    method
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.project.v1.RoleDisplayNameQuery])
}

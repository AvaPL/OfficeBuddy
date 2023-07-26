// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.v1.options

@SerialVersionUID(0L)
final case class AuthOption(
    permission: _root_.scala.Predef.String = "",
    checkFieldName: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[AuthOption] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = permission
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = checkFieldName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
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
        val __v = permission
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = checkFieldName
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withPermission(__v: _root_.scala.Predef.String): AuthOption = copy(permission = __v)
    def withCheckFieldName(__v: _root_.scala.Predef.String): AuthOption = copy(checkFieldName = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = permission
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = checkFieldName
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(permission)
        case 2 => _root_.scalapb.descriptors.PString(checkFieldName)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.v1.options.AuthOption.type = com.zitadel.v1.options.AuthOption
    // @@protoc_insertion_point(GeneratedMessage[zitadel.v1.AuthOption])
}

object AuthOption extends scalapb.GeneratedMessageCompanion[com.zitadel.v1.options.AuthOption] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.v1.options.AuthOption] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.v1.options.AuthOption = {
    var __permission: _root_.scala.Predef.String = ""
    var __checkFieldName: _root_.scala.Predef.String = ""
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __permission = _input__.readStringRequireUtf8()
        case 18 =>
          __checkFieldName = _input__.readStringRequireUtf8()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.v1.options.AuthOption(
        permission = __permission,
        checkFieldName = __checkFieldName,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.v1.options.AuthOption] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.v1.options.AuthOption(
        permission = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        checkFieldName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = OptionsProto.javaDescriptor.getMessageTypes().get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = OptionsProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.v1.options.AuthOption(
    permission = "",
    checkFieldName = ""
  )
  implicit class AuthOptionLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.options.AuthOption]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.v1.options.AuthOption](_l) {
    def permission: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.permission)((c_, f_) => c_.copy(permission = f_))
    def checkFieldName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.checkFieldName)((c_, f_) => c_.copy(checkFieldName = f_))
  }
  final val PERMISSION_FIELD_NUMBER = 1
  final val CHECK_FIELD_NAME_FIELD_NUMBER = 2
  def of(
    permission: _root_.scala.Predef.String,
    checkFieldName: _root_.scala.Predef.String
  ): _root_.com.zitadel.v1.options.AuthOption = _root_.com.zitadel.v1.options.AuthOption(
    permission,
    checkFieldName
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.v1.AuthOption])
}

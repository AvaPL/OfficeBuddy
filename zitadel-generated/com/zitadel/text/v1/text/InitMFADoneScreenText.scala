// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.text.v1.text

@SerialVersionUID(0L)
final case class InitMFADoneScreenText(
    title: _root_.scala.Predef.String = "",
    description: _root_.scala.Predef.String = "",
    cancelButtonText: _root_.scala.Predef.String = "",
    nextButtonText: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[InitMFADoneScreenText] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = title
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = description
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      
      {
        val __value = cancelButtonText
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
        }
      };
      
      {
        val __value = nextButtonText
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
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
        val __v = title
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = description
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      {
        val __v = cancelButtonText
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
      {
        val __v = nextButtonText
        if (!__v.isEmpty) {
          _output__.writeString(4, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withTitle(__v: _root_.scala.Predef.String): InitMFADoneScreenText = copy(title = __v)
    def withDescription(__v: _root_.scala.Predef.String): InitMFADoneScreenText = copy(description = __v)
    def withCancelButtonText(__v: _root_.scala.Predef.String): InitMFADoneScreenText = copy(cancelButtonText = __v)
    def withNextButtonText(__v: _root_.scala.Predef.String): InitMFADoneScreenText = copy(nextButtonText = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = title
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = description
          if (__t != "") __t else null
        }
        case 3 => {
          val __t = cancelButtonText
          if (__t != "") __t else null
        }
        case 4 => {
          val __t = nextButtonText
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(title)
        case 2 => _root_.scalapb.descriptors.PString(description)
        case 3 => _root_.scalapb.descriptors.PString(cancelButtonText)
        case 4 => _root_.scalapb.descriptors.PString(nextButtonText)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.text.v1.text.InitMFADoneScreenText.type = com.zitadel.text.v1.text.InitMFADoneScreenText
    // @@protoc_insertion_point(GeneratedMessage[zitadel.text.v1.InitMFADoneScreenText])
}

object InitMFADoneScreenText extends scalapb.GeneratedMessageCompanion[com.zitadel.text.v1.text.InitMFADoneScreenText] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.text.v1.text.InitMFADoneScreenText] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.text.v1.text.InitMFADoneScreenText = {
    var __title: _root_.scala.Predef.String = ""
    var __description: _root_.scala.Predef.String = ""
    var __cancelButtonText: _root_.scala.Predef.String = ""
    var __nextButtonText: _root_.scala.Predef.String = ""
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __title = _input__.readStringRequireUtf8()
        case 18 =>
          __description = _input__.readStringRequireUtf8()
        case 26 =>
          __cancelButtonText = _input__.readStringRequireUtf8()
        case 34 =>
          __nextButtonText = _input__.readStringRequireUtf8()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.text.v1.text.InitMFADoneScreenText(
        title = __title,
        description = __description,
        cancelButtonText = __cancelButtonText,
        nextButtonText = __nextButtonText,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.text.v1.text.InitMFADoneScreenText] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.text.v1.text.InitMFADoneScreenText(
        title = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        description = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        cancelButtonText = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        nextButtonText = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = TextProto.javaDescriptor.getMessageTypes().get(16)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = TextProto.scalaDescriptor.messages(16)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.text.v1.text.InitMFADoneScreenText(
    title = "",
    description = "",
    cancelButtonText = "",
    nextButtonText = ""
  )
  implicit class InitMFADoneScreenTextLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.text.v1.text.InitMFADoneScreenText]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.text.v1.text.InitMFADoneScreenText](_l) {
    def title: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.title)((c_, f_) => c_.copy(title = f_))
    def description: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.description)((c_, f_) => c_.copy(description = f_))
    def cancelButtonText: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.cancelButtonText)((c_, f_) => c_.copy(cancelButtonText = f_))
    def nextButtonText: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.nextButtonText)((c_, f_) => c_.copy(nextButtonText = f_))
  }
  final val TITLE_FIELD_NUMBER = 1
  final val DESCRIPTION_FIELD_NUMBER = 2
  final val CANCEL_BUTTON_TEXT_FIELD_NUMBER = 3
  final val NEXT_BUTTON_TEXT_FIELD_NUMBER = 4
  def of(
    title: _root_.scala.Predef.String,
    description: _root_.scala.Predef.String,
    cancelButtonText: _root_.scala.Predef.String,
    nextButtonText: _root_.scala.Predef.String
  ): _root_.com.zitadel.text.v1.text.InitMFADoneScreenText = _root_.com.zitadel.text.v1.text.InitMFADoneScreenText(
    title,
    description,
    cancelButtonText,
    nextButtonText
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.text.v1.InitMFADoneScreenText])
}

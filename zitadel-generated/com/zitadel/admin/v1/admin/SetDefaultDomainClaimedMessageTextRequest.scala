// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.admin.v1.admin

@SerialVersionUID(0L)
final case class SetDefaultDomainClaimedMessageTextRequest(
    language: _root_.scala.Predef.String = "",
    title: _root_.scala.Predef.String = "",
    preHeader: _root_.scala.Predef.String = "",
    subject: _root_.scala.Predef.String = "",
    greeting: _root_.scala.Predef.String = "",
    text: _root_.scala.Predef.String = "",
    buttonText: _root_.scala.Predef.String = "",
    footerText: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[SetDefaultDomainClaimedMessageTextRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = language
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = title
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      
      {
        val __value = preHeader
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
        }
      };
      
      {
        val __value = subject
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
        }
      };
      
      {
        val __value = greeting
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(5, __value)
        }
      };
      
      {
        val __value = text
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(6, __value)
        }
      };
      
      {
        val __value = buttonText
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(7, __value)
        }
      };
      
      {
        val __value = footerText
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(8, __value)
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
        val __v = language
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = title
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      {
        val __v = preHeader
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
      {
        val __v = subject
        if (!__v.isEmpty) {
          _output__.writeString(4, __v)
        }
      };
      {
        val __v = greeting
        if (!__v.isEmpty) {
          _output__.writeString(5, __v)
        }
      };
      {
        val __v = text
        if (!__v.isEmpty) {
          _output__.writeString(6, __v)
        }
      };
      {
        val __v = buttonText
        if (!__v.isEmpty) {
          _output__.writeString(7, __v)
        }
      };
      {
        val __v = footerText
        if (!__v.isEmpty) {
          _output__.writeString(8, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withLanguage(__v: _root_.scala.Predef.String): SetDefaultDomainClaimedMessageTextRequest = copy(language = __v)
    def withTitle(__v: _root_.scala.Predef.String): SetDefaultDomainClaimedMessageTextRequest = copy(title = __v)
    def withPreHeader(__v: _root_.scala.Predef.String): SetDefaultDomainClaimedMessageTextRequest = copy(preHeader = __v)
    def withSubject(__v: _root_.scala.Predef.String): SetDefaultDomainClaimedMessageTextRequest = copy(subject = __v)
    def withGreeting(__v: _root_.scala.Predef.String): SetDefaultDomainClaimedMessageTextRequest = copy(greeting = __v)
    def withText(__v: _root_.scala.Predef.String): SetDefaultDomainClaimedMessageTextRequest = copy(text = __v)
    def withButtonText(__v: _root_.scala.Predef.String): SetDefaultDomainClaimedMessageTextRequest = copy(buttonText = __v)
    def withFooterText(__v: _root_.scala.Predef.String): SetDefaultDomainClaimedMessageTextRequest = copy(footerText = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = language
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = title
          if (__t != "") __t else null
        }
        case 3 => {
          val __t = preHeader
          if (__t != "") __t else null
        }
        case 4 => {
          val __t = subject
          if (__t != "") __t else null
        }
        case 5 => {
          val __t = greeting
          if (__t != "") __t else null
        }
        case 6 => {
          val __t = text
          if (__t != "") __t else null
        }
        case 7 => {
          val __t = buttonText
          if (__t != "") __t else null
        }
        case 8 => {
          val __t = footerText
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(language)
        case 2 => _root_.scalapb.descriptors.PString(title)
        case 3 => _root_.scalapb.descriptors.PString(preHeader)
        case 4 => _root_.scalapb.descriptors.PString(subject)
        case 5 => _root_.scalapb.descriptors.PString(greeting)
        case 6 => _root_.scalapb.descriptors.PString(text)
        case 7 => _root_.scalapb.descriptors.PString(buttonText)
        case 8 => _root_.scalapb.descriptors.PString(footerText)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest.type = com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.admin.v1.SetDefaultDomainClaimedMessageTextRequest])
}

object SetDefaultDomainClaimedMessageTextRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest = {
    var __language: _root_.scala.Predef.String = ""
    var __title: _root_.scala.Predef.String = ""
    var __preHeader: _root_.scala.Predef.String = ""
    var __subject: _root_.scala.Predef.String = ""
    var __greeting: _root_.scala.Predef.String = ""
    var __text: _root_.scala.Predef.String = ""
    var __buttonText: _root_.scala.Predef.String = ""
    var __footerText: _root_.scala.Predef.String = ""
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __language = _input__.readStringRequireUtf8()
        case 18 =>
          __title = _input__.readStringRequireUtf8()
        case 26 =>
          __preHeader = _input__.readStringRequireUtf8()
        case 34 =>
          __subject = _input__.readStringRequireUtf8()
        case 42 =>
          __greeting = _input__.readStringRequireUtf8()
        case 50 =>
          __text = _input__.readStringRequireUtf8()
        case 58 =>
          __buttonText = _input__.readStringRequireUtf8()
        case 66 =>
          __footerText = _input__.readStringRequireUtf8()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest(
        language = __language,
        title = __title,
        preHeader = __preHeader,
        subject = __subject,
        greeting = __greeting,
        text = __text,
        buttonText = __buttonText,
        footerText = __footerText,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest(
        language = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        title = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        preHeader = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        subject = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        greeting = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        text = __fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        buttonText = __fieldsMap.get(scalaDescriptor.findFieldByNumber(7).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        footerText = __fieldsMap.get(scalaDescriptor.findFieldByNumber(8).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = AdminProto.javaDescriptor.getMessageTypes().get(264)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = AdminProto.scalaDescriptor.messages(264)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest(
    language = "",
    title = "",
    preHeader = "",
    subject = "",
    greeting = "",
    text = "",
    buttonText = "",
    footerText = ""
  )
  implicit class SetDefaultDomainClaimedMessageTextRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest](_l) {
    def language: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.language)((c_, f_) => c_.copy(language = f_))
    def title: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.title)((c_, f_) => c_.copy(title = f_))
    def preHeader: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.preHeader)((c_, f_) => c_.copy(preHeader = f_))
    def subject: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.subject)((c_, f_) => c_.copy(subject = f_))
    def greeting: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.greeting)((c_, f_) => c_.copy(greeting = f_))
    def text: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.text)((c_, f_) => c_.copy(text = f_))
    def buttonText: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.buttonText)((c_, f_) => c_.copy(buttonText = f_))
    def footerText: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.footerText)((c_, f_) => c_.copy(footerText = f_))
  }
  final val LANGUAGE_FIELD_NUMBER = 1
  final val TITLE_FIELD_NUMBER = 2
  final val PRE_HEADER_FIELD_NUMBER = 3
  final val SUBJECT_FIELD_NUMBER = 4
  final val GREETING_FIELD_NUMBER = 5
  final val TEXT_FIELD_NUMBER = 6
  final val BUTTON_TEXT_FIELD_NUMBER = 7
  final val FOOTER_TEXT_FIELD_NUMBER = 8
  def of(
    language: _root_.scala.Predef.String,
    title: _root_.scala.Predef.String,
    preHeader: _root_.scala.Predef.String,
    subject: _root_.scala.Predef.String,
    greeting: _root_.scala.Predef.String,
    text: _root_.scala.Predef.String,
    buttonText: _root_.scala.Predef.String,
    footerText: _root_.scala.Predef.String
  ): _root_.com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest = _root_.com.zitadel.admin.v1.admin.SetDefaultDomainClaimedMessageTextRequest(
    language,
    title,
    preHeader,
    subject,
    greeting,
    text,
    buttonText,
    footerText
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.admin.v1.SetDefaultDomainClaimedMessageTextRequest])
}

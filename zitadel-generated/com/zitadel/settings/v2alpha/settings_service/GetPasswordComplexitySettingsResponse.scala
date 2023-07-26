// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.settings.v2alpha.settings_service

@SerialVersionUID(0L)
final case class GetPasswordComplexitySettingsResponse(
    details: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details] = _root_.scala.None,
    settings: _root_.scala.Option[com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[GetPasswordComplexitySettingsResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (details.isDefined) {
        val __value = details.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (settings.isDefined) {
        val __value = settings.get
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
      details.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      settings.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getDetails: com.zitadel.`object`.v2alpha.`object`.Details = details.getOrElse(com.zitadel.`object`.v2alpha.`object`.Details.defaultInstance)
    def clearDetails: GetPasswordComplexitySettingsResponse = copy(details = _root_.scala.None)
    def withDetails(__v: com.zitadel.`object`.v2alpha.`object`.Details): GetPasswordComplexitySettingsResponse = copy(details = Option(__v))
    def getSettings: com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings = settings.getOrElse(com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings.defaultInstance)
    def clearSettings: GetPasswordComplexitySettingsResponse = copy(settings = _root_.scala.None)
    def withSettings(__v: com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings): GetPasswordComplexitySettingsResponse = copy(settings = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => details.orNull
        case 2 => settings.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => details.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => settings.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse.type = com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.settings.v2alpha.GetPasswordComplexitySettingsResponse])
}

object GetPasswordComplexitySettingsResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse = {
    var __details: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details] = _root_.scala.None
    var __settings: _root_.scala.Option[com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __details = Option(__details.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.`object`.v2alpha.`object`.Details](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 18 =>
          __settings = Option(__settings.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse(
        details = __details,
        settings = __settings,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse(
        details = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details]]),
        settings = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SettingsServiceProto.javaDescriptor.getMessageTypes().get(3)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SettingsServiceProto.scalaDescriptor.messages(3)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.`object`.v2alpha.`object`.Details
      case 2 => __out = com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse(
    details = _root_.scala.None,
    settings = _root_.scala.None
  )
  implicit class GetPasswordComplexitySettingsResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse](_l) {
    def details: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.`object`.v2alpha.`object`.Details] = field(_.getDetails)((c_, f_) => c_.copy(details = Option(f_)))
    def optionalDetails: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details]] = field(_.details)((c_, f_) => c_.copy(details = f_))
    def settings: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings] = field(_.getSettings)((c_, f_) => c_.copy(settings = Option(f_)))
    def optionalSettings: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings]] = field(_.settings)((c_, f_) => c_.copy(settings = f_))
  }
  final val DETAILS_FIELD_NUMBER = 1
  final val SETTINGS_FIELD_NUMBER = 2
  def of(
    details: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Details],
    settings: _root_.scala.Option[com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings]
  ): _root_.com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse = _root_.com.zitadel.settings.v2alpha.settings_service.GetPasswordComplexitySettingsResponse(
    details,
    settings
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.settings.v2alpha.GetPasswordComplexitySettingsResponse])
}

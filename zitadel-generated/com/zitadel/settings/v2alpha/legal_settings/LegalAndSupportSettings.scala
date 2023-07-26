// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.settings.v2alpha.legal_settings

/** @param resourceOwnerType
  *   resource_owner_type returns if the setting is managed on the organization or on the instance
  */
@SerialVersionUID(0L)
final case class LegalAndSupportSettings(
    tosLink: _root_.scala.Predef.String = "",
    privacyPolicyLink: _root_.scala.Predef.String = "",
    helpLink: _root_.scala.Predef.String = "",
    supportEmail: _root_.scala.Predef.String = "",
    resourceOwnerType: com.zitadel.settings.v2alpha.settings.ResourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[LegalAndSupportSettings] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = tosLink
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = privacyPolicyLink
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      
      {
        val __value = helpLink
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
        }
      };
      
      {
        val __value = supportEmail
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
        }
      };
      
      {
        val __value = resourceOwnerType.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(5, __value)
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
        val __v = tosLink
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = privacyPolicyLink
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      {
        val __v = helpLink
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
      {
        val __v = supportEmail
        if (!__v.isEmpty) {
          _output__.writeString(4, __v)
        }
      };
      {
        val __v = resourceOwnerType.value
        if (__v != 0) {
          _output__.writeEnum(5, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withTosLink(__v: _root_.scala.Predef.String): LegalAndSupportSettings = copy(tosLink = __v)
    def withPrivacyPolicyLink(__v: _root_.scala.Predef.String): LegalAndSupportSettings = copy(privacyPolicyLink = __v)
    def withHelpLink(__v: _root_.scala.Predef.String): LegalAndSupportSettings = copy(helpLink = __v)
    def withSupportEmail(__v: _root_.scala.Predef.String): LegalAndSupportSettings = copy(supportEmail = __v)
    def withResourceOwnerType(__v: com.zitadel.settings.v2alpha.settings.ResourceOwnerType): LegalAndSupportSettings = copy(resourceOwnerType = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = tosLink
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = privacyPolicyLink
          if (__t != "") __t else null
        }
        case 3 => {
          val __t = helpLink
          if (__t != "") __t else null
        }
        case 4 => {
          val __t = supportEmail
          if (__t != "") __t else null
        }
        case 5 => {
          val __t = resourceOwnerType.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(tosLink)
        case 2 => _root_.scalapb.descriptors.PString(privacyPolicyLink)
        case 3 => _root_.scalapb.descriptors.PString(helpLink)
        case 4 => _root_.scalapb.descriptors.PString(supportEmail)
        case 5 => _root_.scalapb.descriptors.PEnum(resourceOwnerType.scalaValueDescriptor)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings.type = com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings
    // @@protoc_insertion_point(GeneratedMessage[zitadel.settings.v2alpha.LegalAndSupportSettings])
}

object LegalAndSupportSettings extends scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings = {
    var __tosLink: _root_.scala.Predef.String = ""
    var __privacyPolicyLink: _root_.scala.Predef.String = ""
    var __helpLink: _root_.scala.Predef.String = ""
    var __supportEmail: _root_.scala.Predef.String = ""
    var __resourceOwnerType: com.zitadel.settings.v2alpha.settings.ResourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __tosLink = _input__.readStringRequireUtf8()
        case 18 =>
          __privacyPolicyLink = _input__.readStringRequireUtf8()
        case 26 =>
          __helpLink = _input__.readStringRequireUtf8()
        case 34 =>
          __supportEmail = _input__.readStringRequireUtf8()
        case 40 =>
          __resourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.fromValue(_input__.readEnum())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings(
        tosLink = __tosLink,
        privacyPolicyLink = __privacyPolicyLink,
        helpLink = __helpLink,
        supportEmail = __supportEmail,
        resourceOwnerType = __resourceOwnerType,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings(
        tosLink = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        privacyPolicyLink = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        helpLink = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        supportEmail = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        resourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED.scalaValueDescriptor).number)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = LegalSettingsProto.javaDescriptor.getMessageTypes().get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = LegalSettingsProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 5 => com.zitadel.settings.v2alpha.settings.ResourceOwnerType
    }
  }
  lazy val defaultInstance = com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings(
    tosLink = "",
    privacyPolicyLink = "",
    helpLink = "",
    supportEmail = "",
    resourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED
  )
  implicit class LegalAndSupportSettingsLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings](_l) {
    def tosLink: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.tosLink)((c_, f_) => c_.copy(tosLink = f_))
    def privacyPolicyLink: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.privacyPolicyLink)((c_, f_) => c_.copy(privacyPolicyLink = f_))
    def helpLink: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.helpLink)((c_, f_) => c_.copy(helpLink = f_))
    def supportEmail: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.supportEmail)((c_, f_) => c_.copy(supportEmail = f_))
    def resourceOwnerType: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.settings.v2alpha.settings.ResourceOwnerType] = field(_.resourceOwnerType)((c_, f_) => c_.copy(resourceOwnerType = f_))
  }
  final val TOS_LINK_FIELD_NUMBER = 1
  final val PRIVACY_POLICY_LINK_FIELD_NUMBER = 2
  final val HELP_LINK_FIELD_NUMBER = 3
  final val SUPPORT_EMAIL_FIELD_NUMBER = 4
  final val RESOURCE_OWNER_TYPE_FIELD_NUMBER = 5
  def of(
    tosLink: _root_.scala.Predef.String,
    privacyPolicyLink: _root_.scala.Predef.String,
    helpLink: _root_.scala.Predef.String,
    supportEmail: _root_.scala.Predef.String,
    resourceOwnerType: com.zitadel.settings.v2alpha.settings.ResourceOwnerType
  ): _root_.com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings = _root_.com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings(
    tosLink,
    privacyPolicyLink,
    helpLink,
    supportEmail,
    resourceOwnerType
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.settings.v2alpha.LegalAndSupportSettings])
}

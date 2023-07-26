// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.settings.v2alpha.domain_settings

/** @param resourceOwnerType
  *   resource_owner_type returns if the setting is managed on the organization or on the instance
  */
@SerialVersionUID(0L)
final case class DomainSettings(
    loginNameIncludesDomain: _root_.scala.Boolean = false,
    requireOrgDomainVerification: _root_.scala.Boolean = false,
    smtpSenderAddressMatchesInstanceDomain: _root_.scala.Boolean = false,
    resourceOwnerType: com.zitadel.settings.v2alpha.settings.ResourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[DomainSettings] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = loginNameIncludesDomain
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(1, __value)
        }
      };
      
      {
        val __value = requireOrgDomainVerification
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(2, __value)
        }
      };
      
      {
        val __value = smtpSenderAddressMatchesInstanceDomain
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(3, __value)
        }
      };
      
      {
        val __value = resourceOwnerType.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(6, __value)
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
        val __v = loginNameIncludesDomain
        if (__v != false) {
          _output__.writeBool(1, __v)
        }
      };
      {
        val __v = requireOrgDomainVerification
        if (__v != false) {
          _output__.writeBool(2, __v)
        }
      };
      {
        val __v = smtpSenderAddressMatchesInstanceDomain
        if (__v != false) {
          _output__.writeBool(3, __v)
        }
      };
      {
        val __v = resourceOwnerType.value
        if (__v != 0) {
          _output__.writeEnum(6, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withLoginNameIncludesDomain(__v: _root_.scala.Boolean): DomainSettings = copy(loginNameIncludesDomain = __v)
    def withRequireOrgDomainVerification(__v: _root_.scala.Boolean): DomainSettings = copy(requireOrgDomainVerification = __v)
    def withSmtpSenderAddressMatchesInstanceDomain(__v: _root_.scala.Boolean): DomainSettings = copy(smtpSenderAddressMatchesInstanceDomain = __v)
    def withResourceOwnerType(__v: com.zitadel.settings.v2alpha.settings.ResourceOwnerType): DomainSettings = copy(resourceOwnerType = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = loginNameIncludesDomain
          if (__t != false) __t else null
        }
        case 2 => {
          val __t = requireOrgDomainVerification
          if (__t != false) __t else null
        }
        case 3 => {
          val __t = smtpSenderAddressMatchesInstanceDomain
          if (__t != false) __t else null
        }
        case 6 => {
          val __t = resourceOwnerType.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PBoolean(loginNameIncludesDomain)
        case 2 => _root_.scalapb.descriptors.PBoolean(requireOrgDomainVerification)
        case 3 => _root_.scalapb.descriptors.PBoolean(smtpSenderAddressMatchesInstanceDomain)
        case 6 => _root_.scalapb.descriptors.PEnum(resourceOwnerType.scalaValueDescriptor)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.settings.v2alpha.domain_settings.DomainSettings.type = com.zitadel.settings.v2alpha.domain_settings.DomainSettings
    // @@protoc_insertion_point(GeneratedMessage[zitadel.settings.v2alpha.DomainSettings])
}

object DomainSettings extends scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.domain_settings.DomainSettings] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.domain_settings.DomainSettings] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.settings.v2alpha.domain_settings.DomainSettings = {
    var __loginNameIncludesDomain: _root_.scala.Boolean = false
    var __requireOrgDomainVerification: _root_.scala.Boolean = false
    var __smtpSenderAddressMatchesInstanceDomain: _root_.scala.Boolean = false
    var __resourceOwnerType: com.zitadel.settings.v2alpha.settings.ResourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 8 =>
          __loginNameIncludesDomain = _input__.readBool()
        case 16 =>
          __requireOrgDomainVerification = _input__.readBool()
        case 24 =>
          __smtpSenderAddressMatchesInstanceDomain = _input__.readBool()
        case 48 =>
          __resourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.fromValue(_input__.readEnum())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.settings.v2alpha.domain_settings.DomainSettings(
        loginNameIncludesDomain = __loginNameIncludesDomain,
        requireOrgDomainVerification = __requireOrgDomainVerification,
        smtpSenderAddressMatchesInstanceDomain = __smtpSenderAddressMatchesInstanceDomain,
        resourceOwnerType = __resourceOwnerType,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.settings.v2alpha.domain_settings.DomainSettings] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.settings.v2alpha.domain_settings.DomainSettings(
        loginNameIncludesDomain = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Boolean]).getOrElse(false),
        requireOrgDomainVerification = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Boolean]).getOrElse(false),
        smtpSenderAddressMatchesInstanceDomain = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Boolean]).getOrElse(false),
        resourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED.scalaValueDescriptor).number)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = DomainSettingsProto.javaDescriptor.getMessageTypes().get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = DomainSettingsProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 6 => com.zitadel.settings.v2alpha.settings.ResourceOwnerType
    }
  }
  lazy val defaultInstance = com.zitadel.settings.v2alpha.domain_settings.DomainSettings(
    loginNameIncludesDomain = false,
    requireOrgDomainVerification = false,
    smtpSenderAddressMatchesInstanceDomain = false,
    resourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED
  )
  implicit class DomainSettingsLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.settings.v2alpha.domain_settings.DomainSettings]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.settings.v2alpha.domain_settings.DomainSettings](_l) {
    def loginNameIncludesDomain: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.loginNameIncludesDomain)((c_, f_) => c_.copy(loginNameIncludesDomain = f_))
    def requireOrgDomainVerification: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.requireOrgDomainVerification)((c_, f_) => c_.copy(requireOrgDomainVerification = f_))
    def smtpSenderAddressMatchesInstanceDomain: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.smtpSenderAddressMatchesInstanceDomain)((c_, f_) => c_.copy(smtpSenderAddressMatchesInstanceDomain = f_))
    def resourceOwnerType: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.settings.v2alpha.settings.ResourceOwnerType] = field(_.resourceOwnerType)((c_, f_) => c_.copy(resourceOwnerType = f_))
  }
  final val LOGIN_NAME_INCLUDES_DOMAIN_FIELD_NUMBER = 1
  final val REQUIRE_ORG_DOMAIN_VERIFICATION_FIELD_NUMBER = 2
  final val SMTP_SENDER_ADDRESS_MATCHES_INSTANCE_DOMAIN_FIELD_NUMBER = 3
  final val RESOURCE_OWNER_TYPE_FIELD_NUMBER = 6
  def of(
    loginNameIncludesDomain: _root_.scala.Boolean,
    requireOrgDomainVerification: _root_.scala.Boolean,
    smtpSenderAddressMatchesInstanceDomain: _root_.scala.Boolean,
    resourceOwnerType: com.zitadel.settings.v2alpha.settings.ResourceOwnerType
  ): _root_.com.zitadel.settings.v2alpha.domain_settings.DomainSettings = _root_.com.zitadel.settings.v2alpha.domain_settings.DomainSettings(
    loginNameIncludesDomain,
    requireOrgDomainVerification,
    smtpSenderAddressMatchesInstanceDomain,
    resourceOwnerType
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.settings.v2alpha.DomainSettings])
}

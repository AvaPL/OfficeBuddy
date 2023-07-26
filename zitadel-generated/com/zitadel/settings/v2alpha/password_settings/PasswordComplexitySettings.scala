// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.settings.v2alpha.password_settings

/** @param resourceOwnerType
  *   resource_owner_type returns if the settings is managed on the organization or on the instance
  */
@SerialVersionUID(0L)
final case class PasswordComplexitySettings(
    minLength: _root_.scala.Long = 0L,
    requiresUppercase: _root_.scala.Boolean = false,
    requiresLowercase: _root_.scala.Boolean = false,
    requiresNumber: _root_.scala.Boolean = false,
    requiresSymbol: _root_.scala.Boolean = false,
    resourceOwnerType: com.zitadel.settings.v2alpha.settings.ResourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[PasswordComplexitySettings] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = minLength
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeUInt64Size(1, __value)
        }
      };
      
      {
        val __value = requiresUppercase
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(2, __value)
        }
      };
      
      {
        val __value = requiresLowercase
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(3, __value)
        }
      };
      
      {
        val __value = requiresNumber
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(4, __value)
        }
      };
      
      {
        val __value = requiresSymbol
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(5, __value)
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
        val __v = minLength
        if (__v != 0L) {
          _output__.writeUInt64(1, __v)
        }
      };
      {
        val __v = requiresUppercase
        if (__v != false) {
          _output__.writeBool(2, __v)
        }
      };
      {
        val __v = requiresLowercase
        if (__v != false) {
          _output__.writeBool(3, __v)
        }
      };
      {
        val __v = requiresNumber
        if (__v != false) {
          _output__.writeBool(4, __v)
        }
      };
      {
        val __v = requiresSymbol
        if (__v != false) {
          _output__.writeBool(5, __v)
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
    def withMinLength(__v: _root_.scala.Long): PasswordComplexitySettings = copy(minLength = __v)
    def withRequiresUppercase(__v: _root_.scala.Boolean): PasswordComplexitySettings = copy(requiresUppercase = __v)
    def withRequiresLowercase(__v: _root_.scala.Boolean): PasswordComplexitySettings = copy(requiresLowercase = __v)
    def withRequiresNumber(__v: _root_.scala.Boolean): PasswordComplexitySettings = copy(requiresNumber = __v)
    def withRequiresSymbol(__v: _root_.scala.Boolean): PasswordComplexitySettings = copy(requiresSymbol = __v)
    def withResourceOwnerType(__v: com.zitadel.settings.v2alpha.settings.ResourceOwnerType): PasswordComplexitySettings = copy(resourceOwnerType = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = minLength
          if (__t != 0L) __t else null
        }
        case 2 => {
          val __t = requiresUppercase
          if (__t != false) __t else null
        }
        case 3 => {
          val __t = requiresLowercase
          if (__t != false) __t else null
        }
        case 4 => {
          val __t = requiresNumber
          if (__t != false) __t else null
        }
        case 5 => {
          val __t = requiresSymbol
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
        case 1 => _root_.scalapb.descriptors.PLong(minLength)
        case 2 => _root_.scalapb.descriptors.PBoolean(requiresUppercase)
        case 3 => _root_.scalapb.descriptors.PBoolean(requiresLowercase)
        case 4 => _root_.scalapb.descriptors.PBoolean(requiresNumber)
        case 5 => _root_.scalapb.descriptors.PBoolean(requiresSymbol)
        case 6 => _root_.scalapb.descriptors.PEnum(resourceOwnerType.scalaValueDescriptor)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings.type = com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings
    // @@protoc_insertion_point(GeneratedMessage[zitadel.settings.v2alpha.PasswordComplexitySettings])
}

object PasswordComplexitySettings extends scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings = {
    var __minLength: _root_.scala.Long = 0L
    var __requiresUppercase: _root_.scala.Boolean = false
    var __requiresLowercase: _root_.scala.Boolean = false
    var __requiresNumber: _root_.scala.Boolean = false
    var __requiresSymbol: _root_.scala.Boolean = false
    var __resourceOwnerType: com.zitadel.settings.v2alpha.settings.ResourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 8 =>
          __minLength = _input__.readUInt64()
        case 16 =>
          __requiresUppercase = _input__.readBool()
        case 24 =>
          __requiresLowercase = _input__.readBool()
        case 32 =>
          __requiresNumber = _input__.readBool()
        case 40 =>
          __requiresSymbol = _input__.readBool()
        case 48 =>
          __resourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.fromValue(_input__.readEnum())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings(
        minLength = __minLength,
        requiresUppercase = __requiresUppercase,
        requiresLowercase = __requiresLowercase,
        requiresNumber = __requiresNumber,
        requiresSymbol = __requiresSymbol,
        resourceOwnerType = __resourceOwnerType,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings(
        minLength = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Long]).getOrElse(0L),
        requiresUppercase = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Boolean]).getOrElse(false),
        requiresLowercase = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Boolean]).getOrElse(false),
        requiresNumber = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Boolean]).getOrElse(false),
        requiresSymbol = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.scala.Boolean]).getOrElse(false),
        resourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED.scalaValueDescriptor).number)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = PasswordSettingsProto.javaDescriptor.getMessageTypes().get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = PasswordSettingsProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 6 => com.zitadel.settings.v2alpha.settings.ResourceOwnerType
    }
  }
  lazy val defaultInstance = com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings(
    minLength = 0L,
    requiresUppercase = false,
    requiresLowercase = false,
    requiresNumber = false,
    requiresSymbol = false,
    resourceOwnerType = com.zitadel.settings.v2alpha.settings.ResourceOwnerType.RESOURCE_OWNER_TYPE_UNSPECIFIED
  )
  implicit class PasswordComplexitySettingsLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings](_l) {
    def minLength: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.minLength)((c_, f_) => c_.copy(minLength = f_))
    def requiresUppercase: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.requiresUppercase)((c_, f_) => c_.copy(requiresUppercase = f_))
    def requiresLowercase: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.requiresLowercase)((c_, f_) => c_.copy(requiresLowercase = f_))
    def requiresNumber: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.requiresNumber)((c_, f_) => c_.copy(requiresNumber = f_))
    def requiresSymbol: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.requiresSymbol)((c_, f_) => c_.copy(requiresSymbol = f_))
    def resourceOwnerType: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.settings.v2alpha.settings.ResourceOwnerType] = field(_.resourceOwnerType)((c_, f_) => c_.copy(resourceOwnerType = f_))
  }
  final val MIN_LENGTH_FIELD_NUMBER = 1
  final val REQUIRES_UPPERCASE_FIELD_NUMBER = 2
  final val REQUIRES_LOWERCASE_FIELD_NUMBER = 3
  final val REQUIRES_NUMBER_FIELD_NUMBER = 4
  final val REQUIRES_SYMBOL_FIELD_NUMBER = 5
  final val RESOURCE_OWNER_TYPE_FIELD_NUMBER = 6
  def of(
    minLength: _root_.scala.Long,
    requiresUppercase: _root_.scala.Boolean,
    requiresLowercase: _root_.scala.Boolean,
    requiresNumber: _root_.scala.Boolean,
    requiresSymbol: _root_.scala.Boolean,
    resourceOwnerType: com.zitadel.settings.v2alpha.settings.ResourceOwnerType
  ): _root_.com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings = _root_.com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings(
    minLength,
    requiresUppercase,
    requiresLowercase,
    requiresNumber,
    requiresSymbol,
    resourceOwnerType
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.settings.v2alpha.PasswordComplexitySettings])
}

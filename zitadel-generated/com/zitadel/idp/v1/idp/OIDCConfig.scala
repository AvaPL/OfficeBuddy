// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.idp.v1.idp

@SerialVersionUID(0L)
final case class OIDCConfig(
    clientId: _root_.scala.Predef.String = "",
    issuer: _root_.scala.Predef.String = "",
    scopes: _root_.scala.Seq[_root_.scala.Predef.String] = _root_.scala.Seq.empty,
    displayNameMapping: com.zitadel.idp.v1.idp.OIDCMappingField = com.zitadel.idp.v1.idp.OIDCMappingField.OIDC_MAPPING_FIELD_UNSPECIFIED,
    usernameMapping: com.zitadel.idp.v1.idp.OIDCMappingField = com.zitadel.idp.v1.idp.OIDCMappingField.OIDC_MAPPING_FIELD_UNSPECIFIED,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[OIDCConfig] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = clientId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = issuer
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      scopes.foreach { __item =>
        val __value = __item
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
      }
      
      {
        val __value = displayNameMapping.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(4, __value)
        }
      };
      
      {
        val __value = usernameMapping.value
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
        val __v = clientId
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = issuer
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      scopes.foreach { __v =>
        val __m = __v
        _output__.writeString(3, __m)
      };
      {
        val __v = displayNameMapping.value
        if (__v != 0) {
          _output__.writeEnum(4, __v)
        }
      };
      {
        val __v = usernameMapping.value
        if (__v != 0) {
          _output__.writeEnum(5, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withClientId(__v: _root_.scala.Predef.String): OIDCConfig = copy(clientId = __v)
    def withIssuer(__v: _root_.scala.Predef.String): OIDCConfig = copy(issuer = __v)
    def clearScopes = copy(scopes = _root_.scala.Seq.empty)
    def addScopes(__vs: _root_.scala.Predef.String *): OIDCConfig = addAllScopes(__vs)
    def addAllScopes(__vs: Iterable[_root_.scala.Predef.String]): OIDCConfig = copy(scopes = scopes ++ __vs)
    def withScopes(__v: _root_.scala.Seq[_root_.scala.Predef.String]): OIDCConfig = copy(scopes = __v)
    def withDisplayNameMapping(__v: com.zitadel.idp.v1.idp.OIDCMappingField): OIDCConfig = copy(displayNameMapping = __v)
    def withUsernameMapping(__v: com.zitadel.idp.v1.idp.OIDCMappingField): OIDCConfig = copy(usernameMapping = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = clientId
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = issuer
          if (__t != "") __t else null
        }
        case 3 => scopes
        case 4 => {
          val __t = displayNameMapping.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
        case 5 => {
          val __t = usernameMapping.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(clientId)
        case 2 => _root_.scalapb.descriptors.PString(issuer)
        case 3 => _root_.scalapb.descriptors.PRepeated(scopes.iterator.map(_root_.scalapb.descriptors.PString(_)).toVector)
        case 4 => _root_.scalapb.descriptors.PEnum(displayNameMapping.scalaValueDescriptor)
        case 5 => _root_.scalapb.descriptors.PEnum(usernameMapping.scalaValueDescriptor)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.idp.v1.idp.OIDCConfig.type = com.zitadel.idp.v1.idp.OIDCConfig
    // @@protoc_insertion_point(GeneratedMessage[zitadel.idp.v1.OIDCConfig])
}

object OIDCConfig extends scalapb.GeneratedMessageCompanion[com.zitadel.idp.v1.idp.OIDCConfig] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.idp.v1.idp.OIDCConfig] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.idp.v1.idp.OIDCConfig = {
    var __clientId: _root_.scala.Predef.String = ""
    var __issuer: _root_.scala.Predef.String = ""
    val __scopes: _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String] = new _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String]
    var __displayNameMapping: com.zitadel.idp.v1.idp.OIDCMappingField = com.zitadel.idp.v1.idp.OIDCMappingField.OIDC_MAPPING_FIELD_UNSPECIFIED
    var __usernameMapping: com.zitadel.idp.v1.idp.OIDCMappingField = com.zitadel.idp.v1.idp.OIDCMappingField.OIDC_MAPPING_FIELD_UNSPECIFIED
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __clientId = _input__.readStringRequireUtf8()
        case 18 =>
          __issuer = _input__.readStringRequireUtf8()
        case 26 =>
          __scopes += _input__.readStringRequireUtf8()
        case 32 =>
          __displayNameMapping = com.zitadel.idp.v1.idp.OIDCMappingField.fromValue(_input__.readEnum())
        case 40 =>
          __usernameMapping = com.zitadel.idp.v1.idp.OIDCMappingField.fromValue(_input__.readEnum())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.idp.v1.idp.OIDCConfig(
        clientId = __clientId,
        issuer = __issuer,
        scopes = __scopes.result(),
        displayNameMapping = __displayNameMapping,
        usernameMapping = __usernameMapping,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.idp.v1.idp.OIDCConfig] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.idp.v1.idp.OIDCConfig(
        clientId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        issuer = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        scopes = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Seq[_root_.scala.Predef.String]]).getOrElse(_root_.scala.Seq.empty),
        displayNameMapping = com.zitadel.idp.v1.idp.OIDCMappingField.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.idp.v1.idp.OIDCMappingField.OIDC_MAPPING_FIELD_UNSPECIFIED.scalaValueDescriptor).number),
        usernameMapping = com.zitadel.idp.v1.idp.OIDCMappingField.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.idp.v1.idp.OIDCMappingField.OIDC_MAPPING_FIELD_UNSPECIFIED.scalaValueDescriptor).number)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = IdpProto.javaDescriptor.getMessageTypes().get(3)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = IdpProto.scalaDescriptor.messages(3)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 4 => com.zitadel.idp.v1.idp.OIDCMappingField
      case 5 => com.zitadel.idp.v1.idp.OIDCMappingField
    }
  }
  lazy val defaultInstance = com.zitadel.idp.v1.idp.OIDCConfig(
    clientId = "",
    issuer = "",
    scopes = _root_.scala.Seq.empty,
    displayNameMapping = com.zitadel.idp.v1.idp.OIDCMappingField.OIDC_MAPPING_FIELD_UNSPECIFIED,
    usernameMapping = com.zitadel.idp.v1.idp.OIDCMappingField.OIDC_MAPPING_FIELD_UNSPECIFIED
  )
  implicit class OIDCConfigLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.OIDCConfig]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.idp.v1.idp.OIDCConfig](_l) {
    def clientId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.clientId)((c_, f_) => c_.copy(clientId = f_))
    def issuer: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.issuer)((c_, f_) => c_.copy(issuer = f_))
    def scopes: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[_root_.scala.Predef.String]] = field(_.scopes)((c_, f_) => c_.copy(scopes = f_))
    def displayNameMapping: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.OIDCMappingField] = field(_.displayNameMapping)((c_, f_) => c_.copy(displayNameMapping = f_))
    def usernameMapping: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.OIDCMappingField] = field(_.usernameMapping)((c_, f_) => c_.copy(usernameMapping = f_))
  }
  final val CLIENT_ID_FIELD_NUMBER = 1
  final val ISSUER_FIELD_NUMBER = 2
  final val SCOPES_FIELD_NUMBER = 3
  final val DISPLAY_NAME_MAPPING_FIELD_NUMBER = 4
  final val USERNAME_MAPPING_FIELD_NUMBER = 5
  def of(
    clientId: _root_.scala.Predef.String,
    issuer: _root_.scala.Predef.String,
    scopes: _root_.scala.Seq[_root_.scala.Predef.String],
    displayNameMapping: com.zitadel.idp.v1.idp.OIDCMappingField,
    usernameMapping: com.zitadel.idp.v1.idp.OIDCMappingField
  ): _root_.com.zitadel.idp.v1.idp.OIDCConfig = _root_.com.zitadel.idp.v1.idp.OIDCConfig(
    clientId,
    issuer,
    scopes,
    displayNameMapping,
    usernameMapping
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.idp.v1.OIDCConfig])
}

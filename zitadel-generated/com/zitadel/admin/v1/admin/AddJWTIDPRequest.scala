// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.admin.v1.admin

@SerialVersionUID(0L)
final case class AddJWTIDPRequest(
    name: _root_.scala.Predef.String = "",
    stylingType: com.zitadel.idp.v1.idp.IDPStylingType = com.zitadel.idp.v1.idp.IDPStylingType.STYLING_TYPE_UNSPECIFIED,
    jwtEndpoint: _root_.scala.Predef.String = "",
    issuer: _root_.scala.Predef.String = "",
    keysEndpoint: _root_.scala.Predef.String = "",
    headerName: _root_.scala.Predef.String = "",
    autoRegister: _root_.scala.Boolean = false,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[AddJWTIDPRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = name
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = stylingType.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(2, __value)
        }
      };
      
      {
        val __value = jwtEndpoint
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
        }
      };
      
      {
        val __value = issuer
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
        }
      };
      
      {
        val __value = keysEndpoint
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(5, __value)
        }
      };
      
      {
        val __value = headerName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(6, __value)
        }
      };
      
      {
        val __value = autoRegister
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(7, __value)
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
        val __v = name
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = stylingType.value
        if (__v != 0) {
          _output__.writeEnum(2, __v)
        }
      };
      {
        val __v = jwtEndpoint
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
      {
        val __v = issuer
        if (!__v.isEmpty) {
          _output__.writeString(4, __v)
        }
      };
      {
        val __v = keysEndpoint
        if (!__v.isEmpty) {
          _output__.writeString(5, __v)
        }
      };
      {
        val __v = headerName
        if (!__v.isEmpty) {
          _output__.writeString(6, __v)
        }
      };
      {
        val __v = autoRegister
        if (__v != false) {
          _output__.writeBool(7, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withName(__v: _root_.scala.Predef.String): AddJWTIDPRequest = copy(name = __v)
    def withStylingType(__v: com.zitadel.idp.v1.idp.IDPStylingType): AddJWTIDPRequest = copy(stylingType = __v)
    def withJwtEndpoint(__v: _root_.scala.Predef.String): AddJWTIDPRequest = copy(jwtEndpoint = __v)
    def withIssuer(__v: _root_.scala.Predef.String): AddJWTIDPRequest = copy(issuer = __v)
    def withKeysEndpoint(__v: _root_.scala.Predef.String): AddJWTIDPRequest = copy(keysEndpoint = __v)
    def withHeaderName(__v: _root_.scala.Predef.String): AddJWTIDPRequest = copy(headerName = __v)
    def withAutoRegister(__v: _root_.scala.Boolean): AddJWTIDPRequest = copy(autoRegister = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = name
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = stylingType.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
        case 3 => {
          val __t = jwtEndpoint
          if (__t != "") __t else null
        }
        case 4 => {
          val __t = issuer
          if (__t != "") __t else null
        }
        case 5 => {
          val __t = keysEndpoint
          if (__t != "") __t else null
        }
        case 6 => {
          val __t = headerName
          if (__t != "") __t else null
        }
        case 7 => {
          val __t = autoRegister
          if (__t != false) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(name)
        case 2 => _root_.scalapb.descriptors.PEnum(stylingType.scalaValueDescriptor)
        case 3 => _root_.scalapb.descriptors.PString(jwtEndpoint)
        case 4 => _root_.scalapb.descriptors.PString(issuer)
        case 5 => _root_.scalapb.descriptors.PString(keysEndpoint)
        case 6 => _root_.scalapb.descriptors.PString(headerName)
        case 7 => _root_.scalapb.descriptors.PBoolean(autoRegister)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.admin.v1.admin.AddJWTIDPRequest.type = com.zitadel.admin.v1.admin.AddJWTIDPRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.admin.v1.AddJWTIDPRequest])
}

object AddJWTIDPRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.AddJWTIDPRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.AddJWTIDPRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.admin.v1.admin.AddJWTIDPRequest = {
    var __name: _root_.scala.Predef.String = ""
    var __stylingType: com.zitadel.idp.v1.idp.IDPStylingType = com.zitadel.idp.v1.idp.IDPStylingType.STYLING_TYPE_UNSPECIFIED
    var __jwtEndpoint: _root_.scala.Predef.String = ""
    var __issuer: _root_.scala.Predef.String = ""
    var __keysEndpoint: _root_.scala.Predef.String = ""
    var __headerName: _root_.scala.Predef.String = ""
    var __autoRegister: _root_.scala.Boolean = false
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __name = _input__.readStringRequireUtf8()
        case 16 =>
          __stylingType = com.zitadel.idp.v1.idp.IDPStylingType.fromValue(_input__.readEnum())
        case 26 =>
          __jwtEndpoint = _input__.readStringRequireUtf8()
        case 34 =>
          __issuer = _input__.readStringRequireUtf8()
        case 42 =>
          __keysEndpoint = _input__.readStringRequireUtf8()
        case 50 =>
          __headerName = _input__.readStringRequireUtf8()
        case 56 =>
          __autoRegister = _input__.readBool()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.admin.v1.admin.AddJWTIDPRequest(
        name = __name,
        stylingType = __stylingType,
        jwtEndpoint = __jwtEndpoint,
        issuer = __issuer,
        keysEndpoint = __keysEndpoint,
        headerName = __headerName,
        autoRegister = __autoRegister,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.admin.v1.admin.AddJWTIDPRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.admin.v1.admin.AddJWTIDPRequest(
        name = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        stylingType = com.zitadel.idp.v1.idp.IDPStylingType.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.idp.v1.idp.IDPStylingType.STYLING_TYPE_UNSPECIFIED.scalaValueDescriptor).number),
        jwtEndpoint = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        issuer = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        keysEndpoint = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        headerName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        autoRegister = __fieldsMap.get(scalaDescriptor.findFieldByNumber(7).get).map(_.as[_root_.scala.Boolean]).getOrElse(false)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = AdminProto.javaDescriptor.getMessageTypes().get(79)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = AdminProto.scalaDescriptor.messages(79)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 2 => com.zitadel.idp.v1.idp.IDPStylingType
    }
  }
  lazy val defaultInstance = com.zitadel.admin.v1.admin.AddJWTIDPRequest(
    name = "",
    stylingType = com.zitadel.idp.v1.idp.IDPStylingType.STYLING_TYPE_UNSPECIFIED,
    jwtEndpoint = "",
    issuer = "",
    keysEndpoint = "",
    headerName = "",
    autoRegister = false
  )
  implicit class AddJWTIDPRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.admin.v1.admin.AddJWTIDPRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.admin.v1.admin.AddJWTIDPRequest](_l) {
    def name: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.name)((c_, f_) => c_.copy(name = f_))
    def stylingType: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.IDPStylingType] = field(_.stylingType)((c_, f_) => c_.copy(stylingType = f_))
    def jwtEndpoint: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.jwtEndpoint)((c_, f_) => c_.copy(jwtEndpoint = f_))
    def issuer: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.issuer)((c_, f_) => c_.copy(issuer = f_))
    def keysEndpoint: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.keysEndpoint)((c_, f_) => c_.copy(keysEndpoint = f_))
    def headerName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.headerName)((c_, f_) => c_.copy(headerName = f_))
    def autoRegister: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.autoRegister)((c_, f_) => c_.copy(autoRegister = f_))
  }
  final val NAME_FIELD_NUMBER = 1
  final val STYLING_TYPE_FIELD_NUMBER = 2
  final val JWT_ENDPOINT_FIELD_NUMBER = 3
  final val ISSUER_FIELD_NUMBER = 4
  final val KEYS_ENDPOINT_FIELD_NUMBER = 5
  final val HEADER_NAME_FIELD_NUMBER = 6
  final val AUTO_REGISTER_FIELD_NUMBER = 7
  def of(
    name: _root_.scala.Predef.String,
    stylingType: com.zitadel.idp.v1.idp.IDPStylingType,
    jwtEndpoint: _root_.scala.Predef.String,
    issuer: _root_.scala.Predef.String,
    keysEndpoint: _root_.scala.Predef.String,
    headerName: _root_.scala.Predef.String,
    autoRegister: _root_.scala.Boolean
  ): _root_.com.zitadel.admin.v1.admin.AddJWTIDPRequest = _root_.com.zitadel.admin.v1.admin.AddJWTIDPRequest(
    name,
    stylingType,
    jwtEndpoint,
    issuer,
    keysEndpoint,
    headerName,
    autoRegister
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.admin.v1.AddJWTIDPRequest])
}

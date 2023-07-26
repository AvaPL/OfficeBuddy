// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.idp.v1.idp

@SerialVersionUID(0L)
final case class JWTConfig(
    jwtEndpoint: _root_.scala.Predef.String = "",
    issuer: _root_.scala.Predef.String = "",
    keysEndpoint: _root_.scala.Predef.String = "",
    headerName: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[JWTConfig] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = jwtEndpoint
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
      
      {
        val __value = keysEndpoint
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
        }
      };
      
      {
        val __value = headerName
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
        val __v = jwtEndpoint
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
      {
        val __v = keysEndpoint
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
      {
        val __v = headerName
        if (!__v.isEmpty) {
          _output__.writeString(4, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withJwtEndpoint(__v: _root_.scala.Predef.String): JWTConfig = copy(jwtEndpoint = __v)
    def withIssuer(__v: _root_.scala.Predef.String): JWTConfig = copy(issuer = __v)
    def withKeysEndpoint(__v: _root_.scala.Predef.String): JWTConfig = copy(keysEndpoint = __v)
    def withHeaderName(__v: _root_.scala.Predef.String): JWTConfig = copy(headerName = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = jwtEndpoint
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = issuer
          if (__t != "") __t else null
        }
        case 3 => {
          val __t = keysEndpoint
          if (__t != "") __t else null
        }
        case 4 => {
          val __t = headerName
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(jwtEndpoint)
        case 2 => _root_.scalapb.descriptors.PString(issuer)
        case 3 => _root_.scalapb.descriptors.PString(keysEndpoint)
        case 4 => _root_.scalapb.descriptors.PString(headerName)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.idp.v1.idp.JWTConfig.type = com.zitadel.idp.v1.idp.JWTConfig
    // @@protoc_insertion_point(GeneratedMessage[zitadel.idp.v1.JWTConfig])
}

object JWTConfig extends scalapb.GeneratedMessageCompanion[com.zitadel.idp.v1.idp.JWTConfig] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.idp.v1.idp.JWTConfig] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.idp.v1.idp.JWTConfig = {
    var __jwtEndpoint: _root_.scala.Predef.String = ""
    var __issuer: _root_.scala.Predef.String = ""
    var __keysEndpoint: _root_.scala.Predef.String = ""
    var __headerName: _root_.scala.Predef.String = ""
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __jwtEndpoint = _input__.readStringRequireUtf8()
        case 18 =>
          __issuer = _input__.readStringRequireUtf8()
        case 26 =>
          __keysEndpoint = _input__.readStringRequireUtf8()
        case 34 =>
          __headerName = _input__.readStringRequireUtf8()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.idp.v1.idp.JWTConfig(
        jwtEndpoint = __jwtEndpoint,
        issuer = __issuer,
        keysEndpoint = __keysEndpoint,
        headerName = __headerName,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.idp.v1.idp.JWTConfig] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.idp.v1.idp.JWTConfig(
        jwtEndpoint = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        issuer = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        keysEndpoint = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        headerName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = IdpProto.javaDescriptor.getMessageTypes().get(4)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = IdpProto.scalaDescriptor.messages(4)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.idp.v1.idp.JWTConfig(
    jwtEndpoint = "",
    issuer = "",
    keysEndpoint = "",
    headerName = ""
  )
  implicit class JWTConfigLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.JWTConfig]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.idp.v1.idp.JWTConfig](_l) {
    def jwtEndpoint: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.jwtEndpoint)((c_, f_) => c_.copy(jwtEndpoint = f_))
    def issuer: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.issuer)((c_, f_) => c_.copy(issuer = f_))
    def keysEndpoint: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.keysEndpoint)((c_, f_) => c_.copy(keysEndpoint = f_))
    def headerName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.headerName)((c_, f_) => c_.copy(headerName = f_))
  }
  final val JWT_ENDPOINT_FIELD_NUMBER = 1
  final val ISSUER_FIELD_NUMBER = 2
  final val KEYS_ENDPOINT_FIELD_NUMBER = 3
  final val HEADER_NAME_FIELD_NUMBER = 4
  def of(
    jwtEndpoint: _root_.scala.Predef.String,
    issuer: _root_.scala.Predef.String,
    keysEndpoint: _root_.scala.Predef.String,
    headerName: _root_.scala.Predef.String
  ): _root_.com.zitadel.idp.v1.idp.JWTConfig = _root_.com.zitadel.idp.v1.idp.JWTConfig(
    jwtEndpoint,
    issuer,
    keysEndpoint,
    headerName
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.idp.v1.JWTConfig])
}

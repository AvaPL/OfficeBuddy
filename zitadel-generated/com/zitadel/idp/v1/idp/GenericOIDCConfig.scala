// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.idp.v1.idp

@SerialVersionUID(0L)
final case class GenericOIDCConfig(
    issuer: _root_.scala.Predef.String = "",
    clientId: _root_.scala.Predef.String = "",
    scopes: _root_.scala.Seq[_root_.scala.Predef.String] = _root_.scala.Seq.empty,
    isIdTokenMapping: _root_.scala.Boolean = false,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[GenericOIDCConfig] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = issuer
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = clientId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      scopes.foreach { __item =>
        val __value = __item
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
      }
      
      {
        val __value = isIdTokenMapping
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(4, __value)
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
        val __v = issuer
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = clientId
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      scopes.foreach { __v =>
        val __m = __v
        _output__.writeString(3, __m)
      };
      {
        val __v = isIdTokenMapping
        if (__v != false) {
          _output__.writeBool(4, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withIssuer(__v: _root_.scala.Predef.String): GenericOIDCConfig = copy(issuer = __v)
    def withClientId(__v: _root_.scala.Predef.String): GenericOIDCConfig = copy(clientId = __v)
    def clearScopes = copy(scopes = _root_.scala.Seq.empty)
    def addScopes(__vs: _root_.scala.Predef.String *): GenericOIDCConfig = addAllScopes(__vs)
    def addAllScopes(__vs: Iterable[_root_.scala.Predef.String]): GenericOIDCConfig = copy(scopes = scopes ++ __vs)
    def withScopes(__v: _root_.scala.Seq[_root_.scala.Predef.String]): GenericOIDCConfig = copy(scopes = __v)
    def withIsIdTokenMapping(__v: _root_.scala.Boolean): GenericOIDCConfig = copy(isIdTokenMapping = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = issuer
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = clientId
          if (__t != "") __t else null
        }
        case 3 => scopes
        case 4 => {
          val __t = isIdTokenMapping
          if (__t != false) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(issuer)
        case 2 => _root_.scalapb.descriptors.PString(clientId)
        case 3 => _root_.scalapb.descriptors.PRepeated(scopes.iterator.map(_root_.scalapb.descriptors.PString(_)).toVector)
        case 4 => _root_.scalapb.descriptors.PBoolean(isIdTokenMapping)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.idp.v1.idp.GenericOIDCConfig.type = com.zitadel.idp.v1.idp.GenericOIDCConfig
    // @@protoc_insertion_point(GeneratedMessage[zitadel.idp.v1.GenericOIDCConfig])
}

object GenericOIDCConfig extends scalapb.GeneratedMessageCompanion[com.zitadel.idp.v1.idp.GenericOIDCConfig] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.idp.v1.idp.GenericOIDCConfig] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.idp.v1.idp.GenericOIDCConfig = {
    var __issuer: _root_.scala.Predef.String = ""
    var __clientId: _root_.scala.Predef.String = ""
    val __scopes: _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String] = new _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String]
    var __isIdTokenMapping: _root_.scala.Boolean = false
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __issuer = _input__.readStringRequireUtf8()
        case 18 =>
          __clientId = _input__.readStringRequireUtf8()
        case 26 =>
          __scopes += _input__.readStringRequireUtf8()
        case 32 =>
          __isIdTokenMapping = _input__.readBool()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.idp.v1.idp.GenericOIDCConfig(
        issuer = __issuer,
        clientId = __clientId,
        scopes = __scopes.result(),
        isIdTokenMapping = __isIdTokenMapping,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.idp.v1.idp.GenericOIDCConfig] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.idp.v1.idp.GenericOIDCConfig(
        issuer = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        clientId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        scopes = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Seq[_root_.scala.Predef.String]]).getOrElse(_root_.scala.Seq.empty),
        isIdTokenMapping = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Boolean]).getOrElse(false)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = IdpProto.javaDescriptor.getMessageTypes().get(11)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = IdpProto.scalaDescriptor.messages(11)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.idp.v1.idp.GenericOIDCConfig(
    issuer = "",
    clientId = "",
    scopes = _root_.scala.Seq.empty,
    isIdTokenMapping = false
  )
  implicit class GenericOIDCConfigLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.GenericOIDCConfig]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.idp.v1.idp.GenericOIDCConfig](_l) {
    def issuer: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.issuer)((c_, f_) => c_.copy(issuer = f_))
    def clientId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.clientId)((c_, f_) => c_.copy(clientId = f_))
    def scopes: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[_root_.scala.Predef.String]] = field(_.scopes)((c_, f_) => c_.copy(scopes = f_))
    def isIdTokenMapping: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.isIdTokenMapping)((c_, f_) => c_.copy(isIdTokenMapping = f_))
  }
  final val ISSUER_FIELD_NUMBER = 1
  final val CLIENT_ID_FIELD_NUMBER = 2
  final val SCOPES_FIELD_NUMBER = 3
  final val IS_ID_TOKEN_MAPPING_FIELD_NUMBER = 4
  def of(
    issuer: _root_.scala.Predef.String,
    clientId: _root_.scala.Predef.String,
    scopes: _root_.scala.Seq[_root_.scala.Predef.String],
    isIdTokenMapping: _root_.scala.Boolean
  ): _root_.com.zitadel.idp.v1.idp.GenericOIDCConfig = _root_.com.zitadel.idp.v1.idp.GenericOIDCConfig(
    issuer,
    clientId,
    scopes,
    isIdTokenMapping
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.idp.v1.GenericOIDCConfig])
}

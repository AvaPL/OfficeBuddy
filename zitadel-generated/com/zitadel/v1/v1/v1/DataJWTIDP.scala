// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.v1.v1.v1

@SerialVersionUID(0L)
final case class DataJWTIDP(
    idpId: _root_.scala.Predef.String = "",
    idp: _root_.scala.Option[com.zitadel.management.v1.management.AddOrgJWTIDPRequest] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[DataJWTIDP] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = idpId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      if (idp.isDefined) {
        val __value = idp.get
        __size += 2 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
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
        val __v = idpId
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      idp.foreach { __v =>
        val __m = __v
        _output__.writeTag(32, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def withIdpId(__v: _root_.scala.Predef.String): DataJWTIDP = copy(idpId = __v)
    def getIdp: com.zitadel.management.v1.management.AddOrgJWTIDPRequest = idp.getOrElse(com.zitadel.management.v1.management.AddOrgJWTIDPRequest.defaultInstance)
    def clearIdp: DataJWTIDP = copy(idp = _root_.scala.None)
    def withIdp(__v: com.zitadel.management.v1.management.AddOrgJWTIDPRequest): DataJWTIDP = copy(idp = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = idpId
          if (__t != "") __t else null
        }
        case 32 => idp.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(idpId)
        case 32 => idp.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.v1.v1.v1.DataJWTIDP.type = com.zitadel.v1.v1.v1.DataJWTIDP
    // @@protoc_insertion_point(GeneratedMessage[zitadel.v1.v1.DataJWTIDP])
}

object DataJWTIDP extends scalapb.GeneratedMessageCompanion[com.zitadel.v1.v1.v1.DataJWTIDP] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.v1.v1.v1.DataJWTIDP] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.v1.v1.v1.DataJWTIDP = {
    var __idpId: _root_.scala.Predef.String = ""
    var __idp: _root_.scala.Option[com.zitadel.management.v1.management.AddOrgJWTIDPRequest] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __idpId = _input__.readStringRequireUtf8()
        case 258 =>
          __idp = Option(__idp.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.management.v1.management.AddOrgJWTIDPRequest](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.v1.v1.v1.DataJWTIDP(
        idpId = __idpId,
        idp = __idp,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.v1.v1.v1.DataJWTIDP] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.v1.v1.v1.DataJWTIDP(
        idpId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        idp = __fieldsMap.get(scalaDescriptor.findFieldByNumber(32).get).flatMap(_.as[_root_.scala.Option[com.zitadel.management.v1.management.AddOrgJWTIDPRequest]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = V1Proto.javaDescriptor.getMessageTypes().get(4)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = V1Proto.scalaDescriptor.messages(4)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 32 => __out = com.zitadel.management.v1.management.AddOrgJWTIDPRequest
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.v1.v1.v1.DataJWTIDP(
    idpId = "",
    idp = _root_.scala.None
  )
  implicit class DataJWTIDPLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.v1.v1.DataJWTIDP]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.v1.v1.v1.DataJWTIDP](_l) {
    def idpId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.idpId)((c_, f_) => c_.copy(idpId = f_))
    def idp: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.AddOrgJWTIDPRequest] = field(_.getIdp)((c_, f_) => c_.copy(idp = Option(f_)))
    def optionalIdp: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.management.v1.management.AddOrgJWTIDPRequest]] = field(_.idp)((c_, f_) => c_.copy(idp = f_))
  }
  final val IDP_ID_FIELD_NUMBER = 1
  final val IDP_FIELD_NUMBER = 32
  def of(
    idpId: _root_.scala.Predef.String,
    idp: _root_.scala.Option[com.zitadel.management.v1.management.AddOrgJWTIDPRequest]
  ): _root_.com.zitadel.v1.v1.v1.DataJWTIDP = _root_.com.zitadel.v1.v1.v1.DataJWTIDP(
    idpId,
    idp
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.v1.v1.DataJWTIDP])
}

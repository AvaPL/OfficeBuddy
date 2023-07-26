// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

@SerialVersionUID(0L)
final case class UpdateSAMLAppConfigRequest(
    projectId: _root_.scala.Predef.String = "",
    appId: _root_.scala.Predef.String = "",
    metadata: com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.Empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[UpdateSAMLAppConfigRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = projectId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = appId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      if (metadata.metadataXml.isDefined) {
        val __value = metadata.metadataXml.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(3, __value)
      };
      if (metadata.metadataUrl.isDefined) {
        val __value = metadata.metadataUrl.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
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
        val __v = projectId
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = appId
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      metadata.metadataXml.foreach { __v =>
        val __m = __v
        _output__.writeBytes(3, __m)
      };
      metadata.metadataUrl.foreach { __v =>
        val __m = __v
        _output__.writeString(4, __m)
      };
      unknownFields.writeTo(_output__)
    }
    def withProjectId(__v: _root_.scala.Predef.String): UpdateSAMLAppConfigRequest = copy(projectId = __v)
    def withAppId(__v: _root_.scala.Predef.String): UpdateSAMLAppConfigRequest = copy(appId = __v)
    def getMetadataXml: _root_.com.google.protobuf.ByteString = metadata.metadataXml.getOrElse(_root_.com.google.protobuf.ByteString.EMPTY)
    def withMetadataXml(__v: _root_.com.google.protobuf.ByteString): UpdateSAMLAppConfigRequest = copy(metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.MetadataXml(__v))
    def getMetadataUrl: _root_.scala.Predef.String = metadata.metadataUrl.getOrElse("")
    def withMetadataUrl(__v: _root_.scala.Predef.String): UpdateSAMLAppConfigRequest = copy(metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.MetadataUrl(__v))
    def clearMetadata: UpdateSAMLAppConfigRequest = copy(metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.Empty)
    def withMetadata(__v: com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata): UpdateSAMLAppConfigRequest = copy(metadata = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = projectId
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = appId
          if (__t != "") __t else null
        }
        case 3 => metadata.metadataXml.orNull
        case 4 => metadata.metadataUrl.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(projectId)
        case 2 => _root_.scalapb.descriptors.PString(appId)
        case 3 => metadata.metadataXml.map(_root_.scalapb.descriptors.PByteString(_)).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 4 => metadata.metadataUrl.map(_root_.scalapb.descriptors.PString(_)).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.type = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.UpdateSAMLAppConfigRequest])
}

object UpdateSAMLAppConfigRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest = {
    var __projectId: _root_.scala.Predef.String = ""
    var __appId: _root_.scala.Predef.String = ""
    var __metadata: com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.Empty
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __projectId = _input__.readStringRequireUtf8()
        case 18 =>
          __appId = _input__.readStringRequireUtf8()
        case 26 =>
          __metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.MetadataXml(_input__.readBytes())
        case 34 =>
          __metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.MetadataUrl(_input__.readStringRequireUtf8())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest(
        projectId = __projectId,
        appId = __appId,
        metadata = __metadata,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest(
        projectId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        appId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        metadata = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[_root_.scala.Option[_root_.com.google.protobuf.ByteString]]).map(com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.MetadataXml(_))
            .orElse[com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata](__fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Predef.String]]).map(com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.MetadataUrl(_)))
            .getOrElse(com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(226)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(226)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest(
    projectId = "",
    appId = "",
    metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.Empty
  )
  sealed trait Metadata extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isMetadataXml: _root_.scala.Boolean = false
    def isMetadataUrl: _root_.scala.Boolean = false
    def metadataXml: _root_.scala.Option[_root_.com.google.protobuf.ByteString] = _root_.scala.None
    def metadataUrl: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None
  }
  object Metadata {
    @SerialVersionUID(0L)
    case object Empty extends com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class MetadataXml(value: _root_.com.google.protobuf.ByteString) extends com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata {
      type ValueType = _root_.com.google.protobuf.ByteString
      override def isMetadataXml: _root_.scala.Boolean = true
      override def metadataXml: _root_.scala.Option[_root_.com.google.protobuf.ByteString] = Some(value)
      override def number: _root_.scala.Int = 3
    }
    @SerialVersionUID(0L)
    final case class MetadataUrl(value: _root_.scala.Predef.String) extends com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata {
      type ValueType = _root_.scala.Predef.String
      override def isMetadataUrl: _root_.scala.Boolean = true
      override def metadataUrl: _root_.scala.Option[_root_.scala.Predef.String] = Some(value)
      override def number: _root_.scala.Int = 4
    }
  }
  implicit class UpdateSAMLAppConfigRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest](_l) {
    def projectId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.projectId)((c_, f_) => c_.copy(projectId = f_))
    def appId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.appId)((c_, f_) => c_.copy(appId = f_))
    def metadataXml: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.getMetadataXml)((c_, f_) => c_.copy(metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.MetadataXml(f_)))
    def metadataUrl: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.getMetadataUrl)((c_, f_) => c_.copy(metadata = com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata.MetadataUrl(f_)))
    def metadata: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata] = field(_.metadata)((c_, f_) => c_.copy(metadata = f_))
  }
  final val PROJECT_ID_FIELD_NUMBER = 1
  final val APP_ID_FIELD_NUMBER = 2
  final val METADATA_XML_FIELD_NUMBER = 3
  final val METADATA_URL_FIELD_NUMBER = 4
  def of(
    projectId: _root_.scala.Predef.String,
    appId: _root_.scala.Predef.String,
    metadata: com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest.Metadata
  ): _root_.com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest = _root_.com.zitadel.management.v1.management.UpdateSAMLAppConfigRequest(
    projectId,
    appId,
    metadata
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.UpdateSAMLAppConfigRequest])
}

// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

@SerialVersionUID(0L)
final case class ListOrgIDPsResponse(
    details: _root_.scala.Option[com.zitadel.v1.`object`.ListDetails] = _root_.scala.None,
    sortingColumn: com.zitadel.idp.v1.idp.IDPFieldName = com.zitadel.idp.v1.idp.IDPFieldName.IDP_FIELD_NAME_UNSPECIFIED,
    result: _root_.scala.Seq[com.zitadel.idp.v1.idp.IDP] = _root_.scala.Seq.empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[ListOrgIDPsResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (details.isDefined) {
        val __value = details.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      
      {
        val __value = sortingColumn.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(2, __value)
        }
      };
      result.foreach { __item =>
        val __value = __item
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      }
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
      {
        val __v = sortingColumn.value
        if (__v != 0) {
          _output__.writeEnum(2, __v)
        }
      };
      result.foreach { __v =>
        val __m = __v
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getDetails: com.zitadel.v1.`object`.ListDetails = details.getOrElse(com.zitadel.v1.`object`.ListDetails.defaultInstance)
    def clearDetails: ListOrgIDPsResponse = copy(details = _root_.scala.None)
    def withDetails(__v: com.zitadel.v1.`object`.ListDetails): ListOrgIDPsResponse = copy(details = Option(__v))
    def withSortingColumn(__v: com.zitadel.idp.v1.idp.IDPFieldName): ListOrgIDPsResponse = copy(sortingColumn = __v)
    def clearResult = copy(result = _root_.scala.Seq.empty)
    def addResult(__vs: com.zitadel.idp.v1.idp.IDP *): ListOrgIDPsResponse = addAllResult(__vs)
    def addAllResult(__vs: Iterable[com.zitadel.idp.v1.idp.IDP]): ListOrgIDPsResponse = copy(result = result ++ __vs)
    def withResult(__v: _root_.scala.Seq[com.zitadel.idp.v1.idp.IDP]): ListOrgIDPsResponse = copy(result = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => details.orNull
        case 2 => {
          val __t = sortingColumn.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
        case 3 => result
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => details.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => _root_.scalapb.descriptors.PEnum(sortingColumn.scalaValueDescriptor)
        case 3 => _root_.scalapb.descriptors.PRepeated(result.iterator.map(_.toPMessage).toVector)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.ListOrgIDPsResponse.type = com.zitadel.management.v1.management.ListOrgIDPsResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.ListOrgIDPsResponse])
}

object ListOrgIDPsResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ListOrgIDPsResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ListOrgIDPsResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.ListOrgIDPsResponse = {
    var __details: _root_.scala.Option[com.zitadel.v1.`object`.ListDetails] = _root_.scala.None
    var __sortingColumn: com.zitadel.idp.v1.idp.IDPFieldName = com.zitadel.idp.v1.idp.IDPFieldName.IDP_FIELD_NAME_UNSPECIFIED
    val __result: _root_.scala.collection.immutable.VectorBuilder[com.zitadel.idp.v1.idp.IDP] = new _root_.scala.collection.immutable.VectorBuilder[com.zitadel.idp.v1.idp.IDP]
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __details = Option(__details.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.v1.`object`.ListDetails](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 16 =>
          __sortingColumn = com.zitadel.idp.v1.idp.IDPFieldName.fromValue(_input__.readEnum())
        case 26 =>
          __result += _root_.scalapb.LiteParser.readMessage[com.zitadel.idp.v1.idp.IDP](_input__)
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.ListOrgIDPsResponse(
        details = __details,
        sortingColumn = __sortingColumn,
        result = __result.result(),
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.ListOrgIDPsResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.ListOrgIDPsResponse(
        details = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.v1.`object`.ListDetails]]),
        sortingColumn = com.zitadel.idp.v1.idp.IDPFieldName.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.idp.v1.idp.IDPFieldName.IDP_FIELD_NAME_UNSPECIFIED.scalaValueDescriptor).number),
        result = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Seq[com.zitadel.idp.v1.idp.IDP]]).getOrElse(_root_.scala.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(466)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(466)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.v1.`object`.ListDetails
      case 3 => __out = com.zitadel.idp.v1.idp.IDP
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 2 => com.zitadel.idp.v1.idp.IDPFieldName
    }
  }
  lazy val defaultInstance = com.zitadel.management.v1.management.ListOrgIDPsResponse(
    details = _root_.scala.None,
    sortingColumn = com.zitadel.idp.v1.idp.IDPFieldName.IDP_FIELD_NAME_UNSPECIFIED,
    result = _root_.scala.Seq.empty
  )
  implicit class ListOrgIDPsResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.ListOrgIDPsResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.ListOrgIDPsResponse](_l) {
    def details: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.`object`.ListDetails] = field(_.getDetails)((c_, f_) => c_.copy(details = Option(f_)))
    def optionalDetails: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.v1.`object`.ListDetails]] = field(_.details)((c_, f_) => c_.copy(details = f_))
    def sortingColumn: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.IDPFieldName] = field(_.sortingColumn)((c_, f_) => c_.copy(sortingColumn = f_))
    def result: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[com.zitadel.idp.v1.idp.IDP]] = field(_.result)((c_, f_) => c_.copy(result = f_))
  }
  final val DETAILS_FIELD_NUMBER = 1
  final val SORTING_COLUMN_FIELD_NUMBER = 2
  final val RESULT_FIELD_NUMBER = 3
  def of(
    details: _root_.scala.Option[com.zitadel.v1.`object`.ListDetails],
    sortingColumn: com.zitadel.idp.v1.idp.IDPFieldName,
    result: _root_.scala.Seq[com.zitadel.idp.v1.idp.IDP]
  ): _root_.com.zitadel.management.v1.management.ListOrgIDPsResponse = _root_.com.zitadel.management.v1.management.ListOrgIDPsResponse(
    details,
    sortingColumn,
    result
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.ListOrgIDPsResponse])
}

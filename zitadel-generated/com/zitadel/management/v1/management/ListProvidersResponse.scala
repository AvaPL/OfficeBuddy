// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

@SerialVersionUID(0L)
final case class ListProvidersResponse(
    details: _root_.scala.Option[com.zitadel.v1.`object`.ListDetails] = _root_.scala.None,
    result: _root_.scala.Seq[com.zitadel.idp.v1.idp.Provider] = _root_.scala.Seq.empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[ListProvidersResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (details.isDefined) {
        val __value = details.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
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
      result.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getDetails: com.zitadel.v1.`object`.ListDetails = details.getOrElse(com.zitadel.v1.`object`.ListDetails.defaultInstance)
    def clearDetails: ListProvidersResponse = copy(details = _root_.scala.None)
    def withDetails(__v: com.zitadel.v1.`object`.ListDetails): ListProvidersResponse = copy(details = Option(__v))
    def clearResult = copy(result = _root_.scala.Seq.empty)
    def addResult(__vs: com.zitadel.idp.v1.idp.Provider *): ListProvidersResponse = addAllResult(__vs)
    def addAllResult(__vs: Iterable[com.zitadel.idp.v1.idp.Provider]): ListProvidersResponse = copy(result = result ++ __vs)
    def withResult(__v: _root_.scala.Seq[com.zitadel.idp.v1.idp.Provider]): ListProvidersResponse = copy(result = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => details.orNull
        case 2 => result
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => details.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => _root_.scalapb.descriptors.PRepeated(result.iterator.map(_.toPMessage).toVector)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.ListProvidersResponse.type = com.zitadel.management.v1.management.ListProvidersResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.ListProvidersResponse])
}

object ListProvidersResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ListProvidersResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ListProvidersResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.ListProvidersResponse = {
    var __details: _root_.scala.Option[com.zitadel.v1.`object`.ListDetails] = _root_.scala.None
    val __result: _root_.scala.collection.immutable.VectorBuilder[com.zitadel.idp.v1.idp.Provider] = new _root_.scala.collection.immutable.VectorBuilder[com.zitadel.idp.v1.idp.Provider]
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __details = Option(__details.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.v1.`object`.ListDetails](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 18 =>
          __result += _root_.scalapb.LiteParser.readMessage[com.zitadel.idp.v1.idp.Provider](_input__)
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.ListProvidersResponse(
        details = __details,
        result = __result.result(),
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.ListProvidersResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.ListProvidersResponse(
        details = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.v1.`object`.ListDetails]]),
        result = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Seq[com.zitadel.idp.v1.idp.Provider]]).getOrElse(_root_.scala.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(485)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(485)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.v1.`object`.ListDetails
      case 2 => __out = com.zitadel.idp.v1.idp.Provider
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.ListProvidersResponse(
    details = _root_.scala.None,
    result = _root_.scala.Seq.empty
  )
  implicit class ListProvidersResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.ListProvidersResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.ListProvidersResponse](_l) {
    def details: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.`object`.ListDetails] = field(_.getDetails)((c_, f_) => c_.copy(details = Option(f_)))
    def optionalDetails: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.v1.`object`.ListDetails]] = field(_.details)((c_, f_) => c_.copy(details = f_))
    def result: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[com.zitadel.idp.v1.idp.Provider]] = field(_.result)((c_, f_) => c_.copy(result = f_))
  }
  final val DETAILS_FIELD_NUMBER = 1
  final val RESULT_FIELD_NUMBER = 2
  def of(
    details: _root_.scala.Option[com.zitadel.v1.`object`.ListDetails],
    result: _root_.scala.Seq[com.zitadel.idp.v1.idp.Provider]
  ): _root_.com.zitadel.management.v1.management.ListProvidersResponse = _root_.com.zitadel.management.v1.management.ListProvidersResponse(
    details,
    result
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.ListProvidersResponse])
}

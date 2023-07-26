// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

@SerialVersionUID(0L)
final case class AddPasswordlessRegistrationResponse(
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None,
    link: _root_.scala.Predef.String = "",
    expiration: _root_.scala.Option[com.google.protobuf.duration.Duration] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[AddPasswordlessRegistrationResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (details.isDefined) {
        val __value = details.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      
      {
        val __value = link
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      if (expiration.isDefined) {
        val __value = expiration.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
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
      details.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      {
        val __v = link
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      expiration.foreach { __v =>
        val __m = __v
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getDetails: com.zitadel.v1.`object`.ObjectDetails = details.getOrElse(com.zitadel.v1.`object`.ObjectDetails.defaultInstance)
    def clearDetails: AddPasswordlessRegistrationResponse = copy(details = _root_.scala.None)
    def withDetails(__v: com.zitadel.v1.`object`.ObjectDetails): AddPasswordlessRegistrationResponse = copy(details = Option(__v))
    def withLink(__v: _root_.scala.Predef.String): AddPasswordlessRegistrationResponse = copy(link = __v)
    def getExpiration: com.google.protobuf.duration.Duration = expiration.getOrElse(com.google.protobuf.duration.Duration.defaultInstance)
    def clearExpiration: AddPasswordlessRegistrationResponse = copy(expiration = _root_.scala.None)
    def withExpiration(__v: com.google.protobuf.duration.Duration): AddPasswordlessRegistrationResponse = copy(expiration = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => details.orNull
        case 2 => {
          val __t = link
          if (__t != "") __t else null
        }
        case 3 => expiration.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => details.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => _root_.scalapb.descriptors.PString(link)
        case 3 => expiration.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse.type = com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.AddPasswordlessRegistrationResponse])
}

object AddPasswordlessRegistrationResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse = {
    var __details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None
    var __link: _root_.scala.Predef.String = ""
    var __expiration: _root_.scala.Option[com.google.protobuf.duration.Duration] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __details = Option(__details.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.v1.`object`.ObjectDetails](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 18 =>
          __link = _input__.readStringRequireUtf8()
        case 26 =>
          __expiration = Option(__expiration.fold(_root_.scalapb.LiteParser.readMessage[com.google.protobuf.duration.Duration](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse(
        details = __details,
        link = __link,
        expiration = __expiration,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse(
        details = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]]),
        link = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        expiration = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[_root_.scala.Option[com.google.protobuf.duration.Duration]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(85)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(85)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.v1.`object`.ObjectDetails
      case 3 => __out = com.google.protobuf.duration.Duration
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse(
    details = _root_.scala.None,
    link = "",
    expiration = _root_.scala.None
  )
  implicit class AddPasswordlessRegistrationResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse](_l) {
    def details: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.`object`.ObjectDetails] = field(_.getDetails)((c_, f_) => c_.copy(details = Option(f_)))
    def optionalDetails: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]] = field(_.details)((c_, f_) => c_.copy(details = f_))
    def link: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.link)((c_, f_) => c_.copy(link = f_))
    def expiration: _root_.scalapb.lenses.Lens[UpperPB, com.google.protobuf.duration.Duration] = field(_.getExpiration)((c_, f_) => c_.copy(expiration = Option(f_)))
    def optionalExpiration: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.google.protobuf.duration.Duration]] = field(_.expiration)((c_, f_) => c_.copy(expiration = f_))
  }
  final val DETAILS_FIELD_NUMBER = 1
  final val LINK_FIELD_NUMBER = 2
  final val EXPIRATION_FIELD_NUMBER = 3
  def of(
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails],
    link: _root_.scala.Predef.String,
    expiration: _root_.scala.Option[com.google.protobuf.duration.Duration]
  ): _root_.com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse = _root_.com.zitadel.management.v1.management.AddPasswordlessRegistrationResponse(
    details,
    link,
    expiration
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.AddPasswordlessRegistrationResponse])
}

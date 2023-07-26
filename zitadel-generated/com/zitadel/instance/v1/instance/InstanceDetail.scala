// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.instance.v1.instance

@SerialVersionUID(0L)
final case class InstanceDetail(
    id: _root_.scala.Predef.String = "",
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None,
    state: com.zitadel.instance.v1.instance.State = com.zitadel.instance.v1.instance.State.STATE_UNSPECIFIED,
    name: _root_.scala.Predef.String = "",
    version: _root_.scala.Predef.String = "",
    domains: _root_.scala.Seq[com.zitadel.instance.v1.instance.Domain] = _root_.scala.Seq.empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[InstanceDetail] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = id
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      if (details.isDefined) {
        val __value = details.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      
      {
        val __value = state.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(3, __value)
        }
      };
      
      {
        val __value = name
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
        }
      };
      
      {
        val __value = version
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(5, __value)
        }
      };
      domains.foreach { __item =>
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
      {
        val __v = id
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      details.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      {
        val __v = state.value
        if (__v != 0) {
          _output__.writeEnum(3, __v)
        }
      };
      {
        val __v = name
        if (!__v.isEmpty) {
          _output__.writeString(4, __v)
        }
      };
      {
        val __v = version
        if (!__v.isEmpty) {
          _output__.writeString(5, __v)
        }
      };
      domains.foreach { __v =>
        val __m = __v
        _output__.writeTag(6, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def withId(__v: _root_.scala.Predef.String): InstanceDetail = copy(id = __v)
    def getDetails: com.zitadel.v1.`object`.ObjectDetails = details.getOrElse(com.zitadel.v1.`object`.ObjectDetails.defaultInstance)
    def clearDetails: InstanceDetail = copy(details = _root_.scala.None)
    def withDetails(__v: com.zitadel.v1.`object`.ObjectDetails): InstanceDetail = copy(details = Option(__v))
    def withState(__v: com.zitadel.instance.v1.instance.State): InstanceDetail = copy(state = __v)
    def withName(__v: _root_.scala.Predef.String): InstanceDetail = copy(name = __v)
    def withVersion(__v: _root_.scala.Predef.String): InstanceDetail = copy(version = __v)
    def clearDomains = copy(domains = _root_.scala.Seq.empty)
    def addDomains(__vs: com.zitadel.instance.v1.instance.Domain *): InstanceDetail = addAllDomains(__vs)
    def addAllDomains(__vs: Iterable[com.zitadel.instance.v1.instance.Domain]): InstanceDetail = copy(domains = domains ++ __vs)
    def withDomains(__v: _root_.scala.Seq[com.zitadel.instance.v1.instance.Domain]): InstanceDetail = copy(domains = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = id
          if (__t != "") __t else null
        }
        case 2 => details.orNull
        case 3 => {
          val __t = state.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
        case 4 => {
          val __t = name
          if (__t != "") __t else null
        }
        case 5 => {
          val __t = version
          if (__t != "") __t else null
        }
        case 6 => domains
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(id)
        case 2 => details.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => _root_.scalapb.descriptors.PEnum(state.scalaValueDescriptor)
        case 4 => _root_.scalapb.descriptors.PString(name)
        case 5 => _root_.scalapb.descriptors.PString(version)
        case 6 => _root_.scalapb.descriptors.PRepeated(domains.iterator.map(_.toPMessage).toVector)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.instance.v1.instance.InstanceDetail.type = com.zitadel.instance.v1.instance.InstanceDetail
    // @@protoc_insertion_point(GeneratedMessage[zitadel.instance.v1.InstanceDetail])
}

object InstanceDetail extends scalapb.GeneratedMessageCompanion[com.zitadel.instance.v1.instance.InstanceDetail] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.instance.v1.instance.InstanceDetail] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.instance.v1.instance.InstanceDetail = {
    var __id: _root_.scala.Predef.String = ""
    var __details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None
    var __state: com.zitadel.instance.v1.instance.State = com.zitadel.instance.v1.instance.State.STATE_UNSPECIFIED
    var __name: _root_.scala.Predef.String = ""
    var __version: _root_.scala.Predef.String = ""
    val __domains: _root_.scala.collection.immutable.VectorBuilder[com.zitadel.instance.v1.instance.Domain] = new _root_.scala.collection.immutable.VectorBuilder[com.zitadel.instance.v1.instance.Domain]
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __id = _input__.readStringRequireUtf8()
        case 18 =>
          __details = Option(__details.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.v1.`object`.ObjectDetails](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 24 =>
          __state = com.zitadel.instance.v1.instance.State.fromValue(_input__.readEnum())
        case 34 =>
          __name = _input__.readStringRequireUtf8()
        case 42 =>
          __version = _input__.readStringRequireUtf8()
        case 50 =>
          __domains += _root_.scalapb.LiteParser.readMessage[com.zitadel.instance.v1.instance.Domain](_input__)
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.instance.v1.instance.InstanceDetail(
        id = __id,
        details = __details,
        state = __state,
        name = __name,
        version = __version,
        domains = __domains.result(),
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.instance.v1.instance.InstanceDetail] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.instance.v1.instance.InstanceDetail(
        id = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        details = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]]),
        state = com.zitadel.instance.v1.instance.State.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.instance.v1.instance.State.STATE_UNSPECIFIED.scalaValueDescriptor).number),
        name = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        version = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        domains = __fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scala.Seq[com.zitadel.instance.v1.instance.Domain]]).getOrElse(_root_.scala.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = InstanceProto.javaDescriptor.getMessageTypes().get(1)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = InstanceProto.scalaDescriptor.messages(1)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 2 => __out = com.zitadel.v1.`object`.ObjectDetails
      case 6 => __out = com.zitadel.instance.v1.instance.Domain
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 3 => com.zitadel.instance.v1.instance.State
    }
  }
  lazy val defaultInstance = com.zitadel.instance.v1.instance.InstanceDetail(
    id = "",
    details = _root_.scala.None,
    state = com.zitadel.instance.v1.instance.State.STATE_UNSPECIFIED,
    name = "",
    version = "",
    domains = _root_.scala.Seq.empty
  )
  implicit class InstanceDetailLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.instance.v1.instance.InstanceDetail]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.instance.v1.instance.InstanceDetail](_l) {
    def id: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.id)((c_, f_) => c_.copy(id = f_))
    def details: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.`object`.ObjectDetails] = field(_.getDetails)((c_, f_) => c_.copy(details = Option(f_)))
    def optionalDetails: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]] = field(_.details)((c_, f_) => c_.copy(details = f_))
    def state: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.instance.v1.instance.State] = field(_.state)((c_, f_) => c_.copy(state = f_))
    def name: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.name)((c_, f_) => c_.copy(name = f_))
    def version: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.version)((c_, f_) => c_.copy(version = f_))
    def domains: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[com.zitadel.instance.v1.instance.Domain]] = field(_.domains)((c_, f_) => c_.copy(domains = f_))
  }
  final val ID_FIELD_NUMBER = 1
  final val DETAILS_FIELD_NUMBER = 2
  final val STATE_FIELD_NUMBER = 3
  final val NAME_FIELD_NUMBER = 4
  final val VERSION_FIELD_NUMBER = 5
  final val DOMAINS_FIELD_NUMBER = 6
  def of(
    id: _root_.scala.Predef.String,
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails],
    state: com.zitadel.instance.v1.instance.State,
    name: _root_.scala.Predef.String,
    version: _root_.scala.Predef.String,
    domains: _root_.scala.Seq[com.zitadel.instance.v1.instance.Domain]
  ): _root_.com.zitadel.instance.v1.instance.InstanceDetail = _root_.com.zitadel.instance.v1.instance.InstanceDetail(
    id,
    details,
    state,
    name,
    version,
    domains
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.instance.v1.InstanceDetail])
}

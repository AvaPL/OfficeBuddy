// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

/** @param query
  *  list limitations and ordering
  */
@SerialVersionUID(0L)
final case class ListUserChangesRequest(
    query: _root_.scala.Option[com.zitadel.change.v1.change.ChangeQuery] = _root_.scala.None,
    userId: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[ListUserChangesRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (query.isDefined) {
        val __value = query.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      
      {
        val __value = userId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
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
      query.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      {
        val __v = userId
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def getQuery: com.zitadel.change.v1.change.ChangeQuery = query.getOrElse(com.zitadel.change.v1.change.ChangeQuery.defaultInstance)
    def clearQuery: ListUserChangesRequest = copy(query = _root_.scala.None)
    def withQuery(__v: com.zitadel.change.v1.change.ChangeQuery): ListUserChangesRequest = copy(query = Option(__v))
    def withUserId(__v: _root_.scala.Predef.String): ListUserChangesRequest = copy(userId = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => query.orNull
        case 2 => {
          val __t = userId
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => query.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => _root_.scalapb.descriptors.PString(userId)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.ListUserChangesRequest.type = com.zitadel.management.v1.management.ListUserChangesRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.ListUserChangesRequest])
}

object ListUserChangesRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ListUserChangesRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ListUserChangesRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.ListUserChangesRequest = {
    var __query: _root_.scala.Option[com.zitadel.change.v1.change.ChangeQuery] = _root_.scala.None
    var __userId: _root_.scala.Predef.String = ""
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __query = Option(__query.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.change.v1.change.ChangeQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 18 =>
          __userId = _input__.readStringRequireUtf8()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.ListUserChangesRequest(
        query = __query,
        userId = __userId,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.ListUserChangesRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.ListUserChangesRequest(
        query = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.change.v1.change.ChangeQuery]]),
        userId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(14)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(14)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.change.v1.change.ChangeQuery
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.ListUserChangesRequest(
    query = _root_.scala.None,
    userId = ""
  )
  implicit class ListUserChangesRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.ListUserChangesRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.ListUserChangesRequest](_l) {
    def query: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.change.v1.change.ChangeQuery] = field(_.getQuery)((c_, f_) => c_.copy(query = Option(f_)))
    def optionalQuery: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.change.v1.change.ChangeQuery]] = field(_.query)((c_, f_) => c_.copy(query = f_))
    def userId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.userId)((c_, f_) => c_.copy(userId = f_))
  }
  final val QUERY_FIELD_NUMBER = 1
  final val USER_ID_FIELD_NUMBER = 2
  def of(
    query: _root_.scala.Option[com.zitadel.change.v1.change.ChangeQuery],
    userId: _root_.scala.Predef.String
  ): _root_.com.zitadel.management.v1.management.ListUserChangesRequest = _root_.com.zitadel.management.v1.management.ListUserChangesRequest(
    query,
    userId
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.ListUserChangesRequest])
}

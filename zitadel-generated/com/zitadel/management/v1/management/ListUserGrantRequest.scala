// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

/** @param query
  *  list limitations and ordering
  * @param queries
  *  criteria the client is looking for
  */
@SerialVersionUID(0L)
final case class ListUserGrantRequest(
    query: _root_.scala.Option[com.zitadel.v1.`object`.ListQuery] = _root_.scala.None,
    queries: _root_.scala.Seq[com.zitadel.user.v1.user.UserGrantQuery] = _root_.scala.Seq.empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[ListUserGrantRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (query.isDefined) {
        val __value = query.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      queries.foreach { __item =>
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
      query.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      queries.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getQuery: com.zitadel.v1.`object`.ListQuery = query.getOrElse(com.zitadel.v1.`object`.ListQuery.defaultInstance)
    def clearQuery: ListUserGrantRequest = copy(query = _root_.scala.None)
    def withQuery(__v: com.zitadel.v1.`object`.ListQuery): ListUserGrantRequest = copy(query = Option(__v))
    def clearQueries = copy(queries = _root_.scala.Seq.empty)
    def addQueries(__vs: com.zitadel.user.v1.user.UserGrantQuery *): ListUserGrantRequest = addAllQueries(__vs)
    def addAllQueries(__vs: Iterable[com.zitadel.user.v1.user.UserGrantQuery]): ListUserGrantRequest = copy(queries = queries ++ __vs)
    def withQueries(__v: _root_.scala.Seq[com.zitadel.user.v1.user.UserGrantQuery]): ListUserGrantRequest = copy(queries = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => query.orNull
        case 2 => queries
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => query.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => _root_.scalapb.descriptors.PRepeated(queries.iterator.map(_.toPMessage).toVector)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.ListUserGrantRequest.type = com.zitadel.management.v1.management.ListUserGrantRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.ListUserGrantRequest])
}

object ListUserGrantRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ListUserGrantRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.ListUserGrantRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.ListUserGrantRequest = {
    var __query: _root_.scala.Option[com.zitadel.v1.`object`.ListQuery] = _root_.scala.None
    val __queries: _root_.scala.collection.immutable.VectorBuilder[com.zitadel.user.v1.user.UserGrantQuery] = new _root_.scala.collection.immutable.VectorBuilder[com.zitadel.user.v1.user.UserGrantQuery]
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __query = Option(__query.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.v1.`object`.ListQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 18 =>
          __queries += _root_.scalapb.LiteParser.readMessage[com.zitadel.user.v1.user.UserGrantQuery](_input__)
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.ListUserGrantRequest(
        query = __query,
        queries = __queries.result(),
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.ListUserGrantRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.ListUserGrantRequest(
        query = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.v1.`object`.ListQuery]]),
        queries = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Seq[com.zitadel.user.v1.user.UserGrantQuery]]).getOrElse(_root_.scala.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(278)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(278)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.v1.`object`.ListQuery
      case 2 => __out = com.zitadel.user.v1.user.UserGrantQuery
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.ListUserGrantRequest(
    query = _root_.scala.None,
    queries = _root_.scala.Seq.empty
  )
  implicit class ListUserGrantRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.ListUserGrantRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.ListUserGrantRequest](_l) {
    def query: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.`object`.ListQuery] = field(_.getQuery)((c_, f_) => c_.copy(query = Option(f_)))
    def optionalQuery: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.v1.`object`.ListQuery]] = field(_.query)((c_, f_) => c_.copy(query = f_))
    def queries: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[com.zitadel.user.v1.user.UserGrantQuery]] = field(_.queries)((c_, f_) => c_.copy(queries = f_))
  }
  final val QUERY_FIELD_NUMBER = 1
  final val QUERIES_FIELD_NUMBER = 2
  def of(
    query: _root_.scala.Option[com.zitadel.v1.`object`.ListQuery],
    queries: _root_.scala.Seq[com.zitadel.user.v1.user.UserGrantQuery]
  ): _root_.com.zitadel.management.v1.management.ListUserGrantRequest = _root_.com.zitadel.management.v1.management.ListUserGrantRequest(
    query,
    queries
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.ListUserGrantRequest])
}

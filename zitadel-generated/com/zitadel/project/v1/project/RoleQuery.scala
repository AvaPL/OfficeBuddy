// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.project.v1.project

@SerialVersionUID(0L)
final case class RoleQuery(
    query: com.zitadel.project.v1.project.RoleQuery.Query = com.zitadel.project.v1.project.RoleQuery.Query.Empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[RoleQuery] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (query.keyQuery.isDefined) {
        val __value = query.keyQuery.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (query.displayNameQuery.isDefined) {
        val __value = query.displayNameQuery.get
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
      query.keyQuery.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      query.displayNameQuery.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getKeyQuery: com.zitadel.project.v1.project.RoleKeyQuery = query.keyQuery.getOrElse(com.zitadel.project.v1.project.RoleKeyQuery.defaultInstance)
    def withKeyQuery(__v: com.zitadel.project.v1.project.RoleKeyQuery): RoleQuery = copy(query = com.zitadel.project.v1.project.RoleQuery.Query.KeyQuery(__v))
    def getDisplayNameQuery: com.zitadel.project.v1.project.RoleDisplayNameQuery = query.displayNameQuery.getOrElse(com.zitadel.project.v1.project.RoleDisplayNameQuery.defaultInstance)
    def withDisplayNameQuery(__v: com.zitadel.project.v1.project.RoleDisplayNameQuery): RoleQuery = copy(query = com.zitadel.project.v1.project.RoleQuery.Query.DisplayNameQuery(__v))
    def clearQuery: RoleQuery = copy(query = com.zitadel.project.v1.project.RoleQuery.Query.Empty)
    def withQuery(__v: com.zitadel.project.v1.project.RoleQuery.Query): RoleQuery = copy(query = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => query.keyQuery.orNull
        case 2 => query.displayNameQuery.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => query.keyQuery.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => query.displayNameQuery.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.project.v1.project.RoleQuery.type = com.zitadel.project.v1.project.RoleQuery
    // @@protoc_insertion_point(GeneratedMessage[zitadel.project.v1.RoleQuery])
}

object RoleQuery extends scalapb.GeneratedMessageCompanion[com.zitadel.project.v1.project.RoleQuery] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.project.v1.project.RoleQuery] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.project.v1.project.RoleQuery = {
    var __query: com.zitadel.project.v1.project.RoleQuery.Query = com.zitadel.project.v1.project.RoleQuery.Query.Empty
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __query = com.zitadel.project.v1.project.RoleQuery.Query.KeyQuery(__query.keyQuery.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.project.v1.project.RoleKeyQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 18 =>
          __query = com.zitadel.project.v1.project.RoleQuery.Query.DisplayNameQuery(__query.displayNameQuery.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.project.v1.project.RoleDisplayNameQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.project.v1.project.RoleQuery(
        query = __query,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.project.v1.project.RoleQuery] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.project.v1.project.RoleQuery(
        query = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.project.v1.project.RoleKeyQuery]]).map(com.zitadel.project.v1.project.RoleQuery.Query.KeyQuery(_))
            .orElse[com.zitadel.project.v1.project.RoleQuery.Query](__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.project.v1.project.RoleDisplayNameQuery]]).map(com.zitadel.project.v1.project.RoleQuery.Query.DisplayNameQuery(_)))
            .getOrElse(com.zitadel.project.v1.project.RoleQuery.Query.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ProjectProto.javaDescriptor.getMessageTypes().get(6)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ProjectProto.scalaDescriptor.messages(6)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.project.v1.project.RoleKeyQuery
      case 2 => __out = com.zitadel.project.v1.project.RoleDisplayNameQuery
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.project.v1.project.RoleQuery(
    query = com.zitadel.project.v1.project.RoleQuery.Query.Empty
  )
  sealed trait Query extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isKeyQuery: _root_.scala.Boolean = false
    def isDisplayNameQuery: _root_.scala.Boolean = false
    def keyQuery: _root_.scala.Option[com.zitadel.project.v1.project.RoleKeyQuery] = _root_.scala.None
    def displayNameQuery: _root_.scala.Option[com.zitadel.project.v1.project.RoleDisplayNameQuery] = _root_.scala.None
  }
  object Query {
    @SerialVersionUID(0L)
    case object Empty extends com.zitadel.project.v1.project.RoleQuery.Query {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class KeyQuery(value: com.zitadel.project.v1.project.RoleKeyQuery) extends com.zitadel.project.v1.project.RoleQuery.Query {
      type ValueType = com.zitadel.project.v1.project.RoleKeyQuery
      override def isKeyQuery: _root_.scala.Boolean = true
      override def keyQuery: _root_.scala.Option[com.zitadel.project.v1.project.RoleKeyQuery] = Some(value)
      override def number: _root_.scala.Int = 1
    }
    @SerialVersionUID(0L)
    final case class DisplayNameQuery(value: com.zitadel.project.v1.project.RoleDisplayNameQuery) extends com.zitadel.project.v1.project.RoleQuery.Query {
      type ValueType = com.zitadel.project.v1.project.RoleDisplayNameQuery
      override def isDisplayNameQuery: _root_.scala.Boolean = true
      override def displayNameQuery: _root_.scala.Option[com.zitadel.project.v1.project.RoleDisplayNameQuery] = Some(value)
      override def number: _root_.scala.Int = 2
    }
  }
  implicit class RoleQueryLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.RoleQuery]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.project.v1.project.RoleQuery](_l) {
    def keyQuery: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.RoleKeyQuery] = field(_.getKeyQuery)((c_, f_) => c_.copy(query = com.zitadel.project.v1.project.RoleQuery.Query.KeyQuery(f_)))
    def displayNameQuery: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.RoleDisplayNameQuery] = field(_.getDisplayNameQuery)((c_, f_) => c_.copy(query = com.zitadel.project.v1.project.RoleQuery.Query.DisplayNameQuery(f_)))
    def query: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.RoleQuery.Query] = field(_.query)((c_, f_) => c_.copy(query = f_))
  }
  final val KEY_QUERY_FIELD_NUMBER = 1
  final val DISPLAY_NAME_QUERY_FIELD_NUMBER = 2
  def of(
    query: com.zitadel.project.v1.project.RoleQuery.Query
  ): _root_.com.zitadel.project.v1.project.RoleQuery = _root_.com.zitadel.project.v1.project.RoleQuery(
    query
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.project.v1.RoleQuery])
}

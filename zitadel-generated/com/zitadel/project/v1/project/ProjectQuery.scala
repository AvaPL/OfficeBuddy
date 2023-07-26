// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.project.v1.project

@SerialVersionUID(0L)
final case class ProjectQuery(
    query: com.zitadel.project.v1.project.ProjectQuery.Query = com.zitadel.project.v1.project.ProjectQuery.Query.Empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[ProjectQuery] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (query.nameQuery.isDefined) {
        val __value = query.nameQuery.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (query.projectResourceOwnerQuery.isDefined) {
        val __value = query.projectResourceOwnerQuery.get
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
      query.nameQuery.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      query.projectResourceOwnerQuery.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getNameQuery: com.zitadel.project.v1.project.ProjectNameQuery = query.nameQuery.getOrElse(com.zitadel.project.v1.project.ProjectNameQuery.defaultInstance)
    def withNameQuery(__v: com.zitadel.project.v1.project.ProjectNameQuery): ProjectQuery = copy(query = com.zitadel.project.v1.project.ProjectQuery.Query.NameQuery(__v))
    def getProjectResourceOwnerQuery: com.zitadel.project.v1.project.ProjectResourceOwnerQuery = query.projectResourceOwnerQuery.getOrElse(com.zitadel.project.v1.project.ProjectResourceOwnerQuery.defaultInstance)
    def withProjectResourceOwnerQuery(__v: com.zitadel.project.v1.project.ProjectResourceOwnerQuery): ProjectQuery = copy(query = com.zitadel.project.v1.project.ProjectQuery.Query.ProjectResourceOwnerQuery(__v))
    def clearQuery: ProjectQuery = copy(query = com.zitadel.project.v1.project.ProjectQuery.Query.Empty)
    def withQuery(__v: com.zitadel.project.v1.project.ProjectQuery.Query): ProjectQuery = copy(query = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => query.nameQuery.orNull
        case 2 => query.projectResourceOwnerQuery.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => query.nameQuery.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => query.projectResourceOwnerQuery.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.project.v1.project.ProjectQuery.type = com.zitadel.project.v1.project.ProjectQuery
    // @@protoc_insertion_point(GeneratedMessage[zitadel.project.v1.ProjectQuery])
}

object ProjectQuery extends scalapb.GeneratedMessageCompanion[com.zitadel.project.v1.project.ProjectQuery] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.project.v1.project.ProjectQuery] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.project.v1.project.ProjectQuery = {
    var __query: com.zitadel.project.v1.project.ProjectQuery.Query = com.zitadel.project.v1.project.ProjectQuery.Query.Empty
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __query = com.zitadel.project.v1.project.ProjectQuery.Query.NameQuery(__query.nameQuery.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.project.v1.project.ProjectNameQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 18 =>
          __query = com.zitadel.project.v1.project.ProjectQuery.Query.ProjectResourceOwnerQuery(__query.projectResourceOwnerQuery.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.project.v1.project.ProjectResourceOwnerQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.project.v1.project.ProjectQuery(
        query = __query,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.project.v1.project.ProjectQuery] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.project.v1.project.ProjectQuery(
        query = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.project.v1.project.ProjectNameQuery]]).map(com.zitadel.project.v1.project.ProjectQuery.Query.NameQuery(_))
            .orElse[com.zitadel.project.v1.project.ProjectQuery.Query](__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.project.v1.project.ProjectResourceOwnerQuery]]).map(com.zitadel.project.v1.project.ProjectQuery.Query.ProjectResourceOwnerQuery(_)))
            .getOrElse(com.zitadel.project.v1.project.ProjectQuery.Query.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ProjectProto.javaDescriptor.getMessageTypes().get(2)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ProjectProto.scalaDescriptor.messages(2)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.project.v1.project.ProjectNameQuery
      case 2 => __out = com.zitadel.project.v1.project.ProjectResourceOwnerQuery
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.project.v1.project.ProjectQuery(
    query = com.zitadel.project.v1.project.ProjectQuery.Query.Empty
  )
  sealed trait Query extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isNameQuery: _root_.scala.Boolean = false
    def isProjectResourceOwnerQuery: _root_.scala.Boolean = false
    def nameQuery: _root_.scala.Option[com.zitadel.project.v1.project.ProjectNameQuery] = _root_.scala.None
    def projectResourceOwnerQuery: _root_.scala.Option[com.zitadel.project.v1.project.ProjectResourceOwnerQuery] = _root_.scala.None
  }
  object Query {
    @SerialVersionUID(0L)
    case object Empty extends com.zitadel.project.v1.project.ProjectQuery.Query {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class NameQuery(value: com.zitadel.project.v1.project.ProjectNameQuery) extends com.zitadel.project.v1.project.ProjectQuery.Query {
      type ValueType = com.zitadel.project.v1.project.ProjectNameQuery
      override def isNameQuery: _root_.scala.Boolean = true
      override def nameQuery: _root_.scala.Option[com.zitadel.project.v1.project.ProjectNameQuery] = Some(value)
      override def number: _root_.scala.Int = 1
    }
    @SerialVersionUID(0L)
    final case class ProjectResourceOwnerQuery(value: com.zitadel.project.v1.project.ProjectResourceOwnerQuery) extends com.zitadel.project.v1.project.ProjectQuery.Query {
      type ValueType = com.zitadel.project.v1.project.ProjectResourceOwnerQuery
      override def isProjectResourceOwnerQuery: _root_.scala.Boolean = true
      override def projectResourceOwnerQuery: _root_.scala.Option[com.zitadel.project.v1.project.ProjectResourceOwnerQuery] = Some(value)
      override def number: _root_.scala.Int = 2
    }
  }
  implicit class ProjectQueryLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.ProjectQuery]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.project.v1.project.ProjectQuery](_l) {
    def nameQuery: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.ProjectNameQuery] = field(_.getNameQuery)((c_, f_) => c_.copy(query = com.zitadel.project.v1.project.ProjectQuery.Query.NameQuery(f_)))
    def projectResourceOwnerQuery: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.ProjectResourceOwnerQuery] = field(_.getProjectResourceOwnerQuery)((c_, f_) => c_.copy(query = com.zitadel.project.v1.project.ProjectQuery.Query.ProjectResourceOwnerQuery(f_)))
    def query: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.ProjectQuery.Query] = field(_.query)((c_, f_) => c_.copy(query = f_))
  }
  final val NAME_QUERY_FIELD_NUMBER = 1
  final val PROJECT_RESOURCE_OWNER_QUERY_FIELD_NUMBER = 2
  def of(
    query: com.zitadel.project.v1.project.ProjectQuery.Query
  ): _root_.com.zitadel.project.v1.project.ProjectQuery = _root_.com.zitadel.project.v1.project.ProjectQuery(
    query
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.project.v1.ProjectQuery])
}

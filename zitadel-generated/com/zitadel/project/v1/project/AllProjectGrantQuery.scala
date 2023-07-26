// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.project.v1.project

@SerialVersionUID(0L)
final case class AllProjectGrantQuery(
    query: com.zitadel.project.v1.project.AllProjectGrantQuery.Query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.Empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[AllProjectGrantQuery] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (query.projectNameQuery.isDefined) {
        val __value = query.projectNameQuery.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (query.roleKeyQuery.isDefined) {
        val __value = query.roleKeyQuery.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (query.projectIdQuery.isDefined) {
        val __value = query.projectIdQuery.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (query.grantedOrgIdQuery.isDefined) {
        val __value = query.grantedOrgIdQuery.get
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
      query.projectNameQuery.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      query.roleKeyQuery.foreach { __v =>
        val __m = __v
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      query.projectIdQuery.foreach { __v =>
        val __m = __v
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      query.grantedOrgIdQuery.foreach { __v =>
        val __m = __v
        _output__.writeTag(4, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getProjectNameQuery: com.zitadel.project.v1.project.GrantProjectNameQuery = query.projectNameQuery.getOrElse(com.zitadel.project.v1.project.GrantProjectNameQuery.defaultInstance)
    def withProjectNameQuery(__v: com.zitadel.project.v1.project.GrantProjectNameQuery): AllProjectGrantQuery = copy(query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.ProjectNameQuery(__v))
    def getRoleKeyQuery: com.zitadel.project.v1.project.GrantRoleKeyQuery = query.roleKeyQuery.getOrElse(com.zitadel.project.v1.project.GrantRoleKeyQuery.defaultInstance)
    def withRoleKeyQuery(__v: com.zitadel.project.v1.project.GrantRoleKeyQuery): AllProjectGrantQuery = copy(query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.RoleKeyQuery(__v))
    def getProjectIdQuery: com.zitadel.project.v1.project.ProjectIDQuery = query.projectIdQuery.getOrElse(com.zitadel.project.v1.project.ProjectIDQuery.defaultInstance)
    def withProjectIdQuery(__v: com.zitadel.project.v1.project.ProjectIDQuery): AllProjectGrantQuery = copy(query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.ProjectIdQuery(__v))
    def getGrantedOrgIdQuery: com.zitadel.project.v1.project.GrantedOrgIDQuery = query.grantedOrgIdQuery.getOrElse(com.zitadel.project.v1.project.GrantedOrgIDQuery.defaultInstance)
    def withGrantedOrgIdQuery(__v: com.zitadel.project.v1.project.GrantedOrgIDQuery): AllProjectGrantQuery = copy(query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.GrantedOrgIdQuery(__v))
    def clearQuery: AllProjectGrantQuery = copy(query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.Empty)
    def withQuery(__v: com.zitadel.project.v1.project.AllProjectGrantQuery.Query): AllProjectGrantQuery = copy(query = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => query.projectNameQuery.orNull
        case 2 => query.roleKeyQuery.orNull
        case 3 => query.projectIdQuery.orNull
        case 4 => query.grantedOrgIdQuery.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => query.projectNameQuery.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => query.roleKeyQuery.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => query.projectIdQuery.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 4 => query.grantedOrgIdQuery.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.project.v1.project.AllProjectGrantQuery.type = com.zitadel.project.v1.project.AllProjectGrantQuery
    // @@protoc_insertion_point(GeneratedMessage[zitadel.project.v1.AllProjectGrantQuery])
}

object AllProjectGrantQuery extends scalapb.GeneratedMessageCompanion[com.zitadel.project.v1.project.AllProjectGrantQuery] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.project.v1.project.AllProjectGrantQuery] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.project.v1.project.AllProjectGrantQuery = {
    var __query: com.zitadel.project.v1.project.AllProjectGrantQuery.Query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.Empty
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.ProjectNameQuery(__query.projectNameQuery.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.project.v1.project.GrantProjectNameQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 18 =>
          __query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.RoleKeyQuery(__query.roleKeyQuery.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.project.v1.project.GrantRoleKeyQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 26 =>
          __query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.ProjectIdQuery(__query.projectIdQuery.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.project.v1.project.ProjectIDQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 34 =>
          __query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.GrantedOrgIdQuery(__query.grantedOrgIdQuery.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.project.v1.project.GrantedOrgIDQuery](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.project.v1.project.AllProjectGrantQuery(
        query = __query,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.project.v1.project.AllProjectGrantQuery] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.project.v1.project.AllProjectGrantQuery(
        query = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.project.v1.project.GrantProjectNameQuery]]).map(com.zitadel.project.v1.project.AllProjectGrantQuery.Query.ProjectNameQuery(_))
            .orElse[com.zitadel.project.v1.project.AllProjectGrantQuery.Query](__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.project.v1.project.GrantRoleKeyQuery]]).map(com.zitadel.project.v1.project.AllProjectGrantQuery.Query.RoleKeyQuery(_)))
            .orElse[com.zitadel.project.v1.project.AllProjectGrantQuery.Query](__fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[_root_.scala.Option[com.zitadel.project.v1.project.ProjectIDQuery]]).map(com.zitadel.project.v1.project.AllProjectGrantQuery.Query.ProjectIdQuery(_)))
            .orElse[com.zitadel.project.v1.project.AllProjectGrantQuery.Query](__fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).flatMap(_.as[_root_.scala.Option[com.zitadel.project.v1.project.GrantedOrgIDQuery]]).map(com.zitadel.project.v1.project.AllProjectGrantQuery.Query.GrantedOrgIdQuery(_)))
            .getOrElse(com.zitadel.project.v1.project.AllProjectGrantQuery.Query.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ProjectProto.javaDescriptor.getMessageTypes().get(10)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ProjectProto.scalaDescriptor.messages(10)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.project.v1.project.GrantProjectNameQuery
      case 2 => __out = com.zitadel.project.v1.project.GrantRoleKeyQuery
      case 3 => __out = com.zitadel.project.v1.project.ProjectIDQuery
      case 4 => __out = com.zitadel.project.v1.project.GrantedOrgIDQuery
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.project.v1.project.AllProjectGrantQuery(
    query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.Empty
  )
  sealed trait Query extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isProjectNameQuery: _root_.scala.Boolean = false
    def isRoleKeyQuery: _root_.scala.Boolean = false
    def isProjectIdQuery: _root_.scala.Boolean = false
    def isGrantedOrgIdQuery: _root_.scala.Boolean = false
    def projectNameQuery: _root_.scala.Option[com.zitadel.project.v1.project.GrantProjectNameQuery] = _root_.scala.None
    def roleKeyQuery: _root_.scala.Option[com.zitadel.project.v1.project.GrantRoleKeyQuery] = _root_.scala.None
    def projectIdQuery: _root_.scala.Option[com.zitadel.project.v1.project.ProjectIDQuery] = _root_.scala.None
    def grantedOrgIdQuery: _root_.scala.Option[com.zitadel.project.v1.project.GrantedOrgIDQuery] = _root_.scala.None
  }
  object Query {
    @SerialVersionUID(0L)
    case object Empty extends com.zitadel.project.v1.project.AllProjectGrantQuery.Query {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class ProjectNameQuery(value: com.zitadel.project.v1.project.GrantProjectNameQuery) extends com.zitadel.project.v1.project.AllProjectGrantQuery.Query {
      type ValueType = com.zitadel.project.v1.project.GrantProjectNameQuery
      override def isProjectNameQuery: _root_.scala.Boolean = true
      override def projectNameQuery: _root_.scala.Option[com.zitadel.project.v1.project.GrantProjectNameQuery] = Some(value)
      override def number: _root_.scala.Int = 1
    }
    @SerialVersionUID(0L)
    final case class RoleKeyQuery(value: com.zitadel.project.v1.project.GrantRoleKeyQuery) extends com.zitadel.project.v1.project.AllProjectGrantQuery.Query {
      type ValueType = com.zitadel.project.v1.project.GrantRoleKeyQuery
      override def isRoleKeyQuery: _root_.scala.Boolean = true
      override def roleKeyQuery: _root_.scala.Option[com.zitadel.project.v1.project.GrantRoleKeyQuery] = Some(value)
      override def number: _root_.scala.Int = 2
    }
    @SerialVersionUID(0L)
    final case class ProjectIdQuery(value: com.zitadel.project.v1.project.ProjectIDQuery) extends com.zitadel.project.v1.project.AllProjectGrantQuery.Query {
      type ValueType = com.zitadel.project.v1.project.ProjectIDQuery
      override def isProjectIdQuery: _root_.scala.Boolean = true
      override def projectIdQuery: _root_.scala.Option[com.zitadel.project.v1.project.ProjectIDQuery] = Some(value)
      override def number: _root_.scala.Int = 3
    }
    @SerialVersionUID(0L)
    final case class GrantedOrgIdQuery(value: com.zitadel.project.v1.project.GrantedOrgIDQuery) extends com.zitadel.project.v1.project.AllProjectGrantQuery.Query {
      type ValueType = com.zitadel.project.v1.project.GrantedOrgIDQuery
      override def isGrantedOrgIdQuery: _root_.scala.Boolean = true
      override def grantedOrgIdQuery: _root_.scala.Option[com.zitadel.project.v1.project.GrantedOrgIDQuery] = Some(value)
      override def number: _root_.scala.Int = 4
    }
  }
  implicit class AllProjectGrantQueryLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.AllProjectGrantQuery]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.project.v1.project.AllProjectGrantQuery](_l) {
    def projectNameQuery: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.GrantProjectNameQuery] = field(_.getProjectNameQuery)((c_, f_) => c_.copy(query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.ProjectNameQuery(f_)))
    def roleKeyQuery: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.GrantRoleKeyQuery] = field(_.getRoleKeyQuery)((c_, f_) => c_.copy(query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.RoleKeyQuery(f_)))
    def projectIdQuery: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.ProjectIDQuery] = field(_.getProjectIdQuery)((c_, f_) => c_.copy(query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.ProjectIdQuery(f_)))
    def grantedOrgIdQuery: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.GrantedOrgIDQuery] = field(_.getGrantedOrgIdQuery)((c_, f_) => c_.copy(query = com.zitadel.project.v1.project.AllProjectGrantQuery.Query.GrantedOrgIdQuery(f_)))
    def query: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.project.v1.project.AllProjectGrantQuery.Query] = field(_.query)((c_, f_) => c_.copy(query = f_))
  }
  final val PROJECT_NAME_QUERY_FIELD_NUMBER = 1
  final val ROLE_KEY_QUERY_FIELD_NUMBER = 2
  final val PROJECT_ID_QUERY_FIELD_NUMBER = 3
  final val GRANTED_ORG_ID_QUERY_FIELD_NUMBER = 4
  def of(
    query: com.zitadel.project.v1.project.AllProjectGrantQuery.Query
  ): _root_.com.zitadel.project.v1.project.AllProjectGrantQuery = _root_.com.zitadel.project.v1.project.AllProjectGrantQuery(
    query
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.project.v1.AllProjectGrantQuery])
}

// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.session.v2alpha.session_service

@SerialVersionUID(0L)
final case class CheckUser(
    search: com.zitadel.session.v2alpha.session_service.CheckUser.Search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.Empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[CheckUser] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (search.userId.isDefined) {
        val __value = search.userId.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
      };
      if (search.loginName.isDefined) {
        val __value = search.loginName.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
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
      search.userId.foreach { __v =>
        val __m = __v
        _output__.writeString(1, __m)
      };
      search.loginName.foreach { __v =>
        val __m = __v
        _output__.writeString(2, __m)
      };
      unknownFields.writeTo(_output__)
    }
    def getUserId: _root_.scala.Predef.String = search.userId.getOrElse("")
    def withUserId(__v: _root_.scala.Predef.String): CheckUser = copy(search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.UserId(__v))
    def getLoginName: _root_.scala.Predef.String = search.loginName.getOrElse("")
    def withLoginName(__v: _root_.scala.Predef.String): CheckUser = copy(search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.LoginName(__v))
    def clearSearch: CheckUser = copy(search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.Empty)
    def withSearch(__v: com.zitadel.session.v2alpha.session_service.CheckUser.Search): CheckUser = copy(search = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => search.userId.orNull
        case 2 => search.loginName.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => search.userId.map(_root_.scalapb.descriptors.PString(_)).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => search.loginName.map(_root_.scalapb.descriptors.PString(_)).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.session.v2alpha.session_service.CheckUser.type = com.zitadel.session.v2alpha.session_service.CheckUser
    // @@protoc_insertion_point(GeneratedMessage[zitadel.session.v2alpha.CheckUser])
}

object CheckUser extends scalapb.GeneratedMessageCompanion[com.zitadel.session.v2alpha.session_service.CheckUser] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.session.v2alpha.session_service.CheckUser] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.session.v2alpha.session_service.CheckUser = {
    var __search: com.zitadel.session.v2alpha.session_service.CheckUser.Search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.Empty
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.UserId(_input__.readStringRequireUtf8())
        case 18 =>
          __search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.LoginName(_input__.readStringRequireUtf8())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.session.v2alpha.session_service.CheckUser(
        search = __search,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.session.v2alpha.session_service.CheckUser] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.session.v2alpha.session_service.CheckUser(
        search = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Predef.String]]).map(com.zitadel.session.v2alpha.session_service.CheckUser.Search.UserId(_))
            .orElse[com.zitadel.session.v2alpha.session_service.CheckUser.Search](__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Predef.String]]).map(com.zitadel.session.v2alpha.session_service.CheckUser.Search.LoginName(_)))
            .getOrElse(com.zitadel.session.v2alpha.session_service.CheckUser.Search.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SessionServiceProto.javaDescriptor.getMessageTypes().get(11)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SessionServiceProto.scalaDescriptor.messages(11)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.session.v2alpha.session_service.CheckUser(
    search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.Empty
  )
  sealed trait Search extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isUserId: _root_.scala.Boolean = false
    def isLoginName: _root_.scala.Boolean = false
    def userId: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None
    def loginName: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None
  }
  object Search {
    @SerialVersionUID(0L)
    case object Empty extends com.zitadel.session.v2alpha.session_service.CheckUser.Search {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class UserId(value: _root_.scala.Predef.String) extends com.zitadel.session.v2alpha.session_service.CheckUser.Search {
      type ValueType = _root_.scala.Predef.String
      override def isUserId: _root_.scala.Boolean = true
      override def userId: _root_.scala.Option[_root_.scala.Predef.String] = Some(value)
      override def number: _root_.scala.Int = 1
    }
    @SerialVersionUID(0L)
    final case class LoginName(value: _root_.scala.Predef.String) extends com.zitadel.session.v2alpha.session_service.CheckUser.Search {
      type ValueType = _root_.scala.Predef.String
      override def isLoginName: _root_.scala.Boolean = true
      override def loginName: _root_.scala.Option[_root_.scala.Predef.String] = Some(value)
      override def number: _root_.scala.Int = 2
    }
  }
  implicit class CheckUserLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.session.v2alpha.session_service.CheckUser]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.session.v2alpha.session_service.CheckUser](_l) {
    def userId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.getUserId)((c_, f_) => c_.copy(search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.UserId(f_)))
    def loginName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.getLoginName)((c_, f_) => c_.copy(search = com.zitadel.session.v2alpha.session_service.CheckUser.Search.LoginName(f_)))
    def search: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.session.v2alpha.session_service.CheckUser.Search] = field(_.search)((c_, f_) => c_.copy(search = f_))
  }
  final val USER_ID_FIELD_NUMBER = 1
  final val LOGIN_NAME_FIELD_NUMBER = 2
  def of(
    search: com.zitadel.session.v2alpha.session_service.CheckUser.Search
  ): _root_.com.zitadel.session.v2alpha.session_service.CheckUser = _root_.com.zitadel.session.v2alpha.session_service.CheckUser(
    search
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.session.v2alpha.CheckUser])
}

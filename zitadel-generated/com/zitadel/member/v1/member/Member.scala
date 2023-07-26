// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.member.v1.member

@SerialVersionUID(0L)
final case class Member(
    userId: _root_.scala.Predef.String = "",
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None,
    roles: _root_.scala.Seq[_root_.scala.Predef.String] = _root_.scala.Seq.empty,
    preferredLoginName: _root_.scala.Predef.String = "",
    email: _root_.scala.Predef.String = "",
    firstName: _root_.scala.Predef.String = "",
    lastName: _root_.scala.Predef.String = "",
    displayName: _root_.scala.Predef.String = "",
    avatarUrl: _root_.scala.Predef.String = "",
    userType: com.zitadel.user.v1.user.Type = com.zitadel.user.v1.user.Type.TYPE_UNSPECIFIED,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[Member] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = userId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      if (details.isDefined) {
        val __value = details.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      roles.foreach { __item =>
        val __value = __item
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
      }
      
      {
        val __value = preferredLoginName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
        }
      };
      
      {
        val __value = email
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(5, __value)
        }
      };
      
      {
        val __value = firstName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(6, __value)
        }
      };
      
      {
        val __value = lastName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(7, __value)
        }
      };
      
      {
        val __value = displayName
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(8, __value)
        }
      };
      
      {
        val __value = avatarUrl
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(9, __value)
        }
      };
      
      {
        val __value = userType.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(10, __value)
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
      {
        val __v = userId
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
      roles.foreach { __v =>
        val __m = __v
        _output__.writeString(3, __m)
      };
      {
        val __v = preferredLoginName
        if (!__v.isEmpty) {
          _output__.writeString(4, __v)
        }
      };
      {
        val __v = email
        if (!__v.isEmpty) {
          _output__.writeString(5, __v)
        }
      };
      {
        val __v = firstName
        if (!__v.isEmpty) {
          _output__.writeString(6, __v)
        }
      };
      {
        val __v = lastName
        if (!__v.isEmpty) {
          _output__.writeString(7, __v)
        }
      };
      {
        val __v = displayName
        if (!__v.isEmpty) {
          _output__.writeString(8, __v)
        }
      };
      {
        val __v = avatarUrl
        if (!__v.isEmpty) {
          _output__.writeString(9, __v)
        }
      };
      {
        val __v = userType.value
        if (__v != 0) {
          _output__.writeEnum(10, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withUserId(__v: _root_.scala.Predef.String): Member = copy(userId = __v)
    def getDetails: com.zitadel.v1.`object`.ObjectDetails = details.getOrElse(com.zitadel.v1.`object`.ObjectDetails.defaultInstance)
    def clearDetails: Member = copy(details = _root_.scala.None)
    def withDetails(__v: com.zitadel.v1.`object`.ObjectDetails): Member = copy(details = Option(__v))
    def clearRoles = copy(roles = _root_.scala.Seq.empty)
    def addRoles(__vs: _root_.scala.Predef.String *): Member = addAllRoles(__vs)
    def addAllRoles(__vs: Iterable[_root_.scala.Predef.String]): Member = copy(roles = roles ++ __vs)
    def withRoles(__v: _root_.scala.Seq[_root_.scala.Predef.String]): Member = copy(roles = __v)
    def withPreferredLoginName(__v: _root_.scala.Predef.String): Member = copy(preferredLoginName = __v)
    def withEmail(__v: _root_.scala.Predef.String): Member = copy(email = __v)
    def withFirstName(__v: _root_.scala.Predef.String): Member = copy(firstName = __v)
    def withLastName(__v: _root_.scala.Predef.String): Member = copy(lastName = __v)
    def withDisplayName(__v: _root_.scala.Predef.String): Member = copy(displayName = __v)
    def withAvatarUrl(__v: _root_.scala.Predef.String): Member = copy(avatarUrl = __v)
    def withUserType(__v: com.zitadel.user.v1.user.Type): Member = copy(userType = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = userId
          if (__t != "") __t else null
        }
        case 2 => details.orNull
        case 3 => roles
        case 4 => {
          val __t = preferredLoginName
          if (__t != "") __t else null
        }
        case 5 => {
          val __t = email
          if (__t != "") __t else null
        }
        case 6 => {
          val __t = firstName
          if (__t != "") __t else null
        }
        case 7 => {
          val __t = lastName
          if (__t != "") __t else null
        }
        case 8 => {
          val __t = displayName
          if (__t != "") __t else null
        }
        case 9 => {
          val __t = avatarUrl
          if (__t != "") __t else null
        }
        case 10 => {
          val __t = userType.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(userId)
        case 2 => details.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => _root_.scalapb.descriptors.PRepeated(roles.iterator.map(_root_.scalapb.descriptors.PString(_)).toVector)
        case 4 => _root_.scalapb.descriptors.PString(preferredLoginName)
        case 5 => _root_.scalapb.descriptors.PString(email)
        case 6 => _root_.scalapb.descriptors.PString(firstName)
        case 7 => _root_.scalapb.descriptors.PString(lastName)
        case 8 => _root_.scalapb.descriptors.PString(displayName)
        case 9 => _root_.scalapb.descriptors.PString(avatarUrl)
        case 10 => _root_.scalapb.descriptors.PEnum(userType.scalaValueDescriptor)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.member.v1.member.Member.type = com.zitadel.member.v1.member.Member
    // @@protoc_insertion_point(GeneratedMessage[zitadel.member.v1.Member])
}

object Member extends scalapb.GeneratedMessageCompanion[com.zitadel.member.v1.member.Member] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.member.v1.member.Member] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.member.v1.member.Member = {
    var __userId: _root_.scala.Predef.String = ""
    var __details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails] = _root_.scala.None
    val __roles: _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String] = new _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String]
    var __preferredLoginName: _root_.scala.Predef.String = ""
    var __email: _root_.scala.Predef.String = ""
    var __firstName: _root_.scala.Predef.String = ""
    var __lastName: _root_.scala.Predef.String = ""
    var __displayName: _root_.scala.Predef.String = ""
    var __avatarUrl: _root_.scala.Predef.String = ""
    var __userType: com.zitadel.user.v1.user.Type = com.zitadel.user.v1.user.Type.TYPE_UNSPECIFIED
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __userId = _input__.readStringRequireUtf8()
        case 18 =>
          __details = Option(__details.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.v1.`object`.ObjectDetails](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 26 =>
          __roles += _input__.readStringRequireUtf8()
        case 34 =>
          __preferredLoginName = _input__.readStringRequireUtf8()
        case 42 =>
          __email = _input__.readStringRequireUtf8()
        case 50 =>
          __firstName = _input__.readStringRequireUtf8()
        case 58 =>
          __lastName = _input__.readStringRequireUtf8()
        case 66 =>
          __displayName = _input__.readStringRequireUtf8()
        case 74 =>
          __avatarUrl = _input__.readStringRequireUtf8()
        case 80 =>
          __userType = com.zitadel.user.v1.user.Type.fromValue(_input__.readEnum())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.member.v1.member.Member(
        userId = __userId,
        details = __details,
        roles = __roles.result(),
        preferredLoginName = __preferredLoginName,
        email = __email,
        firstName = __firstName,
        lastName = __lastName,
        displayName = __displayName,
        avatarUrl = __avatarUrl,
        userType = __userType,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.member.v1.member.Member] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.member.v1.member.Member(
        userId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        details = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]]),
        roles = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Seq[_root_.scala.Predef.String]]).getOrElse(_root_.scala.Seq.empty),
        preferredLoginName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        email = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        firstName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        lastName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(7).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        displayName = __fieldsMap.get(scalaDescriptor.findFieldByNumber(8).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        avatarUrl = __fieldsMap.get(scalaDescriptor.findFieldByNumber(9).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        userType = com.zitadel.user.v1.user.Type.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(10).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.zitadel.user.v1.user.Type.TYPE_UNSPECIFIED.scalaValueDescriptor).number)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MemberProto.javaDescriptor.getMessageTypes().get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MemberProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 2 => __out = com.zitadel.v1.`object`.ObjectDetails
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 10 => com.zitadel.user.v1.user.Type
    }
  }
  lazy val defaultInstance = com.zitadel.member.v1.member.Member(
    userId = "",
    details = _root_.scala.None,
    roles = _root_.scala.Seq.empty,
    preferredLoginName = "",
    email = "",
    firstName = "",
    lastName = "",
    displayName = "",
    avatarUrl = "",
    userType = com.zitadel.user.v1.user.Type.TYPE_UNSPECIFIED
  )
  implicit class MemberLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.member.v1.member.Member]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.member.v1.member.Member](_l) {
    def userId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.userId)((c_, f_) => c_.copy(userId = f_))
    def details: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.v1.`object`.ObjectDetails] = field(_.getDetails)((c_, f_) => c_.copy(details = Option(f_)))
    def optionalDetails: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails]] = field(_.details)((c_, f_) => c_.copy(details = f_))
    def roles: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[_root_.scala.Predef.String]] = field(_.roles)((c_, f_) => c_.copy(roles = f_))
    def preferredLoginName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.preferredLoginName)((c_, f_) => c_.copy(preferredLoginName = f_))
    def email: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.email)((c_, f_) => c_.copy(email = f_))
    def firstName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.firstName)((c_, f_) => c_.copy(firstName = f_))
    def lastName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.lastName)((c_, f_) => c_.copy(lastName = f_))
    def displayName: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.displayName)((c_, f_) => c_.copy(displayName = f_))
    def avatarUrl: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.avatarUrl)((c_, f_) => c_.copy(avatarUrl = f_))
    def userType: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v1.user.Type] = field(_.userType)((c_, f_) => c_.copy(userType = f_))
  }
  final val USER_ID_FIELD_NUMBER = 1
  final val DETAILS_FIELD_NUMBER = 2
  final val ROLES_FIELD_NUMBER = 3
  final val PREFERRED_LOGIN_NAME_FIELD_NUMBER = 4
  final val EMAIL_FIELD_NUMBER = 5
  final val FIRST_NAME_FIELD_NUMBER = 6
  final val LAST_NAME_FIELD_NUMBER = 7
  final val DISPLAY_NAME_FIELD_NUMBER = 8
  final val AVATAR_URL_FIELD_NUMBER = 9
  final val USER_TYPE_FIELD_NUMBER = 10
  def of(
    userId: _root_.scala.Predef.String,
    details: _root_.scala.Option[com.zitadel.v1.`object`.ObjectDetails],
    roles: _root_.scala.Seq[_root_.scala.Predef.String],
    preferredLoginName: _root_.scala.Predef.String,
    email: _root_.scala.Predef.String,
    firstName: _root_.scala.Predef.String,
    lastName: _root_.scala.Predef.String,
    displayName: _root_.scala.Predef.String,
    avatarUrl: _root_.scala.Predef.String,
    userType: com.zitadel.user.v1.user.Type
  ): _root_.com.zitadel.member.v1.member.Member = _root_.com.zitadel.member.v1.member.Member(
    userId,
    details,
    roles,
    preferredLoginName,
    email,
    firstName,
    lastName,
    displayName,
    avatarUrl,
    userType
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.member.v1.Member])
}

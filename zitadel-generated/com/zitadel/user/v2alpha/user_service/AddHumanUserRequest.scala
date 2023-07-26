// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.user.v2alpha.user_service

/** @param userId
  *   optionally set your own id unique for the user
  * @param username
  *   optionally set a unique username, if none is provided the email will be used
  */
@SerialVersionUID(0L)
final case class AddHumanUserRequest(
    userId: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None,
    username: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None,
    organisation: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Organisation] = _root_.scala.None,
    profile: _root_.scala.Option[com.zitadel.user.v2alpha.user.SetHumanProfile] = _root_.scala.None,
    email: _root_.scala.Option[com.zitadel.user.v2alpha.email.SetHumanEmail] = _root_.scala.None,
    metadata: _root_.scala.Seq[com.zitadel.user.v2alpha.user.SetMetadataEntry] = _root_.scala.Seq.empty,
    passwordType: com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.Empty,
    idpLinks: _root_.scala.Seq[com.zitadel.user.v2alpha.idp.IDPLink] = _root_.scala.Seq.empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[AddHumanUserRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (userId.isDefined) {
        val __value = userId.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
      };
      if (username.isDefined) {
        val __value = username.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
      };
      if (organisation.isDefined) {
        val __value = organisation.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (profile.isDefined) {
        val __value = profile.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (email.isDefined) {
        val __value = email.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      metadata.foreach { __item =>
        val __value = __item
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      }
      if (passwordType.password.isDefined) {
        val __value = passwordType.password.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (passwordType.hashedPassword.isDefined) {
        val __value = passwordType.hashedPassword.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      idpLinks.foreach { __item =>
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
      userId.foreach { __v =>
        val __m = __v
        _output__.writeString(1, __m)
      };
      username.foreach { __v =>
        val __m = __v
        _output__.writeString(2, __m)
      };
      organisation.foreach { __v =>
        val __m = __v
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      profile.foreach { __v =>
        val __m = __v
        _output__.writeTag(4, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      email.foreach { __v =>
        val __m = __v
        _output__.writeTag(5, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      metadata.foreach { __v =>
        val __m = __v
        _output__.writeTag(6, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      passwordType.password.foreach { __v =>
        val __m = __v
        _output__.writeTag(7, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      passwordType.hashedPassword.foreach { __v =>
        val __m = __v
        _output__.writeTag(8, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      idpLinks.foreach { __v =>
        val __m = __v
        _output__.writeTag(9, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getUserId: _root_.scala.Predef.String = userId.getOrElse("")
    def clearUserId: AddHumanUserRequest = copy(userId = _root_.scala.None)
    def withUserId(__v: _root_.scala.Predef.String): AddHumanUserRequest = copy(userId = Option(__v))
    def getUsername: _root_.scala.Predef.String = username.getOrElse("")
    def clearUsername: AddHumanUserRequest = copy(username = _root_.scala.None)
    def withUsername(__v: _root_.scala.Predef.String): AddHumanUserRequest = copy(username = Option(__v))
    def getOrganisation: com.zitadel.`object`.v2alpha.`object`.Organisation = organisation.getOrElse(com.zitadel.`object`.v2alpha.`object`.Organisation.defaultInstance)
    def clearOrganisation: AddHumanUserRequest = copy(organisation = _root_.scala.None)
    def withOrganisation(__v: com.zitadel.`object`.v2alpha.`object`.Organisation): AddHumanUserRequest = copy(organisation = Option(__v))
    def getProfile: com.zitadel.user.v2alpha.user.SetHumanProfile = profile.getOrElse(com.zitadel.user.v2alpha.user.SetHumanProfile.defaultInstance)
    def clearProfile: AddHumanUserRequest = copy(profile = _root_.scala.None)
    def withProfile(__v: com.zitadel.user.v2alpha.user.SetHumanProfile): AddHumanUserRequest = copy(profile = Option(__v))
    def getEmail: com.zitadel.user.v2alpha.email.SetHumanEmail = email.getOrElse(com.zitadel.user.v2alpha.email.SetHumanEmail.defaultInstance)
    def clearEmail: AddHumanUserRequest = copy(email = _root_.scala.None)
    def withEmail(__v: com.zitadel.user.v2alpha.email.SetHumanEmail): AddHumanUserRequest = copy(email = Option(__v))
    def clearMetadata = copy(metadata = _root_.scala.Seq.empty)
    def addMetadata(__vs: com.zitadel.user.v2alpha.user.SetMetadataEntry *): AddHumanUserRequest = addAllMetadata(__vs)
    def addAllMetadata(__vs: Iterable[com.zitadel.user.v2alpha.user.SetMetadataEntry]): AddHumanUserRequest = copy(metadata = metadata ++ __vs)
    def withMetadata(__v: _root_.scala.Seq[com.zitadel.user.v2alpha.user.SetMetadataEntry]): AddHumanUserRequest = copy(metadata = __v)
    def getPassword: com.zitadel.user.v2alpha.password.Password = passwordType.password.getOrElse(com.zitadel.user.v2alpha.password.Password.defaultInstance)
    def withPassword(__v: com.zitadel.user.v2alpha.password.Password): AddHumanUserRequest = copy(passwordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.Password(__v))
    def getHashedPassword: com.zitadel.user.v2alpha.password.HashedPassword = passwordType.hashedPassword.getOrElse(com.zitadel.user.v2alpha.password.HashedPassword.defaultInstance)
    def withHashedPassword(__v: com.zitadel.user.v2alpha.password.HashedPassword): AddHumanUserRequest = copy(passwordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.HashedPassword(__v))
    def clearIdpLinks = copy(idpLinks = _root_.scala.Seq.empty)
    def addIdpLinks(__vs: com.zitadel.user.v2alpha.idp.IDPLink *): AddHumanUserRequest = addAllIdpLinks(__vs)
    def addAllIdpLinks(__vs: Iterable[com.zitadel.user.v2alpha.idp.IDPLink]): AddHumanUserRequest = copy(idpLinks = idpLinks ++ __vs)
    def withIdpLinks(__v: _root_.scala.Seq[com.zitadel.user.v2alpha.idp.IDPLink]): AddHumanUserRequest = copy(idpLinks = __v)
    def clearPasswordType: AddHumanUserRequest = copy(passwordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.Empty)
    def withPasswordType(__v: com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType): AddHumanUserRequest = copy(passwordType = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => userId.orNull
        case 2 => username.orNull
        case 3 => organisation.orNull
        case 4 => profile.orNull
        case 5 => email.orNull
        case 6 => metadata
        case 7 => passwordType.password.orNull
        case 8 => passwordType.hashedPassword.orNull
        case 9 => idpLinks
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => userId.map(_root_.scalapb.descriptors.PString(_)).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => username.map(_root_.scalapb.descriptors.PString(_)).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => organisation.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 4 => profile.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 5 => email.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 6 => _root_.scalapb.descriptors.PRepeated(metadata.iterator.map(_.toPMessage).toVector)
        case 7 => passwordType.password.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 8 => passwordType.hashedPassword.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 9 => _root_.scalapb.descriptors.PRepeated(idpLinks.iterator.map(_.toPMessage).toVector)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.type = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.user.v2alpha.AddHumanUserRequest])
}

object AddHumanUserRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.AddHumanUserRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.user.v2alpha.user_service.AddHumanUserRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.user.v2alpha.user_service.AddHumanUserRequest = {
    var __userId: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None
    var __username: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None
    var __organisation: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Organisation] = _root_.scala.None
    var __profile: _root_.scala.Option[com.zitadel.user.v2alpha.user.SetHumanProfile] = _root_.scala.None
    var __email: _root_.scala.Option[com.zitadel.user.v2alpha.email.SetHumanEmail] = _root_.scala.None
    val __metadata: _root_.scala.collection.immutable.VectorBuilder[com.zitadel.user.v2alpha.user.SetMetadataEntry] = new _root_.scala.collection.immutable.VectorBuilder[com.zitadel.user.v2alpha.user.SetMetadataEntry]
    val __idpLinks: _root_.scala.collection.immutable.VectorBuilder[com.zitadel.user.v2alpha.idp.IDPLink] = new _root_.scala.collection.immutable.VectorBuilder[com.zitadel.user.v2alpha.idp.IDPLink]
    var __passwordType: com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.Empty
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __userId = Option(_input__.readStringRequireUtf8())
        case 18 =>
          __username = Option(_input__.readStringRequireUtf8())
        case 26 =>
          __organisation = Option(__organisation.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.`object`.v2alpha.`object`.Organisation](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 34 =>
          __profile = Option(__profile.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.user.v2alpha.user.SetHumanProfile](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 42 =>
          __email = Option(__email.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.user.v2alpha.email.SetHumanEmail](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 50 =>
          __metadata += _root_.scalapb.LiteParser.readMessage[com.zitadel.user.v2alpha.user.SetMetadataEntry](_input__)
        case 58 =>
          __passwordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.Password(__passwordType.password.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.user.v2alpha.password.Password](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 66 =>
          __passwordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.HashedPassword(__passwordType.hashedPassword.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.user.v2alpha.password.HashedPassword](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 74 =>
          __idpLinks += _root_.scalapb.LiteParser.readMessage[com.zitadel.user.v2alpha.idp.IDPLink](_input__)
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.user.v2alpha.user_service.AddHumanUserRequest(
        userId = __userId,
        username = __username,
        organisation = __organisation,
        profile = __profile,
        email = __email,
        metadata = __metadata.result(),
        idpLinks = __idpLinks.result(),
        passwordType = __passwordType,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.user.v2alpha.user_service.AddHumanUserRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.user.v2alpha.user_service.AddHumanUserRequest(
        userId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Predef.String]]),
        username = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Predef.String]]),
        organisation = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[_root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Organisation]]),
        profile = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).flatMap(_.as[_root_.scala.Option[com.zitadel.user.v2alpha.user.SetHumanProfile]]),
        email = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).flatMap(_.as[_root_.scala.Option[com.zitadel.user.v2alpha.email.SetHumanEmail]]),
        metadata = __fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scala.Seq[com.zitadel.user.v2alpha.user.SetMetadataEntry]]).getOrElse(_root_.scala.Seq.empty),
        idpLinks = __fieldsMap.get(scalaDescriptor.findFieldByNumber(9).get).map(_.as[_root_.scala.Seq[com.zitadel.user.v2alpha.idp.IDPLink]]).getOrElse(_root_.scala.Seq.empty),
        passwordType = __fieldsMap.get(scalaDescriptor.findFieldByNumber(7).get).flatMap(_.as[_root_.scala.Option[com.zitadel.user.v2alpha.password.Password]]).map(com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.Password(_))
            .orElse[com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType](__fieldsMap.get(scalaDescriptor.findFieldByNumber(8).get).flatMap(_.as[_root_.scala.Option[com.zitadel.user.v2alpha.password.HashedPassword]]).map(com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.HashedPassword(_)))
            .getOrElse(com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = UserServiceProto.javaDescriptor.getMessageTypes().get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = UserServiceProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 3 => __out = com.zitadel.`object`.v2alpha.`object`.Organisation
      case 4 => __out = com.zitadel.user.v2alpha.user.SetHumanProfile
      case 5 => __out = com.zitadel.user.v2alpha.email.SetHumanEmail
      case 6 => __out = com.zitadel.user.v2alpha.user.SetMetadataEntry
      case 7 => __out = com.zitadel.user.v2alpha.password.Password
      case 8 => __out = com.zitadel.user.v2alpha.password.HashedPassword
      case 9 => __out = com.zitadel.user.v2alpha.idp.IDPLink
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest(
    userId = _root_.scala.None,
    username = _root_.scala.None,
    organisation = _root_.scala.None,
    profile = _root_.scala.None,
    email = _root_.scala.None,
    metadata = _root_.scala.Seq.empty,
    idpLinks = _root_.scala.Seq.empty,
    passwordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.Empty
  )
  sealed trait PasswordType extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isPassword: _root_.scala.Boolean = false
    def isHashedPassword: _root_.scala.Boolean = false
    def password: _root_.scala.Option[com.zitadel.user.v2alpha.password.Password] = _root_.scala.None
    def hashedPassword: _root_.scala.Option[com.zitadel.user.v2alpha.password.HashedPassword] = _root_.scala.None
  }
  object PasswordType {
    @SerialVersionUID(0L)
    case object Empty extends com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class Password(value: com.zitadel.user.v2alpha.password.Password) extends com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType {
      type ValueType = com.zitadel.user.v2alpha.password.Password
      override def isPassword: _root_.scala.Boolean = true
      override def password: _root_.scala.Option[com.zitadel.user.v2alpha.password.Password] = Some(value)
      override def number: _root_.scala.Int = 7
    }
    @SerialVersionUID(0L)
    final case class HashedPassword(value: com.zitadel.user.v2alpha.password.HashedPassword) extends com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType {
      type ValueType = com.zitadel.user.v2alpha.password.HashedPassword
      override def isHashedPassword: _root_.scala.Boolean = true
      override def hashedPassword: _root_.scala.Option[com.zitadel.user.v2alpha.password.HashedPassword] = Some(value)
      override def number: _root_.scala.Int = 8
    }
  }
  implicit class AddHumanUserRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.user_service.AddHumanUserRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.user.v2alpha.user_service.AddHumanUserRequest](_l) {
    def userId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.getUserId)((c_, f_) => c_.copy(userId = Option(f_)))
    def optionalUserId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[_root_.scala.Predef.String]] = field(_.userId)((c_, f_) => c_.copy(userId = f_))
    def username: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.getUsername)((c_, f_) => c_.copy(username = Option(f_)))
    def optionalUsername: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[_root_.scala.Predef.String]] = field(_.username)((c_, f_) => c_.copy(username = f_))
    def organisation: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.`object`.v2alpha.`object`.Organisation] = field(_.getOrganisation)((c_, f_) => c_.copy(organisation = Option(f_)))
    def optionalOrganisation: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Organisation]] = field(_.organisation)((c_, f_) => c_.copy(organisation = f_))
    def profile: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.user.SetHumanProfile] = field(_.getProfile)((c_, f_) => c_.copy(profile = Option(f_)))
    def optionalProfile: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.user.v2alpha.user.SetHumanProfile]] = field(_.profile)((c_, f_) => c_.copy(profile = f_))
    def email: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.email.SetHumanEmail] = field(_.getEmail)((c_, f_) => c_.copy(email = Option(f_)))
    def optionalEmail: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.user.v2alpha.email.SetHumanEmail]] = field(_.email)((c_, f_) => c_.copy(email = f_))
    def metadata: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[com.zitadel.user.v2alpha.user.SetMetadataEntry]] = field(_.metadata)((c_, f_) => c_.copy(metadata = f_))
    def password: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.password.Password] = field(_.getPassword)((c_, f_) => c_.copy(passwordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.Password(f_)))
    def hashedPassword: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.password.HashedPassword] = field(_.getHashedPassword)((c_, f_) => c_.copy(passwordType = com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType.HashedPassword(f_)))
    def idpLinks: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[com.zitadel.user.v2alpha.idp.IDPLink]] = field(_.idpLinks)((c_, f_) => c_.copy(idpLinks = f_))
    def passwordType: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType] = field(_.passwordType)((c_, f_) => c_.copy(passwordType = f_))
  }
  final val USER_ID_FIELD_NUMBER = 1
  final val USERNAME_FIELD_NUMBER = 2
  final val ORGANISATION_FIELD_NUMBER = 3
  final val PROFILE_FIELD_NUMBER = 4
  final val EMAIL_FIELD_NUMBER = 5
  final val METADATA_FIELD_NUMBER = 6
  final val PASSWORD_FIELD_NUMBER = 7
  final val HASHED_PASSWORD_FIELD_NUMBER = 8
  final val IDP_LINKS_FIELD_NUMBER = 9
  def of(
    userId: _root_.scala.Option[_root_.scala.Predef.String],
    username: _root_.scala.Option[_root_.scala.Predef.String],
    organisation: _root_.scala.Option[com.zitadel.`object`.v2alpha.`object`.Organisation],
    profile: _root_.scala.Option[com.zitadel.user.v2alpha.user.SetHumanProfile],
    email: _root_.scala.Option[com.zitadel.user.v2alpha.email.SetHumanEmail],
    metadata: _root_.scala.Seq[com.zitadel.user.v2alpha.user.SetMetadataEntry],
    passwordType: com.zitadel.user.v2alpha.user_service.AddHumanUserRequest.PasswordType,
    idpLinks: _root_.scala.Seq[com.zitadel.user.v2alpha.idp.IDPLink]
  ): _root_.com.zitadel.user.v2alpha.user_service.AddHumanUserRequest = _root_.com.zitadel.user.v2alpha.user_service.AddHumanUserRequest(
    userId,
    username,
    organisation,
    profile,
    email,
    metadata,
    passwordType,
    idpLinks
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.user.v2alpha.AddHumanUserRequest])
}

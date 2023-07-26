// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.admin.v1.admin

@SerialVersionUID(0L)
final case class AddGitHubEnterpriseServerProviderRequest(
    clientId: _root_.scala.Predef.String = "",
    name: _root_.scala.Predef.String = "",
    clientSecret: _root_.scala.Predef.String = "",
    authorizationEndpoint: _root_.scala.Predef.String = "",
    tokenEndpoint: _root_.scala.Predef.String = "",
    userEndpoint: _root_.scala.Predef.String = "",
    scopes: _root_.scala.Seq[_root_.scala.Predef.String] = _root_.scala.Seq.empty,
    providerOptions: _root_.scala.Option[com.zitadel.idp.v1.idp.Options] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[AddGitHubEnterpriseServerProviderRequest] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = clientId
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
        }
      };
      
      {
        val __value = name
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      
      {
        val __value = clientSecret
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
        }
      };
      
      {
        val __value = authorizationEndpoint
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
        }
      };
      
      {
        val __value = tokenEndpoint
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(5, __value)
        }
      };
      
      {
        val __value = userEndpoint
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(6, __value)
        }
      };
      scopes.foreach { __item =>
        val __value = __item
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(7, __value)
      }
      if (providerOptions.isDefined) {
        val __value = providerOptions.get
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
      {
        val __v = clientId
        if (!__v.isEmpty) {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = name
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      {
        val __v = clientSecret
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
      {
        val __v = authorizationEndpoint
        if (!__v.isEmpty) {
          _output__.writeString(4, __v)
        }
      };
      {
        val __v = tokenEndpoint
        if (!__v.isEmpty) {
          _output__.writeString(5, __v)
        }
      };
      {
        val __v = userEndpoint
        if (!__v.isEmpty) {
          _output__.writeString(6, __v)
        }
      };
      scopes.foreach { __v =>
        val __m = __v
        _output__.writeString(7, __m)
      };
      providerOptions.foreach { __v =>
        val __m = __v
        _output__.writeTag(8, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def withClientId(__v: _root_.scala.Predef.String): AddGitHubEnterpriseServerProviderRequest = copy(clientId = __v)
    def withName(__v: _root_.scala.Predef.String): AddGitHubEnterpriseServerProviderRequest = copy(name = __v)
    def withClientSecret(__v: _root_.scala.Predef.String): AddGitHubEnterpriseServerProviderRequest = copy(clientSecret = __v)
    def withAuthorizationEndpoint(__v: _root_.scala.Predef.String): AddGitHubEnterpriseServerProviderRequest = copy(authorizationEndpoint = __v)
    def withTokenEndpoint(__v: _root_.scala.Predef.String): AddGitHubEnterpriseServerProviderRequest = copy(tokenEndpoint = __v)
    def withUserEndpoint(__v: _root_.scala.Predef.String): AddGitHubEnterpriseServerProviderRequest = copy(userEndpoint = __v)
    def clearScopes = copy(scopes = _root_.scala.Seq.empty)
    def addScopes(__vs: _root_.scala.Predef.String *): AddGitHubEnterpriseServerProviderRequest = addAllScopes(__vs)
    def addAllScopes(__vs: Iterable[_root_.scala.Predef.String]): AddGitHubEnterpriseServerProviderRequest = copy(scopes = scopes ++ __vs)
    def withScopes(__v: _root_.scala.Seq[_root_.scala.Predef.String]): AddGitHubEnterpriseServerProviderRequest = copy(scopes = __v)
    def getProviderOptions: com.zitadel.idp.v1.idp.Options = providerOptions.getOrElse(com.zitadel.idp.v1.idp.Options.defaultInstance)
    def clearProviderOptions: AddGitHubEnterpriseServerProviderRequest = copy(providerOptions = _root_.scala.None)
    def withProviderOptions(__v: com.zitadel.idp.v1.idp.Options): AddGitHubEnterpriseServerProviderRequest = copy(providerOptions = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = clientId
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = name
          if (__t != "") __t else null
        }
        case 3 => {
          val __t = clientSecret
          if (__t != "") __t else null
        }
        case 4 => {
          val __t = authorizationEndpoint
          if (__t != "") __t else null
        }
        case 5 => {
          val __t = tokenEndpoint
          if (__t != "") __t else null
        }
        case 6 => {
          val __t = userEndpoint
          if (__t != "") __t else null
        }
        case 7 => scopes
        case 8 => providerOptions.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(clientId)
        case 2 => _root_.scalapb.descriptors.PString(name)
        case 3 => _root_.scalapb.descriptors.PString(clientSecret)
        case 4 => _root_.scalapb.descriptors.PString(authorizationEndpoint)
        case 5 => _root_.scalapb.descriptors.PString(tokenEndpoint)
        case 6 => _root_.scalapb.descriptors.PString(userEndpoint)
        case 7 => _root_.scalapb.descriptors.PRepeated(scopes.iterator.map(_root_.scalapb.descriptors.PString(_)).toVector)
        case 8 => providerOptions.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest.type = com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.admin.v1.AddGitHubEnterpriseServerProviderRequest])
}

object AddGitHubEnterpriseServerProviderRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest = {
    var __clientId: _root_.scala.Predef.String = ""
    var __name: _root_.scala.Predef.String = ""
    var __clientSecret: _root_.scala.Predef.String = ""
    var __authorizationEndpoint: _root_.scala.Predef.String = ""
    var __tokenEndpoint: _root_.scala.Predef.String = ""
    var __userEndpoint: _root_.scala.Predef.String = ""
    val __scopes: _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String] = new _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String]
    var __providerOptions: _root_.scala.Option[com.zitadel.idp.v1.idp.Options] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __clientId = _input__.readStringRequireUtf8()
        case 18 =>
          __name = _input__.readStringRequireUtf8()
        case 26 =>
          __clientSecret = _input__.readStringRequireUtf8()
        case 34 =>
          __authorizationEndpoint = _input__.readStringRequireUtf8()
        case 42 =>
          __tokenEndpoint = _input__.readStringRequireUtf8()
        case 50 =>
          __userEndpoint = _input__.readStringRequireUtf8()
        case 58 =>
          __scopes += _input__.readStringRequireUtf8()
        case 66 =>
          __providerOptions = Option(__providerOptions.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.idp.v1.idp.Options](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest(
        clientId = __clientId,
        name = __name,
        clientSecret = __clientSecret,
        authorizationEndpoint = __authorizationEndpoint,
        tokenEndpoint = __tokenEndpoint,
        userEndpoint = __userEndpoint,
        scopes = __scopes.result(),
        providerOptions = __providerOptions,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest(
        clientId = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        name = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        clientSecret = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        authorizationEndpoint = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        tokenEndpoint = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        userEndpoint = __fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        scopes = __fieldsMap.get(scalaDescriptor.findFieldByNumber(7).get).map(_.as[_root_.scala.Seq[_root_.scala.Predef.String]]).getOrElse(_root_.scala.Seq.empty),
        providerOptions = __fieldsMap.get(scalaDescriptor.findFieldByNumber(8).get).flatMap(_.as[_root_.scala.Option[com.zitadel.idp.v1.idp.Options]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = AdminProto.javaDescriptor.getMessageTypes().get(120)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = AdminProto.scalaDescriptor.messages(120)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 8 => __out = com.zitadel.idp.v1.idp.Options
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest(
    clientId = "",
    name = "",
    clientSecret = "",
    authorizationEndpoint = "",
    tokenEndpoint = "",
    userEndpoint = "",
    scopes = _root_.scala.Seq.empty,
    providerOptions = _root_.scala.None
  )
  implicit class AddGitHubEnterpriseServerProviderRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest](_l) {
    def clientId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.clientId)((c_, f_) => c_.copy(clientId = f_))
    def name: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.name)((c_, f_) => c_.copy(name = f_))
    def clientSecret: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.clientSecret)((c_, f_) => c_.copy(clientSecret = f_))
    def authorizationEndpoint: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.authorizationEndpoint)((c_, f_) => c_.copy(authorizationEndpoint = f_))
    def tokenEndpoint: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.tokenEndpoint)((c_, f_) => c_.copy(tokenEndpoint = f_))
    def userEndpoint: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.userEndpoint)((c_, f_) => c_.copy(userEndpoint = f_))
    def scopes: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[_root_.scala.Predef.String]] = field(_.scopes)((c_, f_) => c_.copy(scopes = f_))
    def providerOptions: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.Options] = field(_.getProviderOptions)((c_, f_) => c_.copy(providerOptions = Option(f_)))
    def optionalProviderOptions: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.idp.v1.idp.Options]] = field(_.providerOptions)((c_, f_) => c_.copy(providerOptions = f_))
  }
  final val CLIENT_ID_FIELD_NUMBER = 1
  final val NAME_FIELD_NUMBER = 2
  final val CLIENT_SECRET_FIELD_NUMBER = 3
  final val AUTHORIZATION_ENDPOINT_FIELD_NUMBER = 4
  final val TOKEN_ENDPOINT_FIELD_NUMBER = 5
  final val USER_ENDPOINT_FIELD_NUMBER = 6
  final val SCOPES_FIELD_NUMBER = 7
  final val PROVIDER_OPTIONS_FIELD_NUMBER = 8
  def of(
    clientId: _root_.scala.Predef.String,
    name: _root_.scala.Predef.String,
    clientSecret: _root_.scala.Predef.String,
    authorizationEndpoint: _root_.scala.Predef.String,
    tokenEndpoint: _root_.scala.Predef.String,
    userEndpoint: _root_.scala.Predef.String,
    scopes: _root_.scala.Seq[_root_.scala.Predef.String],
    providerOptions: _root_.scala.Option[com.zitadel.idp.v1.idp.Options]
  ): _root_.com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest = _root_.com.zitadel.admin.v1.admin.AddGitHubEnterpriseServerProviderRequest(
    clientId,
    name,
    clientSecret,
    authorizationEndpoint,
    tokenEndpoint,
    userEndpoint,
    scopes,
    providerOptions
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.admin.v1.AddGitHubEnterpriseServerProviderRequest])
}

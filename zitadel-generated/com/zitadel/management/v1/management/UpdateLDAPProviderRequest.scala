// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.management.v1.management

@SerialVersionUID(0L)
final case class UpdateLDAPProviderRequest(
    id: _root_.scala.Predef.String = "",
    name: _root_.scala.Predef.String = "",
    servers: _root_.scala.Seq[_root_.scala.Predef.String] = _root_.scala.Seq.empty,
    startTls: _root_.scala.Boolean = false,
    baseDn: _root_.scala.Predef.String = "",
    bindDn: _root_.scala.Predef.String = "",
    bindPassword: _root_.scala.Predef.String = "",
    userBase: _root_.scala.Predef.String = "",
    userObjectClasses: _root_.scala.Seq[_root_.scala.Predef.String] = _root_.scala.Seq.empty,
    userFilters: _root_.scala.Seq[_root_.scala.Predef.String] = _root_.scala.Seq.empty,
    timeout: _root_.scala.Option[com.google.protobuf.duration.Duration] = _root_.scala.None,
    attributes: _root_.scala.Option[com.zitadel.idp.v1.idp.LDAPAttributes] = _root_.scala.None,
    providerOptions: _root_.scala.Option[com.zitadel.idp.v1.idp.Options] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[UpdateLDAPProviderRequest] {
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
      
      {
        val __value = name
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      servers.foreach { __item =>
        val __value = __item
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
      }
      
      {
        val __value = startTls
        if (__value != false) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(4, __value)
        }
      };
      
      {
        val __value = baseDn
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(5, __value)
        }
      };
      
      {
        val __value = bindDn
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(6, __value)
        }
      };
      
      {
        val __value = bindPassword
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(7, __value)
        }
      };
      
      {
        val __value = userBase
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(8, __value)
        }
      };
      userObjectClasses.foreach { __item =>
        val __value = __item
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(9, __value)
      }
      userFilters.foreach { __item =>
        val __value = __item
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(10, __value)
      }
      if (timeout.isDefined) {
        val __value = timeout.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      if (attributes.isDefined) {
        val __value = attributes.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
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
        val __v = id
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
      servers.foreach { __v =>
        val __m = __v
        _output__.writeString(3, __m)
      };
      {
        val __v = startTls
        if (__v != false) {
          _output__.writeBool(4, __v)
        }
      };
      {
        val __v = baseDn
        if (!__v.isEmpty) {
          _output__.writeString(5, __v)
        }
      };
      {
        val __v = bindDn
        if (!__v.isEmpty) {
          _output__.writeString(6, __v)
        }
      };
      {
        val __v = bindPassword
        if (!__v.isEmpty) {
          _output__.writeString(7, __v)
        }
      };
      {
        val __v = userBase
        if (!__v.isEmpty) {
          _output__.writeString(8, __v)
        }
      };
      userObjectClasses.foreach { __v =>
        val __m = __v
        _output__.writeString(9, __m)
      };
      userFilters.foreach { __v =>
        val __m = __v
        _output__.writeString(10, __m)
      };
      timeout.foreach { __v =>
        val __m = __v
        _output__.writeTag(11, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      attributes.foreach { __v =>
        val __m = __v
        _output__.writeTag(12, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      providerOptions.foreach { __v =>
        val __m = __v
        _output__.writeTag(13, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def withId(__v: _root_.scala.Predef.String): UpdateLDAPProviderRequest = copy(id = __v)
    def withName(__v: _root_.scala.Predef.String): UpdateLDAPProviderRequest = copy(name = __v)
    def clearServers = copy(servers = _root_.scala.Seq.empty)
    def addServers(__vs: _root_.scala.Predef.String *): UpdateLDAPProviderRequest = addAllServers(__vs)
    def addAllServers(__vs: Iterable[_root_.scala.Predef.String]): UpdateLDAPProviderRequest = copy(servers = servers ++ __vs)
    def withServers(__v: _root_.scala.Seq[_root_.scala.Predef.String]): UpdateLDAPProviderRequest = copy(servers = __v)
    def withStartTls(__v: _root_.scala.Boolean): UpdateLDAPProviderRequest = copy(startTls = __v)
    def withBaseDn(__v: _root_.scala.Predef.String): UpdateLDAPProviderRequest = copy(baseDn = __v)
    def withBindDn(__v: _root_.scala.Predef.String): UpdateLDAPProviderRequest = copy(bindDn = __v)
    def withBindPassword(__v: _root_.scala.Predef.String): UpdateLDAPProviderRequest = copy(bindPassword = __v)
    def withUserBase(__v: _root_.scala.Predef.String): UpdateLDAPProviderRequest = copy(userBase = __v)
    def clearUserObjectClasses = copy(userObjectClasses = _root_.scala.Seq.empty)
    def addUserObjectClasses(__vs: _root_.scala.Predef.String *): UpdateLDAPProviderRequest = addAllUserObjectClasses(__vs)
    def addAllUserObjectClasses(__vs: Iterable[_root_.scala.Predef.String]): UpdateLDAPProviderRequest = copy(userObjectClasses = userObjectClasses ++ __vs)
    def withUserObjectClasses(__v: _root_.scala.Seq[_root_.scala.Predef.String]): UpdateLDAPProviderRequest = copy(userObjectClasses = __v)
    def clearUserFilters = copy(userFilters = _root_.scala.Seq.empty)
    def addUserFilters(__vs: _root_.scala.Predef.String *): UpdateLDAPProviderRequest = addAllUserFilters(__vs)
    def addAllUserFilters(__vs: Iterable[_root_.scala.Predef.String]): UpdateLDAPProviderRequest = copy(userFilters = userFilters ++ __vs)
    def withUserFilters(__v: _root_.scala.Seq[_root_.scala.Predef.String]): UpdateLDAPProviderRequest = copy(userFilters = __v)
    def getTimeout: com.google.protobuf.duration.Duration = timeout.getOrElse(com.google.protobuf.duration.Duration.defaultInstance)
    def clearTimeout: UpdateLDAPProviderRequest = copy(timeout = _root_.scala.None)
    def withTimeout(__v: com.google.protobuf.duration.Duration): UpdateLDAPProviderRequest = copy(timeout = Option(__v))
    def getAttributes: com.zitadel.idp.v1.idp.LDAPAttributes = attributes.getOrElse(com.zitadel.idp.v1.idp.LDAPAttributes.defaultInstance)
    def clearAttributes: UpdateLDAPProviderRequest = copy(attributes = _root_.scala.None)
    def withAttributes(__v: com.zitadel.idp.v1.idp.LDAPAttributes): UpdateLDAPProviderRequest = copy(attributes = Option(__v))
    def getProviderOptions: com.zitadel.idp.v1.idp.Options = providerOptions.getOrElse(com.zitadel.idp.v1.idp.Options.defaultInstance)
    def clearProviderOptions: UpdateLDAPProviderRequest = copy(providerOptions = _root_.scala.None)
    def withProviderOptions(__v: com.zitadel.idp.v1.idp.Options): UpdateLDAPProviderRequest = copy(providerOptions = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = id
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = name
          if (__t != "") __t else null
        }
        case 3 => servers
        case 4 => {
          val __t = startTls
          if (__t != false) __t else null
        }
        case 5 => {
          val __t = baseDn
          if (__t != "") __t else null
        }
        case 6 => {
          val __t = bindDn
          if (__t != "") __t else null
        }
        case 7 => {
          val __t = bindPassword
          if (__t != "") __t else null
        }
        case 8 => {
          val __t = userBase
          if (__t != "") __t else null
        }
        case 9 => userObjectClasses
        case 10 => userFilters
        case 11 => timeout.orNull
        case 12 => attributes.orNull
        case 13 => providerOptions.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(id)
        case 2 => _root_.scalapb.descriptors.PString(name)
        case 3 => _root_.scalapb.descriptors.PRepeated(servers.iterator.map(_root_.scalapb.descriptors.PString(_)).toVector)
        case 4 => _root_.scalapb.descriptors.PBoolean(startTls)
        case 5 => _root_.scalapb.descriptors.PString(baseDn)
        case 6 => _root_.scalapb.descriptors.PString(bindDn)
        case 7 => _root_.scalapb.descriptors.PString(bindPassword)
        case 8 => _root_.scalapb.descriptors.PString(userBase)
        case 9 => _root_.scalapb.descriptors.PRepeated(userObjectClasses.iterator.map(_root_.scalapb.descriptors.PString(_)).toVector)
        case 10 => _root_.scalapb.descriptors.PRepeated(userFilters.iterator.map(_root_.scalapb.descriptors.PString(_)).toVector)
        case 11 => timeout.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 12 => attributes.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 13 => providerOptions.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.management.v1.management.UpdateLDAPProviderRequest.type = com.zitadel.management.v1.management.UpdateLDAPProviderRequest
    // @@protoc_insertion_point(GeneratedMessage[zitadel.management.v1.UpdateLDAPProviderRequest])
}

object UpdateLDAPProviderRequest extends scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.UpdateLDAPProviderRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.management.v1.management.UpdateLDAPProviderRequest] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.management.v1.management.UpdateLDAPProviderRequest = {
    var __id: _root_.scala.Predef.String = ""
    var __name: _root_.scala.Predef.String = ""
    val __servers: _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String] = new _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String]
    var __startTls: _root_.scala.Boolean = false
    var __baseDn: _root_.scala.Predef.String = ""
    var __bindDn: _root_.scala.Predef.String = ""
    var __bindPassword: _root_.scala.Predef.String = ""
    var __userBase: _root_.scala.Predef.String = ""
    val __userObjectClasses: _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String] = new _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String]
    val __userFilters: _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String] = new _root_.scala.collection.immutable.VectorBuilder[_root_.scala.Predef.String]
    var __timeout: _root_.scala.Option[com.google.protobuf.duration.Duration] = _root_.scala.None
    var __attributes: _root_.scala.Option[com.zitadel.idp.v1.idp.LDAPAttributes] = _root_.scala.None
    var __providerOptions: _root_.scala.Option[com.zitadel.idp.v1.idp.Options] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __id = _input__.readStringRequireUtf8()
        case 18 =>
          __name = _input__.readStringRequireUtf8()
        case 26 =>
          __servers += _input__.readStringRequireUtf8()
        case 32 =>
          __startTls = _input__.readBool()
        case 42 =>
          __baseDn = _input__.readStringRequireUtf8()
        case 50 =>
          __bindDn = _input__.readStringRequireUtf8()
        case 58 =>
          __bindPassword = _input__.readStringRequireUtf8()
        case 66 =>
          __userBase = _input__.readStringRequireUtf8()
        case 74 =>
          __userObjectClasses += _input__.readStringRequireUtf8()
        case 82 =>
          __userFilters += _input__.readStringRequireUtf8()
        case 90 =>
          __timeout = Option(__timeout.fold(_root_.scalapb.LiteParser.readMessage[com.google.protobuf.duration.Duration](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 98 =>
          __attributes = Option(__attributes.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.idp.v1.idp.LDAPAttributes](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 106 =>
          __providerOptions = Option(__providerOptions.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.idp.v1.idp.Options](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.management.v1.management.UpdateLDAPProviderRequest(
        id = __id,
        name = __name,
        servers = __servers.result(),
        startTls = __startTls,
        baseDn = __baseDn,
        bindDn = __bindDn,
        bindPassword = __bindPassword,
        userBase = __userBase,
        userObjectClasses = __userObjectClasses.result(),
        userFilters = __userFilters.result(),
        timeout = __timeout,
        attributes = __attributes,
        providerOptions = __providerOptions,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.management.v1.management.UpdateLDAPProviderRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.management.v1.management.UpdateLDAPProviderRequest(
        id = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        name = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        servers = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Seq[_root_.scala.Predef.String]]).getOrElse(_root_.scala.Seq.empty),
        startTls = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Boolean]).getOrElse(false),
        baseDn = __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        bindDn = __fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        bindPassword = __fieldsMap.get(scalaDescriptor.findFieldByNumber(7).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        userBase = __fieldsMap.get(scalaDescriptor.findFieldByNumber(8).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        userObjectClasses = __fieldsMap.get(scalaDescriptor.findFieldByNumber(9).get).map(_.as[_root_.scala.Seq[_root_.scala.Predef.String]]).getOrElse(_root_.scala.Seq.empty),
        userFilters = __fieldsMap.get(scalaDescriptor.findFieldByNumber(10).get).map(_.as[_root_.scala.Seq[_root_.scala.Predef.String]]).getOrElse(_root_.scala.Seq.empty),
        timeout = __fieldsMap.get(scalaDescriptor.findFieldByNumber(11).get).flatMap(_.as[_root_.scala.Option[com.google.protobuf.duration.Duration]]),
        attributes = __fieldsMap.get(scalaDescriptor.findFieldByNumber(12).get).flatMap(_.as[_root_.scala.Option[com.zitadel.idp.v1.idp.LDAPAttributes]]),
        providerOptions = __fieldsMap.get(scalaDescriptor.findFieldByNumber(13).get).flatMap(_.as[_root_.scala.Option[com.zitadel.idp.v1.idp.Options]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ManagementProto.javaDescriptor.getMessageTypes().get(528)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ManagementProto.scalaDescriptor.messages(528)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 11 => __out = com.google.protobuf.duration.Duration
      case 12 => __out = com.zitadel.idp.v1.idp.LDAPAttributes
      case 13 => __out = com.zitadel.idp.v1.idp.Options
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.management.v1.management.UpdateLDAPProviderRequest(
    id = "",
    name = "",
    servers = _root_.scala.Seq.empty,
    startTls = false,
    baseDn = "",
    bindDn = "",
    bindPassword = "",
    userBase = "",
    userObjectClasses = _root_.scala.Seq.empty,
    userFilters = _root_.scala.Seq.empty,
    timeout = _root_.scala.None,
    attributes = _root_.scala.None,
    providerOptions = _root_.scala.None
  )
  implicit class UpdateLDAPProviderRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.management.v1.management.UpdateLDAPProviderRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.management.v1.management.UpdateLDAPProviderRequest](_l) {
    def id: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.id)((c_, f_) => c_.copy(id = f_))
    def name: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.name)((c_, f_) => c_.copy(name = f_))
    def servers: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[_root_.scala.Predef.String]] = field(_.servers)((c_, f_) => c_.copy(servers = f_))
    def startTls: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.startTls)((c_, f_) => c_.copy(startTls = f_))
    def baseDn: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.baseDn)((c_, f_) => c_.copy(baseDn = f_))
    def bindDn: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.bindDn)((c_, f_) => c_.copy(bindDn = f_))
    def bindPassword: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.bindPassword)((c_, f_) => c_.copy(bindPassword = f_))
    def userBase: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.userBase)((c_, f_) => c_.copy(userBase = f_))
    def userObjectClasses: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[_root_.scala.Predef.String]] = field(_.userObjectClasses)((c_, f_) => c_.copy(userObjectClasses = f_))
    def userFilters: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[_root_.scala.Predef.String]] = field(_.userFilters)((c_, f_) => c_.copy(userFilters = f_))
    def timeout: _root_.scalapb.lenses.Lens[UpperPB, com.google.protobuf.duration.Duration] = field(_.getTimeout)((c_, f_) => c_.copy(timeout = Option(f_)))
    def optionalTimeout: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.google.protobuf.duration.Duration]] = field(_.timeout)((c_, f_) => c_.copy(timeout = f_))
    def attributes: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.LDAPAttributes] = field(_.getAttributes)((c_, f_) => c_.copy(attributes = Option(f_)))
    def optionalAttributes: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.idp.v1.idp.LDAPAttributes]] = field(_.attributes)((c_, f_) => c_.copy(attributes = f_))
    def providerOptions: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.idp.v1.idp.Options] = field(_.getProviderOptions)((c_, f_) => c_.copy(providerOptions = Option(f_)))
    def optionalProviderOptions: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.idp.v1.idp.Options]] = field(_.providerOptions)((c_, f_) => c_.copy(providerOptions = f_))
  }
  final val ID_FIELD_NUMBER = 1
  final val NAME_FIELD_NUMBER = 2
  final val SERVERS_FIELD_NUMBER = 3
  final val START_TLS_FIELD_NUMBER = 4
  final val BASE_DN_FIELD_NUMBER = 5
  final val BIND_DN_FIELD_NUMBER = 6
  final val BIND_PASSWORD_FIELD_NUMBER = 7
  final val USER_BASE_FIELD_NUMBER = 8
  final val USER_OBJECT_CLASSES_FIELD_NUMBER = 9
  final val USER_FILTERS_FIELD_NUMBER = 10
  final val TIMEOUT_FIELD_NUMBER = 11
  final val ATTRIBUTES_FIELD_NUMBER = 12
  final val PROVIDER_OPTIONS_FIELD_NUMBER = 13
  def of(
    id: _root_.scala.Predef.String,
    name: _root_.scala.Predef.String,
    servers: _root_.scala.Seq[_root_.scala.Predef.String],
    startTls: _root_.scala.Boolean,
    baseDn: _root_.scala.Predef.String,
    bindDn: _root_.scala.Predef.String,
    bindPassword: _root_.scala.Predef.String,
    userBase: _root_.scala.Predef.String,
    userObjectClasses: _root_.scala.Seq[_root_.scala.Predef.String],
    userFilters: _root_.scala.Seq[_root_.scala.Predef.String],
    timeout: _root_.scala.Option[com.google.protobuf.duration.Duration],
    attributes: _root_.scala.Option[com.zitadel.idp.v1.idp.LDAPAttributes],
    providerOptions: _root_.scala.Option[com.zitadel.idp.v1.idp.Options]
  ): _root_.com.zitadel.management.v1.management.UpdateLDAPProviderRequest = _root_.com.zitadel.management.v1.management.UpdateLDAPProviderRequest(
    id,
    name,
    servers,
    startTls,
    baseDn,
    bindDn,
    bindPassword,
    userBase,
    userObjectClasses,
    userFilters,
    timeout,
    attributes,
    providerOptions
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.management.v1.UpdateLDAPProviderRequest])
}

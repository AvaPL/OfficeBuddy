// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.`object`.v2alpha.`object`

@SerialVersionUID(0L)
final case class Organisation(
    org: com.zitadel.`object`.v2alpha.`object`.Organisation.Org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.Empty,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[Organisation] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (org.orgId.isDefined) {
        val __value = org.orgId.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
      };
      if (org.orgDomain.isDefined) {
        val __value = org.orgDomain.get
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
      org.orgId.foreach { __v =>
        val __m = __v
        _output__.writeString(1, __m)
      };
      org.orgDomain.foreach { __v =>
        val __m = __v
        _output__.writeString(2, __m)
      };
      unknownFields.writeTo(_output__)
    }
    def getOrgId: _root_.scala.Predef.String = org.orgId.getOrElse("")
    def withOrgId(__v: _root_.scala.Predef.String): Organisation = copy(org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.OrgId(__v))
    def getOrgDomain: _root_.scala.Predef.String = org.orgDomain.getOrElse("")
    def withOrgDomain(__v: _root_.scala.Predef.String): Organisation = copy(org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.OrgDomain(__v))
    def clearOrg: Organisation = copy(org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.Empty)
    def withOrg(__v: com.zitadel.`object`.v2alpha.`object`.Organisation.Org): Organisation = copy(org = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => org.orgId.orNull
        case 2 => org.orgDomain.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => org.orgId.map(_root_.scalapb.descriptors.PString(_)).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => org.orgDomain.map(_root_.scalapb.descriptors.PString(_)).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.`object`.v2alpha.`object`.Organisation.type = com.zitadel.`object`.v2alpha.`object`.Organisation
    // @@protoc_insertion_point(GeneratedMessage[zitadel.object.v2alpha.Organisation])
}

object Organisation extends scalapb.GeneratedMessageCompanion[com.zitadel.`object`.v2alpha.`object`.Organisation] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.`object`.v2alpha.`object`.Organisation] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.`object`.v2alpha.`object`.Organisation = {
    var __org: com.zitadel.`object`.v2alpha.`object`.Organisation.Org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.Empty
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.OrgId(_input__.readStringRequireUtf8())
        case 18 =>
          __org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.OrgDomain(_input__.readStringRequireUtf8())
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.`object`.v2alpha.`object`.Organisation(
        org = __org,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.`object`.v2alpha.`object`.Organisation] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.`object`.v2alpha.`object`.Organisation(
        org = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Predef.String]]).map(com.zitadel.`object`.v2alpha.`object`.Organisation.Org.OrgId(_))
            .orElse[com.zitadel.`object`.v2alpha.`object`.Organisation.Org](__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Predef.String]]).map(com.zitadel.`object`.v2alpha.`object`.Organisation.Org.OrgDomain(_)))
            .getOrElse(com.zitadel.`object`.v2alpha.`object`.Organisation.Org.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ObjectProto.javaDescriptor.getMessageTypes().get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ObjectProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.`object`.v2alpha.`object`.Organisation(
    org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.Empty
  )
  sealed trait Org extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isOrgId: _root_.scala.Boolean = false
    def isOrgDomain: _root_.scala.Boolean = false
    def orgId: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None
    def orgDomain: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None
  }
  object Org {
    @SerialVersionUID(0L)
    case object Empty extends com.zitadel.`object`.v2alpha.`object`.Organisation.Org {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class OrgId(value: _root_.scala.Predef.String) extends com.zitadel.`object`.v2alpha.`object`.Organisation.Org {
      type ValueType = _root_.scala.Predef.String
      override def isOrgId: _root_.scala.Boolean = true
      override def orgId: _root_.scala.Option[_root_.scala.Predef.String] = Some(value)
      override def number: _root_.scala.Int = 1
    }
    @SerialVersionUID(0L)
    final case class OrgDomain(value: _root_.scala.Predef.String) extends com.zitadel.`object`.v2alpha.`object`.Organisation.Org {
      type ValueType = _root_.scala.Predef.String
      override def isOrgDomain: _root_.scala.Boolean = true
      override def orgDomain: _root_.scala.Option[_root_.scala.Predef.String] = Some(value)
      override def number: _root_.scala.Int = 2
    }
  }
  implicit class OrganisationLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.`object`.v2alpha.`object`.Organisation]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.`object`.v2alpha.`object`.Organisation](_l) {
    def orgId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.getOrgId)((c_, f_) => c_.copy(org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.OrgId(f_)))
    def orgDomain: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.getOrgDomain)((c_, f_) => c_.copy(org = com.zitadel.`object`.v2alpha.`object`.Organisation.Org.OrgDomain(f_)))
    def org: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.`object`.v2alpha.`object`.Organisation.Org] = field(_.org)((c_, f_) => c_.copy(org = f_))
  }
  final val ORG_ID_FIELD_NUMBER = 1
  final val ORG_DOMAIN_FIELD_NUMBER = 2
  def of(
    org: com.zitadel.`object`.v2alpha.`object`.Organisation.Org
  ): _root_.com.zitadel.`object`.v2alpha.`object`.Organisation = _root_.com.zitadel.`object`.v2alpha.`object`.Organisation(
    org
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.object.v2alpha.Organisation])
}

// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.admin.v1.admin

@SerialVersionUID(0L)
final case class GetLockoutPolicyResponse(
    policy: _root_.scala.Option[com.zitadel.policy.v1.policy.LockoutPolicy] = _root_.scala.None,
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[GetLockoutPolicyResponse] {
    @transient
    private[this] var __serializedSizeMemoized: _root_.scala.Int = 0
    private[this] def __computeSerializedSize(): _root_.scala.Int = {
      var __size = 0
      if (policy.isDefined) {
        val __value = policy.get
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
      policy.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      unknownFields.writeTo(_output__)
    }
    def getPolicy: com.zitadel.policy.v1.policy.LockoutPolicy = policy.getOrElse(com.zitadel.policy.v1.policy.LockoutPolicy.defaultInstance)
    def clearPolicy: GetLockoutPolicyResponse = copy(policy = _root_.scala.None)
    def withPolicy(__v: com.zitadel.policy.v1.policy.LockoutPolicy): GetLockoutPolicyResponse = copy(policy = Option(__v))
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => policy.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => policy.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion: com.zitadel.admin.v1.admin.GetLockoutPolicyResponse.type = com.zitadel.admin.v1.admin.GetLockoutPolicyResponse
    // @@protoc_insertion_point(GeneratedMessage[zitadel.admin.v1.GetLockoutPolicyResponse])
}

object GetLockoutPolicyResponse extends scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.GetLockoutPolicyResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.zitadel.admin.v1.admin.GetLockoutPolicyResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.zitadel.admin.v1.admin.GetLockoutPolicyResponse = {
    var __policy: _root_.scala.Option[com.zitadel.policy.v1.policy.LockoutPolicy] = _root_.scala.None
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __policy = Option(__policy.fold(_root_.scalapb.LiteParser.readMessage[com.zitadel.policy.v1.policy.LockoutPolicy](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    com.zitadel.admin.v1.admin.GetLockoutPolicyResponse(
        policy = __policy,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.zitadel.admin.v1.admin.GetLockoutPolicyResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      com.zitadel.admin.v1.admin.GetLockoutPolicyResponse(
        policy = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.zitadel.policy.v1.policy.LockoutPolicy]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = AdminProto.javaDescriptor.getMessageTypes().get(215)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = AdminProto.scalaDescriptor.messages(215)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.zitadel.policy.v1.policy.LockoutPolicy
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.zitadel.admin.v1.admin.GetLockoutPolicyResponse(
    policy = _root_.scala.None
  )
  implicit class GetLockoutPolicyResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.admin.v1.admin.GetLockoutPolicyResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.zitadel.admin.v1.admin.GetLockoutPolicyResponse](_l) {
    def policy: _root_.scalapb.lenses.Lens[UpperPB, com.zitadel.policy.v1.policy.LockoutPolicy] = field(_.getPolicy)((c_, f_) => c_.copy(policy = Option(f_)))
    def optionalPolicy: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.zitadel.policy.v1.policy.LockoutPolicy]] = field(_.policy)((c_, f_) => c_.copy(policy = f_))
  }
  final val POLICY_FIELD_NUMBER = 1
  def of(
    policy: _root_.scala.Option[com.zitadel.policy.v1.policy.LockoutPolicy]
  ): _root_.com.zitadel.admin.v1.admin.GetLockoutPolicyResponse = _root_.com.zitadel.admin.v1.admin.GetLockoutPolicyResponse(
    policy
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[zitadel.admin.v1.GetLockoutPolicyResponse])
}

// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.user.v2alpha.email

object EmailProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq(
    com.google.api.annotations.AnnotationsProto,
    com.google.api.field_behavior.FieldBehaviorProto,
    com.grpc.gateway.protoc_gen_openapiv2.options.annotations.AnnotationsProto,
    com.validate.validate.ValidateProto
  )
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] =
    Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]](
      com.zitadel.user.v2alpha.email.SetHumanEmail,
      com.zitadel.user.v2alpha.email.SendEmailVerificationCode,
      com.zitadel.user.v2alpha.email.ReturnEmailVerificationCode
    )
  private lazy val ProtoBytes: _root_.scala.Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.immutable.Seq(
  """CiB6aXRhZGVsL3VzZXIvdjJhbHBoYS9lbWFpbC5wcm90bxIUeml0YWRlbC51c2VyLnYyYWxwaGEaHGdvb2dsZS9hcGkvYW5ub
  3RhdGlvbnMucHJvdG8aH2dvb2dsZS9hcGkvZmllbGRfYmVoYXZpb3IucHJvdG8aLnByb3RvYy1nZW4tb3BlbmFwaXYyL29wdGlvb
  nMvYW5ub3RhdGlvbnMucHJvdG8aF3ZhbGlkYXRlL3ZhbGlkYXRlLnByb3RvIuwCCg1TZXRIdW1hbkVtYWlsEkoKBWVtYWlsGAEgA
  SgJQjTiPwcSBWVtYWlskkEYShAibWluaUBtb3VzZS5jb20ieMgBgAEB4EEC+kIJcgcQARjIAWABUgVlbWFpbBJdCglzZW5kX2NvZ
  GUYAiABKAsyLy56aXRhZGVsLnVzZXIudjJhbHBoYS5TZW5kRW1haWxWZXJpZmljYXRpb25Db2RlQg3iPwoSCHNlbmRDb2RlSABSC
  HNlbmRDb2RlEmUKC3JldHVybl9jb2RlGAMgASgLMjEueml0YWRlbC51c2VyLnYyYWxwaGEuUmV0dXJuRW1haWxWZXJpZmljYXRpb
  25Db2RlQg/iPwwSCnJldHVybkNvZGVIAFIKcmV0dXJuQ29kZRI5Cgtpc192ZXJpZmllZBgEIAEoCEIW4j8MEgppc1ZlcmlmaWVk+
  kIEagIIAUgAUgppc1ZlcmlmaWVkQg4KDHZlcmlmaWNhdGlvbiKcAwoZU2VuZEVtYWlsVmVyaWZpY2F0aW9uQ29kZRLtAgoMdXJsX
  3RlbXBsYXRlGAEgASgJQsQC4j8NEgt1cmxUZW1wbGF0ZZJBpgIyxgEiT3B0aW9uYWxseSBzZXQgYSB1cmxfdGVtcGxhdGUsIHdoa
  WNoIHdpbGwgYmUgdXNlZCBpbiB0aGUgdmVyaWZpY2F0aW9uIG1haWwgc2VudCBieSBaSVRBREVMIHRvIGd1aWRlIHRoZSB1c2VyI
  HRvIHlvdXIgdmVyaWZpY2F0aW9uIHBhZ2UuIElmIG5vIHRlbXBsYXRlIGlzIHNldCwgdGhlIGRlZmF1bHQgWklUQURFTCB1cmwgd
  2lsbCBiZSB1c2VkLiJKVSJodHRwczovL2V4YW1wbGUuY29tL2VtYWlsL3ZlcmlmeT91c2VySUQ9e3suVXNlcklEfX0mY29kZT17e
  y5Db2RlfX0mb3JnSUQ9e3suT3JnSUR9fSJ4yAGAAQH6QgdyBRABGMgBSABSC3VybFRlbXBsYXRliAEBQg8KDV91cmxfdGVtcGxhd
  GUiHQobUmV0dXJuRW1haWxWZXJpZmljYXRpb25Db2RlQs8BChhjb20ueml0YWRlbC51c2VyLnYyYWxwaGFCCkVtYWlsUHJvdG9QA
  Vo1Z2l0aHViLmNvbS96aXRhZGVsL3ppdGFkZWwvcGtnL2dycGMvdXNlci92MmFscGhhO3VzZXKiAgNaVViqAhRaaXRhZGVsLlVzZ
  XIuVjJhbHBoYcoCFFppdGFkZWxcVXNlclxWMmFscGhh4gIgWml0YWRlbFxVc2VyXFYyYWxwaGFcR1BCTWV0YWRhdGHqAhZaaXRhZ
  GVsOjpVc2VyOjpWMmFscGhhYgZwcm90bzM="""
      ).mkString)
  lazy val scalaDescriptor: _root_.scalapb.descriptors.FileDescriptor = {
    val scalaProto = com.google.protobuf.descriptor.FileDescriptorProto.parseFrom(ProtoBytes)
    _root_.scalapb.descriptors.FileDescriptor.buildFrom(scalaProto, dependencies.map(_.scalaDescriptor))
  }
  lazy val javaDescriptor: com.google.protobuf.Descriptors.FileDescriptor = {
    val javaProto = com.google.protobuf.DescriptorProtos.FileDescriptorProto.parseFrom(ProtoBytes)
    com.google.protobuf.Descriptors.FileDescriptor.buildFrom(javaProto, _root_.scala.Array(
      com.google.api.annotations.AnnotationsProto.javaDescriptor,
      com.google.api.field_behavior.FieldBehaviorProto.javaDescriptor,
      com.grpc.gateway.protoc_gen_openapiv2.options.annotations.AnnotationsProto.javaDescriptor,
      com.validate.validate.ValidateProto.javaDescriptor
    ))
  }
  @deprecated("Use javaDescriptor instead. In a future version this will refer to scalaDescriptor.", "ScalaPB 0.5.47")
  def descriptor: com.google.protobuf.Descriptors.FileDescriptor = javaDescriptor
}
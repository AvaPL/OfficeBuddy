// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.settings.v2alpha.legal_settings

object LegalSettingsProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq(
    com.grpc.gateway.protoc_gen_openapiv2.options.annotations.AnnotationsProto,
    com.zitadel.settings.v2alpha.settings.SettingsProto,
    com.validate.validate.ValidateProto
  )
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] =
    Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]](
      com.zitadel.settings.v2alpha.legal_settings.LegalAndSupportSettings
    )
  private lazy val ProtoBytes: _root_.scala.Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.immutable.Seq(
  """Ci16aXRhZGVsL3NldHRpbmdzL3YyYWxwaGEvbGVnYWxfc2V0dGluZ3MucHJvdG8SGHppdGFkZWwuc2V0dGluZ3MudjJhbHBoY
  RoucHJvdG9jLWdlbi1vcGVuYXBpdjIvb3B0aW9ucy9hbm5vdGF0aW9ucy5wcm90bxoneml0YWRlbC9zZXR0aW5ncy92MmFscGhhL
  3NldHRpbmdzLnByb3RvGhd2YWxpZGF0ZS92YWxpZGF0ZS5wcm90byKsBQoXTGVnYWxBbmRTdXBwb3J0U2V0dGluZ3MSXQoIdG9zX
  2xpbmsYASABKAlCQuI/CRIHdG9zTGlua5JBM0oxImh0dHBzOi8veml0YWRlbC5jb20vZG9jcy9sZWdhbC90ZXJtcy1vZi1zZXJ2a
  WNlIlIHdG9zTGluaxJ6ChNwcml2YWN5X3BvbGljeV9saW5rGAIgASgJQkriPxMSEXByaXZhY3lQb2xpY3lMaW5rkkExSi8iaHR0c
  HM6Ly96aXRhZGVsLmNvbS9kb2NzL2xlZ2FsL3ByaXZhY3ktcG9saWN5IlIRcHJpdmFjeVBvbGljeUxpbmsSXgoJaGVscF9saW5rG
  AMgASgJQkHiPwoSCGhlbHBMaW5rkkExSi8iaHR0cHM6Ly96aXRhZGVsLmNvbS9kb2NzL21hbnVhbHMvaW50cm9kdWN0aW9uIlIIa
  GVscExpbmsSfwoNc3VwcG9ydF9lbWFpbBgEIAEoCUJa4j8OEgxzdXBwb3J0RW1haWySQTkyHWhlbHAgLyBzdXBwb3J0IGVtYWlsI
  GFkZHJlc3MuShgic3VwcG9ydC1lbWFpbEB0ZXN0LmNvbSL6QgpyCBjAAtABAWABUgxzdXBwb3J0RW1haWwS1AEKE3Jlc291cmNlX
  293bmVyX3R5cGUYBSABKA4yKy56aXRhZGVsLnNldHRpbmdzLnYyYWxwaGEuUmVzb3VyY2VPd25lclR5cGVCd+I/ExIRcmVzb3VyY
  2VPd25lclR5cGWSQV4yXHJlc291cmNlX293bmVyX3R5cGUgcmV0dXJucyBpZiB0aGUgc2V0dGluZyBpcyBtYW5hZ2VkIG9uIHRoZ
  SBvcmdhbml6YXRpb24gb3Igb24gdGhlIGluc3RhbmNlUhFyZXNvdXJjZU93bmVyVHlwZULzAQocY29tLnppdGFkZWwuc2V0dGluZ
  3MudjJhbHBoYUISTGVnYWxTZXR0aW5nc1Byb3RvUAFaPWdpdGh1Yi5jb20veml0YWRlbC96aXRhZGVsL3BrZy9ncnBjL3NldHRpb
  mdzL3YyYWxwaGE7c2V0dGluZ3OiAgNaU1iqAhhaaXRhZGVsLlNldHRpbmdzLlYyYWxwaGHKAhhaaXRhZGVsXFNldHRpbmdzXFYyY
  WxwaGHiAiRaaXRhZGVsXFNldHRpbmdzXFYyYWxwaGFcR1BCTWV0YWRhdGHqAhpaaXRhZGVsOjpTZXR0aW5nczo6VjJhbHBoYWIGc
  HJvdG8z"""
      ).mkString)
  lazy val scalaDescriptor: _root_.scalapb.descriptors.FileDescriptor = {
    val scalaProto = com.google.protobuf.descriptor.FileDescriptorProto.parseFrom(ProtoBytes)
    _root_.scalapb.descriptors.FileDescriptor.buildFrom(scalaProto, dependencies.map(_.scalaDescriptor))
  }
  lazy val javaDescriptor: com.google.protobuf.Descriptors.FileDescriptor = {
    val javaProto = com.google.protobuf.DescriptorProtos.FileDescriptorProto.parseFrom(ProtoBytes)
    com.google.protobuf.Descriptors.FileDescriptor.buildFrom(javaProto, _root_.scala.Array(
      com.grpc.gateway.protoc_gen_openapiv2.options.annotations.AnnotationsProto.javaDescriptor,
      com.zitadel.settings.v2alpha.settings.SettingsProto.javaDescriptor,
      com.validate.validate.ValidateProto.javaDescriptor
    ))
  }
  @deprecated("Use javaDescriptor instead. In a future version this will refer to scalaDescriptor.", "ScalaPB 0.5.47")
  def descriptor: com.google.protobuf.Descriptors.FileDescriptor = javaDescriptor
}
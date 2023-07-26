// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.settings.v2alpha.password_settings

object PasswordSettingsProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq(
    com.grpc.gateway.protoc_gen_openapiv2.options.annotations.AnnotationsProto,
    com.zitadel.settings.v2alpha.settings.SettingsProto
  )
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] =
    Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]](
      com.zitadel.settings.v2alpha.password_settings.PasswordComplexitySettings
    )
  private lazy val ProtoBytes: _root_.scala.Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.immutable.Seq(
  """CjB6aXRhZGVsL3NldHRpbmdzL3YyYWxwaGEvcGFzc3dvcmRfc2V0dGluZ3MucHJvdG8SGHppdGFkZWwuc2V0dGluZ3MudjJhb
  HBoYRoucHJvdG9jLWdlbi1vcGVuYXBpdjIvb3B0aW9ucy9hbm5vdGF0aW9ucy5wcm90bxoneml0YWRlbC9zZXR0aW5ncy92MmFsc
  GhhL3NldHRpbmdzLnByb3RvIsoGChpQYXNzd29yZENvbXBsZXhpdHlTZXR0aW5ncxJgCgptaW5fbGVuZ3RoGAEgASgEQkHiPwsSC
  W1pbkxlbmd0aJJBMDIpRGVmaW5lcyB0aGUgbWluaW11bSBsZW5ndGggb2YgYSBwYXNzd29yZC5KAyI4IlIJbWluTGVuZ3RoEoMBC
  hJyZXF1aXJlc191cHBlcmNhc2UYAiABKAhCVOI/ExIRcmVxdWlyZXNVcHBlcmNhc2WSQTsyOWRlZmluZXMgaWYgdGhlIHBhc3N3b
  3JkIE1VU1QgY29udGFpbiBhbiB1cHBlciBjYXNlIGxldHRlclIRcmVxdWlyZXNVcHBlcmNhc2USgQEKEnJlcXVpcmVzX2xvd2VyY
  2FzZRgDIAEoCEJS4j8TEhFyZXF1aXJlc0xvd2VyY2FzZZJBOTI3ZGVmaW5lcyBpZiB0aGUgcGFzc3dvcmQgTVVTVCBjb250YWluI
  GEgbG93ZXJjYXNlIGxldHRlclIRcmVxdWlyZXNMb3dlcmNhc2USbgoPcmVxdWlyZXNfbnVtYmVyGAQgASgIQkXiPxASDnJlcXVpc
  mVzTnVtYmVykkEvMi1kZWZpbmVzIGlmIHRoZSBwYXNzd29yZCBNVVNUIGNvbnRhaW4gYSBudW1iZXJSDnJlcXVpcmVzTnVtYmVyE
  ngKD3JlcXVpcmVzX3N5bWJvbBgFIAEoCEJP4j8QEg5yZXF1aXJlc1N5bWJvbJJBOTI3ZGVmaW5lcyBpZiB0aGUgcGFzc3dvcmQgT
  VVTVCBjb250YWluIGEgc3ltYm9sLiBFLmcuICIkIlIOcmVxdWlyZXNTeW1ib2wS1QEKE3Jlc291cmNlX293bmVyX3R5cGUYBiABK
  A4yKy56aXRhZGVsLnNldHRpbmdzLnYyYWxwaGEuUmVzb3VyY2VPd25lclR5cGVCeOI/ExIRcmVzb3VyY2VPd25lclR5cGWSQV8yX
  XJlc291cmNlX293bmVyX3R5cGUgcmV0dXJucyBpZiB0aGUgc2V0dGluZ3MgaXMgbWFuYWdlZCBvbiB0aGUgb3JnYW5pemF0aW9uI
  G9yIG9uIHRoZSBpbnN0YW5jZVIRcmVzb3VyY2VPd25lclR5cGVC9gEKHGNvbS56aXRhZGVsLnNldHRpbmdzLnYyYWxwaGFCFVBhc
  3N3b3JkU2V0dGluZ3NQcm90b1ABWj1naXRodWIuY29tL3ppdGFkZWwveml0YWRlbC9wa2cvZ3JwYy9zZXR0aW5ncy92MmFscGhhO
  3NldHRpbmdzogIDWlNYqgIYWml0YWRlbC5TZXR0aW5ncy5WMmFscGhhygIYWml0YWRlbFxTZXR0aW5nc1xWMmFscGhh4gIkWml0Y
  WRlbFxTZXR0aW5nc1xWMmFscGhhXEdQQk1ldGFkYXRh6gIaWml0YWRlbDo6U2V0dGluZ3M6OlYyYWxwaGFiBnByb3RvMw=="""
      ).mkString)
  lazy val scalaDescriptor: _root_.scalapb.descriptors.FileDescriptor = {
    val scalaProto = com.google.protobuf.descriptor.FileDescriptorProto.parseFrom(ProtoBytes)
    _root_.scalapb.descriptors.FileDescriptor.buildFrom(scalaProto, dependencies.map(_.scalaDescriptor))
  }
  lazy val javaDescriptor: com.google.protobuf.Descriptors.FileDescriptor = {
    val javaProto = com.google.protobuf.DescriptorProtos.FileDescriptorProto.parseFrom(ProtoBytes)
    com.google.protobuf.Descriptors.FileDescriptor.buildFrom(javaProto, _root_.scala.Array(
      com.grpc.gateway.protoc_gen_openapiv2.options.annotations.AnnotationsProto.javaDescriptor,
      com.zitadel.settings.v2alpha.settings.SettingsProto.javaDescriptor
    ))
  }
  @deprecated("Use javaDescriptor instead. In a future version this will refer to scalaDescriptor.", "ScalaPB 0.5.47")
  def descriptor: com.google.protobuf.Descriptors.FileDescriptor = javaDescriptor
}
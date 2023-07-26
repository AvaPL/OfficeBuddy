// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.zitadel.user.v2alpha.user_service

object UserServiceProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq(
    com.zitadel.`object`.v2alpha.`object`.ObjectProto,
    com.zitadel.protoc_gen_zitadel.v2.options.OptionsProto,
    com.zitadel.user.v2alpha.auth.AuthProto,
    com.zitadel.user.v2alpha.email.EmailProto,
    com.zitadel.user.v2alpha.idp.IdpProto,
    com.zitadel.user.v2alpha.password.PasswordProto,
    com.zitadel.user.v2alpha.user.UserProto,
    com.google.api.annotations.AnnotationsProto,
    com.google.api.field_behavior.FieldBehaviorProto,
    com.google.protobuf.duration.DurationProto,
    com.google.protobuf.struct.StructProto,
    com.grpc.gateway.protoc_gen_openapiv2.options.annotations.AnnotationsProto,
    com.validate.validate.ValidateProto
  )
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] =
    Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]](
      com.zitadel.user.v2alpha.user_service.AddHumanUserRequest,
      com.zitadel.user.v2alpha.user_service.AddHumanUserResponse,
      com.zitadel.user.v2alpha.user_service.SetEmailRequest,
      com.zitadel.user.v2alpha.user_service.SetEmailResponse,
      com.zitadel.user.v2alpha.user_service.VerifyEmailRequest,
      com.zitadel.user.v2alpha.user_service.VerifyEmailResponse,
      com.zitadel.user.v2alpha.user_service.RegisterPasskeyRequest,
      com.zitadel.user.v2alpha.user_service.RegisterPasskeyResponse,
      com.zitadel.user.v2alpha.user_service.VerifyPasskeyRegistrationRequest,
      com.zitadel.user.v2alpha.user_service.VerifyPasskeyRegistrationResponse,
      com.zitadel.user.v2alpha.user_service.RegisterU2FRequest,
      com.zitadel.user.v2alpha.user_service.RegisterU2FResponse,
      com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationRequest,
      com.zitadel.user.v2alpha.user_service.VerifyU2FRegistrationResponse,
      com.zitadel.user.v2alpha.user_service.RegisterTOTPRequest,
      com.zitadel.user.v2alpha.user_service.RegisterTOTPResponse,
      com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationRequest,
      com.zitadel.user.v2alpha.user_service.VerifyTOTPRegistrationResponse,
      com.zitadel.user.v2alpha.user_service.CreatePasskeyRegistrationLinkRequest,
      com.zitadel.user.v2alpha.user_service.CreatePasskeyRegistrationLinkResponse,
      com.zitadel.user.v2alpha.user_service.StartIdentityProviderFlowRequest,
      com.zitadel.user.v2alpha.user_service.StartIdentityProviderFlowResponse,
      com.zitadel.user.v2alpha.user_service.RetrieveIdentityProviderInformationRequest,
      com.zitadel.user.v2alpha.user_service.RetrieveIdentityProviderInformationResponse,
      com.zitadel.user.v2alpha.user_service.AddIDPLinkRequest,
      com.zitadel.user.v2alpha.user_service.AddIDPLinkResponse,
      com.zitadel.user.v2alpha.user_service.PasswordResetRequest,
      com.zitadel.user.v2alpha.user_service.PasswordResetResponse,
      com.zitadel.user.v2alpha.user_service.SetPasswordRequest,
      com.zitadel.user.v2alpha.user_service.SetPasswordResponse,
      com.zitadel.user.v2alpha.user_service.ListAuthenticationMethodTypesRequest,
      com.zitadel.user.v2alpha.user_service.ListAuthenticationMethodTypesResponse
    )
  private lazy val ProtoBytes: _root_.scala.Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.immutable.Seq(
  """Cid6aXRhZGVsL3VzZXIvdjJhbHBoYS91c2VyX3NlcnZpY2UucHJvdG8SFHppdGFkZWwudXNlci52MmFscGhhGiN6aXRhZGVsL
  29iamVjdC92MmFscGhhL29iamVjdC5wcm90bxoreml0YWRlbC9wcm90b2NfZ2VuX3ppdGFkZWwvdjIvb3B0aW9ucy5wcm90bxofe
  ml0YWRlbC91c2VyL3YyYWxwaGEvYXV0aC5wcm90bxogeml0YWRlbC91c2VyL3YyYWxwaGEvZW1haWwucHJvdG8aHnppdGFkZWwvd
  XNlci92MmFscGhhL2lkcC5wcm90bxojeml0YWRlbC91c2VyL3YyYWxwaGEvcGFzc3dvcmQucHJvdG8aH3ppdGFkZWwvdXNlci92M
  mFscGhhL3VzZXIucHJvdG8aHGdvb2dsZS9hcGkvYW5ub3RhdGlvbnMucHJvdG8aH2dvb2dsZS9hcGkvZmllbGRfYmVoYXZpb3Iuc
  HJvdG8aHmdvb2dsZS9wcm90b2J1Zi9kdXJhdGlvbi5wcm90bxocZ29vZ2xlL3Byb3RvYnVmL3N0cnVjdC5wcm90bxoucHJvdG9jL
  Wdlbi1vcGVuYXBpdjIvb3B0aW9ucy9hbm5vdGF0aW9ucy5wcm90bxoXdmFsaWRhdGUvdmFsaWRhdGUucHJvdG8i0gYKE0FkZEh1b
  WFuVXNlclJlcXVlc3QSZAoHdXNlcl9pZBgBIAEoCUJG4j8IEgZ1c2VySWSSQS5KJiJkNjU0ZTZiYS03MGEzLTQ4ZWYtYTk1ZC0zN
  2M4ZDhhNzkwMWEieMgBgAEB+kIHcgUQARjIAUgBUgZ1c2VySWSIAQESUQoIdXNlcm5hbWUYAiABKAlCMOI/ChIIdXNlcm5hbWWSQ
  RZKDiJtaW5uaWUtbW91c2UieMgBgAEB+kIHcgUQARjIAUgCUgh1c2VybmFtZYgBARJbCgxvcmdhbmlzYXRpb24YAyABKAsyJC56a
  XRhZGVsLm9iamVjdC52MmFscGhhLk9yZ2FuaXNhdGlvbkIR4j8OEgxvcmdhbmlzYXRpb25SDG9yZ2FuaXNhdGlvbhJYCgdwcm9ma
  WxlGAQgASgLMiUueml0YWRlbC51c2VyLnYyYWxwaGEuU2V0SHVtYW5Qcm9maWxlQhfiPwkSB3Byb2ZpbGXgQQL6QgWKAQIQAVIHc
  HJvZmlsZRJQCgVlbWFpbBgFIAEoCzIjLnppdGFkZWwudXNlci52MmFscGhhLlNldEh1bWFuRW1haWxCFeI/BxIFZW1haWzgQQL6Q
  gWKAQIQAVIFZW1haWwSUQoIbWV0YWRhdGEYBiADKAsyJi56aXRhZGVsLnVzZXIudjJhbHBoYS5TZXRNZXRhZGF0YUVudHJ5Qg3iP
  woSCG1ldGFkYXRhUghtZXRhZGF0YRJLCghwYXNzd29yZBgHIAEoCzIeLnppdGFkZWwudXNlci52MmFscGhhLlBhc3N3b3JkQg3iP
  woSCHBhc3N3b3JkSABSCHBhc3N3b3JkEmQKD2hhc2hlZF9wYXNzd29yZBgIIAEoCzIkLnppdGFkZWwudXNlci52MmFscGhhLkhhc
  2hlZFBhc3N3b3JkQhPiPxASDmhhc2hlZFBhc3N3b3JkSABSDmhhc2hlZFBhc3N3b3JkEkkKCWlkcF9saW5rcxgJIAMoCzIdLnppd
  GFkZWwudXNlci52MmFscGhhLklEUExpbmtCDeI/ChIIaWRwTGlua3NSCGlkcExpbmtzQg8KDXBhc3N3b3JkX3R5cGVCCgoIX3VzZ
  XJfaWRCCwoJX3VzZXJuYW1lIsgBChRBZGRIdW1hblVzZXJSZXNwb25zZRIkCgd1c2VyX2lkGAEgASgJQgviPwgSBnVzZXJJZFIGd
  XNlcklkEkcKB2RldGFpbHMYAiABKAsyHy56aXRhZGVsLm9iamVjdC52MmFscGhhLkRldGFpbHNCDOI/CRIHZGV0YWlsc1IHZGV0Y
  WlscxIyCgplbWFpbF9jb2RlGAMgASgJQg7iPwsSCWVtYWlsQ29kZUgAUgllbWFpbENvZGWIAQFCDQoLX2VtYWlsX2NvZGUivwMKD
  1NldEVtYWlsUmVxdWVzdBJPCgd1c2VyX2lkGAEgASgJQjbiPwgSBnVzZXJJZJJBG0oTIjY5NjI5MDI2ODA2NDg5NDU1InjIAYABA
  eBBAvpCB3IFEAEYyAFSBnVzZXJJZBJKCgVlbWFpbBgCIAEoCUI04j8HEgVlbWFpbJJBGEoQIm1pbmlAbW91c2UuY29tInjIAYABA
  eBBAvpCCXIHEAEYyAFgAVIFZW1haWwSXQoJc2VuZF9jb2RlGAMgASgLMi8ueml0YWRlbC51c2VyLnYyYWxwaGEuU2VuZEVtYWlsV
  mVyaWZpY2F0aW9uQ29kZUIN4j8KEghzZW5kQ29kZUgAUghzZW5kQ29kZRJlCgtyZXR1cm5fY29kZRgEIAEoCzIxLnppdGFkZWwud
  XNlci52MmFscGhhLlJldHVybkVtYWlsVmVyaWZpY2F0aW9uQ29kZUIP4j8MEgpyZXR1cm5Db2RlSABSCnJldHVybkNvZGUSOQoLa
  XNfdmVyaWZpZWQYBSABKAhCFuI/DBIKaXNWZXJpZmllZPpCBGoCCAFIAFIKaXNWZXJpZmllZEIOCgx2ZXJpZmljYXRpb24iugEKE
  FNldEVtYWlsUmVzcG9uc2USRwoHZGV0YWlscxgBIAEoCzIfLnppdGFkZWwub2JqZWN0LnYyYWxwaGEuRGV0YWlsc0IM4j8JEgdkZ
  XRhaWxzUgdkZXRhaWxzEkcKEXZlcmlmaWNhdGlvbl9jb2RlGAIgASgJQhXiPxISEHZlcmlmaWNhdGlvbkNvZGVIAFIQdmVyaWZpY
  2F0aW9uQ29kZYgBAUIUChJfdmVyaWZpY2F0aW9uX2NvZGUiigIKElZlcmlmeUVtYWlsUmVxdWVzdBJPCgd1c2VyX2lkGAEgASgJQ
  jbiPwgSBnVzZXJJZJJBG0oTIjY5NjI5MDI2ODA2NDg5NDU1InjIAYABAeBBAvpCB3IFEAEYyAFSBnVzZXJJZBKiAQoRdmVyaWZpY
  2F0aW9uX2NvZGUYAiABKAlCdeI/EhIQdmVyaWZpY2F0aW9uQ29kZZJBUTI+InRoZSB2ZXJpZmljYXRpb24gY29kZSBnZW5lcmF0Z
  WQgZHVyaW5nIHRoZSBzZXQgZW1haWwgcmVxdWVzdCJKCiJTS0pkMzQyayJ4FIABAeBBAvpCBnIEEAEYFFIQdmVyaWZpY2F0aW9uQ
  29kZSJeChNWZXJpZnlFbWFpbFJlc3BvbnNlEkcKB2RldGFpbHMYASABKAsyHy56aXRhZGVsLm9iamVjdC52MmFscGhhLkRldGFpb
  HNCDOI/CRIHZGV0YWlsc1IHZGV0YWlscyKKBQoWUmVnaXN0ZXJQYXNza2V5UmVxdWVzdBJQCgd1c2VyX2lkGAEgASgJQjfiPwgSB
  nVzZXJJZJJBHEoUIjE2Mzg0MDc3NjgzNTQzMjcwNSJ4yAGAAQHgQQL6QgdyBRABGMgBUgZ1c2VySWQSwgEKBGNvZGUYAiABKAsyL
  S56aXRhZGVsLnVzZXIudjJhbHBoYS5QYXNza2V5UmVnaXN0cmF0aW9uQ29kZUJ64j8GEgRjb2RlkkFuMmwib25lIHRpbWUgY29kZ
  SBnZW5lcmF0ZWQgYnkgWklUQURFTDsgcmVxdWlyZWQgdG8gc3RhcnQgdGhlIHBhc3NrZXkgcmVnaXN0cmF0aW9uIHdpdGhvdXQgd
  XNlciBhdXRoZW50aWNhdGlvbiJIAFIEY29kZYgBARL5AQoNYXV0aGVudGljYXRvchgDIAEoDjIqLnppdGFkZWwudXNlci52MmFsc
  GhhLlBhc3NrZXlBdXRoZW50aWNhdG9yQqYB4j8PEg1hdXRoZW50aWNhdG9ykkGQATKNASJPcHRpb25hbGx5IHNwZWNpZnkgdGhlI
  GF1dGhlbnRpY2F0b3IgdHlwZSBvZiB0aGUgcGFzc2tleSBkZXZpY2UgKHBsYXRmb3JtIG9yIGNyb3NzLXBsYXRmb3JtKS4gSWYgb
  m9uZSBpcyBwcm92aWRlZCwgYm90aCB2YWx1ZXMgYXJlIGFsbG93ZWQuIlINYXV0aGVudGljYXRvchJUCgZkb21haW4YBCABKAlCP
  OI/CBIGZG9tYWlukkEuMiwiRG9tYWluIG9uIHdoaWNoIHRoZSB1c2VyIGlzIGF1dGhlbnRpY2F0ZWQuIlIGZG9tYWluQgcKBV9jb
  2RlIv4HChdSZWdpc3RlclBhc3NrZXlSZXNwb25zZRJHCgdkZXRhaWxzGAEgASgLMh8ueml0YWRlbC5vYmplY3QudjJhbHBoYS5EZ
  XRhaWxzQgziPwkSB2RldGFpbHNSB2RldGFpbHMSRgoKcGFzc2tleV9pZBgCIAEoCUIn4j8LEglwYXNza2V5SWSSQRZKFCIxNjM4N
  DA3NzY4MzU0MzI3MDUiUglwYXNza2V5SWQS0QYKJnB1YmxpY19rZXlfY3JlZGVudGlhbF9jcmVhdGlvbl9vcHRpb25zGAMgASgLM
  hcuZ29vZ2xlLnByb3RvYnVmLlN0cnVjdELjBeI/JBIicHVibGljS2V5Q3JlZGVudGlhbENyZWF0aW9uT3B0aW9uc5JBuAUy+gFPc
  HRpb25zIGZvciBDcmVkZW50aWFsIENyZWF0aW9uIChkaWN0aW9uYXJ5IFB1YmxpY0tleUNyZWRlbnRpYWxDcmVhdGlvbk9wdGlvb
  nMpLiBHZW5lcmF0ZWQgaGVscGVyIG1ldGhvZHMgdHJhbnNmb3JtIHRoZSBmaWVsZCB0byBKU09OLCBmb3IgdXNlIGluIGEgV2ViY
  XV0aE4gY2xpZW50LiBTZWUgYWxzbzogIGh0dHBzOi8vd3d3LnczLm9yZy9UUi93ZWJhdXRobi8jZGljdGRlZi1wdWJsaWNrZXljc
  mVkZW50aWFsY3JlYXRpb25vcHRpb25zSrgDeyJwdWJsaWNLZXkiOnsiYXR0ZXN0YXRpb24iOiJub25lIiwiYXV0aGVudGljYXRvc
  lNlbGVjdGlvbiI6eyJ1c2VyVmVyaWZpY2F0aW9uIjoicmVxdWlyZWQifSwiY2hhbGxlbmdlIjoiWGFNWXdXT1o1aGo2cHd0d0pKb
  HBjSS1FeGtPNVR4ZXZCTUc0UjhEb0tRUSIsImV4Y2x1ZGVDcmVkZW50aWFscyI6W3siaWQiOiJ0VnAxUWZZaFQ4RGt5RUhWcnY3Y
  mxucEFvMllKemJaZ1pOQmY3elBzNkNJIiwidHlwZSI6InB1YmxpYy1rZXkifV0sInB1YktleUNyZWRQYXJhbXMiOlt7ImFsZyI6L
  TcsInR5cGUiOiJwdWJsaWMta2V5In1dLCJycCI6eyJpZCI6ImxvY2FsaG9zdCIsIm5hbWUiOiJaSVRBREVMIn0sInRpbWVvdXQiO
  jMwMDAwMCwidXNlciI6eyJkaXNwbGF5TmFtZSI6IlRpbSBNb2hsbWFubiIsImlkIjoiTWpFMU5UazRNREF3TkRZME9UazRPVFF3I
  iwibmFtZSI6InRpbSJ9fX1SInB1YmxpY0tleUNyZWRlbnRpYWxDcmVhdGlvbk9wdGlvbnMiowoKIFZlcmlmeVBhc3NrZXlSZWdpc
  3RyYXRpb25SZXF1ZXN0ElAKB3VzZXJfaWQYASABKAlCN+I/CBIGdXNlcklkkkEcShQiMTYzODQwNzc2ODM1NDMyNzA1InjIAYABA
  eBBAvpCB3IFEAEYyAFSBnVzZXJJZBJZCgpwYXNza2V5X2lkGAIgASgJQjriPwsSCXBhc3NrZXlJZJJBHEoUIjE2Mzg0MDc3NjgzN
  TQzMjcwNSJ4yAGAAQHgQQL6QgdyBRABGMgBUglwYXNza2V5SWQS+gcKFXB1YmxpY19rZXlfY3JlZGVudGlhbBgDIAEoCzIXLmdvb
  2dsZS5wcm90b2J1Zi5TdHJ1Y3RCrAfiPxUSE3B1YmxpY0tleUNyZWRlbnRpYWySQYUHMrEBUHVibGljS2V5Q3JlZGVudGlhbCBJb
  nRlcmZhY2UuIEdlbmVyYXRlZCBoZWxwZXIgbWV0aG9kcyBwb3B1bGF0ZSB0aGUgZmllbGQgZnJvbSBKU09OIGNyZWF0ZWQgYnkgY
  SBXZWJhdXRoTiBjbGllbnQuIFNlZSBhbHNvOiAgaHR0cHM6Ly93d3cudzMub3JnL1RSL3dlYmF1dGhuLyNwdWJsaWNrZXljcmVkZ
  W50aWFsSscFeyJ0eXBlIjoicHVibGljLWtleSIsImlkIjoicGF3VmFyRjR4UHhMRm1mQ25Sa3dYV2VUcktHemFiY0FpOTJMRUkxV
  0MwMCIsInJhd0lkIjoicGF3VmFyRjR4UHhMRm1mQ25Sa3dYV2VUcktHemFiY0FpOTJMRUkxV0MwMCIsInJlc3BvbnNlIjp7ImF0d
  GVzdGF0aW9uT2JqZWN0IjoibzJObWJYUm1jR0ZqYTJWa1oyRjBkRk4wYlhTaVkyRnNaeVpqYzJsbldFY3dSUUlnUktTM1ZwZUU5d
  GZFeFhSemtvVUtuRzRyUVdQdnRTU3Q0WXRER2dUeDMyb0NJUURQZXktMllKNHVJZy1RQ000amo2YUUyVTN0Z01GTV9SUDdFZng2e
  FJ1M0pHaGhkWFJvUkdGMFlWaWtTWllONVlnT2pHaDBOQmNQWkhaZ1c0X2tycm1paGpMSG1Wenp1b01kbDJORkFBQUFBRGp1NzYwO
  DVZaG1sdDFDRU9Ia3dMUUFJS1dzRldxeGVNVDhTeFpud3AwWk1GMW5rNnloczJtM0FJdmRpeENOVmd0TnBRRUNBeVlnQVNGWUlNR
  1VEU1AyRkFRbjJNSWZQTXk3Y3lCX1kzMFZxaXhWZ0dVTFRCdEZqZlJpSWxnZ2pVR2ZRbzNfLUNyTW1IM1MtWlFrRktXS25OQlFFQ
  U1rRnRHLTlBNHpxVzAiLCJjbGllbnREYXRhSlNPTiI6ImV5SjBlWEJsSWpvaWQyVmlZWFYwYUc0dVkzSmxZWFJsSWl3aVkyaGhiR
  3hsYm1kbElqb2lRbGhYZEhoMFdHeEplRlpaYTBwSFQxZFZhVVZtTTI1emJ5MDJhWFpLZFd3MlltTm1XSGRNVmxGSWF5SXNJbTl5Y
  VdkcGJpSTZJbWgwZEhCek9pOHZiRzlqWVd4b2IzTjBPamd3T0RBaWZRIn19eICAQIABN+BBAvpCBYoBAhABUhNwdWJsaWNLZXlDc
  mVkZW50aWFsElUKDHBhc3NrZXlfbmFtZRgEIAEoCUIy4j8NEgtwYXNza2V5TmFtZZJBEkoKImZpZG8ga2V5InjIAYABAeBBAvpCB
  3IFEAEYyAFSC3Bhc3NrZXlOYW1lImwKIVZlcmlmeVBhc3NrZXlSZWdpc3RyYXRpb25SZXNwb25zZRJHCgdkZXRhaWxzGAEgASgLM
  h8ueml0YWRlbC5vYmplY3QudjJhbHBoYS5EZXRhaWxzQgziPwkSB2RldGFpbHNSB2RldGFpbHMivAEKElJlZ2lzdGVyVTJGUmVxd
  WVzdBJQCgd1c2VyX2lkGAEgASgJQjfiPwgSBnVzZXJJZJJBHEoUIjE2Mzg0MDc3NjgzNTQzMjcwNSJ4yAGAAQHgQQL6QgdyBRABG
  MgBUgZ1c2VySWQSVAoGZG9tYWluGAIgASgJQjziPwgSBmRvbWFpbpJBLjIsIkRvbWFpbiBvbiB3aGljaCB0aGUgdXNlciBpcyBhd
  XRoZW50aWNhdGVkLiJSBmRvbWFpbiLuBwoTUmVnaXN0ZXJVMkZSZXNwb25zZRJHCgdkZXRhaWxzGAEgASgLMh8ueml0YWRlbC5vY
  mplY3QudjJhbHBoYS5EZXRhaWxzQgziPwkSB2RldGFpbHNSB2RldGFpbHMSOgoGdTJmX2lkGAIgASgJQiPiPwcSBXUyRklkkkEWS
  hQiMTYzODQwNzc2ODM1NDMyNzA1IlIFdTJmSWQS0QYKJnB1YmxpY19rZXlfY3JlZGVudGlhbF9jcmVhdGlvbl9vcHRpb25zGAMgA
  SgLMhcuZ29vZ2xlLnByb3RvYnVmLlN0cnVjdELjBeI/JBIicHVibGljS2V5Q3JlZGVudGlhbENyZWF0aW9uT3B0aW9uc5JBuAUy+
  gFPcHRpb25zIGZvciBDcmVkZW50aWFsIENyZWF0aW9uIChkaWN0aW9uYXJ5IFB1YmxpY0tleUNyZWRlbnRpYWxDcmVhdGlvbk9wd
  GlvbnMpLiBHZW5lcmF0ZWQgaGVscGVyIG1ldGhvZHMgdHJhbnNmb3JtIHRoZSBmaWVsZCB0byBKU09OLCBmb3IgdXNlIGluIGEgV
  2ViYXV0aE4gY2xpZW50LiBTZWUgYWxzbzogIGh0dHBzOi8vd3d3LnczLm9yZy9UUi93ZWJhdXRobi8jZGljdGRlZi1wdWJsaWNrZ
  XljcmVkZW50aWFsY3JlYXRpb25vcHRpb25zSrgDeyJwdWJsaWNLZXkiOnsiYXR0ZXN0YXRpb24iOiJub25lIiwiYXV0aGVudGljY
  XRvclNlbGVjdGlvbiI6eyJ1c2VyVmVyaWZpY2F0aW9uIjoicmVxdWlyZWQifSwiY2hhbGxlbmdlIjoiWGFNWXdXT1o1aGo2cHd0d
  0pKbHBjSS1FeGtPNVR4ZXZCTUc0UjhEb0tRUSIsImV4Y2x1ZGVDcmVkZW50aWFscyI6W3siaWQiOiJ0VnAxUWZZaFQ4RGt5RUhWc
  nY3YmxucEFvMllKemJaZ1pOQmY3elBzNkNJIiwidHlwZSI6InB1YmxpYy1rZXkifV0sInB1YktleUNyZWRQYXJhbXMiOlt7ImFsZ
  yI6LTcsInR5cGUiOiJwdWJsaWMta2V5In1dLCJycCI6eyJpZCI6ImxvY2FsaG9zdCIsIm5hbWUiOiJaSVRBREVMIn0sInRpbWVvd
  XQiOjMwMDAwMCwidXNlciI6eyJkaXNwbGF5TmFtZSI6IlRpbSBNb2hsbWFubiIsImlkIjoiTWpFMU5UazRNREF3TkRZME9UazRPV
  FF3IiwibmFtZSI6InRpbSJ9fX1SInB1YmxpY0tleUNyZWRlbnRpYWxDcmVhdGlvbk9wdGlvbnMijQoKHFZlcmlmeVUyRlJlZ2lzd
  HJhdGlvblJlcXVlc3QSUAoHdXNlcl9pZBgBIAEoCUI34j8IEgZ1c2VySWSSQRxKFCIxNjM4NDA3NzY4MzU0MzI3MDUieMgBgAEB4
  EEC+kIHcgUQARjIAVIGdXNlcklkEk0KBnUyZl9pZBgCIAEoCUI24j8HEgV1MkZJZJJBHEoUIjE2Mzg0MDc3NjgzNTQzMjcwNSJ4y
  AGAAQHgQQL6QgdyBRABGMgBUgV1MmZJZBL6BwoVcHVibGljX2tleV9jcmVkZW50aWFsGAMgASgLMhcuZ29vZ2xlLnByb3RvYnVmL
  lN0cnVjdEKsB+I/FRITcHVibGljS2V5Q3JlZGVudGlhbJJBhQcysQFQdWJsaWNLZXlDcmVkZW50aWFsIEludGVyZmFjZS4gR2VuZ
  XJhdGVkIGhlbHBlciBtZXRob2RzIHBvcHVsYXRlIHRoZSBmaWVsZCBmcm9tIEpTT04gY3JlYXRlZCBieSBhIFdlYmF1dGhOIGNsa
  WVudC4gU2VlIGFsc286ICBodHRwczovL3d3dy53My5vcmcvVFIvd2ViYXV0aG4vI3B1YmxpY2tleWNyZWRlbnRpYWxKxwV7InR5c
  GUiOiJwdWJsaWMta2V5IiwiaWQiOiJwYXdWYXJGNHhQeExGbWZDblJrd1hXZVRyS0d6YWJjQWk5MkxFSTFXQzAwIiwicmF3SWQiO
  iJwYXdWYXJGNHhQeExGbWZDblJrd1hXZVRyS0d6YWJjQWk5MkxFSTFXQzAwIiwicmVzcG9uc2UiOnsiYXR0ZXN0YXRpb25PYmplY
  3QiOiJvMk5tYlhSbWNHRmphMlZrWjJGMGRGTjBiWFNpWTJGc1p5WmpjMmxuV0Vjd1JRSWdSS1MzVnBlRTl0ZkV4WFJ6a29VS25HN
  HJRV1B2dFNTdDRZdERHZ1R4MzJvQ0lRRFBleS0yWUo0dUlnLVFDTTRqajZhRTJVM3RnTUZNX1JQN0VmeDZ4UnUzSkdoaGRYUm9SR
  0YwWVZpa1NaWU41WWdPakdoME5CY1BaSFpnVzRfa3JybWloakxIbVZ6enVvTWRsMk5GQUFBQUFEanU3NjA4NVlobWx0MUNFT0hrd
  0xRQUlLV3NGV3F4ZU1UOFN4Wm53cDBaTUYxbms2eWhzMm0zQUl2ZGl4Q05WZ3ROcFFFQ0F5WWdBU0ZZSU1HVURTUDJGQVFuMk1JZ
  lBNeTdjeUJfWTMwVnFpeFZnR1VMVEJ0RmpmUmlJbGdnalVHZlFvM18tQ3JNbUgzUy1aUWtGS1dLbk5CUUVBTWtGdEctOUE0enFXM
  CIsImNsaWVudERhdGFKU09OIjoiZXlKMGVYQmxJam9pZDJWaVlYVjBhRzR1WTNKbFlYUmxJaXdpWTJoaGJHeGxibWRsSWpvaVFsa
  FhkSGgwV0d4SmVGWlphMHBIVDFkVmFVVm1NMjV6YnkwMmFYWktkV3cyWW1ObVdIZE1WbEZJYXlJc0ltOXlhV2RwYmlJNkltaDBkS
  EJ6T2k4dmJHOWpZV3hvYjNOME9qZ3dPREFpZlEifX14gIBAgAE34EEC+kIFigECEAFSE3B1YmxpY0tleUNyZWRlbnRpYWwSTwoKd
  G9rZW5fbmFtZRgEIAEoCUIw4j8LEgl0b2tlbk5hbWWSQRJKCiJmaWRvIGtleSJ4yAGAAQHgQQL6QgdyBRABGMgBUgl0b2tlbk5hb
  WUiaAodVmVyaWZ5VTJGUmVnaXN0cmF0aW9uUmVzcG9uc2USRwoHZGV0YWlscxgBIAEoCzIfLnppdGFkZWwub2JqZWN0LnYyYWxwa
  GEuRGV0YWlsc0IM4j8JEgdkZXRhaWxzUgdkZXRhaWxzImcKE1JlZ2lzdGVyVE9UUFJlcXVlc3QSUAoHdXNlcl9pZBgBIAEoCUI34
  j8IEgZ1c2VySWSSQRxKFCIxNjM4NDA3NzY4MzU0MzI3MDUieMgBgAEB4EEC+kIHcgUQARjIAVIGdXNlcklkItkCChRSZWdpc3Rlc
  lRPVFBSZXNwb25zZRJHCgdkZXRhaWxzGAEgASgLMh8ueml0YWRlbC5vYmplY3QudjJhbHBoYS5EZXRhaWxzQgziPwkSB2RldGFpb
  HNSB2RldGFpbHMSqwEKA3VyaRgCIAEoCUKYAeI/BRIDdXJpkkGMAUqJASJvdHBhdXRoOi8vdG90cC9aSVRBREVMOmdpZ2lAYWNtZ
  S56aXRhZGVsLmNsb3VkP2FsZ29yaXRobT1TSEExJmRpZ2l0cz02Jmlzc3Vlcj1aSVRBREVMJnBlcmlvZD0zMCZzZWNyZXQ9VEpPU
  FdTRFlJTExIWEZWNE1MS05OSk9XRkc3VlNEQ0siUgN1cmkSSgoGc2VjcmV0GAMgASgJQjLiPwgSBnNlY3JldJJBJEoiIlRKT1BXU
  0RZSUxMSFhGVjRNTEtOTkpPV0ZHN1ZTRENLIlIGc2VjcmV0ItABCh1WZXJpZnlUT1RQUmVnaXN0cmF0aW9uUmVxdWVzdBJQCgd1c
  2VyX2lkGAEgASgJQjfiPwgSBnVzZXJJZJJBHEoUIjE2Mzg0MDc3NjgzNTQzMjcwNSJ4yAGAAQHgQQL6QgdyBRABGMgBUgZ1c2VyS
  WQSXQoEY29kZRgCIAEoCUJJ4j8GEgRjb2RlkkEwMiRDb2RlIGdlbmVyYXRlZCBieSBUT1RQIGFwcCBvciBkZXZpY2VKCCIxMjM0N
  TYi4EEC+kIHcgUQARjIAVIEY29kZSJpCh5WZXJpZnlUT1RQUmVnaXN0cmF0aW9uUmVzcG9uc2USRwoHZGV0YWlscxgBIAEoCzIfL
  nppdGFkZWwub2JqZWN0LnYyYWxwaGEuRGV0YWlsc0IM4j8JEgdkZXRhaWxzUgdkZXRhaWxzIswCCiRDcmVhdGVQYXNza2V5UmVna
  XN0cmF0aW9uTGlua1JlcXVlc3QSUAoHdXNlcl9pZBgBIAEoCUI34j8IEgZ1c2VySWSSQRxKFCIxNjM4NDA3NzY4MzU0MzI3MDUie
  MgBgAEB4EEC+kIHcgUQARjIAVIGdXNlcklkEl8KCXNlbmRfbGluaxgCIAEoCzIxLnppdGFkZWwudXNlci52MmFscGhhLlNlbmRQY
  XNza2V5UmVnaXN0cmF0aW9uTGlua0IN4j8KEghzZW5kTGlua0gAUghzZW5kTGluaxJnCgtyZXR1cm5fY29kZRgDIAEoCzIzLnppd
  GFkZWwudXNlci52MmFscGhhLlJldHVyblBhc3NrZXlSZWdpc3RyYXRpb25Db2RlQg/iPwwSCnJldHVybkNvZGVIAFIKcmV0dXJuQ
  29kZUIICgZtZWRpdW0ivgIKJUNyZWF0ZVBhc3NrZXlSZWdpc3RyYXRpb25MaW5rUmVzcG9uc2USRwoHZGV0YWlscxgBIAEoCzIfL
  nppdGFkZWwub2JqZWN0LnYyYWxwaGEuRGV0YWlsc0IM4j8JEgdkZXRhaWxzUgdkZXRhaWxzEsIBCgRjb2RlGAIgASgLMi0ueml0Y
  WRlbC51c2VyLnYyYWxwaGEuUGFzc2tleVJlZ2lzdHJhdGlvbkNvZGVCeuI/BhIEY29kZZJBbjJsIm9uZSB0aW1lIGNvZGUgZ2VuZ
  XJhdGVkIGJ5IFpJVEFERUw7IHJlcXVpcmVkIHRvIHN0YXJ0IHRoZSBwYXNza2V5IHJlZ2lzdHJhdGlvbiB3aXRob3V0IHVzZXIgY
  XV0aGVudGljYXRpb24iSABSBGNvZGWIAQFCBwoFX2NvZGUi9AMKIFN0YXJ0SWRlbnRpdHlQcm92aWRlckZsb3dSZXF1ZXN0Em0KB
  mlkcF9pZBgBIAEoCUJW4j8HEgVpZHBJZJJBPzIhSUQgZm9yIGV4aXN0aW5nIGlkZW50aXR5IHByb3ZpZGVyShQiMTYzODQwNzc2O
  DM1NDMyNzA1InjIAYABAfpCB3IFEAEYyAFSBWlkcElkErIBCgtzdWNjZXNzX3VybBgCIAEoCUKQAeI/DBIKc3VjY2Vzc1VybJJBc
  TJBVVJMIG9uIHdoaWNoIHRoZSB1c2VyIHdpbGwgYmUgcmVkaXJlY3RlZCBhZnRlciBhIHN1Y2Nlc3NmdWwgbG9naW5KJiJodHRwc
  zovL2N1c3RvbS5jb20vbG9naW4vaWRwL3N1Y2Nlc3MieMgBgAEB+kIKcggQARjIAZABAVIKc3VjY2Vzc1VybBKrAQoLZmFpbHVyZ
  V91cmwYAyABKAlCiQHiPwwSCmZhaWx1cmVVcmySQWoyPVVSTCBvbiB3aGljaCB0aGUgdXNlciB3aWxsIGJlIHJlZGlyZWN0ZWQgY
  WZ0ZXIgYSBmYWlsZWQgbG9naW5KIyJodHRwczovL2N1c3RvbS5jb20vbG9naW4vaWRwL2ZhaWwieMgBgAEB+kIKcggQARjIAZABA
  VIKZmFpbHVyZVVybCLNAgohU3RhcnRJZGVudGl0eVByb3ZpZGVyRmxvd1Jlc3BvbnNlEkcKB2RldGFpbHMYASABKAsyHy56aXRhZ
  GVsLm9iamVjdC52MmFscGhhLkRldGFpbHNCDOI/CRIHZGV0YWlsc1IHZGV0YWlscxLRAQoIYXV0aF91cmwYAiABKAlCswHiPwkSB
  2F1dGhVcmySQaMBMidVUkwgdG8gd2hpY2ggdGhlIGNsaWVudCBzaG91bGQgcmVkaXJlY3RKeCJodHRwczovL2FjY291bnRzLmdvb
  2dsZS5jb20vby9vYXV0aDIvdjIvYXV0aD9jbGllbnRfaWQ9Y2xpZW50SUQmY2FsbGJhY2s9aHR0cHMlM0ElMkYlMkZ6aXRhZGVsL
  mNsb3VkJTJGaWRwcyUyRmNhbGxiYWNrIkgAUgdhdXRoVXJsQgsKCW5leHRfc3RlcCKKAwoqUmV0cmlldmVJZGVudGl0eVByb3ZpZ
  GVySW5mb3JtYXRpb25SZXF1ZXN0EqcBCglpbnRlbnRfaWQYASABKAlCiQHiPwoSCGludGVudElkkkFvMlFJRCBvZiB0aGUgaW50Z
  W50LCBwcmV2aW91c2x5IHJldHVybmVkIG9uIHRoZSBzdWNjZXNzIHJlc3BvbnNlIG9mIHRoZSBJRFAgY2FsbGJhY2tKFCIxNjM4N
  DA3NzY4MzU0MzI3MDUieMgBgAEB+kIHcgUQARjIAVIIaW50ZW50SWQSsQEKBXRva2VuGAIgASgJQpoB4j8HEgV0b2tlbpJBggEyV
  HRva2VuIG9mIHRoZSBpbnRlbnQsIHByZXZpb3VzbHkgcmV0dXJuZWQgb24gdGhlIHN1Y2Nlc3MgcmVzcG9uc2Ugb2YgdGhlIElEU
  CBjYWxsYmFja0okIlNKS0wzaW9JRHBvMzQyaW9xdzk4ZmpwM3NkZjMyd2FoYj0ieMgBgAEB+kIHcgUQARjIAVIFdG9rZW4i2gEKK
  1JldHJpZXZlSWRlbnRpdHlQcm92aWRlckluZm9ybWF0aW9uUmVzcG9uc2USRwoHZGV0YWlscxgBIAEoCzIfLnppdGFkZWwub2JqZ
  WN0LnYyYWxwaGEuRGV0YWlsc0IM4j8JEgdkZXRhaWxzUgdkZXRhaWxzEmIKD2lkcF9pbmZvcm1hdGlvbhgCIAEoCzIkLnppdGFkZ
  WwudXNlci52MmFscGhhLklEUEluZm9ybWF0aW9uQhPiPxASDmlkcEluZm9ybWF0aW9uUg5pZHBJbmZvcm1hdGlvbiKsAQoRQWRkS
  URQTGlua1JlcXVlc3QSTwoHdXNlcl9pZBgBIAEoCUI24j8IEgZ1c2VySWSSQRtKEyI2OTYyOTAyNjgwNjQ4OTQ1NSJ4yAGAAQHgQ
  QL6QgdyBRABGMgBUgZ1c2VySWQSRgoIaWRwX2xpbmsYAiABKAsyHS56aXRhZGVsLnVzZXIudjJhbHBoYS5JRFBMaW5rQgziPwkSB
  2lkcExpbmtSB2lkcExpbmsiXQoSQWRkSURQTGlua1Jlc3BvbnNlEkcKB2RldGFpbHMYASABKAsyHy56aXRhZGVsLm9iamVjdC52M
  mFscGhhLkRldGFpbHNCDOI/CRIHZGV0YWlsc1IHZGV0YWlscyKvAgoUUGFzc3dvcmRSZXNldFJlcXVlc3QSTwoHdXNlcl9pZBgBI
  AEoCUI24j8IEgZ1c2VySWSSQRtKEyI2OTYyOTAyNjgwNjQ4OTQ1NSJ4yAGAAQHgQQL6QgdyBRABGMgBUgZ1c2VySWQSWQoJc2VuZ
  F9saW5rGAIgASgLMisueml0YWRlbC51c2VyLnYyYWxwaGEuU2VuZFBhc3N3b3JkUmVzZXRMaW5rQg3iPwoSCHNlbmRMaW5rSABSC
  HNlbmRMaW5rEmEKC3JldHVybl9jb2RlGAMgASgLMi0ueml0YWRlbC51c2VyLnYyYWxwaGEuUmV0dXJuUGFzc3dvcmRSZXNldENvZ
  GVCD+I/DBIKcmV0dXJuQ29kZUgAUgpyZXR1cm5Db2RlQggKBm1lZGl1bSK/AQoVUGFzc3dvcmRSZXNldFJlc3BvbnNlEkcKB2Rld
  GFpbHMYASABKAsyHy56aXRhZGVsLm9iamVjdC52MmFscGhhLkRldGFpbHNCDOI/CRIHZGV0YWlsc1IHZGV0YWlscxJHChF2ZXJpZ
  mljYXRpb25fY29kZRgCIAEoCUIV4j8SEhB2ZXJpZmljYXRpb25Db2RlSABSEHZlcmlmaWNhdGlvbkNvZGWIAQFCFAoSX3Zlcmlma
  WNhdGlvbl9jb2RlIt4DChJTZXRQYXNzd29yZFJlcXVlc3QSTwoHdXNlcl9pZBgBIAEoCUI24j8IEgZ1c2VySWSSQRtKEyI2OTYyO
  TAyNjgwNjQ4OTQ1NSJ4yAGAAQHgQQL6QgdyBRABGMgBUgZ1c2VySWQSUwoMbmV3X3Bhc3N3b3JkGAIgASgLMh4ueml0YWRlbC51c
  2VyLnYyYWxwaGEuUGFzc3dvcmRCEOI/DRILbmV3UGFzc3dvcmRSC25ld1Bhc3N3b3JkEmoKEGN1cnJlbnRfcGFzc3dvcmQYAyABK
  AlCPeI/ERIPY3VycmVudFBhc3N3b3JkkkEZShEiU2VjcjN0UDRzc3cwcmQhInjIAYABAeBBAvpCB3IFEAEYyAFIAFIPY3VycmVud
  FBhc3N3b3JkEqUBChF2ZXJpZmljYXRpb25fY29kZRgEIAEoCUJ24j8SEhB2ZXJpZmljYXRpb25Db2RlkkFSMj8idGhlIHZlcmlma
  WNhdGlvbiBjb2RlIGdlbmVyYXRlZCBkdXJpbmcgcGFzc3dvcmQgcmVzZXQgcmVxdWVzdCJKCiJTS0pkMzQyayJ4FIABAeBBAvpCB
  nIEEAEYFEgAUhB2ZXJpZmljYXRpb25Db2RlQg4KDHZlcmlmaWNhdGlvbiJeChNTZXRQYXNzd29yZFJlc3BvbnNlEkcKB2RldGFpb
  HMYASABKAsyHy56aXRhZGVsLm9iamVjdC52MmFscGhhLkRldGFpbHNCDOI/CRIHZGV0YWlsc1IHZGV0YWlscyJ3CiRMaXN0QXV0a
  GVudGljYXRpb25NZXRob2RUeXBlc1JlcXVlc3QSTwoHdXNlcl9pZBgBIAEoCUI24j8IEgZ1c2VySWSSQRtKEyI2OTYyOTAyNjgwN
  jQ4OTQ1NSJ4yAGAAQHgQQL6QgdyBRABGMgBUgZ1c2VySWQi5gEKJUxpc3RBdXRoZW50aWNhdGlvbk1ldGhvZFR5cGVzUmVzcG9uc
  2USSwoHZGV0YWlscxgBIAEoCzIjLnppdGFkZWwub2JqZWN0LnYyYWxwaGEuTGlzdERldGFpbHNCDOI/CRIHZGV0YWlsc1IHZGV0Y
  WlscxJwChFhdXRoX21ldGhvZF90eXBlcxgCIAMoDjIuLnppdGFkZWwudXNlci52MmFscGhhLkF1dGhlbnRpY2F0aW9uTWV0aG9kV
  HlwZUIU4j8REg9hdXRoTWV0aG9kVHlwZXNSD2F1dGhNZXRob2RUeXBlcyr0AwoYQXV0aGVudGljYXRpb25NZXRob2RUeXBlElcKJ
  kFVVEhFTlRJQ0FUSU9OX01FVEhPRF9UWVBFX1VOU1BFQ0lGSUVEEAAaK+I/KBImQVVUSEVOVElDQVRJT05fTUVUSE9EX1RZUEVfV
  U5TUEVDSUZJRUQSUQojQVVUSEVOVElDQVRJT05fTUVUSE9EX1RZUEVfUEFTU1dPUkQQARoo4j8lEiNBVVRIRU5USUNBVElPTl9NR
  VRIT0RfVFlQRV9QQVNTV09SRBJPCiJBVVRIRU5USUNBVElPTl9NRVRIT0RfVFlQRV9QQVNTS0VZEAIaJ+I/JBIiQVVUSEVOVElDQ
  VRJT05fTUVUSE9EX1RZUEVfUEFTU0tFWRJHCh5BVVRIRU5USUNBVElPTl9NRVRIT0RfVFlQRV9JRFAQAxoj4j8gEh5BVVRIRU5US
  UNBVElPTl9NRVRIT0RfVFlQRV9JRFASSQofQVVUSEVOVElDQVRJT05fTUVUSE9EX1RZUEVfVE9UUBAEGiTiPyESH0FVVEhFTlRJQ
  0FUSU9OX01FVEhPRF9UWVBFX1RPVFASRwoeQVVUSEVOVElDQVRJT05fTUVUSE9EX1RZUEVfVTJGEAUaI+I/IBIeQVVUSEVOVElDQ
  VRJT05fTUVUSE9EX1RZUEVfVTJGMpksCgtVc2VyU2VydmljZRKoAwoMQWRkSHVtYW5Vc2VyEikueml0YWRlbC51c2VyLnYyYWxwa
  GEuQWRkSHVtYW5Vc2VyUmVxdWVzdBoqLnppdGFkZWwudXNlci52MmFscGhhLkFkZEh1bWFuVXNlclJlc3BvbnNlIsACkkH4ARIVQ
  3JlYXRlIGEgdXNlciAoSHVtYW4pGtEBQ3JlYXRlL2ltcG9ydCBhIG5ldyB1c2VyIHdpdGggdGhlIHR5cGUgaHVtYW4uIFRoZSBuZ
  XdseSBjcmVhdGVkIHVzZXIgd2lsbCBnZXQgYSB2ZXJpZmljYXRpb24gZW1haWwgaWYgZWl0aGVyIHRoZSBlbWFpbCBhZGRyZXNzI
  GlzIG5vdCBtYXJrZWQgYXMgdmVyaWZpZWQgYW5kIHlvdSBkaWQgbm90IHJlcXVlc3QgdGhlIHZlcmlmaWNhdGlvbiB0byBiZSByZ
  XR1cm5lZC5KCwoDMjAwEgQKAk9LirUYIQoaCgp1c2VyLndyaXRlGgxvcmdhbmlzYXRpb24SAwjJAYLT5JMCGToBKiIUL3YyYWxwa
  GEvdXNlcnMvaHVtYW4S7wIKCFNldEVtYWlsEiUueml0YWRlbC51c2VyLnYyYWxwaGEuU2V0RW1haWxSZXF1ZXN0GiYueml0YWRlb
  C51c2VyLnYyYWxwaGEuU2V0RW1haWxSZXNwb25zZSKTApJB0QESFUNoYW5nZSB0aGUgdXNlciBlbWFpbBqqAUNoYW5nZSB0aGUgZ
  W1haWwgYWRkcmVzcyBvZiBhIHVzZXIuIElmIHRoZSBzdGF0ZSBpcyBzZXQgdG8gbm90IHZlcmlmaWVkLCBhIHZlcmlmaWNhdGlvb
  iBjb2RlIHdpbGwgYmUgZ2VuZXJhdGVkLCB3aGljaCBjYW4gYmUgZWl0aGVyIHJldHVybmVkIG9yIHNlbnQgdG8gdGhlIHVzZXIgY
  nkgZW1haWwuSgsKAzIwMBIECgJPS4q1GBEKDwoNYXV0aGVudGljYXRlZILT5JMCIzoBKiIeL3YyYWxwaGEvdXNlcnMve3VzZXJfa
  WR9L2VtYWlsEvgBCgtWZXJpZnlFbWFpbBIoLnppdGFkZWwudXNlci52MmFscGhhLlZlcmlmeUVtYWlsUmVxdWVzdBopLnppdGFkZ
  WwudXNlci52MmFscGhhLlZlcmlmeUVtYWlsUmVzcG9uc2UikwGSQUoSEFZlcmlmeSB0aGUgZW1haWwaKVZlcmlmeSB0aGUgZW1ha
  Wwgd2l0aCB0aGUgZ2VuZXJhdGVkIGNvZGUuSgsKAzIwMBIECgJPS4q1GBEKDwoNYXV0aGVudGljYXRlZILT5JMCKzoBKiImL3YyY
  WxwaGEvdXNlcnMve3VzZXJfaWR9L2VtYWlsL192ZXJpZnkSkAMKD1JlZ2lzdGVyUGFzc2tleRIsLnppdGFkZWwudXNlci52MmFsc
  GhhLlJlZ2lzdGVyUGFzc2tleVJlcXVlc3QaLS56aXRhZGVsLnVzZXIudjJhbHBoYS5SZWdpc3RlclBhc3NrZXlSZXNwb25zZSKfA
  pJB2gESLFN0YXJ0IHRoZSByZWdpc3RyYXRpb24gb2YgcGFzc2tleSBmb3IgYSB1c2VyGpwBU3RhcnQgdGhlIHJlZ2lzdHJhdGlvb
  iBvZiBhIHBhc3NrZXkgZm9yIGEgdXNlciwgYXMgYSByZXNwb25zZSB0aGUgcHVibGljIGtleSBjcmVkZW50aWFsIGNyZWF0aW9uI
  G9wdGlvbnMgYXJlIHJldHVybmVkLCB3aGljaCBhcmUgdXNlZCB0byB2ZXJpZnkgdGhlIHBhc3NrZXkuSgsKAzIwMBIECgJPS4q1G
  BEKDwoNYXV0aGVudGljYXRlZILT5JMCJjoBKiIhL3YyYWxwaGEvdXNlcnMve3VzZXJfaWR9L3Bhc3NrZXlzEssCChlWZXJpZnlQY
  XNza2V5UmVnaXN0cmF0aW9uEjYueml0YWRlbC51c2VyLnYyYWxwaGEuVmVyaWZ5UGFzc2tleVJlZ2lzdHJhdGlvblJlcXVlc3QaN
  y56aXRhZGVsLnVzZXIudjJhbHBoYS5WZXJpZnlQYXNza2V5UmVnaXN0cmF0aW9uUmVzcG9uc2UivAGSQWsSG1ZlcmlmeSBhIHBhc
  3NrZXkgZm9yIGEgdXNlcho/VmVyaWZ5IHRoZSBwYXNza2V5IHJlZ2lzdHJhdGlvbiB3aXRoIHRoZSBwdWJsaWMga2V5IGNyZWRlb
  nRpYWwuSgsKAzIwMBIECgJPS4q1GBEKDwoNYXV0aGVudGljYXRlZILT5JMCMzoBKiIuL3YyYWxwaGEvdXNlcnMve3VzZXJfaWR9L
  3Bhc3NrZXlzL3twYXNza2V5X2lkfRKaAwodQ3JlYXRlUGFzc2tleVJlZ2lzdHJhdGlvbkxpbmsSOi56aXRhZGVsLnVzZXIudjJhb
  HBoYS5DcmVhdGVQYXNza2V5UmVnaXN0cmF0aW9uTGlua1JlcXVlc3QaOy56aXRhZGVsLnVzZXIudjJhbHBoYS5DcmVhdGVQYXNza
  2V5UmVnaXN0cmF0aW9uTGlua1Jlc3BvbnNlIv8BkkGjARItQ3JlYXRlIGEgcGFzc2tleSByZWdpc3RyYXRpb24gbGluayBmb3IgY
  SB1c2VyGmVDcmVhdGUgYSBwYXNza2V5IHJlZ2lzdHJhdGlvbiBsaW5rIHdoaWNoIGluY2x1ZGVzIGEgY29kZSBhbmQgZWl0aGVyI
  HJldHVybiBpdCBvciBzZW5kIGl0IHRvIHRoZSB1c2VyLkoLCgMyMDASBAoCT0uKtRgWChQKEnVzZXIucGFzc2tleS53cml0ZYLT5
  JMCODoBKiIzL3YyYWxwaGEvdXNlcnMve3VzZXJfaWR9L3Bhc3NrZXlzL3JlZ2lzdHJhdGlvbl9saW5rEocDCgtSZWdpc3RlclUyR
  hIoLnppdGFkZWwudXNlci52MmFscGhhLlJlZ2lzdGVyVTJGUmVxdWVzdBopLnppdGFkZWwudXNlci52MmFscGhhLlJlZ2lzdGVyV
  TJGUmVzcG9uc2UiogKSQeIBEjBTdGFydCB0aGUgcmVnaXN0cmF0aW9uIG9mIGEgdTJmIHRva2VuIGZvciBhIHVzZXIaoAFTdGFyd
  CB0aGUgcmVnaXN0cmF0aW9uIG9mIGEgdTJmIHRva2VuIGZvciBhIHVzZXIsIGFzIGEgcmVzcG9uc2UgdGhlIHB1YmxpYyBrZXkgY
  3JlZGVudGlhbCBjcmVhdGlvbiBvcHRpb25zIGFyZSByZXR1cm5lZCwgd2hpY2ggYXJlIHVzZWQgdG8gdmVyaWZ5IHRoZSB1MmYgd
  G9rZW4uSgsKAzIwMBIECgJPS4q1GBEKDwoNYXV0aGVudGljYXRlZILT5JMCIToBKiIcL3YyYWxwaGEvdXNlcnMve3VzZXJfaWR9L
  3UyZhK6AgoVVmVyaWZ5VTJGUmVnaXN0cmF0aW9uEjIueml0YWRlbC51c2VyLnYyYWxwaGEuVmVyaWZ5VTJGUmVnaXN0cmF0aW9uU
  mVxdWVzdBozLnppdGFkZWwudXNlci52MmFscGhhLlZlcmlmeVUyRlJlZ2lzdHJhdGlvblJlc3BvbnNlIrcBkkFvEh1WZXJpZnkgY
  SB1MmYgdG9rZW4gZm9yIGEgdXNlchpBVmVyaWZ5IHRoZSB1MmYgdG9rZW4gcmVnaXN0cmF0aW9uIHdpdGggdGhlIHB1YmxpYyBrZ
  XkgY3JlZGVudGlhbC5KCwoDMjAwEgQKAk9LirUYEQoPCg1hdXRoZW50aWNhdGVkgtPkkwIqOgEqIiUvdjJhbHBoYS91c2Vycy97d
  XNlcl9pZH0vdTJmL3t1MmZfaWR9EvkCCgxSZWdpc3RlclRPVFASKS56aXRhZGVsLnVzZXIudjJhbHBoYS5SZWdpc3RlclRPVFBSZ
  XF1ZXN0Gioueml0YWRlbC51c2VyLnYyYWxwaGEuUmVnaXN0ZXJUT1RQUmVzcG9uc2UikQKSQdABEjVTdGFydCB0aGUgcmVnaXN0c
  mF0aW9uIG9mIGEgVE9UUCBnZW5lcmF0b3IgZm9yIGEgdXNlchqJAVN0YXJ0IHRoZSByZWdpc3RyYXRpb24gb2YgYSBUT1RQIGdlb
  mVyYXRvciBmb3IgYSB1c2VyLCBhcyBhIHJlc3BvbnNlIGEgc2VjcmV0IHJldHVybmVkLCB3aGljaCBpcyB1c2VkIHRvIGluaXRpY
  WxpemUgYSBUT1RQIGFwcCBvciBkZXZpY2UuSgsKAzIwMBIECgJPS4q1GBEKDwoNYXV0aGVudGljYXRlZILT5JMCIjoBKiIdL3YyY
  WxwaGEvdXNlcnMve3VzZXJfaWR9L3RvdHAStAIKFlZlcmlmeVRPVFBSZWdpc3RyYXRpb24SMy56aXRhZGVsLnVzZXIudjJhbHBoY
  S5WZXJpZnlUT1RQUmVnaXN0cmF0aW9uUmVxdWVzdBo0LnppdGFkZWwudXNlci52MmFscGhhLlZlcmlmeVRPVFBSZWdpc3RyYXRpb
  25SZXNwb25zZSKuAZJBZhIiVmVyaWZ5IGEgVE9UUCBnZW5lcmF0b3IgZm9yIGEgdXNlchozVmVyaWZ5IHRoZSBUT1RQIHJlZ2lzd
  HJhdGlvbiB3aXRoIGEgZ2VuZXJhdGVkIGNvZGUuSgsKAzIwMBIECgJPS4q1GBEKDwoNYXV0aGVudGljYXRlZILT5JMCKjoBKiIlL
  3YyYWxwaGEvdXNlcnMve3VzZXJfaWR9L3RvdHAvX3ZlcmlmeRLdAgoZU3RhcnRJZGVudGl0eVByb3ZpZGVyRmxvdxI2LnppdGFkZ
  WwudXNlci52MmFscGhhLlN0YXJ0SWRlbnRpdHlQcm92aWRlckZsb3dSZXF1ZXN0Gjcueml0YWRlbC51c2VyLnYyYWxwaGEuU3Rhc
  nRJZGVudGl0eVByb3ZpZGVyRmxvd1Jlc3BvbnNlIs4BkkGIARIkU3RhcnQgZmxvdyB3aXRoIGFuIGlkZW50aXR5IHByb3ZpZGVyG
  lNTdGFydCBhIGZsb3cgd2l0aCBhbiBpZGVudGl0eSBwcm92aWRlciwgZm9yIGV4dGVybmFsIGxvZ2luLCByZWdpc3RyYXRpb24gb
  3IgbGlua2luZ0oLCgMyMDASBAoCT0uKtRgRCg8KDWF1dGhlbnRpY2F0ZWSC0+STAic6ASoiIi92MmFscGhhL3VzZXJzL2lkcHMve
  2lkcF9pZH0vc3RhcnQSxwMKI1JldHJpZXZlSWRlbnRpdHlQcm92aWRlckluZm9ybWF0aW9uEkAueml0YWRlbC51c2VyLnYyYWxwa
  GEuUmV0cmlldmVJZGVudGl0eVByb3ZpZGVySW5mb3JtYXRpb25SZXF1ZXN0GkEueml0YWRlbC51c2VyLnYyYWxwaGEuUmV0cmlld
  mVJZGVudGl0eVByb3ZpZGVySW5mb3JtYXRpb25SZXNwb25zZSKaApJByAESOlJldHJpZXZlIHRoZSBpbmZvcm1hdGlvbiByZXR1c
  m5lZCBieSB0aGUgaWRlbnRpdHkgcHJvdmlkZXIafVJldHJpZXZlIHRoZSBpbmZvcm1hdGlvbiByZXR1cm5lZCBieSB0aGUgaWRlb
  nRpdHkgcHJvdmlkZXIgZm9yIHJlZ2lzdHJhdGlvbiBvciB1cGRhdGluZyBhbiBleGlzdGluZyB1c2VyIHdpdGggbmV3IGluZm9yb
  WF0aW9uSgsKAzIwMBIECgJPS4q1GBEKDwoNYXV0aGVudGljYXRlZILT5JMCMzoBKiIuL3YyYWxwaGEvdXNlcnMvaW50ZW50cy97a
  W50ZW50X2lkfS9pbmZvcm1hdGlvbhKQAgoKQWRkSURQTGluaxInLnppdGFkZWwudXNlci52MmFscGhhLkFkZElEUExpbmtSZXF1Z
  XN0Gigueml0YWRlbC51c2VyLnYyYWxwaGEuQWRkSURQTGlua1Jlc3BvbnNlIq4BkkFnEitBZGQgbGluayB0byBhbiBpZGVudGl0e
  SBwcm92aWRlciB0byBhbiB1c2VyGitBZGQgbGluayB0byBhbiBpZGVudGl0eSBwcm92aWRlciB0byBhbiB1c2VySgsKAzIwMBIEC
  gJPS4q1GBEKDwoNYXV0aGVudGljYXRlZILT5JMCKToBKiIkL3YyYWxwaGEvdXNlcnMvdXNlcnMve3VzZXJfaWR9L2xpbmtzEooCC
  g1QYXNzd29yZFJlc2V0Eioueml0YWRlbC51c2VyLnYyYWxwaGEuUGFzc3dvcmRSZXNldFJlcXVlc3QaKy56aXRhZGVsLnVzZXIud
  jJhbHBoYS5QYXNzd29yZFJlc2V0UmVzcG9uc2UinwGSQVUSIlJlcXVlc3QgYSBjb2RlIHRvIHJlc2V0IGEgcGFzc3dvcmQaIlJlc
  XVlc3QgYSBjb2RlIHRvIHJlc2V0IGEgcGFzc3dvcmRKCwoDMjAwEgQKAk9LirUYEQoPCg1hdXRoZW50aWNhdGVkgtPkkwIsOgEqI
  icvdjJhbHBoYS91c2Vycy97dXNlcl9pZH0vcGFzc3dvcmRfcmVzZXQSnwIKC1NldFBhc3N3b3JkEigueml0YWRlbC51c2VyLnYyY
  WxwaGEuU2V0UGFzc3dvcmRSZXF1ZXN0Gikueml0YWRlbC51c2VyLnYyYWxwaGEuU2V0UGFzc3dvcmRSZXNwb25zZSK6AZJBdhIPQ
  2hhbmdlIHBhc3N3b3JkGlZDaGFuZ2UgdGhlIHBhc3N3b3JkIG9mIGEgdXNlciB3aXRoIGVpdGhlciBhIHZlcmlmaWNhdGlvbiBjb
  2RlIG9yIHRoZSBjdXJyZW50IHBhc3N3b3JkLkoLCgMyMDASBAoCT0uKtRgRCg8KDWF1dGhlbnRpY2F0ZWSC0+STAiY6ASoiIS92M
  mFscGhhL3VzZXJzL3t1c2VyX2lkfS9wYXNzd29yZBKNAwodTGlzdEF1dGhlbnRpY2F0aW9uTWV0aG9kVHlwZXMSOi56aXRhZGVsL
  nVzZXIudjJhbHBoYS5MaXN0QXV0aGVudGljYXRpb25NZXRob2RUeXBlc1JlcXVlc3QaOy56aXRhZGVsLnVzZXIudjJhbHBoYS5Ma
  XN0QXV0aGVudGljYXRpb25NZXRob2RUeXBlc1Jlc3BvbnNlIvIBkkGiARIyTGlzdCBhbGwgcG9zc2libGUgYXV0aGVudGljYXRpb
  24gbWV0aG9kcyBvZiBhIHVzZXIaX0xpc3QgYWxsIHBvc3NpYmxlIGF1dGhlbnRpY2F0aW9uIG1ldGhvZHMgb2YgYSB1c2VyIGxpa
  2UgcGFzc3dvcmQsIHBhc3N3b3JkbGVzcywgKFQpT1RQIGFuZCBtb3JlSgsKAzIwMBIECgJPS4q1GBEKDwoNYXV0aGVudGljYXRlZ
  ILT5JMCMRIvL3YyYWxwaGEvdXNlcnMve3VzZXJfaWR9L2F1dGhlbnRpY2F0aW9uX21ldGhvZHNCygcKGGNvbS56aXRhZGVsLnVzZ
  XIudjJhbHBoYUIQVXNlclNlcnZpY2VQcm90b1ABWjVnaXRodWIuY29tL3ppdGFkZWwveml0YWRlbC9wa2cvZ3JwYy91c2VyL3YyY
  WxwaGE7dXNlcqICA1pVWKoCFFppdGFkZWwuVXNlci5WMmFscGhhygIUWml0YWRlbFxVc2VyXFYyYWxwaGHiAiBaaXRhZGVsXFVzZ
  XJcVjJhbHBoYVxHUEJNZXRhZGF0YeoCFlppdGFkZWw6OlVzZXI6OlYyYWxwaGGSQfEFEtUCCgxVc2VyIFNlcnZpY2USxQFUaGlzI
  EFQSSBpcyBpbnRlbmRlZCB0byBtYW5hZ2UgdXNlcnMgaW4gYSBaSVRBREVMIGluc3RhbmNlLiBUaGlzIHByb2plY3QgaXMgaW4gY
  WxwaGEgc3RhdGUuIEl0IGNhbiBBTkQgd2lsbCBjb250aW51ZSBicmVha2luZyB1bnRpbCB0aGUgc2VydmljZXMgcHJvdmlkZSB0a
  GUgc2FtZSBmdW5jdGlvbmFsaXR5IGFzIHRoZSBjdXJyZW50IGxvZ2luLiIuCgdaSVRBREVMEhNodHRwczovL3ppdGFkZWwuY29tG
  g5oaUB6aXRhZGVsLmNvbSpCCgpBcGFjaGUgMi4wEjRodHRwczovL2dpdGh1Yi5jb20veml0YWRlbC96aXRhZGVsL2Jsb2IvbWFpb
  i9MSUNFTlNFMgkyLjAtYWxwaGEaDyRaSVRBREVMX0RPTUFJTiIBLyoCAgEyEGFwcGxpY2F0aW9uL2pzb24yEGFwcGxpY2F0aW9uL
  2dycGMyGmFwcGxpY2F0aW9uL2dycGMtd2ViK3Byb3RvOhBhcHBsaWNhdGlvbi9qc29uOhBhcHBsaWNhdGlvbi9ncnBjOhphcHBsa
  WNhdGlvbi9ncnBjLXdlYitwcm90b1JtCgM0MDMSZgpHUmV0dXJuZWQgd2hlbiB0aGUgdXNlciBkb2VzIG5vdCBoYXZlIHBlcm1pc
  3Npb24gdG8gYWNjZXNzIHRoZSByZXNvdXJjZS4SGwoZGhcjL2RlZmluaXRpb25zL3JwY1N0YXR1c1JQCgM0MDQSSQoqUmV0dXJuZ
  WQgd2hlbiB0aGUgcmVzb3VyY2UgZG9lcyBub3QgZXhpc3QuEhsKGRoXIy9kZWZpbml0aW9ucy9ycGNTdGF0dXNyPgoiRGV0YWlsZ
  WQgaW5mb3JtYXRpb24gYWJvdXQgWklUQURFTBIYaHR0cHM6Ly96aXRhZGVsLmNvbS9kb2NzYgZwcm90bzM="""
      ).mkString)
  lazy val scalaDescriptor: _root_.scalapb.descriptors.FileDescriptor = {
    val scalaProto = com.google.protobuf.descriptor.FileDescriptorProto.parseFrom(ProtoBytes)
    _root_.scalapb.descriptors.FileDescriptor.buildFrom(scalaProto, dependencies.map(_.scalaDescriptor))
  }
  lazy val javaDescriptor: com.google.protobuf.Descriptors.FileDescriptor = {
    val javaProto = com.google.protobuf.DescriptorProtos.FileDescriptorProto.parseFrom(ProtoBytes)
    com.google.protobuf.Descriptors.FileDescriptor.buildFrom(javaProto, _root_.scala.Array(
      com.zitadel.`object`.v2alpha.`object`.ObjectProto.javaDescriptor,
      com.zitadel.protoc_gen_zitadel.v2.options.OptionsProto.javaDescriptor,
      com.zitadel.user.v2alpha.auth.AuthProto.javaDescriptor,
      com.zitadel.user.v2alpha.email.EmailProto.javaDescriptor,
      com.zitadel.user.v2alpha.idp.IdpProto.javaDescriptor,
      com.zitadel.user.v2alpha.password.PasswordProto.javaDescriptor,
      com.zitadel.user.v2alpha.user.UserProto.javaDescriptor,
      com.google.api.annotations.AnnotationsProto.javaDescriptor,
      com.google.api.field_behavior.FieldBehaviorProto.javaDescriptor,
      com.google.protobuf.duration.DurationProto.javaDescriptor,
      com.google.protobuf.struct.StructProto.javaDescriptor,
      com.grpc.gateway.protoc_gen_openapiv2.options.annotations.AnnotationsProto.javaDescriptor,
      com.validate.validate.ValidateProto.javaDescriptor
    ))
  }
  @deprecated("Use javaDescriptor instead. In a future version this will refer to scalaDescriptor.", "ScalaPB 0.5.47")
  def descriptor: com.google.protobuf.Descriptors.FileDescriptor = javaDescriptor
}
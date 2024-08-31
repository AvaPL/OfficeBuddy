package io.github.avapl
package domain.model.office

case class Address(
  addressLine1: String,
  addressLine2: String,
  postalCode: String,
  city: String,
  country: String
)

case class UpdateAddress(
  addressLine1: Option[String] = None,
  addressLine2: Option[String] = None,
  postalCode: Option[String] = None,
  city: Option[String] = None,
  country: Option[String] = None
)

package io.github.avapl
package adapters.http.desk.view

import derevo.derive
import domain.model.desk.view.ReservableDeskView
import io.scalaland.chimney.dsl._
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("ReservableDeskView")
case class ApiReservableDeskView(
  id: UUID,
  name: String,
  isStanding: Boolean,
  monitorsCount: Short,
  hasPhone: Boolean
)

object ApiReservableDeskView {

  def fromDomain(reservableDeskView: ReservableDeskView): ApiReservableDeskView =
    reservableDeskView.transformInto[ApiReservableDeskView]
}

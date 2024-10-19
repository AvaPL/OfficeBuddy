package io.github.avapl
package adapters.postgres.repository.reservation

import domain.model.reservation.ReservationState
import skunk.Encoder
import skunk.codec.all._varchar
import skunk.data.Arr
import scala.util.chaining._

package object view {

  lazy val _reservationStatesEncoder: Encoder[List[ReservationState]] =
    _varchar.asEncoder.contramap[List[ReservationState]](_.map(_.entryName).pipe(Arr(_: _*)))
}

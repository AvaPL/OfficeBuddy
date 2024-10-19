package io.github.avapl
package adapters.postgres.repository

import domain.model.reservation.ReservationState

import skunk.Codec
import skunk.codec.all.varchar

package object reservation {

  lazy val reservationStateCodec: Codec[ReservationState] =
    varchar.imap[ReservationState](ReservationState.withNameInsensitive)(_.entryName)
}

package io.github.avapl
package domain.model.meetingroom

import java.util.UUID

case class MeetingRoom(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  capacity: Int,
  hasProjector: Boolean,
  hasWhiteboard: Boolean,
  hasSpeakerphone: Boolean,
  hasWebcam: Boolean,
  hasTv: Boolean,
  //
  officeId: UUID
)

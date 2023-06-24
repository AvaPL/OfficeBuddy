package io.github.avapl.util

import cats.effect.IO
import weaver.SimpleIOSuite

object FUUIDSuite extends SimpleIOSuite {

  test(
    """WHEN randomUUID is called twice
      |THEN generated UUIDs are different
      |""".stripMargin
  ) {
    val fuuid = implicitly[FUUID[IO]]
    for {
      uuid1 <- fuuid.randomUUID()
      uuid2 <- fuuid.randomUUID()
    } yield expect(uuid1 != uuid2)
  }
}

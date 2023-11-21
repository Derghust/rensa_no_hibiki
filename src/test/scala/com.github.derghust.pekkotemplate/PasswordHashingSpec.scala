package com.github.derghust.pekkotemplate

import com.github.derghust.pekkotemplate.behavior.{
  PasswordHashingSupervisor,
  PasswordHashingWorker,
}
import com.github.derghust.pekkotemplate.message.{ArgumentMessage, Message}
import com.github.derghust.pekkotemplate.util.PasswordHashing
import org.apache.pekko.actor.testkit.typed.scaladsl.{BehaviorTestKit, TestInbox}
import org.scalatest.wordspec.AnyWordSpec

class PasswordHashingSpec extends AnyWordSpec {

  "A PasswordHashing" when {
    "hashing" should {
      "be valid with verification" in {
        val data       = "my_secret"
        val hashedData = PasswordHashing.hash(data)

        assert(PasswordHashing.verify(data, hashedData))
      }
    }
  }
}

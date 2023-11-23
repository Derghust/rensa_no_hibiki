package com.github.derghust.rensa_no_hibiki

import com.github.derghust.rensa_no_hibiki.util.PasswordHashing
import org.scalatest.wordspec.AnyWordSpec

class PasswordHashingSpec extends AnyWordSpec:

  "A PasswordHashing" when {
    "hashing" should {
      "be valid with verification" in {
        val data       = "my_secret"
        val hashedData = PasswordHashing.hash(data)

        assert(PasswordHashing.verify(data, hashedData))
      }
    }
  }

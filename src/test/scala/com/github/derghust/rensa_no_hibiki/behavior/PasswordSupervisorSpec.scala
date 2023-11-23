package com.github.derghust.rensa_no_hibiki.behavior

import com.github.derghust.rensa_no_hibiki.DoobiePostgresUtils
import com.github.derghust.rensa_no_hibiki.message.PasswordMessage
import com.github.derghust.rensa_no_hibiki.structure.{Authentication, GenericType}
import org.apache.pekko.actor.testkit.typed.scaladsl.ActorTestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpec

class PasswordSupervisorSpec extends AnyWordSpec with BeforeAndAfterAll:
  val testKit = ActorTestKit()

  override def beforeAll(): Unit =
    DoobiePostgresUtils.clearAndFillDB

  override def afterAll(): Unit = testKit.shutdownTestKit()

  "A Password Supervisor" when {
    val supervisor = testKit.spawn(PasswordSupervisor(), "password-supervisor-spec")
    val probe      = testKit.createTestProbe[PasswordMessage]()

    val userPassword   = "This1sMyM4gicP4ssword"
    var hashedPassword = ""

    "on initialization message" should {
      "initialize workers" in {
        supervisor ! PasswordMessage.Initialize(4)
      }
    }

    "get hash request" should {
      "response with hashed" in {
        supervisor ! PasswordMessage.Request.Hash(userPassword, probe.ref)
        val message = probe.receiveMessage()

        message match
          case response: PasswordMessage.Response.Hashed =>
            hashedPassword = response.hashedPassword
          case _ => assert(false)
      }

    }
    "get validate request" should {
      "response on invalid password with Unauthenticated" in {
        supervisor ! PasswordMessage.Request.ValidateLogin("invalid", hashedPassword, probe.ref)
        probe.expectMessage(PasswordMessage.Response.ValidatedLogin(Authentication.Unauthenticated))
      }
      "response on valid password with Authenticated" in {
        supervisor ! PasswordMessage.Request.ValidateLogin(userPassword, hashedPassword, probe.ref)
        probe.expectMessage(PasswordMessage.Response.ValidatedLogin(Authentication.Authenticated))
      }
      "response on invalid password registration" in {
        supervisor ! PasswordMessage.Request.ValidateRegister("tooshort", probe.ref)
        probe.expectMessage(
          PasswordMessage.Response.ValidatedRegister(
            PasswordMessage.Validity.Invalid(GenericType.UserPassword.errorMessage.toString)
          )
        )
      }
      "response on valid password registration" in {
        supervisor ! PasswordMessage.Request.ValidateRegister(userPassword, probe.ref)
        probe.expectMessage(PasswordMessage.Response.ValidatedRegister(PasswordMessage.Validity.Valid))
      }
    }
  }

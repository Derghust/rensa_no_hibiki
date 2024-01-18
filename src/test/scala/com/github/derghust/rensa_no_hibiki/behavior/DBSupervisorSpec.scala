package com.github.derghust.rensa_no_hibiki.behavior

import com.github.derghust.rensa_no_hibiki.DoobiePostgresUtils
import com.github.derghust.rensa_no_hibiki.config.DatabaseConfig
import com.github.derghust.rensa_no_hibiki.message.DBMessage
import com.github.derghust.rensa_no_hibiki.structure.User
import org.apache.pekko.actor.testkit.typed.scaladsl.ActorTestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpec
import com.github.derghust.rensa_no_hibiki.jwt.JWTWrapper
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import com.github.derghust.rensa_no_hibiki.structure.Token

class DBSupervisorSpec extends AnyWordSpec with BeforeAndAfterAll:
  val testKit = ActorTestKit()

  override def beforeAll(): Unit =
    DoobiePostgresUtils.clearAndFillDB

  override def afterAll(): Unit = testKit.shutdownTestKit()

  "A Database Supervisor" when {
    val supervisor = testKit.spawn(DBSupervisor(), "db-supervisor-spec")
    val probe      = testKit.createTestProbe[DBMessage]()

    "on initialization" should {
      "initialize workers" in {
        supervisor ! DBMessage.Initialize(
          DatabaseConfig(
            DoobiePostgresUtils.driver,
            DoobiePostgresUtils.url,
            DoobiePostgresUtils.user,
            DoobiePostgresUtils.password
          ),
          4
        )
      }
    }

    "on request message" should {
      "response user by ID" in {
        supervisor ! DBMessage.Request.Read.ByID(DoobiePostgresUtils.user1.id, probe.ref)
        probe.expectMessage(DBMessage.Response(Some(DoobiePostgresUtils.user1)))
      }
      "response user by Username" in {
        supervisor ! DBMessage.Request.Read.ByName(DoobiePostgresUtils.user1.username, probe.ref)
        probe.expectMessage(DBMessage.Response(Some(DoobiePostgresUtils.user1)))
      }
    }

    "on write request" should {
      "write a new user" in {
        val newUser = User(Id.generate, Id.generate.toString(), "pass", Token())

        supervisor ! DBMessage.Request.Write.NewUser(newUser)

        Thread.sleep(100)

        supervisor ! DBMessage.Request.Read.ByID(newUser.id, probe.ref)
        probe.expectMessage(DBMessage.Response(Some(newUser)))
      }
      "write JWT" in {
        val newUser    = User(Id.generate, Id.generate.toString(), "pass", Token())
        val token      = Token(newUser.id)
        val editedUser = newUser.copy(jwt = token)

        supervisor ! DBMessage.Request.Write.NewUser(newUser)

        Thread.sleep(100)

        supervisor ! DBMessage.Request.Write.JWT(editedUser.id, token)

        Thread.sleep(100)

        supervisor ! DBMessage.Request.Read.ByID(editedUser.id, probe.ref)
        probe.expectMessage(DBMessage.Response(Some(editedUser)))
      }
    }
  }

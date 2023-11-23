package com.github.derghust.rensa_no_hibiki.http

import com.github.derghust.rensa_no_hibiki.DoobiePostgresUtils
import com.github.derghust.rensa_no_hibiki.api.authentication.API
import com.github.derghust.rensa_no_hibiki.behavior.{DBSupervisor, PasswordSupervisor}
import com.github.derghust.rensa_no_hibiki.config.DatabaseConfig
import com.github.derghust.rensa_no_hibiki.message.{AuthenticationMessage, DBMessage, PasswordMessage}
import org.apache.pekko.actor.testkit.typed.scaladsl.ActorTestKit
import org.apache.pekko.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, StatusCodes}
import org.apache.pekko.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers.shouldEqual
import org.scalatest.wordspec.AnyWordSpec
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import com.github.derghust.rensa_no_hibiki.structure.User
import com.github.derghust.rensa_no_hibiki.structure.Token
import com.github.derghust.rensa_no_hibiki.util.PasswordHashing

class AuthenticationAPISpec extends AnyWordSpec with BeforeAndAfterAll with ScalatestRouteTest:
  val testKit            = ActorTestKit()
  val dbSupervisor       = testKit.spawn(DBSupervisor(), "db-supervisor-spec")
  val passwordSupervisor = testKit.spawn(PasswordSupervisor(), "password-supervisor-spec")

  dbSupervisor ! DBMessage.Initialize(
    DatabaseConfig(
      DoobiePostgresUtils.driver,
      DoobiePostgresUtils.url,
      DoobiePostgresUtils.user,
      DoobiePostgresUtils.password
    ),
    4
  )
  passwordSupervisor ! PasswordMessage.Initialize(4)

  Thread.sleep(250)

  val api = new API(dbSupervisor, passwordSupervisor)(testKit.system)

  override protected def beforeAll(): Unit =
    DoobiePostgresUtils.clearAndFillDB

  "Authentication API" when {
    "get read request" should {
      "register a new user" in {
        val requestData =
          AuthenticationMessage(Id.generate.toString, Id.generate.toString, None).toJson
        val request = HttpRequest(
          method = HttpMethods.POST,
          uri = "/authentication/register",
          entity = HttpEntity(ContentTypes.`application/json`, requestData)
        )

        request ~> api.route ~> check {
          status shouldEqual StatusCodes.OK
        }
      }
      "login to a new user" in {
        val password = "Th1s1sMyM444gic"
        val newUser  = User(Id.generate, Id.generate.toString, PasswordHashing.hash(password), Token())
        val requestDataLogin =
          AuthenticationMessage(newUser.username, password, None)

        dbSupervisor ! DBMessage.Request.Write.NewUser(newUser)

        Thread.sleep(100)

        val requestLogin = HttpRequest(
          method = HttpMethods.POST,
          uri = "/authentication/login",
          entity = HttpEntity(ContentTypes.`application/json`, requestDataLogin.toJson)
        )

        requestLogin ~> api.route ~> check {
          status shouldEqual StatusCodes.OK
        }
      }
    }
  }

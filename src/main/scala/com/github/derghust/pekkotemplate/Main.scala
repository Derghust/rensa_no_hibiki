package com.github.derghust.pekkotemplate

import com.github.derghust.pekkotemplate.behavior.PekkoSupervisor
import com.github.derghust.pekkotemplate.message.Message
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model.{HttpEntity, ContentTypes}
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.stream.scaladsl.Source
import scala.util.Random
import org.apache.pekko.util.ByteString
import scala.io.StdIn
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.http.scaladsl.server.directives.Credentials
import com.github.derghust.pekkotemplate.message.*
import pdi.jwt.Jwt
import pdi.jwt.JwtAlgorithm
import java.time.Clock
import com.github.derghust.pekkotemplate.swagger.SwaggerDocService
import com.github.derghust.pekkotemplate.api.AuthenticationAPI
import com.github.derghust.pekkotemplate.api.Rest

object Main {

  case class User(name: String)

  def myUserPassAuthenticator(credentials: Credentials): Option[User] =
    credentials match {
      case p @ Credentials.Provided(id) if p.verify("p4ssw0rd") => Some(User(id))
      case _                                                    => None
    }

  def main(args: Array[String]): Unit = {
    implicit val system           = ActorSystem(PekkoSupervisor(), "pekko-supervisor")
    implicit val executionContext = system.executionContext

    val rest = new Rest("localhost", 8080)(system, executionContext)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return

    // streams are re-usable so we can define it here
    // and use it for every request
    // val numbers = Source.fromIterator(() => Iterator.continually(Random.nextInt()))

    // Auth
    // val dynamicRoute: Route = ctx =>
    //   post {
    //     path("jwt") {
    //       entity(as[AuthenticationMessage]) { input =>
    //         complete(responses.get(input))
    //       }
    //     }
    //   }

    // check if user is authorized to perform admin actions:
    // val admins                                   = Set("Peter")
    // def hasAdminPermissions(user: User): Boolean =
    //   admins.contains(user.name)
    // val swaggerUI                                =
    //   path("swagger")(getFromResource("dist/index.html")) ~
    //     getFromResourceDirectory("dist")
    // val route                                    =
    //   SwaggerDocService.routes ~
    //     swaggerUI ~
    //     new AuthenticationAPI().route
    // concat(
    // path("random") {
    //   get {
    //     complete(
    //       HttpEntity(
    //         ContentTypes.`text/plain(UTF-8)`,
    //         // transform each number to a chunk of bytes
    //         numbers.map(n => ByteString(s"$n\n")),
    //       )
    //     )
    //   }
    // },
    // path("jwt") {
    //   post {
    //     entity(as[AuthenticationMessage]) { message =>
    //       complete(s"Authorized: [user=${message.username}; token=$getJWt]")
    //     }
    //   }
    // }
    // Route.seal {
    //   authenticateBasic(realm = "secure site", myUserPassAuthenticator) { user =>
    //     path("secured") {
    //       authorize(hasAdminPermissions(user)) {
    //         complete(s"'${user.name}' visited Peter's lair")
    //       }
    //     }
    //   }
    // },
    // )

    // val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    // bindingFuture
    //   .flatMap(_.unbind())                 // trigger unbinding from the port
    //   .onComplete(_ => system.terminate()) // and shutdown when done
  }
}

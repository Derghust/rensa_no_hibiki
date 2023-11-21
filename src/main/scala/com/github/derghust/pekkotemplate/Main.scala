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

    val rest = Rest("localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
  }
}

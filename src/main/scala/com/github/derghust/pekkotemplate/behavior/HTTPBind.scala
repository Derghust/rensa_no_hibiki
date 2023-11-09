package com.github.derghust.pekkotemplate.behavior

import org.apache.pekko.actor.typed.Behavior
import com.github.derghust.pekkotemplate.message.Message
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model.*
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.actor.typed.ActorSystem

object HTTPBind {
  final case class Init(address: String, port: Int) extends Message

  def apply: Behavior[Message] = Behaviors.setup(_ => empty)

  private lazy val empty: Behavior[Message] =
    Behaviors.receive[Message] { (context, message) =>
      message match
        case Init(address, port) =>
          context.log.info(
            s"Initializing HTTP Bind service! [address=$address; port=$address]"
          )
          run(address, port)
        case unhandled           =>
          context.log.error(s"Unhandled message! [message=$unhandled]")
          empty
    }

  private def run(adress: String, port: Int): Behavior[Message] =
    Behaviors.receive[Message] { (context, message) =>
      message match

        case unhandled =>
          context.log.error(s"Unhandled message! [message=$unhandled]")
          empty
    }
}

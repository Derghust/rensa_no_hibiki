package com.github.derghust.pekkotemplate.behavior

import com.github.derghust.pekkotemplate.message.{
  ArgumentMessage,
  AuthenticationMessage,
  Message,
}
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.ActorRef
import com.github.derghust.pekkotemplate.util.PasswordHashing
import org.apache.pekko.actor.typed.scaladsl.Behaviors

import scala.util.Try

object PasswordHashingWorker {

  final case class HashPasswordRequest(
      requester: ActorRef[Message],
      password: String,
  ) extends Message

  final case class HashPasswordResponse(
      data: String
  ) extends Message

  lazy val run: Behavior[Message] =
    Behaviors.receive[Message] { (context, message) =>
      message match
        case HashPasswordRequest(requester, password) =>
          requester ! HashPasswordResponse(PasswordHashing.hash(password))
          run
        case unhandled                                      =>
          context.log.error(s"Unhandled message! [message=$unhandled]")
          run
    }
}

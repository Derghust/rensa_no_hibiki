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

  trait PasswordHashingWorkerMessage extends Message

  final case class HashPasswordRequest(
      requester: ActorRef[Message],
      password: String,
  ) extends PasswordHashingWorkerMessage

  final case class HashPasswordResponse(
      data: String
  ) extends PasswordHashingWorkerMessage

  final case class ValidatePasswordRequest(
      requester: ActorRef[Message],
      user_password: String,
      db_password: String,
  ) extends PasswordHashingWorkerMessage

  final case class ValidatePasswordResponse(
      isValid: Boolean
  ) extends PasswordHashingWorkerMessage

  lazy val run: Behavior[Message] =
    Behaviors.receive[Message] { (context, message) =>
      message match
        case HashPasswordRequest(requester, password)                       =>
          requester ! HashPasswordResponse(PasswordHashing.hash(password))
          run
        case ValidatePasswordRequest(requester, user_password, db_password) =>
          requester ! ValidatePasswordResponse(
            PasswordHashing.verify(user_password, db_password)
          )
          run
        case unhandled                                                      =>
          context.log.error(s"Unhandled message! [message=$unhandled]")
          run
    }
}

package com.github.derghust.rensa_no_hibiki.behavior

import com.github.derghust.rensa_no_hibiki.message.PasswordMessage
import com.github.derghust.rensa_no_hibiki.message.PasswordMessage.{Request, Response}
import com.github.derghust.rensa_no_hibiki.structure.Authentication
import com.github.derghust.rensa_no_hibiki.structure.GenericType.UserPassword
import com.github.derghust.rensa_no_hibiki.util.PasswordHashing

import org.apache.pekko.actor.typed.scaladsl.{ActorContext, Behaviors}
import org.apache.pekko.actor.typed.{ActorRef, Behavior}

object PasswordWorker:
  lazy val run: Behavior[PasswordMessage] =
    Behaviors.receive[PasswordMessage] { (context, message) =>
      implicit val implContext = context
      GeneralBehavior.log(message)
      message match
        case Request.Hash(userPassword, response, taskID) =>
          response ! Response.Hashed(PasswordHashing.hash(userPassword), taskID)
          Behaviors.same
        case Request.ValidateLogin(userPassword, dbPassword, response, taskID) =>
          response ! Response.ValidatedLogin(Authentication(PasswordHashing.verify(userPassword, dbPassword)), taskID)
          Behaviors.same
        case Request.ValidateRegister(password, response, taskID) =>
          response ! (UserPassword.validate(password) match
            case Left(value)  => Response.ValidatedRegister(PasswordMessage.Validity.Invalid(value.toString), taskID)
            case Right(value) => Response.ValidatedRegister(PasswordMessage.Validity.Valid, taskID)
          )
          Behaviors.same
        case _ =>
          GeneralBehavior.unhandled(message, "DBSupervisor can only process request and worker response!")
    }

  def apply(): Behavior[PasswordMessage] = run

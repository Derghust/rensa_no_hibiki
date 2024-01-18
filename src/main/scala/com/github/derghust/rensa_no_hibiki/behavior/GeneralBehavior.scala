package com.github.derghust.rensa_no_hibiki.behavior

import cats.Inject
import com.github.derghust.rensa_no_hibiki.message.{InitMessage, Message}
import com.github.derghust.rensa_no_hibiki.util.TypeExtension.|:
import org.apache.pekko.actor.typed.scaladsl.{ActorContext, Behaviors}
import org.apache.pekko.actor.typed.{ActorRef, Behavior}

import scala.util.Try

object GeneralBehavior:
  def unhandled[T <: Message](message: T, helpLog: String = "")(implicit context: ActorContext[T]): Behavior[T] =
    context.log.error(s"Unhandled message! [message=$message]. $helpLog")
    Behaviors.same

  def log[T <: Message](message: T)(implicit context: ActorContext[T]): Unit =
    context.log.debug(s"Receive message. [message=$message]")

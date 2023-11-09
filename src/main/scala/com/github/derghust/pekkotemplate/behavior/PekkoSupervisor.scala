package com.github.derghust.pekkotemplate.behavior

import com.github.derghust.pekkotemplate.message.Message
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object PekkoSupervisor {
  def apply(): Behavior[Message] = Behaviors.setup(_ => empty)

  private lazy val empty: Behavior[Message] =
    Behaviors.receive[Message] { (context, message) =>
      message match
        case unhandled =>
          context.log.error(s"Unhandled message! [message=$unhandled]")
          empty
    }
}

package com.github.derghust.pekkotemplate.behavior

import com.github.derghust.pekkotemplate.message.Message
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import com.github.derghust.pekkotemplate.message.ArgumentMessage
import com.github.derghust.pekkotemplate.database.UserDB
import com.github.derghust.pekkotemplate.database.DoobieUtil

object PekkoSupervisor {
  def apply(): Behavior[Message] = Behaviors.setup(_ => empty)

  private lazy val empty: Behavior[Message] =
    Behaviors.receive[Message] { (context, message) =>
      message match
        case ArgumentMessage(cmd, args) =>
          context.log.info(s"Processing Argument message [cmd=$cmd; args=$args]")
          cmd match
            case unhandledCmd =>
              context.log.error(s"Unhandled command! [cmd=$cmd]")
          empty
        case unhandled                  =>
          context.log.error(s"Unhandled message! [message=$unhandled]")
          empty
    }
}

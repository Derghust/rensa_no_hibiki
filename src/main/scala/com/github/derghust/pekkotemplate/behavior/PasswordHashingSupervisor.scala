package com.github.derghust.pekkotemplate.behavior

import org.apache.pekko.actor.typed.Behavior
import com.github.derghust.pekkotemplate.message.Message
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import com.github.derghust.pekkotemplate.message.ArgumentMessage
import org.apache.pekko.actor.typed.ActorRef
import scala.util.Try

object PasswordHashingSupervisor {
  lazy val empty: Behavior[Message] =
    Behaviors.receive[Message] { (context, message) =>
      message match
        case msg @ ArgumentMessage("init", args) =>
          context.log.info(s"Processing Argument message [msg=$msg]")
          Try(args.head.toInt).toEither match
            case Left(value)  =>
              context.log.error(s"Unhandled argument! [message=$msg; throwable=$value]")
              empty
            case Right(value) =>
              val workers =
                (0 until value).map(i =>
                  context.spawn(
                    PasswordHashingWorker.run,
                    s"password-hashing-worker-$i",
                  )
                )
              initialized(workers.toArray, workers.length)
        case unhandled                           =>
          context.log.error(s"Unhandled message! [message=$unhandled]")
          empty
    }

  private def initialized(
      workers: Array[ActorRef[Message]],
      roundRobin: Int,
  ): Behavior[Message] =
    Behaviors.receive[Message] { (context, message) =>
      message match
        case msg @ PasswordHashingWorker.HashPasswordRequest(_, _) =>
          context.log.info(
            s"Processing Argument message [msg=${msg}]"
          )
          val newRoundRobinPosition = (roundRobin + 1) % workers.length
          workers(newRoundRobinPosition) ! msg
          initialized(workers, newRoundRobinPosition)
        case unhandled                                             =>
          context.log.error(s"Unhandled message! [message=$unhandled]")
          initialized(workers, roundRobin)
    }
}

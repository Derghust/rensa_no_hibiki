package com.github.derghust.rensa_no_hibiki.behavior

import com.github.derghust.rensa_no_hibiki.api.Rest
import com.github.derghust.rensa_no_hibiki.message
import com.github.derghust.rensa_no_hibiki.message.*
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.{ActorRef, Behavior}

object ControllerSupervisor:
  def apply(): Behavior[ControllerMessage] = inactive

  private lazy val inactive: Behavior[ControllerMessage] =
    Behaviors.receive { (context, message) =>
      implicit val implContext = context
      GeneralBehavior.log(message)
      message match
        case ControllerMessage.Initialize(
              databaseConfig,
              databaseSupervisorConfig,
              passwordSupervisorConfig,
              restConfig
            ) =>
          val dbActor = context.spawn(DBSupervisor(), "db-supervisor")
          dbActor ! DBMessage.Initialize(databaseConfig, databaseSupervisorConfig.numberOfWorker)
          val passwordActor = context.spawn(PasswordSupervisor(), "password-supervisor")
          passwordActor ! PasswordMessage.Initialize(passwordSupervisorConfig.numberOfWorker)
          val rest = Rest(restConfig.address, restConfig.port, passwordActor, dbActor)(context.system)
          initialized(passwordActor, dbActor, rest)
        case _ => GeneralBehavior.unhandled(message, "Controller Supervisor is not yet initialized!")
    }

  private def initialized(
    passwordSupervisor: ActorRef[PasswordMessage],
    dbSupervisor: ActorRef[DBMessage],
    rest: Rest
  ): Behavior[ControllerMessage] =
    Behaviors.receive[ControllerMessage] { (context, message) =>
      implicit val implContext = context
      GeneralBehavior.log(message)
      message match
        case _ =>
          GeneralBehavior.unhandled(message, "Initialized controller supervisor has no other commands!")
    }

package com.github.derghust.rensa_no_hibiki.behavior

import com.github.derghust.rensa_no_hibiki.message
import com.github.derghust.rensa_no_hibiki.message.PasswordMessage
import com.github.derghust.rensa_no_hibiki.message.PasswordMessage.{Request, Response}
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.{ActorRef, Behavior}

object PasswordSupervisor extends Supervisor[PasswordMessage, PasswordMessage]:
  lazy val inactive: Behavior[PasswordMessage] =
    Behaviors.receive { (context, message) =>
      implicit val implContext = context
      GeneralBehavior.log(message)
      message match
        case PasswordMessage.Initialize(numOfWorkers) =>
          val workers =
            (0 until numOfWorkers).map(i =>
              context.spawn(
                PasswordWorker(),
                s"password-worker-$i"
              )
            )
          active(Map(), workers.toVector, 0)
        case _ =>
          GeneralBehavior.unhandled(message, "DBSupervisor can only process request and worker response!")
    }

  override def active(
    tasks: Map[Id, ActorRef[PasswordMessage]],
    workers: Vector[ActorRef[PasswordMessage]],
    roundRobin: Int
  ): Behavior[PasswordMessage] =
    Behaviors.receive { (context, message) =>
      implicit val implContext = context
      GeneralBehavior.log(message)
      message match
        case Request.Hash(userPassword, response, None) =>
          val taskID = Id.generate
          workers(roundRobin % workers.length) ! Request.Hash(
            userPassword,
            context.self,
            Some(taskID)
          )
          active(tasks + (taskID -> response), workers, roundRobin + 1)
        case Request.ValidateLogin(userPassword, dbPassword, response, None) =>
          val taskID = Id.generate
          workers(roundRobin % workers.length) ! Request.ValidateLogin(
            userPassword,
            dbPassword,
            context.self,
            Some(taskID)
          )
          active(tasks + (taskID -> response), workers, roundRobin + 1)
        case Request.ValidateRegister(userPassword, response, taskID) =>
          val taskID = Id.generate
          workers(roundRobin % workers.length) ! Request.ValidateRegister(
            userPassword,
            context.self,
            Some(taskID)
          )
          active(tasks + (taskID -> response), workers, roundRobin + 1)
        case Response.Hashed(hashedPassword, Some(taskID)) =>
          tasks.get(taskID) match
            case Some(response) =>
              response ! Response.Hashed(hashedPassword)
              active(tasks - taskID, workers, roundRobin)
            case None => GeneralBehavior.unhandled(message, s"Unregistered taskID! [taskID=$taskID]")
        case Response.ValidatedLogin(status, Some(taskID)) =>
          tasks.get(taskID) match
            case Some(response) =>
              response ! Response.ValidatedLogin(status, None)
              active(tasks - taskID, workers, roundRobin)
            case None => GeneralBehavior.unhandled(message, s"Unregistered taskID! [taskID=$taskID]")
        case Response.ValidatedRegister(status, Some(taskID)) =>
          tasks.get(taskID) match
            case Some(response) =>
              response ! Response.ValidatedRegister(status)
              active(tasks - taskID, workers, roundRobin)
            case None => GeneralBehavior.unhandled(message, s"Unregistered taskID! [taskID=$taskID]")
        case _ =>
          GeneralBehavior.unhandled(message, "DBSupervisor can only process request and worker response!")
    }

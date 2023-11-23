package com.github.derghust.rensa_no_hibiki.behavior

import cats.effect.IO
import com.github.derghust.rensa_no_hibiki.database.UserDB
import com.github.derghust.rensa_no_hibiki.message.DBMessage
import com.github.derghust.rensa_no_hibiki.message.DBMessage.{Request, Response}
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import doobie.util.transactor.Transactor
import eu.timepit.refined
import eu.timepit.refined.string
import eu.timepit.refined.string.Uuid
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.{ActorRef, Behavior}

object DBSupervisor extends Supervisor[DBMessage, DBMessage]:
  override lazy val inactive: Behavior[DBMessage] =
    Behaviors.receive[DBMessage] { (context, message) =>
      implicit val implContext = context
      GeneralBehavior.log(message)
      message match
        case DBMessage.Initialize(databaseConfig, workerCount) =>
          val transactor = Transactor.fromDriverManager[IO](
            driver = databaseConfig.driver,
            url = databaseConfig.url,
            user = databaseConfig.user,
            password = databaseConfig.password,
            logHandler = None
          )
          val userDB = UserDB(transactor)
          val workers =
            (0 until workerCount).map(i =>
              context.spawn(
                DBWorker(userDB),
                s"db-worker-$i"
              )
            )
          active(Map(), workers.toVector, 0)
        case _ => GeneralBehavior.unhandled(message, "DBSupervisor is not yet initialized!")
    }

  override def active(
    tasks: Map[Id, ActorRef[DBMessage]],
    workers: Vector[ActorRef[DBMessage]],
    roundRobin: Int
  ): Behavior[DBMessage] =
    Behaviors.receive { (context, message) =>
      implicit val implContext = context
      GeneralBehavior.log(message)
      message match
        case Request.Read.ByID(id, response, None) =>
          val taskID = Id.generate
          workers(roundRobin % workers.length) ! DBMessage.Request.Read.ByID(
            id,
            context.self,
            Some(taskID)
          )
          active(tasks + (taskID -> response), workers, roundRobin + 1)
        case Request.Read.ByName(username, response, None) =>
          val taskID = Id.generate
          workers(roundRobin % workers.length) ! DBMessage.Request.Read.ByName(
            username,
            context.self,
            Some(taskID)
          )
          active(tasks + (taskID -> response), workers, roundRobin + 1)
        case Request.Write.JWT(id, token) =>
          workers(roundRobin % workers.length) ! DBMessage.Request.Write.JWT(
            id,
            token
          )
          active(tasks, workers, roundRobin + 1)
        case msg @ Request.Write.NewUser(_) =>
          workers(roundRobin % workers.length) ! msg
          active(tasks, workers, roundRobin + 1)
        case Response(data, Some(taskID)) =>
          tasks.get(taskID) match
            case Some(response) =>
              response ! DBMessage.Response(data)
              active(tasks - taskID, workers, roundRobin)
            case None => GeneralBehavior.unhandled(message, s"Unregistered taskID! [taskID=$taskID]")
        case _ =>
          GeneralBehavior.unhandled(message, "DBSupervisor can only process request and worker response!")
    }

package com.github.derghust.rensa_no_hibiki.behavior

import cats.effect.unsafe.implicits.global
import com.github.derghust.rensa_no_hibiki.database.UserDB
import com.github.derghust.rensa_no_hibiki.message.DBMessage
import com.github.derghust.rensa_no_hibiki.message.DBMessage.Request.{Read, Write}
import com.github.derghust.rensa_no_hibiki.message.DBMessage.Response
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object DBWorker:
  def apply(userDB: UserDB): Behavior[DBMessage] = run(userDB)

  private def run(userDB: UserDB): Behavior[DBMessage] =
    Behaviors.receive { (context, message) =>
      implicit val implContext = context
      GeneralBehavior.log(message)
      message match
        case Read.ByID(id, response, taskID) =>
          response ! Response(
            userDB
              .getUserById(id)
              .unsafeRunSync(),
            taskID
          )
          Behaviors.same
        case Read.ByName(username, response, taskID) =>
          response ! Response(
            userDB
              .getUserByName(username)
              .unsafeRunSync(),
            taskID
          )
          Behaviors.same
        case Write.JWT(userId, token) =>
          userDB.setJWT(userId, token)
          Behaviors.same
        case Write.NewUser(user) =>
          userDB.writeNewUser(user)
          Behaviors.same
        case _ => GeneralBehavior.unhandled(message, "DBWorker can only accept request!")
    }

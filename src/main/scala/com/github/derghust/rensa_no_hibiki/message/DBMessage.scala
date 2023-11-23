package com.github.derghust.rensa_no_hibiki.message

import com.github.derghust.rensa_no_hibiki.config.DatabaseConfig
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import com.github.derghust.rensa_no_hibiki.structure.{DBData, User}
import com.github.derghust.rensa_no_hibiki.util.TypeExtension.*
import org.apache.pekko.actor.typed.ActorRef
import com.github.derghust.rensa_no_hibiki.structure.Token

sealed trait DBMessage extends Message

object DBMessage:
  sealed trait Request extends DBMessage

  object Request:
    sealed trait Read  extends Request with Metadata.Task
    sealed trait Write extends Request
    object Read:
      final case class ByID(key: Id, response: ActorRef[DBMessage], override val taskID: Option[Id] = None) extends Read
      final case class ByName(username: String, response: ActorRef[DBMessage], override val taskID: Option[Id] = None)
          extends Read
    object Write:
      final case class JWT(key: Id, token: Token) extends Write
      final case class NewUser(user: User)        extends Write

  final case class Response[T <: DBData](data: Option[T], override val taskID: Option[Id] = None)
      extends DBMessage
      with Metadata.Task
  final case class Initialize(databaseConfig: DatabaseConfig, workerCount: Int) extends DBMessage

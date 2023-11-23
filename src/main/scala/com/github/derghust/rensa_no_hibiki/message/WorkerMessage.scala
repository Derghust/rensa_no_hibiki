package com.github.derghust.rensa_no_hibiki.message

import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import org.apache.pekko.actor.typed.ActorRef

trait WorkerMessage extends Message {}

object WorkerMessage:
  trait Request[M <: Message] extends WorkerMessage:
    val taskID: Id
    val response: ActorRef[M]

  trait Response extends WorkerMessage:
    val taskID: Id

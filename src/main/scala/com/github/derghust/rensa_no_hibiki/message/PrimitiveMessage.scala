package com.github.derghust.rensa_no_hibiki.message

import com.github.derghust.rensa_no_hibiki.structure.DBData
import com.github.derghust.rensa_no_hibiki.util.TypeExtension.|:
import org.apache.pekko.actor.typed.ActorRef

trait PrimitiveMessage extends Message

object PrimitiveMessage:
  final case class RequestActorRef[T <: Message](response: ActorRef[T])  extends PrimitiveMessage
  final case class ResponseActorRef[T <: Message](actorRef: ActorRef[T]) extends PrimitiveMessage
  case class Initialize(arguments: Array[(String, Int |: String)])       extends PrimitiveMessage with InitMessage

package com.github.derghust.rensa_no_hibiki.behavior

import com.github.derghust.rensa_no_hibiki.message.Message
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import org.apache.pekko.actor.typed.{ActorRef, Behavior}

trait Supervisor[S <: Message, W <: Message]:
  def apply(): Behavior[S] = inactive

  lazy val inactive: Behavior[S]
  def active(tasks: Map[Id, ActorRef[S]], workers: Vector[ActorRef[W]], roundRobin: Int): Behavior[S]

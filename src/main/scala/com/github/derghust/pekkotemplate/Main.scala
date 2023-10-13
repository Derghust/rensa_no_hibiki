package com.github.derghust.pekkotemplate

import com.github.derghust.pekkotemplate.actor.PekkoSupervisor
import com.github.derghust.pekkotemplate.message.Message
import org.apache.pekko.actor.typed.ActorSystem

object Main extends App {

  val actorSystem = ActorSystem[Message](PekkoSupervisor(), "pekko-supervisor")

}

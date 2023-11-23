package com.github.derghust.rensa_no_hibiki

import com.github.derghust.rensa_no_hibiki.behavior.ControllerSupervisor
import com.github.derghust.rensa_no_hibiki.config.{DatabaseConfig, DatabaseSupervisorConfig, PasswordSupervisorConfig, RestConfig}
import com.github.derghust.rensa_no_hibiki.message.ControllerMessage
import org.apache.pekko.actor.typed.ActorSystem

import scala.io.StdIn

object Main:
  def main(args: Array[String]): Unit =
    implicit val system           = ActorSystem(ControllerSupervisor(), "pekko-supervisor")
    implicit val executionContext = system.executionContext

    system ! ControllerMessage.Initialize(
      DatabaseConfig("org.postgresql.Driver", "jdbc:postgresql:rnh", "docker", "docker"),
      DatabaseSupervisorConfig(8),
      PasswordSupervisorConfig(8),
      RestConfig("localhost", 8080)
    )

    println("Press RETURN to stop...")
    StdIn.readLine()
    system ! ControllerMessage.Stop

package com.github.derghust.rensa_no_hibiki.message

import com.github.derghust.rensa_no_hibiki.config.{DatabaseConfig, DatabaseSupervisorConfig, PasswordSupervisorConfig, RestConfig}

sealed trait ControllerMessage extends Message

object ControllerMessage extends ControllerMessage:
  final case class Initialize(
    databaseConfig: DatabaseConfig,
    databaseSupervisorConfig: DatabaseSupervisorConfig,
    passwordSupervisorConfig: PasswordSupervisorConfig,
    restConfig: RestConfig
  ) extends ControllerMessage
  case object Stop extends ControllerMessage

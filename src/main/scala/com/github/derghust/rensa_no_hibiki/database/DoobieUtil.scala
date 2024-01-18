package com.github.derghust.rensa_no_hibiki.database

import cats.effect.IO
import doobie.util.transactor.Transactor

object DoobieUtil:

  /** Create a new [[Doobie]] postgres [[Transactor]] for given database.
    *
    * @param database
    *   Database name.
    * @return
    *   [[Doobie]] postgres [[Transactor]].
    */
  def transactor(database: String) =
    Transactor.fromDriverManager[IO](
      driver = "org.postgresql.Driver",
      url = s"jdbc:postgresql:$database",
      user = "docker",     // TODO Read user from configuration secrets
      password = "docker", // TODO Read password from configuration secrets
      logHandler = None
    )

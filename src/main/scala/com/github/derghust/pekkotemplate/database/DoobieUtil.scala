package com.github.derghust.pekkotemplate.database

import doobie.util.transactor.Transactor
import cats.effect.IO

object DoobieUtil {

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
      logHandler = None,
    )
}

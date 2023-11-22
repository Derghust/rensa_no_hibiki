package com.github.derghust.pekkotemplate.database

import doobie.util.transactor.Transactor
import cats.effect.IO
import doobie.util.query
import doobie.implicits.*
import doobie.refined.implicits.*
import com.github.derghust.pekkotemplate.structure.User
import java.util.UUID

/** [[User]] Database executor.
  *
  * @param transactor
  *   Doobie [[Transactor]]
  */
class UserDB(transactor: Transactor[IO])
    extends GenericDB[User](transactor, UserDB.sqlSchema, UserDB.sqlTableName) {

  /** Get[[User]] by name formatted as a [[String]].
    *
    * {{{
    *  import cats.effect.unsafe.implicits.global
    *  val transactor = DoobieUtil.transactor("rnh")
    *  val userDB = UserDB(transactor)
    *  userDB.getUserByName("Allan").unsafeRunSync()
    * }}}
    *
    * @param id
    *   User id [[UUID]] format as a [[String]].
    * @return
    *   Scala cats [[IO]] for query on [[Option]] as [[User]].
    */
  def getUserByName(name: String) =
    val sqlSchema    = "id, username, password"
    val sqlTableName = "app_user"
    val segment      = fr""
    // sql"select id, username from app_user where name = $name"
    fr"SELECT $sqlSchema FROM $sqlTableName where name = $name"
      .query[User]
      .option
      .transact(transactor)
}

object UserDB {

  val sqlSchema    = fr"id, username"
  val sqlTableName = fr"app_user"

  /** Initialize [[User]] Database executor.
    *
    * @param transactor
    *   Doobie [[Transactor]]
    */
  def apply(transactor: Transactor[IO]): UserDB = new UserDB(transactor)
}

package com.github.derghust.rensa_no_hibiki.database

import cats.effect.IO
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import com.github.derghust.rensa_no_hibiki.structure.User
import com.typesafe.scalalogging.LazyLogging
import doobie.implicits.*
import doobie.refined.implicits.*
import doobie.util.transactor.Transactor

import java.util.UUID
import com.github.derghust.rensa_no_hibiki.structure.Token

/** [[User]] Database executor.
  *
  * @param transactor
  *   Doobie [[Transactor]]
  */
class UserDB(transactor: Transactor[IO])
    extends GenericDB[User](transactor, UserDB.sqlSchema, UserDB.sqlTableName)
    with LazyLogging:

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
    val sqlSchema    = "id, username, password, jwt"
    val sqlTableName = "app_user"
    val segment      = fr""
    // sql"select id, username from app_user where name = $name"
    fr"SELECT ${UserDB.sqlSchema} FROM ${UserDB.sqlTableName} where username = $name"
      .query[User]
      .option
      .transact(transactor)

  /** {{{
    *  import cats.effect.unsafe.implicits.global
    *  val transactor = DoobieUtil.transactor("rnh")
    *  val userDB = UserDB(transactor)
    *  userDB.getUserByName("Allan").unsafeRunSync()
    * }}}
    *
    * @param id
    * @return
    */
  def getUserById(id: Id) =
    val sqlSchema    = "id, username, password, jwt"
    val sqlTableName = "app_user"
    val segment      = fr""
    // sql"select id, username from app_user where name = $name"
    fr"SELECT ${UserDB.sqlSchema} FROM ${UserDB.sqlTableName} where id = $id"
      .query[User]
      .option
      .transact(transactor)

  /** Update JWT token for user.
    *
    * @param id
    *   User id [[UUID]] format as a [[String]].
    * @param jwt
    *   JWT token.
    */
  def setJWT(id: Id, jwt: Token) =
    import cats.effect.unsafe.implicits.global
    fr"UPDATE ${UserDB.sqlTableName} set jwt = $jwt where id = $id".update.run
      .transact(transactor)
      .unsafeRunSync()

  def writeNewUser(user: User) =
    import cats.effect.unsafe.implicits.global
    fr"INSERT INTO ${UserDB.sqlTableName} (${UserDB.sqlSchemaNewUser}) VALUES (${user.id.toString}, ${user.username}, ${user.password})".update.run
      .transact(transactor)
      .unsafeRunSync()

object UserDB:

  val sqlSchema        = fr"id, username, password, jwt"
  val sqlSchemaNewUser = fr"id, username, password"
  val sqlTableName     = fr"app_user"

  trait Status
  object Status:
    object UserExist        extends Status
    object UserDoesNotExist extends Status
    trait Error             extends Status
    object Error:
      object UnhandledMessage extends Error

  /** Initialize [[User]] Database executor.
    *
    * @param transactor
    *   Doobie [[Transactor]]
    */
  def apply(transactor: Transactor[IO]): UserDB = new UserDB(transactor)

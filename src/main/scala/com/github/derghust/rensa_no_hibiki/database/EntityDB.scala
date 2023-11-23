package com.github.derghust.rensa_no_hibiki.database

import cats.effect.IO
import com.github.derghust.rensa_no_hibiki.structure.Entity
import doobie.implicits.*
import doobie.refined.implicits.*
import doobie.util.transactor.Transactor

/** Initialize [[Entity]] Database executor.
  *
  * @param transactor
  *   Doobie [[Transactor]]
  */
class EntityDB(transactor: Transactor[IO])
    extends GenericDB[Entity](transactor, EntityDB.sqlSchema, EntityDB.sqlTableName)

object EntityDB:

  val sqlSchema    = fr"id, label, description"
  val sqlTableName = fr"entity"

  /** Initialize [[Entity]] Database executor.
    *
    * @param transactor
    *   Doobie [[Transactor]]
    */
  def apply(transactor: Transactor[IO]): EntityDB = new EntityDB(transactor)

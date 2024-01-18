package com.github.derghust.rensa_no_hibiki.database

import cats.effect.IO
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import doobie.implicits.*
import doobie.refined.implicits.*
import doobie.util.Read
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor

class GenericDB[R: Read](
  transactor: Transactor[IO],
  sqlSchema: Fragment,
  sqlTableName: Fragment
):

  /** Get [[R]] with id which should be [[UUID]] format as a [[String]].
    *
    * {{{
    *  import cats.effect.unsafe.implicits.global
    *  val transactor = DoobieUtil.transactor("rnh")
    *  val myExecutorDB = MyExecutorDB(transactor)
    *  myExecutorDB.get("0587ddce-e039-4474-aade-06553e47f2e5").unsafeRunSync()
    * }}}
    *
    * @param id
    *   User id [[UUID]] format as a [[String]].
    * @return
    *   Scala cats [[IO]] for query on [[Option]] as [[User]].
    */
  def get(id: Id) =
    fr"SELECT $sqlSchema FROM $sqlTableName where id = $id"
      .query[R]
      .option
      .transact(transactor)

  /** Get [[Stream]] of [[R]]
    *
    * {{{
    *  def processChunk(chunk: List[User]) =
    *    chunk.foreach(println)
    *
    *  val transactor = DoobieUtil.transactor("rnh")
    *  val myExecutorDB = MyExecutorDB(transactor)
    *  myExecutorDB.getStream
    *              .chunkN(256)           // Set batch size
    *              .evalMap(processChunk) // Set processing function
    *              .compile
    *              .drain
    *              .unsafeRunSync()
    * }}}
    *
    * @return
    *   [[Stream]] of [[User]].
    */
  def getStream =
    fr"select $sqlSchema from $sqlTableName"
      .query[R]
      .stream
      .transact(transactor)

package com.github.derghust.pekkotemplate.database

import doobie.util.transactor.Transactor
import doobie.implicits.*
import doobie.refined.implicits.*
import cats.effect.IO

import com.github.derghust.pekkotemplate.structure.Subscription

/** [[Subscription]] Database executor.
  *
  * @param transactor
  *   Doobie [[Transactor]]
  */
class SubscriptionDB(transactor: Transactor[IO])
    extends GenericDB[Subscription](
      transactor,
      SubscriptionDB.sqlSchema,
      SubscriptionDB.sqlTableName,
    )

object SubscriptionDB {

  val sqlSchema    = fr"id, counter, entity_id, user_id"
  val sqlTableName = fr"subscription"

  /** Initialize [[Subscription]] Database executor.
    *
    * @param transactor
    *   Doobie [[Transactor]]
    */
  def apply(transactor: Transactor[IO]): SubscriptionDB = new SubscriptionDB(transactor)
}

package com.github.derghust.rensa_no_hibiki.database

import cats.effect.IO
import com.github.derghust.rensa_no_hibiki.structure.Subscription
import doobie.implicits.*
import doobie.refined.implicits.*
import doobie.util.transactor.Transactor

/** [[Subscription]] Database executor.
  *
  * @param transactor
  *   Doobie [[Transactor]]
  */
class SubscriptionDB(transactor: Transactor[IO])
    extends GenericDB[Subscription](
      transactor,
      SubscriptionDB.sqlSchema,
      SubscriptionDB.sqlTableName
    )

object SubscriptionDB:

  val sqlSchema    = fr"id, counter, entity_id, user_id"
  val sqlTableName = fr"subscription"

  /** Initialize [[Subscription]] Database executor.
    *
    * @param transactor
    *   Doobie [[Transactor]]
    */
  def apply(transactor: Transactor[IO]): SubscriptionDB = new SubscriptionDB(transactor)

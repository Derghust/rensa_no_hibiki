package com.github.derghust.pekkotemplate

import org.scalatest.wordspec.AnyWordSpec
import doobie.util.transactor.Transactor
import doobie.implicits.*
import doobie.refined.implicits.*
import cats.effect.IO
import cats.effect.ExitCode
import com.github.derghust.pekkotemplate.database.UserDB
import cats.effect.SyncIO
import cats.effect.Sync
import com.github.derghust.pekkotemplate.structure.User
import fs2.Chunk
import cats.effect.unsafe.implicits.global
import cats.effect.kernel.Ref
import com.github.derghust.pekkotemplate.DoobiePostgresUtils.databaseName
import org.scalatest.BeforeAndAfter
import com.github.derghust.pekkotemplate.DoobiePostgresUtils.*
import doobie.util.Read
import com.github.derghust.pekkotemplate.database.EntityDB
import com.github.derghust.pekkotemplate.database.SubscriptionDB

class DoobiePostgresSpec
    extends AnyWordSpec
    with BeforeAndAfter
    with doobie.scalatest.IOChecker {

  val transactor = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url = s"jdbc:postgresql:$databaseName",
    user = "docker",
    password = "docker",
    logHandler = None,
  )

  before {
    DoobiePostgresUtils.drop.transact(transactor).unsafeRunSync()
    DoobiePostgresUtils.create.transact(transactor).unsafeRunSync()
  }

  def processChunk[B: Read](chunk: Chunk[B]): IO[Unit] = IO {
    println(s"Processing chunk with ${chunk.size} records")
    assert(chunk.size > 0)

    chunk.foreach(println)
  }

  "A UserDB transactor" when {
    val executor = UserDB(transactor)

    "call get request on users" should {
      "have size larger than 0" in {
        val stream = executor.getStream
          .chunkN(256)           // Set batch size
          .evalMap(processChunk) // Set processing function
          .compile
          .drain
          .unsafeRunSync()
      }
      "contain user with required UUID and content" in {
        val request1 = executor.get(user1.id).unsafeRunSync()
        val request2 = executor.get(user2.id).unsafeRunSync()
        val request3 = executor.get(user3.id).unsafeRunSync()

        assert(request1.nonEmpty)
        assert(request2.nonEmpty)
        assert(request3.nonEmpty)

        assert(request1.get == user1)
        assert(request2.get == user2)
        assert(request3.get == user3)
      }
    }

  }

  "A EntityDB transactor" when {
    val executor = EntityDB(transactor)

    "call get request on entity" should {
      "have size larger than 0" in {
        val stream = executor.getStream
          .chunkN(256)           // Set batch size
          .evalMap(processChunk) // Set processing function
          .compile
          .drain
          .unsafeRunSync()
      }
      "contain entity with required UUID and content" in {
        val request1 = executor.get(entity1.id).unsafeRunSync()
        val request2 = executor.get(entity2.id).unsafeRunSync()
        val request3 = executor.get(entity3.id).unsafeRunSync()

        assert(request1.nonEmpty)
        assert(request2.nonEmpty)
        assert(request3.nonEmpty)

        assert(request1.get == entity1)
        assert(request2.get == entity2)
        assert(request3.get == entity3)
      }
    }
  }

  "A SubscriptionDB transactor" when {
    val executor = SubscriptionDB(transactor)

    "call get request on subscription" should {
      "have size larger than 0" in {
        val stream = executor.getStream
          .chunkN(256)           // Set batch size
          .evalMap(processChunk) // Set processing function
          .compile
          .drain
          .unsafeRunSync()
      }
      "contain subscription with required UUID and content" in {
        val request1 = executor.get(subscription1.id).unsafeRunSync()
        val request2 = executor.get(subscription2.id).unsafeRunSync()
        val request3 = executor.get(subscription3.id).unsafeRunSync()

        assert(request1.nonEmpty)
        assert(request2.nonEmpty)
        assert(request3.nonEmpty)

        assert(request1.get == subscription1)
        assert(request2.get == subscription2)
        assert(request3.get == subscription3)
      }
    }
  }
}

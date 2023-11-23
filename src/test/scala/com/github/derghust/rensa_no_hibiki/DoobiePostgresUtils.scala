package com.github.derghust.rensa_no_hibiki

import _root_.cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.github.derghust.rensa_no_hibiki.database.UserDB
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import com.github.derghust.rensa_no_hibiki.structure.{Entity, Subscription, User}
import com.typesafe.scalalogging.LazyLogging
import doobie.LogHandler
import doobie.implicits.*
import doobie.util.log.LogEvent
import doobie.util.transactor.Transactor
import eu.timepit.refined.*
import com.github.derghust.rensa_no_hibiki.structure.Token

object DoobiePostgresUtils extends LazyLogging {
  val driver = "org.postgresql.Driver"
  val url    = "jdbc:postgresql:rnh"
//  val url      = "jdbc:postgresql://192.168.50.38:5432/rnh"
  val user     = "docker"
  val password = "docker"

  val printSqlLogHandler: LogHandler[IO] = new LogHandler[IO]:
    def run(logEvent: LogEvent): IO[Unit] =
      IO {
        logger.debug(logEvent.sql)
//        println(logEvent.sql)
      }

  def initTransactor = Transactor.fromDriverManager[IO](
    driver = driver,
    url = url,
    user = user,
    password = password,
    logHandler = Some(printSqlLogHandler)
  )

  def initUserDB = UserDB(initTransactor)

  private lazy val globalTransactor = initTransactor
  private var initializedDB         = false

  def clearAndFillDB =
    this.synchronized {
      if !initializedDB then
        DoobiePostgresUtils.drop.transact(globalTransactor).unsafeRunSync()
        DoobiePostgresUtils.create.transact(globalTransactor).unsafeRunSync()
        initializedDB = true
    }

  val userId1 = Id.generate
  val userId2 = Id.generate
  val userId3 = Id.generate

  val user1 = User(userId1, "User1", "passA", Token(userId1))
  val user2 = User(userId2, "User2", "passB", Token(userId2))
  val user3 = User(userId3, "User3", "passC", Token(userId3))

  val entity1 = Entity(Id.generate, "Entity1", "Description for Entity1")
  val entity2 = Entity(Id.generate, "Entity2", "Description for Entity2")
  val entity3 = Entity(Id.generate, "Entity3", "Description for Entity3")

  val subscription1 = Subscription(Id.generate, 1, entity1.id, user1.id)
  val subscription2 = Subscription(Id.generate, 2, entity2.id, user2.id)
  val subscription3 = Subscription(Id.generate, 3, entity3.id, user3.id)

  val drop =
    sql"""
      -- Drop existing tables
      DROP TABLE IF EXISTS subscription;
      DROP TABLE IF EXISTS entity;
      DROP TABLE IF EXISTS app_user;
    """.update.run

  // format: off
  val create =
    fr"""
      -- Create the 'app_user' table with an array of subscription_id
      CREATE TABLE app_user (
          id VARCHAR(255) PRIMARY KEY,
          username VARCHAR(255) NOT NULL,
          password VARCHAR(255) NOT NULL,
          jwt VARCHAR(255)
          -- Add other user-related columns as needed
      );

      -- Create the 'entity' table
      CREATE TABLE entity (
          id VARCHAR(255) PRIMARY KEY,
          label VARCHAR(255) NOT NULL,
          description TEXT
      );

      -- Create the 'subscription' table with a foreign key reference to 'entity' table and a unique constraint on user_id
      CREATE TABLE subscription (
          id VARCHAR(255) PRIMARY KEY,
          counter INT NOT NULL,
          entity_id VARCHAR(255) REFERENCES entity(id),
          user_id VARCHAR(255) UNIQUE REFERENCES app_user(id)  -- Change 'user' to 'app_user'
          -- Add other subscription-related columns as needed
      );

      -- Insert data into the 'app_user' table
      INSERT INTO app_user (id, username, password, jwt) VALUES
        (${user1.id.toString()}, ${user1.username}, ${user1.password}, ${user1.jwt}),
        (${user2.id.toString()}, ${user2.username}, ${user2.password}, ${user2.jwt}),
        (${user3.id.toString()}, ${user3.username}, ${user3.password}, ${user3.jwt});

      -- Insert data into the 'entity' table
      INSERT INTO entity (id, label, description) VALUES
        (${entity1.id.toString()}, ${entity1.label}, ${entity1.description}),
        (${entity2.id.toString()}, ${entity2.label}, ${entity2.description}),
        (${entity3.id.toString()}, ${entity3.label}, ${entity3.description});

      -- Insert data into the 'subscription' table with a unique user_id constraint
      INSERT INTO subscription (id, counter, entity_id, user_id) VALUES
        (${subscription1.id.toString()}, ${subscription1.counter}, ${subscription1.entity_id.toString()}, ${subscription1.user_id.toString()}),
        (${subscription2.id.toString()}, ${subscription2.counter}, ${subscription2.entity_id.toString()}, ${subscription2.user_id.toString()}),
        (${subscription3.id.toString()}, ${subscription3.counter}, ${subscription3.entity_id.toString()}, ${subscription3.user_id.toString()});
    """.update.run
    // format: on
}

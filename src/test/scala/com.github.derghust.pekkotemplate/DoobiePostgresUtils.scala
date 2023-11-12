package com.github.derghust.pekkotemplate

import doobie.implicits.*
import doobie.refined.implicits.*
import com.github.derghust.pekkotemplate.structure.User
import com.github.derghust.pekkotemplate.structure.Entity
import com.github.derghust.pekkotemplate.structure.Subscription
import com.github.derghust.pekkotemplate.structure.Id
import java.util.UUID
import eu.timepit.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto.*
import eu.timepit.refined.numeric.*
import eu.timepit.refined.string.Uuid
import eu.timepit.refined.api.Refined
import doobie.util.transactor.Transactor
import _root_.cats.effect.IO

object DoobiePostgresUtils {
  val databaseName = "rnh-test"

  def generateUUID: Id = Refined.unsafeApply(UUID.randomUUID().toString())

  val user1 = User(generateUUID, "User1")
  val user2 = User(generateUUID, "User2")
  val user3 = User(generateUUID, "User3")

  val entity1 = Entity(generateUUID, "Entity1", "Description for Entity1")
  val entity2 = Entity(generateUUID, "Entity2", "Description for Entity2")
  val entity3 = Entity(generateUUID, "Entity3", "Description for Entity3")

  val subscription1 = Subscription(generateUUID, 1, entity1.id, user1.id)
  val subscription2 = Subscription(generateUUID, 2, entity2.id, user2.id)
  val subscription3 = Subscription(generateUUID, 3, entity3.id, user3.id)

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
          username VARCHAR(255) NOT NULL
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
      INSERT INTO app_user (id, username) VALUES
        (${user1.id.toString()}, ${user1.username}),
        (${user2.id.toString()}, ${user2.username}),
        (${user3.id.toString()}, ${user3.username});

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

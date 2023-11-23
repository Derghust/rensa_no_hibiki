package com.github.derghust.rensa_no_hibiki.api

import cats.effect.IO
import com.github.derghust.rensa_no_hibiki.api.authentication.API as AuthenticationAPI
import com.github.derghust.rensa_no_hibiki.behavior.ControllerSupervisor
import com.github.derghust.rensa_no_hibiki.database.UserDB
import com.github.derghust.rensa_no_hibiki.message.{DBMessage, Message, PasswordMessage}
import com.github.derghust.rensa_no_hibiki.swagger.SwaggerDocService
import com.typesafe.scalalogging.LazyLogging
import doobie.util.transactor.Transactor
import org.apache.pekko.actor.typed.scaladsl.{ActorContext, Behaviors}
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem, Behavior}
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.server.RouteConcatenation

import scala.concurrent.ExecutionContextExecutor

/** REST API routes.
  *
  * @param address
  *   IPxv address.
  * @param port
  *   address port.
  */
class Rest(
  address: String,
  port: Int,
  passwordSupervisor: ActorRef[PasswordMessage],
  dbSupervisor: ActorRef[DBMessage]
)(implicit
  val actorSystem: ActorSystem[Nothing]
) extends RouteConcatenation
    with LazyLogging:

  val databaseName = "rnh"
  val transactor = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url = s"jdbc:postgresql:$databaseName",
    user = "docker",
    password = "docker",
    logHandler = None
  )
  val userDB = UserDB(transactor)

  val swagger = SwaggerDocService(address, port)

  val routes =
    swagger.routes ~
      swagger.swaggerRoute ~
      AuthenticationAPI(dbSupervisor, passwordSupervisor).route

  val bindingFuture = Http().newServerAt(address, port).bind(routes)

  /** Unbind REST routes
    */
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.executionContext
  def unbind =
    bindingFuture
      .flatMap(_.unbind())                      // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done

object Rest:

  /** REST API routes.
    *
    * @param address
    *   IPxv address.
    * @param port
    *   address port.
    * @param system
    *   Implicit actor system.
    * @param executionContext
    *   Implicit execution context.
    */
  def apply(
    address: String,
    port: Int,
    passwordSupervisor: ActorRef[PasswordMessage],
    dbSupervisor: ActorRef[DBMessage]
  )(implicit
    actorSystem: ActorSystem[Nothing]
  ): Rest = new Rest(address, port, passwordSupervisor, dbSupervisor)

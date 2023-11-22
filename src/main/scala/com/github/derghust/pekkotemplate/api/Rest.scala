package com.github.derghust.pekkotemplate.api

import org.apache.pekko.http.scaladsl.server.RouteConcatenation
import org.apache.pekko.http.scaladsl.Http
import scala.concurrent.ExecutionContextExecutor
import org.apache.pekko.actor.typed.ActorSystem
import com.github.derghust.pekkotemplate.message.Message
import com.github.derghust.pekkotemplate.swagger.SwaggerDocService
import com.github.derghust.pekkotemplate.database.UserDB
import doobie.util.transactor.Transactor
import cats.effect.IO

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
class Rest(address: String, port: Int)(
    implicit val system: ActorSystem[Message],
    val executionContext: ExecutionContextExecutor,
) extends RouteConcatenation {
  val databaseName = "rnh"
  val transactor   = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url = s"jdbc:postgresql:$databaseName",
    user = "docker",
    password = "docker",
    logHandler = None,
  )
  val userDB       = UserDB(transactor)

  val routes =
    SwaggerDocService.routes ~
      SwaggerDocService.swaggerRoute ~
      AuthenticationAPI(userDB).route

  val bindingFuture = Http().newServerAt(address, port).bind(routes)

  /** Unbind REST routes
    */
  def unbind =
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
}

object Rest {

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
  def apply(address: String, port: Int)(
      implicit system: ActorSystem[Message],
      executionContext: ExecutionContextExecutor,
  ): Rest = new Rest(address, port)
}

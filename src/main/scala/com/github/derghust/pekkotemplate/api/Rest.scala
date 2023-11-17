package com.github.derghust.pekkotemplate.api

import org.apache.pekko.http.scaladsl.server.RouteConcatenation
import org.apache.pekko.http.scaladsl.Http
import scala.concurrent.ExecutionContextExecutor
import org.apache.pekko.actor.typed.ActorSystem
import com.github.derghust.pekkotemplate.message.Message
import com.github.derghust.pekkotemplate.swagger.SwaggerDocService

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
  val routes =
    SwaggerDocService.routes ~
      SwaggerDocService.route ~
      AuthenticationAPI().route

  val bindingFuture = Http().newServerAt(address, port).bind(routes)

  /** Unbind REST routes
    */
  def unbind =
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
}

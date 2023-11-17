package com.github.derghust.pekkotemplate.api

import org.apache.pekko.actor.ActorRef
import org.apache.pekko.http.scaladsl.server.{Directives, Route}
import org.apache.pekko.pattern.ask
import org.apache.pekko.util.Timeout
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.{Consumes, POST, Path, Produces}
import spray.json.RootJsonFormat
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import com.github.derghust.pekkotemplate.message.AuthenticationMessage
import jakarta.ws.rs.POST
import com.github.derghust.pekkotemplate.jwt.JWTWrapper

@Path("/authentication")
class AuthenticationAPI extends Directives with DefaultJsonFormats {
  implicit val format: RootJsonFormat[AuthenticationMessage] = jsonFormat2(
    AuthenticationMessage.apply
  )

  val route: Route = getHello

  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Add integers",
    description = "Add integers",
    requestBody = new RequestBody(
      required = true,
      content = Array(
        new Content(
          schema = new Schema(implementation = classOf[AuthenticationMessage])
        )
      ),
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "Add response",
        content = Array(
          new Content(
            schema = new Schema(implementation = classOf[String])
          )
        ),
      ),
      new ApiResponse(responseCode = "500", description = "Internal server error"),
    ),
  )
  def getHello: Route =
    path("authentication") {
      post {
        entity(as[AuthenticationMessage]) { message =>
          val token = JWTWrapper.getJWT("512")
          complete(s"Authorized: [user=${message.username}; token=$token]")
        }
      }
    }
}

object AuthenticationAPI {
  def apply(): AuthenticationAPI = new AuthenticationAPI()
}

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
import org.apache.pekko.http.scaladsl.marshalling.ToResponseMarshallable
import org.apache.pekko.http.scaladsl.model.StatusCodes
import com.github.derghust.pekkotemplate.database.UserDB
import cats.effect.unsafe.implicits.global
import com.github.derghust.pekkotemplate.util.typeExtension.toEither
import org.apache.pekko.actor.typed.Behavior
import com.github.derghust.pekkotemplate.message.Message
import org.apache.pekko.actor.typed.scaladsl.ActorContext
import com.github.derghust.pekkotemplate.behavior.PasswordHashingSupervisor
import com.github.derghust.pekkotemplate.message.ArgumentMessage

@Path("/authentication")
class AuthenticationAPI(userDB: UserDB)(
    implicit val context: ActorContext[Message]
) extends Directives
    with DefaultJsonFormats {
  implicit val format: RootJsonFormat[AuthenticationMessage] = jsonFormat2(
    AuthenticationMessage.apply
  )

  val route: Route       = getHello
  val passwordSupervisor =
    context.spawn(PasswordHashingSupervisor.empty, "password-supervisor")

  passwordSupervisor ! ArgumentMessage("init", List(8.toString()))

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
        description = "Authorized access",
        content = Array(
          new Content(
            schema = new Schema(implementation = classOf[String])
          )
        ),
      ),
      new ApiResponse(responseCode = "401", description = "Unauthorized credentials"),
    ),
  )
  def getHello: Route =
    path("authentication") {
      post {
        entity(as[AuthenticationMessage]) { message =>
          // TODO
          //  1. Get User from UserLoader from username
          //    - UserLoader will load user from DB if not located in cache
          //    - Return Either[Error, User]
          //  2. match case or HOF (map,...)
          //    - Left send complete(204)
          //  3. Validate password
          //    - Left send complete(StatusCodes.Unauthorized)
          //    - Right send complete(s"Authorized: [user=${message.username}; token=$token]")

          import cats.implicits.toBifunctorOps
          val a: Either[Int, Boolean] = Right(true)
          val b                       = a.leftMap(x => x)

          val user = userDB
            .getUserByName(message.username)
            .unsafeRunSync()
            .toEither(complete(StatusCodes.NotFound))
            .map { value =>
              // passwordSupervisor !
            }

          val token = JWTWrapper.getJWT("512")

          complete(s"Authorized: [user=${message.username}; token=$token]")
        }
      }
    }
}

object AuthenticationAPI {
  def apply(userDB: UserDB)(
      implicit context: ActorContext[Message]
  ): AuthenticationAPI = new AuthenticationAPI(userDB)
}

package com.github.derghust.rensa_no_hibiki.api.authentication

import com.github.derghust.rensa_no_hibiki.api.DefaultJsonFormats
import com.github.derghust.rensa_no_hibiki.jwt.JWTWrapper
import com.github.derghust.rensa_no_hibiki.message.{AuthenticationMessage, DBMessage, PasswordMessage}
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import com.github.derghust.rensa_no_hibiki.structure.{Authentication, Token, User}
import com.typesafe.scalalogging.LazyLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.{Consumes, POST, Path, Produces}
import org.apache.pekko.actor.typed.scaladsl.AskPattern.*
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
import org.apache.pekko.http.scaladsl.marshalling.ToResponseMarshallable
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.{Directives, Route, StandardRoute}
import org.apache.pekko.util.Timeout
import spray.json.RootJsonFormat

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Path("/authentication")
class API(dbSupervisor: ActorRef[DBMessage], passwordSupervisor: ActorRef[PasswordMessage])(implicit
  val actorSystem: ActorSystem[Nothing]
) extends Directives
    with DefaultJsonFormats
    with LazyLogging:
  logger.info("Initialized Authentication API")

  implicit val timeout: Timeout = Timeout(3.seconds)
  implicit val format: RootJsonFormat[AuthenticationMessage] = jsonFormat3(
    AuthenticationMessage.apply
  )

  val route: Route = authentication ~ registration

  @POST
  @Path("/login")
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Authenticate user",
    description = "Try to authenticate user and send status",
    requestBody = new RequestBody(
      required = true,
      content = Array(
        new Content(
          schema = new Schema(implementation = classOf[AuthenticationMessage])
        )
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "Authorized access",
        content = Array(
          new Content(
            schema = new Schema(implementation = classOf[String])
          )
        )
      ),
      new ApiResponse(responseCode = "401", description = "Unauthorized credentials")
    )
  )
  def authentication: Route =
    path("authentication" / "login") {
      post {
        entity(as[AuthenticationMessage]) { message =>
          Login(message, dbSupervisor, passwordSupervisor)
        }
      }
    }

  @POST
  @Path("/register")
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Authenticate user",
    description = "Try to register user and send status",
    requestBody = new RequestBody(
      required = true,
      content = Array(
        new Content(
          schema = new Schema(implementation = classOf[AuthenticationMessage])
        )
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "User was registered successfully",
        content = Array(
          new Content(
            schema = new Schema(implementation = classOf[String])
          )
        )
      ),
      new ApiResponse(responseCode = "409", description = "User email or user name is already used"),
      new ApiResponse(responseCode = "422", description = "User password is too weak")
    )
  )
  def registration: Route =
    path("authentication" / "register") {
      post {
        entity(as[AuthenticationMessage]) { message =>
          Register(message, dbSupervisor, passwordSupervisor)
        }
      }
    }

object API:
  def apply(dbSupervisor: ActorRef[DBMessage], passwordSupervisor: ActorRef[PasswordMessage])(implicit
    actorSystem: ActorSystem[Nothing]
  ): API = new API(dbSupervisor, passwordSupervisor)

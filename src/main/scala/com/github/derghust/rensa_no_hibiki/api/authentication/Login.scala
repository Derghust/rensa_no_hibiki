package com.github.derghust.rensa_no_hibiki.api.authentication

import com.github.derghust.rensa_no_hibiki.api.GeneralRoute
import com.github.derghust.rensa_no_hibiki.message.{AuthenticationMessage, DBMessage, PasswordMessage}
import com.github.derghust.rensa_no_hibiki.structure.{Authentication, Token, User}

import org.apache.pekko.actor.typed.scaladsl.AskPattern.*
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Directives.{complete, onComplete}
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.util.Timeout

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object Login extends LazyLogging:
  implicit val timeout: Timeout = 3.seconds
  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  def apply(
    message: AuthenticationMessage,
    dbSupervisor: ActorRef[DBMessage],
    passwordSupervisor: ActorRef[PasswordMessage]
  )(implicit actorSystem: ActorSystem[Nothing]): Route =
    GeneralRoute.log(message)
    val request = dbSupervisor
      .ask[DBMessage](ref => DBMessage.Request.Read.ByName(message.username, ref))
      .map {
        case DBMessage.Response(Some(User(id, _, password, _)), None) =>
          passwordSupervisor
            .ask[PasswordMessage](ref => PasswordMessage.Request.ValidateLogin(message.password, password, ref))
            .map {
              case PasswordMessage.Response.ValidatedLogin(Authentication.Authenticated, None) =>
                val token = Token(id)
                dbSupervisor ! DBMessage.Request.Write.JWT(id, token)
                complete(token.value)
              case PasswordMessage.Response.ValidatedLogin(Authentication.Unauthenticated, None) =>
                complete(StatusCodes.Unauthorized)
              case unhandled => GeneralRoute.unhandledMessage(unhandled)
            }
        case DBMessage.Response(None, None) =>
          logger.debug(s"User does not exist! Request[ID=${message.username}]")
          Future(complete(StatusCodes.Unauthorized))
        case unhandled => GeneralRoute.unhandledMessageF(unhandled)
      }
      .flatMap(x => x)

    onComplete(request) {
      case Failure(exception) =>
        logger.error(s"Failed to process request! [exception=$exception]")
        complete(StatusCodes.InternalServerError)
      case Success(value) => value
    }

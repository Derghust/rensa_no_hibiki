package com.github.derghust.rensa_no_hibiki.api.authentication

import com.github.derghust.rensa_no_hibiki.api.GeneralRoute
import com.github.derghust.rensa_no_hibiki.message.{AuthenticationMessage, DBMessage, PasswordMessage}
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import com.github.derghust.rensa_no_hibiki.structure.{Token, User}

import org.apache.pekko.actor.typed.scaladsl.AskPattern.*
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Directives.{complete, onComplete}
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.util.Timeout

import com.typesafe.scalalogging.LazyLogging

import concurrent.duration.DurationInt
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Register extends LazyLogging:
  implicit val timeout: Timeout = 3.seconds
  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  def apply(
    message: AuthenticationMessage,
    dbSupervisor: ActorRef[DBMessage],
    passwordSupervisor: ActorRef[PasswordMessage]
  )(implicit actorSystem: ActorSystem[Nothing]): Route =
    val request = dbSupervisor
      // Validate username
      .ask[DBMessage](dbRef => DBMessage.Request.Read.ByName(message.username, dbRef))
      .map {
        case DBMessage.Response(Some(User(username, _, _, _)), None) =>
          Left(
            complete(
              s"User registration failed: [" +
                s"Username \"$username\" already exist." +
                s" Try to use other username.]"
            )
          )
        case DBMessage.Response(None, None) => Right(None)
        case unhandled                      => Left(GeneralRoute.unhandledMessage(unhandled))
      }
      // Validate registration password
      .map {
        case left @ Left(_) => Future(left)
        case Right(_) =>
          passwordSupervisor
            .ask[PasswordMessage](ref => PasswordMessage.Request.ValidateRegister(message.password, ref))
            .map {
              case PasswordMessage.Response.ValidatedRegister(PasswordMessage.Validity.Valid, None) =>
                Right(None)
              case PasswordMessage.Response
                    .ValidatedRegister(PasswordMessage.Validity.Invalid(errorMessage), None) =>
                Left(
                  complete(
                    s"User registration failed: [$errorMessage]"
                  )
                )
              case unhandled => Left(GeneralRoute.unhandledMessage(unhandled))
            }
      }
      .flatMap(x => x)
      // Hash password
      .map {
        case Left(value) => Future(value)
        case Right(value) =>
          passwordSupervisor
            .ask[PasswordMessage](hashRef => PasswordMessage.Request.Hash(message.password, hashRef))
            .map {
              case PasswordMessage.Response.Hashed(hashedPassword, None) =>
                dbSupervisor ! DBMessage.Request.Write.NewUser(
                  User(Id.generate, message.username, hashedPassword, Token())
                )
                complete(StatusCodes.OK)
              case unhandled => GeneralRoute.unhandledMessage(unhandled)
            }
      }
      .flatMap(x => x)

    onComplete(request) {
      case Failure(exception) =>
        logger.error(s"Failed to process request! [exception=$exception]")
        complete(StatusCodes.InternalServerError)
      case Success(value) => value
    }

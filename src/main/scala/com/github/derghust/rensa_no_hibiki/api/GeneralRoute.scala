package com.github.derghust.rensa_no_hibiki.api

import com.github.derghust.rensa_no_hibiki.message.Message

import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Directives.complete
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.util.Timeout

import com.typesafe.scalalogging.LazyLogging

import concurrent.duration.DurationInt
import scala.concurrent.Future

object GeneralRoute extends LazyLogging:
  private implicit val timeout: Timeout = 3.seconds
  private implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  def log[T <: Message]: T => Unit = message => logger.debug(s"Receive API request. [request=$message]")

  def unhandledMessage[T <: Message](message: T): Route =
    logger.error(s"Unhandled DBData message! [message=$message]")
    complete(StatusCodes.NotImplemented)

  def unhandledMessageF[T <: Message](message: T): Future[Route] =
    logger.error(s"Unhandled DBData message! [message=$message]")
    Future(complete(StatusCodes.NotImplemented))

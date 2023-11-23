package com.github.derghust.rensa_no_hibiki.util

import eu.timepit.refined
import eu.timepit.refined.api.{RefType, Refined}
import eu.timepit.refined.string.Uuid

import java.util.UUID

object TypeExtension:

  type |:[+A1, +A2] = Either[A1, A2]

  extension [T](value: Option[T])
    def toEither[B](leftValue: B): Either[B, T] =
      value match
        case Some(rightValue) => Right(rightValue)
        case None             => Left(leftValue)

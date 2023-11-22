package com.github.derghust.pekkotemplate.util

object typeExtension {
  extension [T](value: Option[T])
    def toEither[B](leftValue: B): Either[B, T] =
      value match
        case Some(rightValue) => Right(rightValue)
        case None             => Left(leftValue)

}

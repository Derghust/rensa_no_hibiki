package com.github.derghust.rensa_no_hibiki.structure

import scala.util.matching.Regex

final case class Password private (value: String):
  override def toString(): String = "Password(HIDDEN)"

object Password:
  val passwordPattern: Regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$".r

  def apply(value: String): Either[String, Password] =
    passwordPattern.findFirstMatchIn(value) match
      case Some(_) => Right(new Password(value))
      case None    => Left("Password must contain a number and length of at least 8.")

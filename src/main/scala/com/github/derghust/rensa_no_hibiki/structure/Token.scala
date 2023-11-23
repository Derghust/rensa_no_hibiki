package com.github.derghust.rensa_no_hibiki.structure

import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import com.github.derghust.rensa_no_hibiki.jwt.JWTWrapper

final case class Token private (value: Option[String]):
  override def toString(): String =
    if value.nonEmpty then "Token(HIDDEN)"
    else "Token(NONE)"

object Token:
  def apply(): Token           = new Token(None)
  def apply(userId: Id): Token = new Token(Some(JWTWrapper.getJWT(userId.toString())))

package com.github.derghust.rensa_no_hibiki.message

final case class AuthenticationMessage(username: String, password: String, token: Option[String]) extends Message:
  def toJson =
    if token.nonEmpty then
      s"{\n  \"username\": \"$username\",\n  \"password\": \"$password\",\n  \"token\": \"${token.get}\"\n}"
    else s"{\n  \"username\": \"$username\",\n  \"password\": \"$password\"\n}"

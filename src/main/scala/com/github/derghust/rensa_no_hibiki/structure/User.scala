package com.github.derghust.rensa_no_hibiki.structure

import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import eu.timepit.refined.numeric.*

final case class User(
  id: Id,
  username: String,
  password: String,
  jwt: Token
) extends DBData:
  override def toString: String = s"User(id=$id; username=$username)"

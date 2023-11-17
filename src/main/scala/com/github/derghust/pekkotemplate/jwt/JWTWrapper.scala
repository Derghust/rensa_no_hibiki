package com.github.derghust.pekkotemplate.jwt

import java.time.Clock
import pdi.jwt.Jwt
import pdi.jwt.JwtAlgorithm
import com.github.derghust.pekkotemplate.structure.Id

object JWTWrapper {
  implicit val clock: Clock  = Clock.systemUTC
  def getJWT(userID: String) =
    Jwt.encode(s"""{"user":$userID}""", "secretKey", JwtAlgorithm.HS256)
}

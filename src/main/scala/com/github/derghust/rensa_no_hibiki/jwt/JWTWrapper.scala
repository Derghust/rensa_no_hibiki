package com.github.derghust.rensa_no_hibiki.jwt

import pdi.jwt.{Jwt, JwtAlgorithm}

import java.time.Clock

object JWTWrapper:
  implicit val clock: Clock = Clock.systemUTC
  def getJWT(userID: String) =
    Jwt.encode(s"""{"user":$userID}""", "secretKey", JwtAlgorithm.HS256)

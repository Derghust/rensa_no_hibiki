package com.github.derghust.rensa_no_hibiki.structure

import eu.timepit.refined.*
import eu.timepit.refined.api.{RefType, Refined}
import eu.timepit.refined.boolean.*
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.predicates.all.{And, MaxSize, NonEmpty}
import eu.timepit.refined.string.*

import java.util.UUID

object GenericType:
  type Name         = String Refined (NonEmpty And MaxSize[64])
  type Description  = String Refined (NonEmpty And MaxSize[1024])
  type Id           = String Refined Uuid
  type IPAdress     = String Refined (IPv4 Or IPv6)
  type UserPassword = String Refined MatchesRegex["^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"]

  object Id:

    /** Generate UUID refined type for ID purpose.
      *
      * @return
      *   ID
      * @throws Exception
      *   when unable to refine type at runtime.
      */
    def generate: Id =
      RefType.applyRef[Id](UUID.randomUUID.toString) match
        case Left(value) =>
          throw new Exception(s"Fatal! Unable to refine type at runtime! [type=[String Refined Uuid]; value=$value]")
        case Right(value) => value

  object UserPassword:
    val errorMessage = new Exception("Failed to refine user password!")
    def validate(password: String): Either[Exception, UserPassword] = RefType.applyRef[UserPassword](password) match
      case Left(value)  => Left(errorMessage)
      case Right(value) => Right(value)

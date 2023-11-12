package com.github.derghust.pekkotemplate.structure

import eu.timepit.refined.api.Refined
import eu.timepit.refined.predicates.all.{And, MaxSize, NonEmpty}
import eu.timepit.refined.string.Uuid
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.IPv4
import eu.timepit.refined.string.IPv6
import eu.timepit.refined.boolean.Or
import doobie.util.Read
import doobie.util.Write
import eu.timepit.refined.api.RefType
import eu.timepit.refined.*
import eu.timepit.refined.auto.*
import eu.timepit.refined.numeric.*
import eu.timepit.refined.api.{RefType, Refined}
import eu.timepit.refined.boolean.*
import eu.timepit.refined.char.*
import eu.timepit.refined.collection.*
import eu.timepit.refined.generic.*
import eu.timepit.refined.string.*

type Name        = String Refined (NonEmpty And MaxSize[64])
type Description = String Refined (NonEmpty And MaxSize[1024])
type Id          = String Refined Uuid
type IPAdress    = String Refined (IPv4 Or IPv6)

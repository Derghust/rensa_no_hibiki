package com.github.derghust.pekkotemplate.structure

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Uuid
import eu.timepit.refined.string.IPv4
import eu.timepit.refined.string.IPv6
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.boolean.Not
import eu.timepit.refined.numeric.*

final case class User(
    id: Id,
    username: String,
)

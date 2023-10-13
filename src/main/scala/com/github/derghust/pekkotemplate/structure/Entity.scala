package com.github.derghust.pekkotemplate.structure

import eu.timepit.refined.api.Refined
import eu.timepit.refined.predicates.all.{And, MaxSize, NonEmpty}
import eu.timepit.refined.string.Uuid

type ID          = Uuid
type Name        = String Refined NonEmpty And MaxSize[64]
type Description = String Refined NonEmpty And MaxSize[1024]

case class Entity(
    id: ID,
    name: Name,
    description: Description,
)

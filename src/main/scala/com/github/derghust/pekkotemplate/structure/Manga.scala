package com.github.derghust.pekkotemplate.structure

import eu.timepit.refined.api.Refined
import eu.timepit.refined.predicates.all.{And, MaxSize, NonEmpty}
import eu.timepit.refined.string.Uuid
import eu.timepit.refined.numeric.Greater

type Counter     = Greater[0]
type Name        = String Refined NonEmpty And MaxSize[64]
type Description = String Refined NonEmpty And MaxSize[1024]

final case class Manga(
    id: Uuid,
    name: Name,
    description: Description,
)

final case class Popularity(
    mangaID: Uuid,
    userCount: Counter,
    apiCount: Counter,
)

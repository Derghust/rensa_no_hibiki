package com.github.derghust.pekkotemplate.structure

import eu.timepit.refined.api.Refined
import eu.timepit.refined.predicates.all.{And, MaxSize, NonEmpty}
import eu.timepit.refined.string.Uuid
import eu.timepit.refined.numeric.Greater

final case class Subscription(
    id: Id,
    counter: Int,
    entity_id: Id,
    user_id: Id,
)

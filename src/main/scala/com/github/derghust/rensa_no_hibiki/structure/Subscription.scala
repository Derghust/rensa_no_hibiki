package com.github.derghust.rensa_no_hibiki.structure

import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.predicates.all.{And, MaxSize, NonEmpty}
import eu.timepit.refined.string.Uuid

final case class Subscription(
  id: Id,
  counter: Int,
  entity_id: Id,
  user_id: Id
)

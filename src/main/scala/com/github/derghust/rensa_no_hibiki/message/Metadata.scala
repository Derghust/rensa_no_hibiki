package com.github.derghust.rensa_no_hibiki.message

import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id

trait Metadata

object Metadata:
  trait Task extends Message:
    val taskID: Option[Id] = None

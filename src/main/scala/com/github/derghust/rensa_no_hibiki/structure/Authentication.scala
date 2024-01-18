package com.github.derghust.rensa_no_hibiki.structure

trait Authentication

object Authentication:
  case object Authenticated   extends Authentication
  case object Unauthenticated extends Authentication

  def apply(status: Boolean): Authentication =
    if status then Authenticated else Unauthenticated

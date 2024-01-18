package com.github.derghust.rensa_no_hibiki.message

import com.github.derghust.rensa_no_hibiki.util.TypeExtension.*

trait InitMessage:
  val arguments: Array[(String, Int |: String)]

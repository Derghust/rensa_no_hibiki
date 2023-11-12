package com.github.derghust.pekkotemplate.message

final case class ArgumentMessage(cmd: String, args: List[String]) extends Message

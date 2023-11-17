package com.github.derghust.pekkotemplate.message

import spray.json.RootJsonFormat

final case class AuthenticationMessage(username: String, password: String)
    extends Message

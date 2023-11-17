package com.github.derghust.pekkotemplate.message

import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait AuthenticationMessageSupportJsonMessageSupport
    extends SprayJsonSupport
    with DefaultJsonProtocol

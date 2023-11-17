package com.github.derghust.pekkotemplate.swagger

import com.github.swagger.pekko.SwaggerHttpService
import com.github.swagger.pekko.model.Info

import com.github.derghust.pekkotemplate.api.*
import io.swagger.v3.oas.models.ExternalDocumentation

object SwaggerDocService extends SwaggerHttpWithUiService {
  val route = path("swagger")(getFromResource("dist/index.html")) ~
    getFromResourceDirectory("dist")

  override val apiClasses                                  = Set(classOf[AuthenticationAPI])
  override val host                                        = "localhost:8080"
  override val info: Info                                  = Info(version = "1.0")
  override val externalDocs: Option[ExternalDocumentation] = Some(
    new ExternalDocumentation().description("Core Docs").url("http://acme.com/docs")
  )
  // use io.swagger.v3.oas.models.security.SecurityScheme to document authn requirements for API
  // override val securitySchemeDefinitions = Map("basicAuth" -> new SecurityScheme())
  override val unwantedDefinitions                         =
    Seq("Function1", "Function1RequestContextFutureRouteResult")
}

package com.github.derghust.rensa_no_hibiki.swagger

import com.github.derghust.rensa_no_hibiki.api.*
import com.github.derghust.rensa_no_hibiki.api.authentication.API
import com.github.swagger.pekko.SwaggerHttpService
import com.github.swagger.pekko.model.Info
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.info.License

class SwaggerDocService(address: String, port: Int) extends SwaggerHttpWithUiService:
  val swaggerRoute = path("swagger")(getFromResource("dist/index.html")) ~
    getFromResourceDirectory("dist")

  override val apiClasses = Set(classOf[API])
  override val host       = s"$address:$port"
  override val info: Info = Info(version = "1.0")
  override val externalDocs: Option[ExternalDocumentation] = Some(
    new ExternalDocumentation().description("Github Documentation").url("http://github.com/derghust/rensa_no_hibiki")
  )
  // use io.swagger.v3.oas.models.security.SecurityScheme to document authn requirements for API
  // override val securitySchemeDefinitions = Map("basicAuth" -> new SecurityScheme())
  override val unwantedDefinitions =
    Seq("Function1", "Function1RequestContextFutureRouteResult")

object SwaggerDocService:
  def apply(address: String, port: Int): SwaggerDocService = new SwaggerDocService(address, port)

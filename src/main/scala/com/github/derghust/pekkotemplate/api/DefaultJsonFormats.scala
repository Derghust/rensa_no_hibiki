package com.github.derghust.pekkotemplate.api

import spray.json.DefaultJsonProtocol
import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import scala.reflect.ClassTag
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat

trait DefaultJsonFormats extends DefaultJsonProtocol with SprayJsonSupport {

  /** Computes ``RootJsonFormat`` for type ``A`` if ``A`` is object
    */
  def jsonObjectFormat[A: ClassTag]: RootJsonFormat[A] = new RootJsonFormat[A] {
    val ct: ClassTag[A] = implicitly[ClassTag[A]]

    def write(obj: A): JsValue = JsObject(
      "value" -> JsString(ct.runtimeClass.getSimpleName)
    )

    def read(json: JsValue): A = ct.runtimeClass.newInstance().asInstanceOf[A]
  }
}

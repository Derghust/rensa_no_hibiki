package com.github.derghust.rensa_no_hibiki.api

import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsObject, JsString, JsValue, RootJsonFormat}

import scala.reflect.ClassTag

trait DefaultJsonFormats extends DefaultJsonProtocol with SprayJsonSupport:

  /** Computes ``RootJsonFormat`` for type ``A`` if ``A`` is object
    */
  def jsonObjectFormat[A: ClassTag]: RootJsonFormat[A] = new RootJsonFormat[A]:
    val ct: ClassTag[A] = implicitly[ClassTag[A]]

    def write(obj: A): JsValue = JsObject(
      "value" -> JsString(ct.runtimeClass.getSimpleName)
    )

    def read(json: JsValue): A = ct.runtimeClass.newInstance().asInstanceOf[A]

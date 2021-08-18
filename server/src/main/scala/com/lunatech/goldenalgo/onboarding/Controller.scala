package com.lunatech.goldenalgo.onboarding

import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model._

class Controller()(implicit val ec: ExecutionContext) {

  val home = path("/recipes") {
    get {
      complete(HttpEntity(ContentTypes.`application/json`, """{"recipes":"yummy"}"""))
    }
  }

  val routes: Route = home
}

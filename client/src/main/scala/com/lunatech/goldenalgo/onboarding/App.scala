package com.lunatech.goldenalgo.onboarding

import com.lunatech.goldenalgo.onboarding.router.AppRouter

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom
import org.scalajs.dom._
import japgolly.scalajs.react.vdom.html_<^._

object App {

  @JSExport
  def main(args: Array[String]): Unit = {
    val app = dom.document.getElementById("app")
    AppRouter.router().renderIntoDOM(app)
  }
}




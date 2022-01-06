package com.lunatech.goldenalgo.onboarding

import com.lunatech.goldenalgo.onboarding.css.AppCss
import com.lunatech.goldenalgo.onboarding.router.AppRouter
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

object App {

  @JSExport
  def main(args: Array[String]): Unit = {
    AppCss.load()
    val app = dom.document.getElementById("app")
    AppRouter.router().renderIntoDOM(app)
  }
}




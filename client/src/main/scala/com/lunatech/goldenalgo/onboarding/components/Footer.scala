package com.lunatech.goldenalgo.onboarding.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

object Footer {

  private val component = ScalaComponent.builder[Unit]("Footer")
    .renderStatic(
      <.footer(
        ^.className := "text-center text-white fixed-bottom",
        ^.backgroundColor := "rgb(26 19 25)",
        <.div(
          ^.backgroundColor := "rgba(0, 0, 0, 0.2)",
          ^.className := "text-center p-4",
          "Copyright © 2022",
          <.strong(" Golden Recipes Ltd "),
          "- All rights reserved"
        ),
      )
    )
    .build

  def apply(): Unmounted[Unit, Unit, Unit] = component()

}

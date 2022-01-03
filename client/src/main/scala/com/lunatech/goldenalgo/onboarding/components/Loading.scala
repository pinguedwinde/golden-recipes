package com.lunatech.goldenalgo.onboarding.components

import japgolly.scalajs.react.component.Scala.{Component, Unmounted}
import japgolly.scalajs.react.{Children, CtorType, ScalaComponent, UpdateSnapshot}
import japgolly.scalajs.react.component.builder.Builder
import japgolly.scalajs.react.vdom.html_<^._

object Loading {

  private val component =
    ScalaComponent.builder[Unit]("Loading")
    .renderStatic(
     <.div(
       ^.minHeight := "100vh",
       ^.className := "d-flex flex-row justify-content-center align-items-center w-100",
       <.img(
         ^.alt := "loading gif",
         ^.src := "https://i.redd.it/ounq1mw5kdxy.gif"
       )
      )
    ).build

  def apply(): Unmounted[Unit, Unit, Unit] = component()

}

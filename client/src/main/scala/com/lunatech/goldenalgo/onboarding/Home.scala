package com.lunatech.goldenalgo.onboarding

import japgolly.scalajs.react._
import japgolly.scalajs.react.feature.ReactFragment
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.router.RouterCtl

object Home {

  case class Props(
      ctl: RouterCtl[AppRouter.Page]
  )

  private val component = ScalaComponent
    .builder[Props]("Home")
    .render_P { props =>

      val boxStyle = TagMod(
        ^.display := "flex",
        ^.flexDirection := "column"
      )

      <.div(
        ^.display := "flex",
        ^.flexDirection := "column",
        <.div(
          ^.className := "card",
          ^.marginBottom := "20px",
          <.div(
            ^.className := "class-body",
            boxStyle,
            props.ctl.link(AppRouter.Page.Home)(
              "Home"
            )
          )
        ),
      )
    }
    .build

  def apply(ctl: RouterCtl[AppRouter.Page]): VdomElement = component(Props(ctl))
}

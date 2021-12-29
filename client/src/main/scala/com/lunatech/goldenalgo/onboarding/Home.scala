package com.lunatech.goldenalgo.onboarding

import japgolly.scalajs.react.{CtorType, _}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.feature.ReactFragment
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.extra.router.RouterCtl
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.Element

import scala.scalajs.js

object Home {

  case class Props(
      ctl: RouterCtl[AppRouter.Page]
  )

  case class State(secondsElapsed: Long)

  class Backend($: BackendScope[Unit, State]) {
    var interval: js.UndefOr[js.timers.SetIntervalHandle] =
      js.undefined

    def tick: CallbackTo[Unit] =
      $.modState(s => State(s.secondsElapsed + 1))

    def start: Callback = Callback {
      interval = js.timers.setInterval(1000)(tick.runNow())
      Backend.fetchRecipes()
    }

    def clear: Callback = Callback {
      interval foreach js.timers.clearInterval
      interval = js.undefined
    }

    def render(s: State): VdomTagOf[Div] =
      <.div("Seconds elapsed: ", s.secondsElapsed)
  }

  private val component = ScalaComponent
    .builder[Props]("Home")
    .render_P { props =>
      <.div(
        <.p("hello world")
      )
    }
    .build

  def apply(ctl: RouterCtl[AppRouter.Page]): VdomElement = component(Props(ctl))
}

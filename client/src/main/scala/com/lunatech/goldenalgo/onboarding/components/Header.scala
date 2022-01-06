package com.lunatech.goldenalgo.onboarding.components

import com.lunatech.goldenalgo.onboarding.router.AppRouter
import com.lunatech.goldenalgo.onboarding.router.AppRouter.Page
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

object Header {

  case class Props(ctrl: RouterCtl[Page])

  private val component = ScalaComponent.builder[Props]("Header")
    .render_P(props =>
      <.header(
        ^.className := "header-container navbar navbar-expand-lg navbar-dark bg-dark",
        <.span(
          ^.className := "logo navbar-brand",
          "Golden Recipes",
          props.ctrl setOnClick AppRouter.HomePage
        ),
        <.button(
          ^.className := "navbar-toggler",
          <.span(
            ^.className := "navbar-toggler-icon"
          )
        ),
        <.div(
          ^.className := "collapse navbar-collapse",
          <.ul(
            ^.className :="navbar-nav ml-auto",
            <.li(
              ^.className :="nav-item nav-link",
              "Recipes",
              props.ctrl setOnClick AppRouter.RecipesPage
            ),
            <.li(
              ^.className :="nav-item nav-link",
              "Search",
              props.ctrl setOnClick AppRouter.SearchPage
            ),
            <.li(
              ^.className :="nav-item nav-link",
              "Add a recipe",
              props.ctrl setOnClick AppRouter.AddRecipePage
            )
          )
        )
      )
    )
    .build

  def apply(props: Props): Unmounted[Props, Unit, Unit] = component(props)

}

package com.lunatech.goldenalgo.onboarding.page

import com.lunatech.goldenalgo.onboarding.css.Bootstrap.{alignItemsCenter, bgLight, dFlex, flexColumn, justifyContent, textCenter, w100}
import com.lunatech.goldenalgo.onboarding.css.GlobalStyle.heading3
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import org.scalajs.dom.html.Div
import scalacss.ScalaCssReact._


object AddRecipe {
  class Backend($: BackendScope[Unit, Unit]) {

//    def fetch: Callback = $.props.map { props =>
//      AppCircuit.dispatch(RefreshSelectedRecipe(props.id))
//    }

    def render(): VdomTagOf[Div] = {
      <.div(
        dFlex, flexColumn, alignItemsCenter, justifyContent,
        <.div(
          bgLight, dFlex, flexColumn, alignItemsCenter, textCenter,
          ^.className := "recipe-details",
          <.div(
            ^.className := "container",
            <.img(
              ^.alt := "recipe",
              ^.className := "d-block mx-auto",
              ^.src := s"client/src/main/resources/static/assets/img/recipe2.jpg"
            )
          ),
          <.hr(w100),
          <.h3(
            heading3,
            "Add the recipe's ingredients"
          ),
          <.hr(w100),
          <.h3(
            heading3,
            "Add the recipe's steps for cooking this recipe"
          ),
          <.hr(w100),
          <.h3(
            heading3,
            "Add some tags to this recipe"
          ),
        )
      )
    }
  }

  private val component = ScalaComponent
    .builder[Unit]("AddRecipe")
    .renderBackend[Backend]
    .build

  def apply(): VdomElement = component()

}

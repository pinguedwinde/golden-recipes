package com.lunatech.goldenalgo.onboarding.features.recipes.components

import com.lunatech.goldenalgo.onboarding.features.recipes.components.RecipeElement.Props
import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.lunatech.goldenalgo.onboarding.router.AppRouter
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.util.Random

object RecipesList {

  case class Props(ctl: RouterCtl[AppRouter.Page], recipes: Seq[Recipe])

  private val random = Random

  private val component = ScalaComponent.builder[Props]("RecipesList")
    .render_P(props =>
      <.div(
        ^.className := "recipe-list d-flex flex-row flex-wrap justify-content-center",
        props.recipes.toTagMod(recipe => RecipeElement(RecipeElement.Props(
          props.ctl,
          recipe,
          random.nextInt(12) + 1
        )))
      )
    ).build

  def apply(props: Props): Unmounted[Props, Unit, Unit] = component(props)

}

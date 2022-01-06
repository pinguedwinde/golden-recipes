package com.lunatech.goldenalgo.onboarding.features.recipes.components

import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.lunatech.goldenalgo.onboarding.router.AppRouter
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

object RecipeElement {

  type ImgNumber = Int

  case class Props(ctl: RouterCtl[AppRouter.Page], recipe: Recipe, imgNumber: ImgNumber)

  private val component = ScalaComponent.builder[Props]("RecipeElement")
    .render_P(props =>
      <.div(
        ^.className := "recipe-element",
        <.div(
          ^.className := "container d-flex flex-column align-items-center bg-light",
          <.img(
            ^.alt := "recipe",
            ^.width :="320",
            ^.src := s"client/src/main/resources/static/assets/img/recipe${props.imgNumber}.jpg"
          ),
          <.div(
            ^.className := "p-3 text-center",
            <.h5(props.recipe.name),
            <.hr(
              ^.className := "w-100"
            ),
            <.span(
              ^.className := "text-secondary ",
              props.recipe.tags.mkString(" | ")
            )
          ),
        ),
        props.ctl setOnClick AppRouter.RecipeDetailsPage(props.recipe.id, props.imgNumber),
      )
    ).build

  def apply(props: Props): Unmounted[Props, Unit, Unit] = component(props)

}

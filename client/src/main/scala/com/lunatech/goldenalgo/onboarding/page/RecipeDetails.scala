package com.lunatech.goldenalgo.onboarding.page

import com.lunatech.goldenalgo.onboarding.css.Bootstrap.{alignItemsCenter, bgLight, dFlex, flexColumn, justifyContent, textCenter, w100}
import com.lunatech.goldenalgo.onboarding.css.GlobalStyle.heading3
import com.lunatech.goldenalgo.onboarding.diode.{AppCircuit, RefreshSelectedRecipe}
import com.lunatech.goldenalgo.onboarding.features.recipes.components.RecipeElement.ImgNumber
import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.lunatech.goldenalgo.onboarding.model.Recipe.RecipeId
import diode.react.ModelProxy
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import org.scalajs.dom.html.Div
import scalacss.ScalaCssReact._

object RecipeDetails {

  private val recipe1 = Recipe(
    "recipe1-id",
    "recipe1-name",
    Seq("ingredient1", "ingredient2"),
    Seq("instruction1", "instruction2"),
    Seq("tag1", "tag2", "tag3")
  )

  case class Props(proxy: ModelProxy[Option[Recipe]], id: RecipeId, imgNumber: ImgNumber)

  class Backend($: BackendScope[Props, Unit]) {

    def fetch: Callback = $.props.map { props =>
      AppCircuit.dispatch(RefreshSelectedRecipe(props.id))
    }

    def render(p: Props): VdomTagOf[Div] = {
      p.proxy() match {
        case None => <.div()
        case Some(recipe) =>
          <.div(
            dFlex, flexColumn, alignItemsCenter, justifyContent,
            <.div(
              ^.className := "text-center m-5",
              <.h2(
                s"${recipe.name}"
              )
            ),
            <.div(
              bgLight, dFlex, flexColumn, alignItemsCenter, textCenter,
              ^.className := "recipe-details",
              <.div(
                ^.className := "container",
                <.img(
                  ^.alt := "recipe",
                  ^.className := "d-block mx-auto",
                  ^.src := s"client/src/main/resources/static/assets/img/recipe${p.imgNumber}.jpg"
                )
              ),
              <.hr(w100),
              <.span(
                ^.className := "text-secondary",
                recipe.tags.mkString(" | ")
              ),
              <.hr(w100),
              <.h3(
                heading3,
                "The ingredients' list"
              ),
              <.ul(
                ^.className := "w-100 list-group list-group-flush text-left",
                recipe.ingredients.toTagMod{ ingredient =>
                  <.li(
                    ^.className := "w-100 list-group-item",
                    ingredient
                  )
                }
              ),
              <.h3(
                heading3,
                "Steps"
              ),
              <.ol(
                ^.className := "w-100 list-group list-group-flush text-left",
                recipe.instructions.toTagMod{ instruction =>
                  <.li(
                    ^.className := "w-100 list-group-item",
                    instruction
                  )
                }
              ),
            )
          )
      }

    }
  }

  private val component = ScalaComponent
    .builder[Props]("RecipeDetails")
    .renderBackend[Backend]
    .componentDidMount(_.backend.fetch)
    .build

  def apply(proxy: ModelProxy[Option[Recipe]], id: RecipeId, imgNumber: ImgNumber): VdomElement = component(Props(proxy, id, imgNumber))

}

package com.lunatech.goldenalgo.onboarding.page

import com.lunatech.goldenalgo.onboarding.api.RecipeApiClient.futureGet
import com.lunatech.goldenalgo.onboarding.components.Loading
import com.lunatech.goldenalgo.onboarding.features.recipes.components.RecipeElement.ImgNumber
import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.lunatech.goldenalgo.onboarding.model.Recipe.RecipeId
import com.lunatech.goldenalgo.onboarding.router.AppRouter
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import org.scalajs.dom.html.Div

import scala.concurrent.ExecutionContext.Implicits.global

object RecipeDetails {

  private val recipe1 = Recipe(
    "recipe1-id",
    "recipe1-name",
    Seq("ingredient1", "ingredient2"),
    Seq("instruction1", "instruction2"),
    Seq("tag1", "tag2", "tag3")
  )

  case class Props(id: RecipeId, imgNumber: ImgNumber)

  case class State(recipe: Recipe, isLoaded: Boolean =  false)

  class Backend($: BackendScope[Props, State]) {

    def fetch: Callback = Callback.future {
      futureGet[Recipe](s"recipes/${$.props.runNow().id}").map { recipe =>
        $.modState(s => s.copy(recipe = recipe, isLoaded = true))
      }
    }

    def render(p: Props, s: State): VdomTagOf[Div] = {
      <.div(
        if(!s.isLoaded)
          Loading()
        else
          <.div(
            ^.className := "d-flex flex-column align-items-center justify-content",
            <.div(
              ^.className := "text-center m-5",
              <.h2(
                s"${s.recipe.name}"
              )
            ),
            <.div(
              ^.className := "recipe-details bg-light d-flex flex-column align-items-center text-center",
              <.div(
                ^.className := "container",
                <.img(
                  ^.alt := "recipe",
                  ^.className := "d-block mx-auto",
                  ^.src := s"client/src/main/resources/static/assets/img/recipe${p.imgNumber}.jpg"
                )
              ),
              <.hr(
                ^.className := "w-100"
              ),
              <.span(
                ^.className := "text-secondary",
                s.recipe.tags.mkString(" | ")
              ),
              <.hr(
                ^.className := "w-100"
              ),
              <.h3(
                ^.className := "w-100 p-3 bg-dark text-light",
                "The ingredients' list"
              ),
              <.ul(
                ^.className := "w-100 list-group list-group-flush text-left",
                s.recipe.ingredients.toTagMod{ ingredient =>
                  <.li(
                    ^.className := "w-100 list-group-item",
                    ingredient
                  )
                }
              ),
              <.h3(
                ^.className := "w-100 mt-4 p-3 bg-dark text-light",
                "Steps"
              ),
              <.ol(
                ^.className := "w-100 list-group list-group-flush text-left",
                s.recipe.instructions.toTagMod{ instruction =>
                  <.li(
                    ^.className := "w-100 list-group-item",
                    instruction
                  )
                }
              ),
            )
          )
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("RecipeDetails")
    .initialState(State(null))
    .renderBackend[Backend]
    .componentDidMount(_.backend.fetch)
    .build

  def apply(id: String, imgNumber: ImgNumber): VdomElement = component(Props(id, imgNumber))

}

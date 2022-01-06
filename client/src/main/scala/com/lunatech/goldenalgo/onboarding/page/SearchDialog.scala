package com.lunatech.goldenalgo.onboarding.page

import com.lunatech.goldenalgo.onboarding.api.RecipeApiClient.futureGet
import com.lunatech.goldenalgo.onboarding.components.Loading
import com.lunatech.goldenalgo.onboarding.css.Bootstrap.{formControl, formOutline, h4, m5, mb4, mxAuto, p5, px5, py1, textCenter}
import com.lunatech.goldenalgo.onboarding.css.GlobalStyle.darkBtn
import com.lunatech.goldenalgo.onboarding.features.recipes.components.RecipesList
import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.lunatech.goldenalgo.onboarding.router.AppRouter
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, ReactEventFromInput, ScalaComponent}
import org.scalajs.dom.html.Div
import scalacss.ScalaCssReact._

import scala.concurrent.ExecutionContext.Implicits.global

object SearchDialog {

  case class Props(ctl: RouterCtl[AppRouter.Page])

  case class State(
                    recipes: Seq[Recipe],
                    isLoaded: Boolean =  false,
                    recipeText: String = "",
                    ingredientText: String = "",
                    tagText: String = ""
                  )

  class Backend($: BackendScope[Props, State]) {

    def onRecipeInputChange(e: ReactEventFromInput): Callback = {
      val newValue = e.target.value
      $.modState(_.copy(recipeText = newValue))
    }

    def onIngredientInputChange(e: ReactEventFromInput): CallbackTo[Unit] = {
      val newValue = e.target.value
      $.modState(_.copy(ingredientText = newValue))
    }

    def onTagInputChange(e: ReactEventFromInput): CallbackTo[Unit] = {
      val newValue = e.target.value
      $.modState(_.copy(tagText = newValue))
    }

    def onSearchByRecipe(text: String): Callback =
      if(text.nonEmpty) search(s"search?term=$text")
      else Callback.empty

    def onSearchByIngredientAndTag(ingredientText: String, tagText: String): Callback = {
      if(ingredientText.nonEmpty && tagText.isEmpty) search(s"filter?ingredient=$ingredientText")
      else if(ingredientText.isEmpty && tagText.nonEmpty) search(s"filter?tag=$tagText")
      else if(ingredientText.nonEmpty && tagText.nonEmpty) search(s"filter?ingredient=$ingredientText&tag=$tagText")
      else Callback.empty
    }

    def search(url: String): Callback = Callback.future {
      futureGet[Seq[Recipe]](s"recipes/$url").map { recipes =>
        $.modState(s => s.copy(recipes = recipes, isLoaded = true))
      }
    }

    def render(p: Props, s: State): VdomTagOf[Div] = {
      <.div(
        <.div(
          textCenter, m5,
          <.h2(
            "Search for our golden recipes enjoy it!"
          ),
          <.form(
            p5, mxAuto,
            ^.maxWidth := "780px",
            <.p(
              h4, mb4,
              "Find a recipe a keyword"
            ),
            <.div(
              formOutline, mb4,
              <.input(
                formControl,
                ^.`type` := "text",
                ^.id := "recipe-keyword",
                ^.placeholder := "Search recipes",
                ^.onChange ==> onRecipeInputChange
              )
            ),
            <.button(
              darkBtn,
              ^.`type` := "button",
              ^.onClick --> onSearchByRecipe(s.recipeText),
              "Search"
            )
          ),
          <.form(
            mxAuto, px5, py1,
            ^.maxWidth := "780px",
            <.p(
             h4, mb4,
              "Or find recipes by ingredients and tags"
            ),
            <.div(
              formOutline, mb4,
              <.input(
                formControl,
                ^.`type` := "text",
                ^.id := "ingredient-keyword",
                ^.placeholder := s"Ingredient keyword",
                ^.onChange ==> onIngredientInputChange
              )
            ),
            <.div(
              formOutline, mb4,
              <.input(
                formControl,
                ^.`type` := "text",
                ^.id := "tag-keyword",
                ^.placeholder := "Tag keyword",
                ^.onChange ==> onTagInputChange
              )
            ),
            <.button(
              darkBtn,
              ^.`type` := "button",
              "Search",
              ^.onClick --> onSearchByIngredientAndTag(s.ingredientText, s.tagText)
            )
          )
        ),
        if(s.isLoaded)
          RecipesList(RecipesList.Props(p.ctl, s.recipes))
        else if(s.isLoaded && s.recipes.isEmpty)
          <.p(
            ^.className := "text-center h4 mb-4",
            "No recipes to show"
          )
        else Loading()
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("RecipeDetails")
    .initialState(State(Seq.empty))
    .renderBackend[Backend]
    .build

  def apply(ctl: RouterCtl[AppRouter.Page]): VdomElement = component(Props(ctl))

}

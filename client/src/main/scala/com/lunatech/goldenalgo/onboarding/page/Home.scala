package com.lunatech.goldenalgo.onboarding.page

import com.lunatech.goldenalgo.onboarding.api.RecipeApiClient.futureGet
import com.lunatech.goldenalgo.onboarding.components.Loading
import com.lunatech.goldenalgo.onboarding.features.recipes.components.RecipesList
import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.lunatech.goldenalgo.onboarding.router.AppRouter
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{AsyncCallback, BackendScope, Callback, CallbackTo, ScalaComponent}
import org.scalajs.dom.html.Div

import scala.concurrent.ExecutionContext.Implicits.global

object Home {

  case class Props(ctl: RouterCtl[AppRouter.Page])

  case class State(recipes: Seq[Recipe], isLoaded: Boolean =  false)

  class Backend($: BackendScope[Props, State]) {

    def fetch: Callback = Callback.future {
      futureGet[List[Recipe]]("recipes").map { recipes =>
        $.modState(s => s.copy(recipes = recipes, isLoaded = true))
      }
    }

    def render(p: Props, s: State): VdomTagOf[Div] = {
      <.div(
        <.div(
          ^.className := "text-center m-5",
          <.h2(
            "All our golden recipes for your pleasure. Enjoy it!"
          )
        ),
        if(s.isLoaded) RecipesList(RecipesList.Props(p.ctl, s.recipes)) else Loading()
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("HomePage")
    .initialState(State(Seq.empty))
    .renderBackend[Backend]
    .componentDidMount(_.backend.fetch)
    .build

  def apply(ctl: RouterCtl[AppRouter.Page]): VdomElement = component(Props(ctl))
}

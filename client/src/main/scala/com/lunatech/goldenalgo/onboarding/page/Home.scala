package com.lunatech.goldenalgo.onboarding.page

import com.lunatech.goldenalgo.onboarding.components.Loading
import com.lunatech.goldenalgo.onboarding.diode._
import com.lunatech.goldenalgo.onboarding.features.recipes.components.RecipesList
import com.lunatech.goldenalgo.onboarding.router.AppRouter
import diode.react.ModelProxy
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import org.scalajs.dom.html.Div

object Home {

  case class Props(proxy: ModelProxy[RecipesModel], ctl: RouterCtl[AppRouter.Page])

  class Backend($: BackendScope[Props, Unit]) {

    def fetch: Callback = Callback {
      AppCircuit.dispatch(LoadRecipes())
    }

    def render(p: Props): VdomTagOf[Div] = {
      val proxy = p.proxy()
      <.div(
        <.div(
          ^.className := "text-center m-5",
          <.h2(
            "All our golden recipes for your pleasure. Enjoy it!"
          )
        ),
        if(proxy.isRecipesLoading) Loading() else RecipesList(RecipesList.Props(p.ctl, proxy.recipes))
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("HomePage")
    .renderBackend[Backend]
    .componentDidMount(_.backend.fetch)
    .build

  def apply(proxy: ModelProxy[RecipesModel], ctl: RouterCtl[AppRouter.Page]): VdomElement = component(Props(proxy, ctl))
}

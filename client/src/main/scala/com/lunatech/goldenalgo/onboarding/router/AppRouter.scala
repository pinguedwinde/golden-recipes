package com.lunatech.goldenalgo.onboarding.router

import com.lunatech.goldenalgo.onboarding.components.{Footer, Header}
import com.lunatech.goldenalgo.onboarding.diode.AppCircuit
import com.lunatech.goldenalgo.onboarding.features.recipes.components.RecipeElement.ImgNumber
import com.lunatech.goldenalgo.onboarding.model.Recipe.RecipeId
import com.lunatech.goldenalgo.onboarding.page.{AddRecipe, Home, RecipeDetails, SearchDialog}
import japgolly.scalajs.react.extra.router.{BaseUrl, Resolution, Router, RouterConfig, RouterConfigDsl, RouterCtl, SetRouteVia}
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.Div

object AppRouter {

  sealed trait Page

  case object HomePage extends Page
  case object RecipesPage extends Page
  case object SearchPage extends Page
  case object AddRecipePage extends Page
  case class RecipeDetailsPage(id: String, imgNumber: ImgNumber) extends Page

  case object NotFound extends Page

  private val connection = AppCircuit.connect(_.recipesModel)
  private val selectedConnection = AppCircuit.connect(_.recipesModel.selectedRecipe)

  val config: RouterConfig[Page] = RouterConfigDsl[Page].buildConfig { dsl =>

    import dsl._

    val id = string(".*")

    val recipeDetailsRoute = ("#recipes" / "details" / id / int).caseClass[RecipeDetailsPage]

    (emptyRule
      | staticRoute(root, HomePage) ~> renderR(renderHome)
      | staticRoute("#recipes", RecipesPage) ~> renderR(renderHome)
      | staticRoute("#recipes/search", SearchPage) ~> renderR(ctl => SearchDialog(ctl))
      | staticRoute("#recipes/add", AddRecipePage) ~> renderR(ctl => AddRecipe(ctl))
      | dynamicRouteCT[RecipeDetailsPage](recipeDetailsRoute) ~> (o => renderR(_ => renderRecipeDetails(o.id, o.imgNumber)))
      | staticRoute("#notfound", NotFound) ~> render(<.h2("NOT FOUND"))
      ).notFound { _ =>
      redirectToPage(NotFound)(SetRouteVia.HistoryReplace)
    }.logToConsole
      .renderWith(layout)
  }


  def layout(ctl: RouterCtl[Page], r: Resolution[Page]): VdomTagOf[Div] =
    <.div(
      Header(Header.Props(ctl)),
      r.render(),
      Footer()
    )

  private def renderHome(ctl: RouterCtl[Page]) =
    connection(proxy => Home(proxy, ctl))

  private def renderRecipeDetails(id: RecipeId, imgNumber: ImgNumber) =
    selectedConnection(proxy => RecipeDetails(proxy, id, imgNumber))

  def router: Router[Page] =
    Router(BaseUrl.until_#, config)

}

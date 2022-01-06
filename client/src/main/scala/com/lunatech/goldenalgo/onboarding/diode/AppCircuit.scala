package com.lunatech.goldenalgo.onboarding.diode

import com.lunatech.goldenalgo.onboarding.api.RecipeApiClient.futureGet
import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.lunatech.goldenalgo.onboarding.model.Recipe.{RecipeId, Recipes}
import diode.AnyAction.aType
import diode.react.ReactConnector
import diode._

import scala.concurrent.ExecutionContext.Implicits.global

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {

  override protected def initialModel: AppModel = AppModel(
    RecipesModel(
      recipes = Seq.empty[Recipe],
      selectedRecipe = None,
      isRecipesLoading = false,
      isSelectedRecipeLoading = false
    )
  )

  override protected def actionHandler: AppCircuit.HandlerFunction =
    composeHandlers(new RecipesHandler(zoomTo(_.recipesModel)))
}

class RecipesHandler[M](modelRW: ModelRW[M, RecipesModel]) extends ActionHandler(modelRW) {
  override protected def handle: PartialFunction[Any, ActionResult[M]] = {
    case LoadRecipes() => effectOnly(loadRecipesEffect())
    case RefreshSelectedRecipe(id) => effectOnly(refreshSelectedRecipeEffect(id))
    case GetRecipes(recipes) => updated(value.copy(recipes = recipes))
    case SelectRecipe(selectedRecipe) => updated(value.copy(selectedRecipe = selectedRecipe))
    case SetRecipesLoadingState() => updated(value.copy(isRecipesLoading = true))
    case UnSetRecipesLoadingState() => updated(value.copy(isRecipesLoading = false))
    case SetSelectedRecipeLoadingState() => updated(value.copy(isSelectedRecipeLoading = true))
    case UnSetSelectedRecipeLoadingState() => updated(value.copy(isSelectedRecipeLoading = false))
  }

  private def loadRecipesEffect() = {
    if(modelRW.value.recipes.isEmpty)
      Effect.action(SetRecipesLoadingState()) +
        (Effect (futureGet[Recipes]("recipes").map(GetRecipes)) >> Effect.action(UnSetRecipesLoadingState()))
    else Effect.action(NoAction)
  }

  private def refreshSelectedRecipeEffect(id: RecipeId) = {
    if(modelRW.value.selectedRecipe.isEmpty)
      Effect.action(SetSelectedRecipeLoadingState()) +
        (Effect (futureGet[Recipe](s"recipes/$id").map(r => SelectRecipe(Some(r)))) >> Effect.action(UnSetSelectedRecipeLoadingState()))
    else Effect.action(NoAction)
  }
}

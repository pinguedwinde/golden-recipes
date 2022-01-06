package com.lunatech.goldenalgo.onboarding.diode

import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.lunatech.goldenalgo.onboarding.model.Recipe.{RecipeId, Recipes}
import diode.Action


case class RecipesModel(
                         recipes: Recipes,
                         selectedRecipe: Option[Recipe],
                         isRecipesLoading: Boolean,
                         isSelectedRecipeLoading: Boolean
                       )

case class AppModel(recipesModel: RecipesModel)

case class LoadRecipes() extends Action
case class RefreshSelectedRecipe(id: RecipeId) extends Action
case class GetRecipes(recipes: Recipes) extends Action
case class SelectRecipe(recipe: Option[Recipe] ) extends Action
case class SetRecipesLoadingState() extends Action
case class UnSetRecipesLoadingState() extends Action
case class SetSelectedRecipeLoadingState() extends Action
case class UnSetSelectedRecipeLoadingState() extends Action
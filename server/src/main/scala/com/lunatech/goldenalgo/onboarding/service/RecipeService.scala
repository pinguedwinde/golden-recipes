package com.lunatech.goldenalgo.onboarding.service

import com.lunatech.goldenalgo.onboarding.es.EsClientManager.RecipeTagsField
import com.lunatech.goldenalgo.onboarding.model.Recipe.RecipeId
import com.lunatech.goldenalgo.onboarding.model.{Recipe, RecipeDto}

import scala.concurrent.{ExecutionContext, Future}

object RecipeService {

  import com.lunatech.goldenalgo.onboarding.repository.RecipeRepository._
  import com.lunatech.goldenalgo.onboarding.es.EsClientManager.RecipeIngredientsField

  implicit val ec: ExecutionContext = ExecutionContext.global

  case class CreatedRecipesResponse(id: String, msg: String)

  def findAllRecipes(): Future[Seq[Recipe]] = findAll()

  def findRecipeById(id: String): Future[Option[Recipe]] = findById(id)

  def findRecipeByName(name: String): Future[Seq[Recipe]] = findByName(name)

  def insertRecipe(recipeData: RecipeDto): Future[Recipe] = insert(recipeData)

  def insertTagsIntoRecipe(recipeId: String, tags: Set[String]): Future[RecipeId] = insertTags(recipeId, tags)

  def updateRecipeById(id: String, recipeDto: RecipeDto): Future[RecipeId] = updateById(id, recipeDto)

  def searchRecipesByIngredientAndTag(ingredientTerm: Option[String], tagTerm: Option[String]): Future[Seq[Recipe]] = {
    if (ingredientTerm.isEmpty && tagTerm.isEmpty) findAll()
    else if (ingredientTerm.isDefined && tagTerm.isEmpty) {
      findByField(RecipeIngredientsField, ingredientTerm.get)
    } else if (ingredientTerm.isEmpty && tagTerm.isDefined) {
      findByField(RecipeTagsField, tagTerm.get)
    } else if (ingredientTerm.isDefined && tagTerm.isDefined) {
      findByIngredientAndTag(ingredientTerm.get, tagTerm.get)
    } else {
      findAll()
    }
  }

  def searchRecipesByAnyWord(textOption: Option[String]): Future[Seq[Recipe]] =
    textOption match {
      case Some(word) => findByAnyWord(word)
      case None => findAll()
    }

  def deleteRecipeById(id: String): Future[RecipeId] = deleteById(id)

}

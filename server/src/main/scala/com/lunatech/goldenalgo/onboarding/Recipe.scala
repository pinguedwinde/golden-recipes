package com.lunatech.goldenalgo.onboarding

import io.circe._
import io.circe.generic.semiauto._

case class Recipe(id: String, name: String, ingredients: Seq[String], instructions: Seq[String], tags: Set[String])

object Recipe {
  implicit val codec: Codec[Recipe] = deriveCodec[Recipe]

  def apply(id: String, recipeData: RecipeData): Recipe = new Recipe(
    id, recipeData.name, recipeData.ingredients, recipeData.instructions, recipeData.tags
  )

  def apply(id: String, recipe: Recipe): Recipe = new Recipe(
    id, recipe.name, recipe.ingredients, recipe.instructions, recipe.tags
  )
}

case class RecipeData(name: String, ingredients: Seq[String], instructions: Seq[String], tags: Set[String])

object RecipeData {
  implicit val codec: Codec[RecipeData] = deriveCodec[RecipeData]
}
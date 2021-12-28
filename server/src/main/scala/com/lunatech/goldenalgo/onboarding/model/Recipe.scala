package com.lunatech.goldenalgo.onboarding.model

import io.circe._
import io.circe.generic.semiauto._

case class Recipe(id: String, name: String, ingredients: Seq[String], instructions: Seq[String], tags: Set[String])

object Recipe {
  implicit val codec: Codec[Recipe] = deriveCodec[Recipe]

  type RecipeId = String

  def apply(id: String, recipeDto: RecipeDto): Recipe = new Recipe(
    id, recipeDto.name, recipeDto.ingredients, recipeDto.instructions, recipeDto.tags
  )

  def apply(id: String, recipe: Recipe): Recipe = new Recipe(
    id, recipe.name, recipe.ingredients, recipe.instructions, recipe.tags
  )
}

case class RecipeDto(name: String, ingredients: Seq[String], instructions: Seq[String], tags: Set[String])

object RecipeDto {
  implicit val codec: Codec[RecipeDto] = deriveCodec[RecipeDto]
}
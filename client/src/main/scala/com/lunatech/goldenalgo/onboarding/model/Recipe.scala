package com.lunatech.goldenalgo.onboarding.model

import com.lunatech.goldenalgo.onboarding.model.Recipe.RecipeId
import io.circe._
import io.circe.generic.semiauto._

case class Recipe(
                   id: RecipeId,
                   name: String,
                   ingredients: Seq[String],
                   instructions: Seq[String],
                   tags: Seq[String]
                 )

object Recipe {
  implicit val codec: Codec[Recipe] = deriveCodec[Recipe]
  type RecipeId = String
}

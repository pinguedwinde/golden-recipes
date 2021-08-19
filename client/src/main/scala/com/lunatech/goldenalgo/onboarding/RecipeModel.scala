package com.lunatech.goldenalgo.onboarding

import io.circe._
import io.circe.generic.semiauto._

case class RecipeModel(id: String, name: String, ingredients: Seq[String], instructions: Seq[String])

object RecipeModel {
  implicit val codec: Codec[RecipeModel] = deriveCodec[RecipeModel]
}


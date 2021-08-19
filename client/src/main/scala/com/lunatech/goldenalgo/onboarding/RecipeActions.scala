package com.lunatech.goldenalgo.onboarding

import diode._

object RecipeActions {

  case class RootModel(recipes: Set[RecipeModel])
  
  case class Add(recipe: RecipeModel) extends Action
  case object Reset extends Action

}

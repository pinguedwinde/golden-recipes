package com.lunatech.goldenalgo.onboarding.service

import com.lunatech.goldenalgo.onboarding.model.Recipe

import scala.io.Source

object RecipeDataLoader {

  import io.circe.parser._

  def readFromFile(filename: String): List[Recipe] = {
    val recipesRawJson = Source.fromResource(filename).mkString
    val decodeResult = decode[List[Recipe]](recipesRawJson)

    decodeResult match {
      case Right(recipes) => recipes
      case Left(exception) =>
        throw new IllegalArgumentException(
          s"Could not load the file $filename : an error occurred when parsing. \n Exception : ${exception.getMessage}"
        )
    }
  }

}

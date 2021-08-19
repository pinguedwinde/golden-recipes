package com.lunatech.goldenalgo.onboarding

import org.scalajs.dom.ext.Ajax
import io.circe.parser._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }
import diode._

object Backend {

  def fetchRecipes(): Unit = Ajax.get("/recipes").onComplete {
    case Success(xhr) =>
      decode[List[RecipeModel]](xhr.responseText).map { recipes =>
        recipes.map { recipe =>
          dispatch(RecipeActions.Add(recipe))
        }
      }
    case Failure(t) => println("An error has occurred: " + t.getMessage)

  }
}

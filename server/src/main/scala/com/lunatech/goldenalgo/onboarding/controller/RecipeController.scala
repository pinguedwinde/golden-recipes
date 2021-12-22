package com.lunatech.goldenalgo.onboarding.controller

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.lunatech.goldenalgo.onboarding.model.RecipeData
import com.lunatech.goldenalgo.onboarding.service.RecipeService._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.syntax.EncoderOps

import scala.concurrent.ExecutionContext

class RecipeController()(implicit val ec: ExecutionContext) extends FailFastCirceSupport{

  import io.circe.generic.auto._

  case class ResourceNotFound(
                               msg: String,
                               reason: String = NotFound.reason,
                               code: Int = NotFound.intValue,
                               timestamp: Long = System.currentTimeMillis()
                             )

  /**
   * Retrieve all recipes               GET -> /recipes
   * Create a recipe                    POST -> /recipes
   * Retrieve a recipe by $RECIPE_ID    GET -> recipes/$RECIPE_ID
   * Update a recipe                    PUT -> /recipes/$RECIPE_ID
   * Delete a recipe                    DELETE -> /recipes/$RECIPE_ID
   * Tag a recipe                       POST -> /recipes/$RECIPE_ID/tags
   * Filter recipes by ingredient and tag  GET -> /recipes/filter?ingredient=ingredient1&tag=tag1
   * Search for a word in the recipes   GET ->  /recipes/search?term=term
   */

  val routes: Route =
    (pathPrefix("recipes") & extractLog){ log =>
      concat(
        pathEndOrSingleSlash{
          concat(
            get{
              log.info("Accepted GET -> /recipes")
              complete(HttpEntity(ContentTypes.`application/json`, fetchAllRecipes().asJson.noSpaces))
            },
            post{
              entity(as[RecipeData]){ recipe =>
                log.info("Accepted POST -> /recipes")
                complete((Created, saveRecipe(recipe)))
              }
            }
          )
        },
        (path("search") & parameters(Symbol("term").?)){ termOption =>
            log.info(s"Accepted /recipes/search?term=$termOption")
            complete(searchRecipesByAnyWord(termOption))
        },
        (path("filter") & parameters(Symbol("ingredient").?, Symbol("tag").?)){ (ingredientOption, tagOption) =>
            log.info(s"Accepted /recipes/filter?ingredient=$ingredientOption&tag=$tagOption")
            complete(searchRecipesByIngredientAndTag(ingredientOption, tagOption))
        },
        path(Segment){ id =>
          concat(
            pathEndOrSingleSlash{
              concat(
                get{
                  log.info(s"Accepted GET -> recipes/$id")
                  findRecipeById(id)
                    .fold{
                      complete(NotFound,
                        ResourceNotFound(s"Recipe with id $id not found")
                      )
                    }(recipe => complete(OK, recipe))
                },
                put{
                  entity(as[RecipeData]){ recipeData =>
                    log.info(s"Accepted PUT -> recipes/$id")
                    updateRecipe(id, recipeData)
                      .fold {
                        complete(NotFound,
                          ResourceNotFound(s"Recipe with id $id not found. Cannot update this recipe")
                        )
                      }(complete(OK, _))
                  }
                },
                delete{
                  log.info(s"Accepted DELETE -> recipes/$id")
                  complete(OK, deleteRecipe(id))
                }
              )
            },
            path("tags"){
              post{
                log.info(s"Accepted GET -> recipes/$id/tags")
                entity(as[Set[String]]){ tags =>
                  tagRecipe(id, tags)
                    .fold {
                      complete(NotFound,
                        ResourceNotFound(s"Recipe with id $id not found. Cannot tag this recipe")
                      )
                    }(complete(OK, _))
                }
              }
            }
          )
        }
      )
    }

}

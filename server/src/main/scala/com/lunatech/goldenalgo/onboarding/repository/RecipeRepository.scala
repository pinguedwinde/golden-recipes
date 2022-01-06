package com.lunatech.goldenalgo.onboarding.repository

import com.lunatech.goldenalgo.onboarding.es.EsClientManager.{RecipeIndex, RecipeIngredientsField, RecipeTagsField}
import com.lunatech.goldenalgo.onboarding.model.Recipe.RecipeId
import com.lunatech.goldenalgo.onboarding.model.{Recipe, RecipeDto}
import com.sksamuel.elastic4s.ElasticApi.{boolQuery, get, search}
import com.sksamuel.elastic4s.ElasticDsl.{GetHandler, SearchHandler, _}
import com.sksamuel.elastic4s.requests.delete.DeleteByIdRequest
import com.sksamuel.elastic4s.requests.indexes.IndexResponse
import com.sksamuel.elastic4s.requests.searches.SearchResponse
import com.sksamuel.elastic4s.requests.update.{UpdateRequest, UpdateResponse}
import com.sksamuel.elastic4s.{Hit, HitReader, Response}
import io.circe.jawn.decode
import io.circe.syntax.EncoderOps

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.Try

trait RecipeRepository {

  /**
   * In this repository we use id for both ES object _id and the recipeID
   * They are mapped to be the same
   */

  def findAll(): Future[Seq[Recipe]]
  def findById(id: String): Future[Option[Recipe]]
  def findByName(name: String): Future[Seq[Recipe]]
  def findByField(fieldName: String, fieldValue: String): Future[Seq[Recipe]]
  def findByIngredientAndTag(ingredient: String, tag: String): Future[Seq[Recipe]]
  def findByAnyWord(word: String): Future[Seq[Recipe]]
  def insert(recipeDto: RecipeDto): Future[Recipe]
  def insertTags(id: String, tags: Set[String]): Future[RecipeId]
  def updateById(id: String, recipeData: RecipeDto): Future[RecipeId]
  def deleteById(id: String): Future[String]
}

object RecipeRepository extends RecipeRepository {

  import com.lunatech.goldenalgo.onboarding.es.EsClientManager.esClient

  implicit object RecipeHitReader extends HitReader[Recipe] {
    override def read(hit: Hit): Try[Recipe] =
      decode[Recipe](hit.sourceAsString).toTry
  }

  override def findAll(): Future[Seq[Recipe]] =
    mapToFutureOfRecipesArray({
          esClient.execute {
            search(RecipeIndex)
          }
        })

  override def findById(id: String): Future[Option[Recipe]] =
    esClient.execute {
      get(RecipeIndex, id)
    }.map(_.result)
      .map { getResponse =>
        getResponse.toOpt[Recipe]
      }

  override def findByName(name: String): Future[Seq[Recipe]] =
    mapToFutureOfRecipesArray({
          esClient.execute {
            search(RecipeIndex).query(name)
          }
        })

  override def findByField(fieldName: String, fieldValue: String): Future[Seq[Recipe]] =
    mapToFutureOfRecipesArray({
          esClient.execute {
            search(RecipeIndex).matchQuery(fieldName, fieldValue)
          }
        })

  override def findByIngredientAndTag(ingredient: String, tag: String): Future[Seq[Recipe]] =
    mapToFutureOfRecipesArray({
          esClient.execute {
            search(RecipeIndex) query {
              boolQuery() must (
                termQuery(RecipeIngredientsField, ingredient),
                termQuery(RecipeTagsField, tag)
              )
            }
          }
        })

  override def findByAnyWord(word: String): Future[Seq[Recipe]] =
    mapToFutureOfRecipesArray({
          esClient.execute {
            search(RecipeIndex) query multiMatchQuery(word)
          }
        })

  override def insert(recipeDto: RecipeDto): Future[Recipe] = {
    val id = UUID.randomUUID().toString
    val recipe = Recipe(id, recipeDto)
    esClient.execute {
      indexInto(RecipeIndex) withId id doc recipe.asJson.noSpaces
    }.map(_.result)
      .flatMap {
        case IndexResponse(_, _, _, _, _, _, "created", _, _) => Future.successful(recipe)
        case IndexResponse(_, _, _, _, _, _, "updated", _, _) => Future.successful(recipe)
        case other: IndexResponse => Future.failed(new IllegalStateException(s"Could not persist Recipe: $other"))
      }
  }

  override def insertTags(id: String, tags: Set[String]): Future[RecipeId] =
    esClient.execute {
      UpdateRequest(RecipeIndex, id) script {
        script {
          """
            |ctx._source.tags.addAll(params.newTags)
            |""".stripMargin
        } params {
          "newTags" -> tags.toList
        }
      }
    }.map(_.result)
      .map {updateResponse =>
        updateResponse.id
      }

   override def updateById(id: String, recipeData: RecipeDto): Future[RecipeId] =
    esClient.execute {
      UpdateRequest(RecipeIndex, id) doc Recipe(id, recipeData).asJson.noSpaces
    }.map(_.result)
      .map {updateResponse =>
        updateResponse.id
      }

  override def deleteById(id: String): Future[RecipeId] =
    esClient.execute {
      DeleteByIdRequest(RecipeIndex, id)
    }.map(_.result)
      .map { deleteResponse =>
        deleteResponse.id
      }

  private def mapToFutureOfRecipesArray(responseFuture: Future[Response[SearchResponse]]) : Future[Seq[Recipe]] =
    responseFuture.map(_.result)
      .map{ searchResponse =>
        searchResponse.hits.hits.map(_.to[Recipe])
      }
}



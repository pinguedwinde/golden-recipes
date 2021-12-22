package com.lunatech.goldenalgo.onboarding.repository

import akka.http.scaladsl.model.headers.LinkParams.`type`
import com.lunatech.goldenalgo.onboarding.es.EsClientManager.{RECIPE_INDEX, RECIPE_INGREDIENTS_FIELD, RECIPE_TAGS_FIELD}
import com.lunatech.goldenalgo.onboarding.model.{Recipe, RecipeData}
import com.sksamuel.elastic4s.ElasticApi.{boolQuery, get, search}
import com.sksamuel.elastic4s.ElasticDsl.{GetHandler, SearchHandler, must, _}
import com.sksamuel.elastic4s.requests.delete.DeleteByIdRequest
import com.sksamuel.elastic4s.requests.indexes.IndexResponse
import com.sksamuel.elastic4s.requests.update.UpdateRequest
import com.sksamuel.elastic4s.{Hit, HitReader}
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

  def findAll(): Future[Array[Recipe]]
  def findById(id: String): Future[Option[Recipe]]
  def findByName(name: String): Future[Array[Recipe]]
  def findByField(fieldName: String, fieldValue: String): Future[Array[Recipe]]
  def findByIngredientAndTag(ingredient: String, tag: String): Future[Array[Recipe]]
  def findByAnyWord(word: String): Future[Array[Recipe]]
  def insert(recipe: Recipe): Future[Recipe]
  //def insertTags(recipeId: String, tags: Set[String]): Future[Recipe]
  def updateById(id: String, recipeData: RecipeData): Future[String]
  def deleteById(id: String): Future[String]
}

object RecipeRepository extends RecipeRepository {

  import com.lunatech.goldenalgo.onboarding.es.EsClientManager.esClient

  implicit object RecipeHitReader extends HitReader[Recipe] {
    override def read(hit: Hit): Try[Recipe] =
      decode[Recipe](hit.sourceAsString).toTry
  }

  override def findAll(): Future[Array[Recipe]] =
    esClient.execute {
      search(RECIPE_INDEX)
    }.map(_.result)
      .map { searchResponse =>
        searchResponse.hits.hits.map(_.to[Recipe])
      }

  override def findById(id: String): Future[Option[Recipe]] = {
    esClient.execute {
      get(RECIPE_INDEX, id)
    }.map(_.result)
      .map { getResponse =>
        getResponse.toOpt[Recipe]
      }
  }

  override def findByName(name: String): Future[Array[Recipe]] =
    esClient.execute {
      search(RECIPE_INDEX).query(name)
    }.map(_.result)
      .map{ searchResponse =>
        searchResponse.hits.hits.map(_.to[Recipe])
    }

  override def findByField(fieldName: String, fieldValue: String): Future[Array[Recipe]] =
    esClient.execute {
      search(RECIPE_INDEX).matchQuery(fieldName, fieldValue)
    }.map(_.result)
      .map{ searchResponse =>
        searchResponse.hits.hits.map(_.to[Recipe])
      }

  override def findByIngredientAndTag(ingredient: String, tag: String): Future[Array[Recipe]] =

    esClient.execute {
      search(RECIPE_INDEX) query {
        boolQuery must (
          termQuery(RECIPE_INGREDIENTS_FIELD, ingredient),
          termQuery(RECIPE_TAGS_FIELD, tag)
        )
      }
    }.map(_.result)
      .map{ searchResponse =>
        searchResponse.hits.hits.map(_.to[Recipe])
      }

  override def findByAnyWord(word: String): Future[Array[Recipe]] =
    esClient.execute {
      search(RECIPE_INDEX) query multiMatchQuery(word)
    }.map(_.result)
      .map{ searchResponse =>
        searchResponse.hits.hits.map(_.to[Recipe])
      }

  override def insert(recipe: Recipe): Future[Recipe] = {
    val id = UUID.randomUUID().toString
    val recipeWithId = Recipe(id, recipe)
    esClient.execute {
      indexInto(RECIPE_INDEX) withId id doc recipeWithId.asJson.noSpaces
    }.map(_.result)
      .flatMap {
        case IndexResponse(_, _, _, _, _, _, "created", _, _) => Future.successful(recipe)
        case IndexResponse(_, _, _, _, _, _, "updated", _, _) => Future.successful(recipe)
        case other: IndexResponse => Future.failed(new IllegalStateException(s"Could not persist Recipe: $other"))
      }
  }

  //override def insertTags(recipeId: String, tags: Set[String]): Future[Recipe] = ???

   override def updateById(id: String, recipeData: RecipeData): Future[String] =
    esClient.execute {
      UpdateRequest(RECIPE_INDEX, id) doc Recipe(id, recipeData).asJson.noSpaces
    }.map(_.result)
      .map {updateResponse =>
        updateResponse.id
      }

  override def deleteById(id: String): Future[String] =
    esClient.execute {
      DeleteByIdRequest(RECIPE_INDEX, id)
    }.map(_.result)
      .map { deleteResponse =>
        deleteResponse.id
      }
}



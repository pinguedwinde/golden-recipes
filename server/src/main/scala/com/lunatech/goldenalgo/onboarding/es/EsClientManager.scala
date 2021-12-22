package com.lunatech.goldenalgo.onboarding.es

import akka.actor.ActorSystem
import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.sksamuel.elastic4s.ElasticApi.{RichFuture, bulk, createIndex, indexExists, indexInto}
import com.sksamuel.elastic4s.ElasticDsl.{BulkHandler, CreateIndexHandler, IndexExistsHandler}
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties, Response}
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.bulk.BulkResponse
import io.circe.syntax.EncoderOps

import scala.concurrent.{ExecutionContext, Future}

object EsClientManager {

  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher

  val props: ElasticProperties = ElasticProperties("http://localhost:9200")
  val esClient: ElasticClient = ElasticClient(JavaClient(props))

  val RECIPE_INDEX = "recipes"
  val RECIPE_NAME_FIELD = "name"
  val RECIPE_INSTRUCTIONS_FIELD = "instructions"
  val RECIPE_INGREDIENTS_FIELD = "ingredients"
  val RECIPE_TAGS_FIELD = "tags"
  val RECIPE_PICTURE_FIELD = "picture"

  def doesIndexExist(name: String): Boolean =
    esClient.execute {
      indexExists(name)
    }.await
      .result
      .isExists

  def createIndexIfNotExists(name: String): Unit = {
    if (!doesIndexExist(name))
      esClient.execute {
        createIndex(name)
      }.await
  }

  /**
   * The ES object _id is mapped to the recipeID
   * @param recipes : the list of recipes data to bulk index
   * @return
   */
  def indexBulkFromRecipesList(recipes: List[Recipe]): Future[Response[BulkResponse]] = {
    val requests = recipes.map {
      recipe => indexInto(RECIPE_INDEX) withId recipe.id  doc recipe.asJson.noSpaces
    }
    esClient.execute {
      bulk(requests)
    }
  }

}

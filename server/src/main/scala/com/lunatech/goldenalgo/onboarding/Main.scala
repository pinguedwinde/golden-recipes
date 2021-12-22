/*
 * Copyright Audi Electronics Venture GmbH 2019
 */

package com.lunatech.goldenalgo.onboarding

import akka.actor.ActorSystem
import com.lunatech.goldenalgo.onboarding.controller.RecipeController
import com.lunatech.goldenalgo.onboarding.es.EsClientManager
import com.lunatech.goldenalgo.onboarding.repository.RecipeRepository
import com.lunatech.goldenalgo.onboarding.service.RecipeManager

import scala.concurrent.ExecutionContext

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem  = ActorSystem("main-system")
    implicit val ec: ExecutionContext = system.dispatcher


    val controller = new RecipeController()

    val recipesFile = "recipes.json"

    EsClientManager.indexBulkFromRecipesList {
      RecipeManager.readFromFile(recipesFile)
    }

    new WebServer(controller)
      .bind()
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }
}

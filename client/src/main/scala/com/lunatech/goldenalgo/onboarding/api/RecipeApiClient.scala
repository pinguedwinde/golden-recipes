package com.lunatech.goldenalgo.onboarding.api

import io.circe.Decoder
import io.circe.parser.decode
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.{AjaxException, Ajax => FutureAjax}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure

trait Api {
  val baseUrl = "http://localhost:8080"

  def futureGet[Response](url: String)(implicit decoder: Decoder[Response], ec: ExecutionContext): Future[Response] =
    FutureAjax
      .get(
        s"$baseUrl/$url",
      )
      .flatMap(onCompleteFuture)
      .andThen { case Failure(exception: AjaxException) =>
        println(exception.getMessage)
      }


  protected def onCompleteFuture[Response](implicit decoder: Decoder[Response]): XMLHttpRequest => Future[Response] =
    res => Future.fromTry(decode[Response](res.responseText).toTry)

}

object RecipeApiClient extends Api {

}

package com.lunatech.goldenalgo.onboarding.api

import io.circe.{Decoder, Encoder}
import io.circe.parser._
import io.circe.syntax._
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.{AjaxException, Ajax => FutureAjax}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure

trait Api {
  val baseUrl = "http://localhost:8080"
  protected val ContentType           = "Content-Type"
  protected val ApplicationJson       = "application/json"
  protected val ApplicationXml        = "application/xml"
  protected val JsonContentTypeHeader = Map(ContentType -> ApplicationJson)

  def futureGet[Response](url: String)(implicit decoder: Decoder[Response], ec: ExecutionContext): Future[Response] =
    FutureAjax
      .get(
        s"$baseUrl/$url",
        headers = JsonContentTypeHeader
      )
      .flatMap(onCompleteFuture)
      .andThen { case Failure(exception: AjaxException) =>
        println(exception.getMessage)
      }

  def futurePost[Content, Response](url: String)( content: Content )
                                   (implicit encoder: Encoder[Content],
                                    decoder: Decoder[Response],
                                    ec: ExecutionContext): Future[Response] =
    FutureAjax
      .post(
        s"$baseUrl/$url",
        content.asJson.noSpaces,
        headers = JsonContentTypeHeader
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

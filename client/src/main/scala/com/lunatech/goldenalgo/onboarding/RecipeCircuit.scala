package com.lunatech.goldenalgo.onboarding

import diode._
import diode.react.{ ReactConnectProxy, ReactConnector }
import diode.ActionResult.ModelUpdate
import japgolly.scalajs.react.Callback

object RecipeCircuit extends Circuit[RootModel] with ReactConnector[RootModel] {

  val initialModel = RootModel(Set[RecipeModel]())

  override val actionHandler: HandlerFunction =
    (model, action) => action  match {
      case Reset => Some(ModelUpdate(initialModel))
      case Add(recipe) => Some(ModelUpdate(model.copy(recipes = model.recipes + recipe)))
      case _ => None
    }

  val connectRootModel: ReactConnectProxy[RootModel] = RecipeCircuit.connect(identity(_))

  def dispatchCB[A: ActionType](action: => A): Callback = Callback(dispatch(action))

}

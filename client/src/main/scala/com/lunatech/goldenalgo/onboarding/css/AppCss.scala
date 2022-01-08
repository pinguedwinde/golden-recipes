package com.lunatech.goldenalgo.onboarding.css

import com.lunatech.goldenalgo.onboarding.page.AddRecipe
import scalacss.DevDefaults._
import scalacss.internal.mutable.GlobalRegistry

object AppCss {

  def load(): Unit = {
    GlobalRegistry.register(
      GlobalStyle,
      Bootstrap,
      AddRecipe.Styles
    )
    GlobalRegistry.onRegistration(_.addToDocument())
  }

}

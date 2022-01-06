package com.lunatech.goldenalgo.onboarding.css

import scalacss.DevDefaults._
import scalacss.internal.mutable.GlobalRegistry

object AppCss {

  def load(): Unit = {
    GlobalRegistry.register(
      GlobalStyle,
      Bootstrap
    )
    GlobalRegistry.onRegistration(_.addToDocument())
  }

}

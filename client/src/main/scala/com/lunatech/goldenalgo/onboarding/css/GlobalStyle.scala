package com.lunatech.goldenalgo.onboarding.css

import com.lunatech.goldenalgo.onboarding.css.Bootstrap.{bgDark, textCenter, textLight, w100}
import scalacss.DevDefaults._

object GlobalStyle extends StyleSheet.Inline {

  import dsl._

  style(
    unsafeRoot("body")(
      margin.`0`,
      padding.`0`,
      fontSize(14.px),
      fontFamily :=! "'Roboto', sans-serif"
    )
  )

  val heading3: StyleA = style(
    addClassName("w-100 p-3 bg-dark text-light"),
  )

  val darkBtn: StyleA = style(
    addClassName("btn btn-dark btn-block my-4"),
  )

}

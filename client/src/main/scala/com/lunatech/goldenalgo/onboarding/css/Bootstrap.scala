package com.lunatech.goldenalgo.onboarding.css

import scalacss.DevDefaults._

object Bootstrap extends StyleSheet.Inline {

  import dsl._

  val textDark: StyleA = style(
    addClassName("text-dark")
  )
  val textLight: StyleA = style(
    addClassName("text-light")
  )
  val  textCenter: StyleA = style(
    addClassName("text-center")
  )
  val textSecondary: StyleA = style(
    addClassName("text-secondary")
  )
  val h4: StyleA = style(
    addClassName("h-4")
  )



  val bgDark: StyleA = style(
    addClassName("bg-dark")
  )

  val bgLight: StyleA = style(
    addClassName("bg-light")
  )

  val w100: StyleA = style(
    addClassName("w-100")
  )

  val dFlex: StyleA = style(
    addClassName("d-flex")
  )

  val flexColumn: StyleA = style(
    addClassName("flex-column")
  )

  val justifyContentCenter: StyleA = style(
    addClassName("justify-content-center")
  )

  val alignItemsCenter: StyleA = style(
    addClassName("align-items-center")
  )

  val mxAuto: StyleA = style(
    addClassName("mx-auto")
  )
  val m5: StyleA = style(
    addClassName("m-5")
  )
  val m4: StyleA = style(
    addClassName("m-4")
  )
  val mb4: StyleA = style(
    addClassName("mb-4")
  )
  val p5: StyleA = style(
    addClassName("p-5")
  )
  val p4: StyleA = style(
    addClassName("p-4")
  )
  val p3: StyleA = style(
    addClassName("p-3")
  )
  val px5: StyleA = style(
    addClassName("px-5")
  )
  val px4: StyleA = style(
    addClassName("px-4")
  )
  val px3: StyleA = style(
    addClassName("px-3")
  )
  val py5: StyleA = style(
    addClassName("py-5")
  )
  val py4: StyleA = style(
    addClassName("py-4")
  )
  val py3: StyleA = style(
    addClassName("py-3")
  )
  val py2: StyleA = style(
    addClassName("py-2")
  )
  val py1: StyleA = style(
    addClassName("py-1")
  )


  /***********Forms********************/
  val formOutline: StyleA = style(
    addClassName("form-outline")
  )
  val formControl: StyleA = style(
    addClassName("form-control")
  )

}

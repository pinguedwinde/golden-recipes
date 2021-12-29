package com.lunatech.goldenalgo.onboarding

import utest.{TestSuite, Tests, test}

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.ext._

object AppTest extends TestSuite{

  App.setupUI()

  def tests: Tests = Tests {
    test("HelloWorld") {
      assert(document.querySelectorAll("p").count(_.textContent == "Hello world ScalaJS") == 1)
    }
  }
}

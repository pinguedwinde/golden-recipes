package com.lunatech.goldenalgo.onboarding.page

import com.lunatech.goldenalgo.onboarding.api.RecipeApiClient.futurePost
import com.lunatech.goldenalgo.onboarding.css.Bootstrap._
import com.lunatech.goldenalgo.onboarding.css.GlobalStyle.heading3
import com.lunatech.goldenalgo.onboarding.diode.{AddRecipeToRecipes, AppCircuit}
import com.lunatech.goldenalgo.onboarding.features.recipes.components.RecipeElement.ImgNumber
import com.lunatech.goldenalgo.onboarding.model.Recipe
import com.lunatech.goldenalgo.onboarding.page.AddRecipe.Styles.title
import com.lunatech.goldenalgo.onboarding.router.AppRouter
import com.lunatech.goldenalgo.onboarding.router.AppRouter.Page
import diode.AnyAction.aType
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.vdom.{TagOf, VdomElement}
import japgolly.scalajs.react.{BackendScope, Callback, ReactEvent, ReactEventFromInput, Ref, ScalaComponent}
import org.scalajs.dom
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.{HTMLElement, Node}
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util.Random


object AddRecipe {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val dragArea: StyleA = style(
      backgroundColor(c"#fff"),
      border(2 px, dashed, c"#040414"),
      height(500 px),
      width(700 px),
      borderRadius(10 px),
      display.flex,
      flexDirection.column,
      alignItems.center,
      justifyContent.center
    )

    val active: StyleA = style(
      backgroundColor(c"#ddc9c71f"),
      cursor.copy,
      border(2 px, solid, c"#040414"),
      transform := "scale(1.05)",
      transition := "all 0.3s",
    )

    val icon: StyleA = style(
      fontSize(100 px),
      color(c"#040414")
    )

    val addIcon: StyleA = style(
      textAlign.end,
      padding(0 px, 40 px),
      fontSize(40 px),
      color(c"#040414"),
      &.hover(
        cursor.pointer
      )
    )

    val couldUploadAlt: StyleA = style(
      addClassName("fas fa-cloud-upload-alt")
    )

    val plusSquare: StyleA = style(
      addClassName("fas fa-plus-square")
    )

    val title: StyleA = style(
      fontSize(30 px),
      fontWeight._500,
      color(c"#040414")
    )

    val spanOr: StyleA = style(
      fontSize(25 px),
      fontWeight._500,
      color(c"#040414"),
      margin(10 px, 0 px, 15 px, 0 px)
    )

    val browserButton: StyleA = style(
      backgroundColor(c"#36364a"),
      color(c"#fff"),
      padding(10 px, 25 px),
      border.none,
      borderRadius(5 px),
      fontSize(20 px),
      fontWeight._500,
      margin(10 px, 0 px, 15 px, 0 px),
      outline.none,
      cursor.pointer
    )

    val draggedImg: StyleA = style(
      height(100 %%),
      width(100 %%),
      objectFit.cover,
      borderRadius(10 px)
    )

  }

  val initialIngredients = Map(
    "recipe-ingredient-1" -> "",
    "recipe-ingredient-2" -> "",
    "recipe-ingredient-3" -> ""
  )

  val initialSteps = Map(
    "recipe-step-1" -> "",
    "recipe-step-2" -> "",
    "recipe-step-3" -> ""
  )

  val initialTags = Map(
    "recipe-tag-1" -> "",
    "recipe-tag-2" -> "",
    "recipe-tag-3" -> ""
  )

  case class Props(ctl: RouterCtl[AppRouter.Page])

  case class State(
                  isDragAreaActive: Boolean = false,
                  name: String = "",
                  ingredients: Map[String, String] = initialIngredients,
                  steps: Map[String, String] = initialSteps,
                  tags: Map[String, String] = initialTags,
                  )

  class Backend($: BackendScope[Props, State]) {

    private val dragAreaRef = Ref[HTMLElement]

    private def addParagraph(target: Node, text: String) = {
      val p = dom.document.createElement("p")
      p.textContent = text
      p.setAttribute("class", title.htmlClass)
      target.textContent = ""
      target.appendChild(p)
    }

    def clickFileInput(): Callback = Callback {
      val input = dom.document.querySelector("#fileInput").asInstanceOf[HTMLElement]
      input.click()
    }

    def onDragOver(e: ReactEvent): Callback = {
      e.preventDefault()
      $.modState(_.copy(isDragAreaActive = true))
    } >> dragAreaRef.foreach{ div => addParagraph(div, "Release to Upload File") }

    def onDragLeave(e: ReactEvent): Callback = {
      e.preventDefault()
      $.modState(_.copy(isDragAreaActive = false))
    } >> dragAreaRef.foreach { div => addParagraph(div, "Drag & Drop to Upload File") }

    def onDrop(e: ReactEventFromInput): Callback =  e.preventDefaultCB >> Callback {
      val file = e.target.files(0)
      println(file)
    }

    def addIngredientInput(ingredients: Map[String, String]): Callback = {
      val nb = ingredients.size + 1
      $.modState(_.copy(ingredients = ingredients ++ Map(s"recipe-ingredient-$nb" -> "")))
    }

    def addStepInput(steps: Map[String, String]): Callback = {
      val nb = steps.size + 1
      $.modState(_.copy(steps = steps ++ Map(s"recipe-step-$nb" -> "")))
    }

    def addTagInput(tags: Map[String, String]): Callback = {
      val nb = tags.size + 1
      $.modState(_.copy(tags = tags ++ Map(s"recipe-tag-$nb" -> "")))
    }

    def onRecipeInputChange(e: ReactEventFromInput): Callback = {
      val newValue = e.target.value
      e.target.id match {
        case "recipe-name" => $.modState(_.copy(name = newValue))
        case id: String if id.startsWith("recipe-ingredient") =>
          $.modState(s =>
            s.copy(ingredients = s.ingredients.map[String, String](t => if(t._1 == id) t._1 -> newValue else t ))
          )
        case id: String if id.startsWith("recipe-step") =>
          $.modState(s =>
            s.copy(steps = s.steps.map[String, String](t => if(t._1 == id) t._1 -> newValue else t ))
          )
        case id: String if id.startsWith("recipe-tag") =>
          $.modState(s =>
            s.copy(tags = s.tags.map[String, String](t => if(t._1 == id) t._1 -> newValue else t ))
          )
        case _ => Callback.empty
      }

    }

    def submit(): Callback =  $.state.flatMap { s =>
      val recipe = Recipe(
        id = "",
        name = s.name.trim,
        ingredients = s.ingredients.values.toSeq.filter(_.nonEmpty),
        instructions = s.steps.values.toSeq.filter(_.nonEmpty),
        tags = s.tags.values.toSeq.filter(_.nonEmpty)
      )
      Callback.future {
        futurePost[Recipe, Recipe](url = "recipes")(recipe).map { recipe =>
          AppCircuit.dispatch(AddRecipeToRecipes(recipe))
          $.props.map(p => p.ctl setOnClick AppRouter.RecipeDetailsPage(recipe.id, Random.nextInt(32) + 1))
        }
      }
    }

    def render(s: State): VdomTagOf[Div] = {
      import Styles._

      def handleItem(item: (String, String), itemText: String): TagOf[Div] = {
          val i = item._1.split("-")(2).toInt
          <.div(
            formOutline, mb4,
            <.input(
              formControl,
              ^.`type` := "text",
              ^.id := item._1,
              ^.placeholder := {
                if (item._2.isEmpty) s"$itemText $i" else item._2
              },
              ^.onChange ==> onRecipeInputChange
            )
          )
      }

      <.div(
        dFlex, flexColumn, alignItemsCenter, justifyContentCenter,
        <.div(
          bgLight, dFlex, flexColumn, alignItemsCenter, textCenter,
          ^.className := "recipe-details",
          <.div(
            if (s.isDragAreaActive) active + dragArea else dragArea,
            ^.onDragOver ==> onDragOver,
            ^.onDragLeave ==> onDragLeave,
            ^.onDrop ==> onDrop,
            <.div(
              icon, <.i(couldUploadAlt),
            ),
            <.p(title, "Drag & Drop to Upload the recipe's picture"),
            <.span(spanOr, "OR"),
            <.button(
              browserButton,
              "Browse File",
              ^.onClick --> clickFileInput(),
            ),
            <.input(
              ^.id := "fileInput",
              ^.`type` := "file",
              ^.hidden := true,
              ^.accept := "image/png, image/jpeg",
              ^.multiple := false
            ),
          ).withRef(dragAreaRef),
          <.form(
            p5, mxAuto,
            ^.maxWidth := "780px",
            <.p(
              h4, mb4, title,
              "Create and submit your recipe"
            ),
            <.div(
              formOutline, mb4,
              <.input(
                formControl,
                ^.`type` := "text",
                ^.id := "recipe-name",
                ^.placeholder := "Recipe's name",
                ^.onChange ==> onRecipeInputChange
              )
            ),
            <.hr(w100),
            <.h3( heading3, "Add the recipe's ingredients"),
            s.ingredients.toTagMod { ingredient => handleItem(ingredient, "Ingredient") },
            <.div(addIcon, <.i(plusSquare, ^.onClick --> addIngredientInput(s.ingredients))),
            <.hr(w100),
            <.h3(heading3, "Add the recipe's steps for cooking this recipe"),
            s.steps.toTagMod { step => handleItem(step, "Step") },
            <.div(addIcon, <.i(plusSquare, ^.onClick --> addStepInput(s.steps))),
            <.hr(w100),
            <.h3(heading3, "Add some tags to this recipe"),
            s.tags.toTagMod { tag => handleItem(tag, "Tag") },
            <.div(addIcon, <.i(plusSquare, ^.onClick --> addTagInput(s.tags))),
            <.button(
              browserButton,
              ^.`type` := "button",
              ^.onClick --> submit(),
              "Submit to create your recipe"
            )
          )
        )
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("AddRecipe")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(ctl: RouterCtl[Page]): VdomElement = component(Props(ctl))

}

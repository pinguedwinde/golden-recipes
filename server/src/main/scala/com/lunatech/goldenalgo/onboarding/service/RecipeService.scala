package com.lunatech.goldenalgo.onboarding.service

import com.lunatech.goldenalgo.onboarding.model.{Recipe, RecipeData}

import scala.concurrent.ExecutionContext

object RecipeService {

  implicit val ec: ExecutionContext = ExecutionContext.global

  private var recipes: Set[Recipe] = Set(
    Recipe(
      "recipe1-id", "recipe1-name", Seq("ingredient1", "ingredient2"), Seq("instruction1", "instruction2"), Set("tag1", "tag2")
    ),
    Recipe(
      "1",
      "Classic Dinner Rolls",
      Seq(
        "2 cups all-purpose flour, or more if needed",
        "1 envelope Fleischmann's® RapidRise Yeast",
        "2 tablespoons sugar",
        "½ teaspoon salt",
        "½ cup milk",
        "¼ cup water",
        "2 tablespoons butter OR margarine"
      ),
      Seq(
        "Combine 3/4 cup flour, undissolved yeast, sugar and salt in a large bowl. Heat milk, water and butter until very warm " +
          "(120 degrees to 130 degrees F). Add to flour mixture. Beat 2 minutes at medium speed of electric mixer, scraping bowl " +
          "occasionally. Add 1/4 cup flour; beat 2 minutes at high speed. Stir in enough remaining flour to make soft dough. " +
          "Knead on lightly floured surface until smooth and elastic, about 8 to 10 minutes. Cover; let rest 10 minutes.",
        "Divide dough into 12 equal pieces; shape into balls. Place in greased 8-inch round pan. Cover; let rise in warm, " +
          "draft-free place until doubled in size, about 30 minutes.",
        "Bake in preheated 375 degrees F oven for 20 minutes or until done. Remove from pan; brush with additional melted butter, if desired. Serve warm."
      ),
      Set("Classic", "Dinner", "Rolls", "Sugared")
    ),
    Recipe(
      "2",
      "Garlicky Green Beans with Shallot",
      Seq(
        "1 tablespoon butter",
        "3 tablespoons olive oil",
        "1 large shallot, chopped",
        "8 cloves garlic, sliced",
        "15 ounces fresh green beans, trimmed",
        " salt and ground black pepper to taste",
        "½ cup grated Parmesan cheese"
      ),
      Seq(
        "Melt butter with olive oil in a large skillet over medium heat. Cook shallot and garlic in the hot butter mixture, stirring frequently, until soft and slightly browned, 5 to 10 minutes.",
        "Stir green beans with shallot mixture; season with salt and pepper. Cook and stir green beans until tender, about 12 minutes.",
        "Remove skillet from heat and sprinkle Parmesan cheese over the green beans."
      ),
      Set("Shallot", "Garlicky", "Dinner", "Butter", "Beans")
    ),
    Recipe(
      "3",
      "Chef John's Perfect Mashed Potatoes",
      Seq(
        "3 large russet potatoes, peeled and cut in half lengthwise",
        "¼ cup butter",
        "½ cup whole milk",
        "salt and ground black pepper to taste"
      ),
      Seq(
        "Place the potatoes into a large pot, and cover with salted water. Bring to a boil, reduce heat to medium-low, " +
          "cover, and simmer until tender, 20 to 25 minutes. Drain, and return the potatoes to the pot. Turn heat to high, " +
          "and allow the potatoes to dry for about 30 seconds. Turn off the heat.",
        "Mash the potatoes with a potato masher twice around the pot, then add the butter and milk. Continue to mash until " +
          "smooth and fluffy. Whisk in the salt and black pepper until evenly distributed, about 15 seconds."
      ),
      Set("Potatoes", "Milked", "Butter")
    ),
    Recipe(
      "4",
      "The Best Rolled Sugar Cookies",
      Seq(
        "1 ½ cups butter, softened",
        "2 cups white sugar",
        "4 eggs",
        "1 teaspoon vanilla extract",
        "5 cups all-purpose flour",
        "2 teaspoons baking powder",
        "1 teaspoon salt",
      ),
      Seq(
        "In a large bowl, cream together butter and sugar until smooth. Beat in eggs and vanilla. Stir in the flour, baking powder, " +
          "and salt. Cover, and chill dough for at least one hour (or overnight).",
        "Preheat oven to 400 degrees F (200 degrees C). Roll out dough on floured surface 1/4 to 1/2 inch thick. Cut into shapes with any cookie " +
          "cutter. Place cookies 1 inch apart on ungreased cookie sheets.",
        "Bake 6 to 8 minutes in preheated oven. Cool completely."
      ),
      Set("Cookie", "Eggs", "Butter", "Sugared", "Rolls")
    ),
    Recipe(
      "5",
      "Guacamole",
      Seq(
        "3 avocados - peeled, pitted, and mashed",
        "1 lime, juiced",
        "1 teaspoon salt",
        "½ cup diced onion",
        "3 tablespoons chopped fresh cilantro",
        "2 roma (plum) tomatoes, diced",
        "1 teaspoon minced garlic",
        "1 pinch ground cayenne pepper (Optional)",
      ),
      Seq(
        "In a medium bowl, mash together the avocados, lime juice, and salt. Mix in onion, cilantro, tomatoes, and garlic. " +
          "Stir in cayenne pepper. Refrigerate 1 hour for best flavor, or serve immediately."
      ),
      Set("Avocado", "Salted", "Garlicky", "Onion")
    )
  )

  case class CreatedRecipesResponse(id: String, msg: String)

  def fetchAllRecipes(): Set[Recipe] = recipes

  def findRecipeById(id: String): Option[Recipe] = recipes.find(_.id == id)

  def saveRecipe(recipeData: RecipeData): CreatedRecipesResponse = {
    val id = s"${recipes.size + 1}"
    val recipe = Recipe(id, recipeData)
    recipes = recipes + recipe
    CreatedRecipesResponse(id, "A recipe was successfully created")
  }

  def updateRecipe(id: String, recipeData: RecipeData): Option[Recipe] =
    for {
      recipe <- recipes.find(_.id == id)
    } yield {
      val updatedRecipe = Recipe(id, recipeData)
      recipes = recipes - recipe
      recipes = recipes + updatedRecipe
      updatedRecipe
    }

  def deleteRecipe(id: String): Boolean = {
    recipes.find(_.id == id) match {
      case Some(recipe) =>
        recipes = recipes - recipe
        true
      case _ => false
    }
  }

  def tagRecipe(recipeId: String, tags: Set[String]): Option[Recipe] =
    for {
      recipe <- recipes.find(_.id == recipeId)
    } yield {
      val t = recipe.tags ++ tags
      val updatedRecipe = Recipe(recipeId, recipe.name, recipe.ingredients, recipe.instructions, recipe.tags ++ tags)
      recipes = recipes - recipe
      recipes = recipes + updatedRecipe
      updatedRecipe
    }

  def searchRecipesByIngredientAndTag(ingredientTerm: Option[String], tagTerm: Option[String]): Set[Recipe] = {
    if (ingredientTerm.isEmpty && tagTerm.isEmpty) recipes
    else if (ingredientTerm.isDefined && tagTerm.isEmpty) {
      searchRecipes(containsIngredientTerm = true, containsTagTerm = false, ingredientTerm, tagTerm)
    } else if (ingredientTerm.isEmpty && tagTerm.isDefined) {
      searchRecipes(containsIngredientTerm = false, containsTagTerm = true, ingredientTerm, tagTerm)
    } else {
      searchRecipes(containsIngredientTerm = true, containsTagTerm = true, ingredientTerm, tagTerm)
    }
  }

  def searchRecipesByAnyWord(textOption: Option[String]): Set[Recipe] = {
    for {
      recipe <- recipes
      tag <- recipe.tags
      ingredient <- recipe.ingredients
      instruction <- recipe.instructions
      if (
        containsCaseInsensitive(ingredient, textOption)
          || containsCaseInsensitive(tag, textOption)
          || containsCaseInsensitive(instruction, textOption)
          || containsCaseInsensitive(recipe.name, textOption)
        )
    } yield recipe
  }

  private def searchRecipes(containsIngredientTerm: Boolean,
                            containsTagTerm: Boolean,
                            ingredientTerm: Option[String],
                            tagTerm: Option[String]) = {
    for {
      recipe <- recipes
      tag <- recipe.tags
      ingredient <- recipe.ingredients
      if (containsIngredientTerm && containsCaseInsensitive(ingredient, ingredientTerm)) || (containsTagTerm && containsCaseInsensitive(tag, tagTerm))
    } yield recipe
  }

  private def containsCaseInsensitive(text: String, substring: Option[String]): Boolean = {
    substring.forall(sub => text.toLowerCase.contains(sub.toLowerCase))
  }

}

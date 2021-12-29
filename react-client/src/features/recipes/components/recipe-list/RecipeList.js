import React, { Component } from "react";
import RecipeElement from "./recipe-element/RecipeElement";

export default class RecipeList extends Component {
  render() {
    return (
      <div className="w-65 d-flex flex-row flex-wrap justify-content-center">
        {this.props.recipes.map((recipe, index) => (
          <RecipeElement
            key={recipe.id}
            recipe={recipe}
            index={index}
            updateSelectedRecipe={() => this.props.updateSelectedRecipe(index)}
          />
        ))}
      </div>
    );
  }
}

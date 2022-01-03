import React, { Component } from "react";
import Style from "./RecipeElement.module.scss";

export default class RecipeElement extends Component {
  updateSelectedRecipe = () => {
    this.props.updateSelectedRecipe(this.props.recipe.id);
  };

  render() {
    return (
      <div
        onClick={this.updateSelectedRecipe}
        className={"d-flex flex-column bg-light " + Style.container}
      >
        <div>
          <img
            alt="recipe"
            width="320"
            src={require(`../../../../../assets/img/recipe${
              this.props.index + 1
            }.jpg`)}
          />
        </div>
        <div className="p-3 text-center">
          <h5>{this.props.recipe.name}</h5>
          <hr className="w-100" />
          <span className="text-secondary">
            {this.props.recipe.tags.join(" | ")}
          </span>
        </div>
      </div>
    );
  }
}

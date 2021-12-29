import React, { Component } from "react";

export default class RecipeDetails extends Component {
  render() {
    return (
      <div className="w-35 bg-light p-4 d-flex flex-column text-center">
        <h2 className="p-4">{this.props.recipe.name}</h2>
        <div>
          <img
            alt="recipe_picture"
            className="d-block w-100 mx-auto"
            src={require(`../../../../assets/img/recipe${
              this.props.index + 1
            }.jpg`)}
          />
        </div>
        <hr className="w-100" />
        <span className="text-secondary">
          {this.props.recipe.tags.join(" | ")}
        </span>
        <hr className="w-100" />
        <h2 className="p-3 bg-dark text-light">The ingredients' list</h2>
        <ul className="list-group list-group-flush text-start">
          {this.props.recipe.ingredients.map((ingredient, index) => (
            <li key={`${index}-${ingredient}`} className="list-group-item">
              {ingredient}
            </li>
          ))}
        </ul>
        <h2 className="mt-4 p-3 bg-dark text-light">Steps</h2>
        <ol className="list-group list-group-flush text-start">
          {this.props.recipe.instructions.map((instruction, index) => (
            <li key={`${index + 1}-${instruction}`} className="list-group-item">
              {`${index + 1}. ${instruction}`}
            </li>
          ))}
        </ol>
      </div>
    );
  }
}

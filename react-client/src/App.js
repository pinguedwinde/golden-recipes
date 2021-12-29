import React, { Component } from "react";
import { Header } from "./components";
import apiRecipes from "./shared/conf/api-recipes";
import Recipes from "./features/recipes";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from "react-router-dom";

export default class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedRecipe: 0,
      recipes: null,
      loaded: false,
    };
  }

  updateSelectedRecipe = (index) => {
    this.setState({
      selectedRecipe: index,
    });
  };

  componentDidMount() {
    apiRecipes
      .get("/recipes")
      .then((response) => response.data)
      .then((recipes) => {
        this.updateRecipes(recipes);
      })
      .catch((err) => console.log(err));
  }

  updateRecipes = (recipes) => {
    this.setState({
      recipes,
      loaded: true,
    });
  };

  render() {
    return (
      <div className="app">
        <Router>
          <Header></Header>
          <Routes>
            <Route
              path="/recipes"
              element={
                <Recipes
                  loaded={this.state.loaded}
                  recipes={this.state.recipes}
                  updateRecipes={this.updaterecipes}
                  updateSelectedRecipe={this.updateSelectedRecipe}
                  selectedRecipe={this.state.selectedRecipe}
                />
              }
            />
            <Route path="*" element={<Navigate to="/recipes" />} />
          </Routes>
        </Router>
      </div>
    );
  }
}

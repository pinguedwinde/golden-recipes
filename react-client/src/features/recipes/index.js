import React from "react";
import { RecipeList, RecipeDetails } from "./components";
import Loading from "../../components/utils/Loading";

const Recipes = (props) => {
  return (
    <>
      {props.loaded ? (
        <div className="d-flex flex-row flex-fill pt-4 p-2">
          <RecipeList
            recipes={props.recipes}
            updateSelectedRecipe={props.updateSelectedRecipe}
          />
          <RecipeDetails
            index={props.selectedRecipe}
            recipe={props.recipes[props.selectedRecipe]}
          />
        </div>
      ) : (
        <Loading />
      )}
    </>
  );
};

export default Recipes;

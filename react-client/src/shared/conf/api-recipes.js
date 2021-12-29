import * as axios from "axios";

const apiRecipes = axios.create({
  baseURL: "http://localhost:8080",
});

export default apiRecipes;

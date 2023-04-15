package vn.provl.api.manager;

import com.google.common.collect.Lists;
import vn.provl.RecipeX;
import vn.provl.api.recipe.Recipe;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A class for managing all custom recipes.
 */
public class RecipeManager {

    private Map<String, Recipe> registeredRecipes = new HashMap<>();

    public List<Recipe> getRegisteredRecipes() {
        return new ArrayList<>(registeredRecipes.values());
    }

    public boolean registerRecipes(Recipe recipe) {
        if (registeredRecipes.containsKey(recipe.getId())) return false;
        this.registeredRecipes.put(recipe.getId(), recipe);
        return true;
    }

    public Optional<Recipe> getRecipeByID(String ID){
        return Optional.ofNullable(this.registeredRecipes.get(ID));
    }
}

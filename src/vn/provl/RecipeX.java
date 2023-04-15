package vn.provl;

import org.bukkit.plugin.java.JavaPlugin;
import vn.provl.api.manager.RecipeManager;

public class RecipeX extends JavaPlugin {

    private static RecipeX instance;

    private RecipeManager recipeManager;

    @Override
    public void onEnable() {
        instance = this;

        this.recipeManager = new RecipeManager();
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    /**
     * Return the plugin's API instance.
     */
    public static RecipeX getInstance() {
        return instance;
    }
}

package vn.provl.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pdx.mantlecore.item.ItemCheckFactory;
import vn.provl.api.recipe.Recipe;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A class for managing all custom recipes.
 */
public class RecipeManager {

    private Map<String, Recipe> registeredRecipes = new HashMap<>();

    public List<Recipe> getRegisteredRecipes() {
        return new ArrayList<>(registeredRecipes.values());
    }

    /**
     * Load recipes' data and players' data.
     */
    public void load() {
        loadRecipes();
        loadPlayers();
    }

    private void loadPlayers() {
        int loadCount = 0;
        File directory = new File("plugins/player_data");
        if (!directory.exists()) directory.mkdirs();
        if (directory.listFiles() == null || directory.listFiles().length == 0) return;
        for (File file : directory.listFiles()) {
            if (file.isDirectory() || !file.getName().endsWith(".yml")) continue;

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            String playerName = file.getName().replace(".yml", "");
            PlayerDataManager data = new PlayerDataManager();
            data.setUnlockedRecipes(config.getStringList("unlocked-recipes"));

            PlayerDataManager.unlockedRecipesData.put(playerName, data);
        }
    }

    private void loadRecipes() {
        int loadCount = 0;
        File directory = new File("plugins/recipes");
        if (!directory.exists()) directory.mkdirs();
        if (directory.listFiles() == null || directory.listFiles().length == 0) return;
        for (File file : directory.listFiles()) {
            if (file.isDirectory() || !file.getName().endsWith(".yml")) continue;

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            String id = config.getString("id");
            ItemStack result = config.getItemStack("result");
            ItemStack[] inputs = new ItemStack[9];
            for (int i = 0; i < 9; i++) {
                inputs[i] = config.getItemStack("input." + i);
            }

            Recipe recipe = new Recipe(id, result, inputs);
            registerRecipes(recipe);

            loadCount++;
        }
    }

    /**
     * Save recipes' data and players' data.
     */
    public void save() {
        saveRecipe();
        savePlayer();
    }

    private void saveRecipe() {
        int saveCount = 0;
        for (Recipe recipe : getRegisteredRecipes()) {
            File file = new File("plugins/recipes/" + recipe.getId() + ".yml");
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("id", recipe.getId());
            config.set("result", recipe.getResult());
            for (int i = 0; i < 9; i++) {
                ItemStack input = recipe.getInputMatrix()[i];
                config.set("input." + i, input);
            }
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            saveCount++;
        }
        Bukkit.getLogger().info("Saved " + saveCount + " custom recipes.");
    }

    private void savePlayer() {
        int saveCount = 0;
        for (Map.Entry<String, PlayerDataManager> dataManager : PlayerDataManager.unlockedRecipesData.entrySet()) {
            String playerName = dataManager.getKey();
            PlayerDataManager playerDataManager = dataManager.getValue();

            File file = new File("plugins/player_data/" + playerName + ".yml");
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("unlocked-recipes", playerDataManager.getUnlockedRecipes());
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            saveCount++;
        }
        Bukkit.getLogger().info("Saved " + saveCount + " player data.");
    }

    public boolean registerRecipes(Recipe recipe) {
        if (registeredRecipes.containsKey(recipe.getId())) return false;
        this.registeredRecipes.put(recipe.getId().toLowerCase(), recipe);
        return true;
    }

    public Optional<Recipe> getRecipeByID(String ID) {
        return Optional.ofNullable(this.registeredRecipes.get(ID.toLowerCase()));
    }

    public Optional<Recipe> getRecipeByUnlockItem(ItemStack item) {
        return getRegisteredRecipes().stream().filter(recipe ->
                ItemCheckFactory.of(recipe.generateUnlockItem()).compare(item)).findAny();
    }

    public Optional<Recipe> getRecipeByInputs(ItemStack... inputs) {
        return getRegisteredRecipes().stream().filter(recipe -> recipe.matches(inputs)).findAny();
    }
}

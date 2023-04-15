package vn.provl.api.manager;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import vn.provl.api.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerDataManager {

    private static final Map<String, PlayerDataManager> unlockedRecipesData = Maps.newHashMap();

    public static PlayerDataManager getData(Player player){
        PlayerDataManager dataManager = unlockedRecipesData.get(player.getName());
        if(dataManager == null){
            dataManager = new PlayerDataManager();
            unlockedRecipesData.put(player.getName(), dataManager);
        }
        return dataManager;
    }

    private List<String> unlockedRecipes = new ArrayList<>();

    public List<String> getUnlockedRecipes() {
        return unlockedRecipes;
    }

    public boolean hasUnlocked(Recipe recipe){
        return getUnlockedRecipes().contains(recipe.getId());
    }

    public boolean unlock(Recipe recipe){
        if(hasUnlocked(recipe)) return true;
        unlockedRecipes.add(recipe.getId());
        return true;
    }
}

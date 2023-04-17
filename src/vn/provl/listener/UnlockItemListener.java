package vn.provl.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import vn.provl.RecipeX;
import vn.provl.api.manager.PlayerDataManager;
import vn.provl.api.recipe.Recipe;

import java.util.Optional;

public class UnlockItemListener implements Listener {

    @EventHandler
    public void playerUnlockRecipeEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack i = player.getInventory().getItemInMainHand();
        if(i == null || i.getType() == Material.AIR) return;

        if(e.getHand() != EquipmentSlot.HAND) return;

        if(!e.getAction().toString().contains("RIGHT")) return;

        Optional<Recipe> recipeOptional = RecipeX.getInstance().getRecipeManager().getRecipeByUnlockItem(i);
        if(!recipeOptional.isPresent()) return;

        Recipe recipe = recipeOptional.get();
        PlayerDataManager playerDataManager = PlayerDataManager.getData(player);
        playerDataManager.unlock(recipe);
    }

}

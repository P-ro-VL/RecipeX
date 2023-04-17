package vn.provl.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pdx.mantlecore.menu.Menu;
import vn.provl.RecipeX;
import vn.provl.api.recipe.Recipe;
import vn.provl.ui.RecipeCreatorUI;

public class EnterRecipeIDListener implements Listener  {

    @EventHandler
    public void onEnterRecipeID(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        String msg = e.getMessage();

        if(!RecipeCreatorUI.editingIDMap.containsKey(player.getUniqueId())) return;
        e.setCancelled(true);

        if(msg.contains(" ")){
            player.sendMessage(ChatColor.RED + "ID không được có dấu cách!");
            return;
        }

        RecipeCreatorUI.editingIDMap.remove(player.getUniqueId());

        RecipeCreatorUI creatorUI = RecipeCreatorUI.editingIDMap.get(player.getUniqueId());
        if(msg.equalsIgnoreCase("cancel")){
            Menu.open(player, creatorUI);
            player.sendMessage(ChatColor.GOLD + "Huỷ nhập ID thành công.");
            return;
        }

        Recipe committingRecipe = creatorUI.committingRecipe;
        committingRecipe.setId(msg);
        RecipeX.getInstance().getRecipeManager().registerRecipes(committingRecipe);

        player.sendTitle(ChatColor.GREEN + ChatColor.BOLD.toString() + "THÀNH CÔNG", "đã tạo công thức thành công");
    }

}

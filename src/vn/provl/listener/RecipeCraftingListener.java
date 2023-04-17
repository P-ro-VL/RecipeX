package vn.provl.listener;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pdx.mantlecore.item.ItemPushUtil;
import vn.provl.RecipeX;
import vn.provl.api.manager.PlayerDataManager;
import vn.provl.api.recipe.Recipe;

import java.util.Optional;

public class RecipeCraftingListener implements Listener {

    @EventHandler
    public void prepareCraftingEvent(PrepareItemCraftEvent e) {
        Player player = (Player) e.getView().getPlayer();
        PlayerDataManager dataManager = PlayerDataManager.getData(player);
        try {
            ItemStack[] matrix = e.getInventory().getMatrix();
            Optional<Recipe> result = RecipeX.getInstance().getRecipeManager().getRecipeByInputs(matrix);
            result.ifPresent(recipe -> {
                if (dataManager.hasUnlocked(recipe))
                    e.getInventory().setResult(recipe.getResult());
            });
            player.updateInventory();
        } catch (Exception ex) {
            ex.printStackTrace();
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void customCraftingEvent(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;
        if (e.getClickedInventory() instanceof CraftingInventory && e.getSlot() == 0) {
            Player player = (Player) e.getWhoClicked();
            CraftingInventory inventory = (CraftingInventory) e.getClickedInventory();

            int[] slots = Ints.toArray(ContiguousSet.create(Range.closed(0, 35), DiscreteDomain.integers()));
            boolean pushable = ItemPushUtil.isPushable(player.getInventory(), e.getCurrentItem(), slots);
            if (!pushable) {
                e.setCancelled(true);

                player.playSound(player.getEyeLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 0);
                player.sendMessage(ChatColor.RED + "Túi đồ đầy! Vui lòng dọn dẹp túi đồ trước khi chế tạo!");
                return;
            }

            ItemStack[] matrix = (ItemStack[]) inventory.getMatrix().clone();
            ItemStack[] newMatrix = new ItemStack[matrix.length];
            for (int so = 0; so < matrix.length; so++) {
                ItemStack i = matrix[so];
                if (i == null) {
                    newMatrix[so] = null;
                    continue;
                }
                ItemStack clone = i.clone();
                clone.add(-1);
                newMatrix[so] = clone;
            }
            e.setCancelled(true);

            Bukkit.getScheduler().runTask(RecipeX.getInstance(),
                    () -> inventory.setMatrix(newMatrix));
            ItemPushUtil.pushItem(player.getInventory(), inventory.getResult());

            player.playSound(player.getEyeLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
            player.updateInventory();
        } else if (e.getClickedInventory() instanceof CraftingInventory) {
            Bukkit.getScheduler().runTaskLater(RecipeX.getInstance(), () -> {
                PrepareItemCraftEvent event1 = new PrepareItemCraftEvent((CraftingInventory) e.getClickedInventory(),
                        e.getView(), false);
                Bukkit.getPluginManager().callEvent(event1);
            }, 1L);
        }
    }
}

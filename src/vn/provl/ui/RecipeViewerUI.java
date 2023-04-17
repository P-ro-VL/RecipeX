package vn.provl.ui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pdx.mantlecore.item.ItemBuilder;
import pdx.mantlecore.menu.Menu;
import pdx.mantlecore.menu.elements.MenuItem;
import vn.provl.api.recipe.Recipe;

public class RecipeViewerUI extends Menu {

    public static final int[] INPUT_SLOTS = {11, 12, 13, 20, 21, 22, 29, 30, 31};
    public static final int ARROW_SLOT = 23, RESULT_SLOT = 24;

    public RecipeViewerUI(MainUI mainUI, Recipe recipe) {
        super(54, "Xem công thức");

        int[] blackBorders = {0, 1, 2, 3, 4, 5, 6, 7, 8, 53, 52, 51, 50, 49, 48, 47, 46, 45};
        for (int slot : blackBorders)
            setItem(slot, new MenuItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, " ", "")) {
                @Override
                public void onClick(InventoryClickEvent e) {
                    e.setCancelled(true);
                }
            });

        for (int i = 0; i < INPUT_SLOTS.length; i++) {
            int slot = INPUT_SLOTS[i];
            ItemStack item = recipe.getInputMatrix()[i];
            setItem(slot, new MenuItem(item == null ? new ItemStack(Material.AIR) : item){
                @Override
                public void onClick(InventoryClickEvent e) {
                    e.setCancelled(true);
                }
            });
        }

        setItem(ARROW_SLOT, new MenuItem(new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(" ")
                .setTextureURL("956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311")){
            @Override
            public void onClick(InventoryClickEvent e) {
                e.setCancelled(true);
            }
        });

        setItem(RESULT_SLOT, new MenuItem(recipe.getResult()){
            @Override
            public void onClick(InventoryClickEvent e) {
                e.setCancelled(true);
            }
        });

        setItem(0, new MenuItem(new ItemBuilder(Material.ARROW, "§6< Quay về", "§7Nhấn để quay về màn hình chính.")){
            @Override
            public void onClick(InventoryClickEvent e) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                Menu.open(player, mainUI);
                player.playSound(player.getEyeLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            }
        });
    }
}

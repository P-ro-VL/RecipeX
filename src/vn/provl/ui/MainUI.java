package vn.provl.ui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pdx.mantlecore.item.ItemBuilder;
import pdx.mantlecore.item.ItemNameUtil;
import pdx.mantlecore.menu.Menu;
import pdx.mantlecore.menu.elements.MenuItem;

import vn.provl.RecipeX;
import vn.provl.api.manager.PlayerDataManager;
import vn.provl.api.recipe.Recipe;

import java.util.List;

public class MainUI extends Menu {
    private Player player;
    private int page;
    private int maxPage;

    public MainUI(Player player, int page) {
        super(54, "Danh sách công thức");

        List<Recipe> registeredRecipes = RecipeX.getInstance().getRecipeManager().getRegisteredRecipes();

        this.page = page;
        this.player = player;
        this.maxPage = registeredRecipes.size() / 36;

        MainUI thisUI = this;

        PlayerDataManager playerData = PlayerDataManager.getData(player);

        setupFrame();
        setItem(49, new MenuItem(new ItemBuilder(Material.SUNFLOWER, "§a§lThông tin",
                "§7- Tổng số công thức: §c" + RecipeX.getInstance().getRecipeManager().getRegisteredRecipes().size(),
                    "§7- Đã mở khóa: §a" + playerData.getUnlockedRecipes().size())){
            @Override
            public void onClick(InventoryClickEvent e) {
                e.setCancelled(true);
            }
        });

        for (int i = page * 36; i < (page + 1) * 36; i++) {
            int slot = i - (36 * page);
            if (i >= registeredRecipes.size()) {
                setItem(slot, new MenuItem(new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " ", "")) {
                    @Override
                    public void onClick(InventoryClickEvent e) {
                        e.setCancelled(true);
                    }
                });
                continue;
            }

            Recipe recipe = registeredRecipes.get(i);
            if (!playerData.hasUnlocked(recipe)) {
                setItem(slot, new MenuItem(new ItemBuilder(Material.BARRIER, ItemNameUtil.formatItemName(recipe.getResult()),
                        "§c§lChưa mở khóa", "§7Bạn chưa mở khóa công thức này.")) {
                    @Override
                    public void onClick(InventoryClickEvent e) {
                        e.setCancelled(true);

                        player.playSound(player.getEyeLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 0);
                    }
                });
            } else {
                setItem(slot, new MenuItem(new ItemBuilder(recipe.getResult()).addLoreLine("", "§7Nhấn để xem công thức")) {
                    @Override
                    public void onClick(InventoryClickEvent e) {
                        e.setCancelled(true);
                        Menu.open(player, new RecipeViewerUI(thisUI, recipe));

                        player.playSound(player.getEyeLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                    }
                });
            }
        }

    }

    private void setupFrame() {
        int[] blackBorders = {0, 1, 2, 3, 4, 5, 6, 7, 8, 53, 52, 51, 50, 49, 48, 47, 46, 45};
        int previousPageSlot = 47, nextPageSlot = 51;

        for (int slot : blackBorders)
            setItem(slot, new MenuItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, " ", "")) {
                @Override
                public void onClick(InventoryClickEvent e) {
                    e.setCancelled(true);
                }
            });

        if (this.page > 0)
            setItem(previousPageSlot, new MenuItem(new ItemBuilder(Material.ARROW, "§6< Trang trước", "§7Nhấn để về trang trước.")) {
                @Override
                public void onClick(InventoryClickEvent e) {
                    e.setCancelled(true);
                    Menu.open(player, new MainUI(player, page - 1));
                }
            });

        if (page < maxPage) {
            setItem(nextPageSlot, new MenuItem(new ItemBuilder(Material.ARROW, "§6Trang sau >", "§7Nhấn để sang trang kế tiếp")) {
                @Override
                public void onClick(InventoryClickEvent e) {
                    e.setCancelled(true);
                    Menu.open(player, new MainUI(player, page + 1));
                }
            });
        }
    }
}

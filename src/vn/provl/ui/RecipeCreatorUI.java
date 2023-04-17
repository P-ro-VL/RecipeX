package vn.provl.ui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pdx.mantlecore.item.ItemBuilder;
import pdx.mantlecore.menu.Menu;
import pdx.mantlecore.menu.elements.MenuItem;
import vn.provl.api.recipe.Recipe;
import vn.provl.listener.RecipeCraftingListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecipeCreatorUI extends Menu {

	public static final Map<UUID, RecipeCreatorUI> editingIDMap = new HashMap<>();
	public Recipe committingRecipe;

	public RecipeCreatorUI() {
		super(27, "Tạo công thức mới");

		for (int so = 0; so < 27; so++) {
			setItem(so, new MenuItem(Material.GRAY_STAINED_GLASS_PANE) {
				@Override
				public void onClick(InventoryClickEvent e) {
					e.setCancelled(true);
				}
			});
		}

		int[] matrix = { 0, 1, 2, 9, 10, 11, 18, 19, 20 };
		int result = 13;
		
		for(int so : matrix) {
			setItem(so, new MenuItem(Material.AIR));
		}
		setItem(result, new MenuItem(Material.AIR));

		RecipeCreatorUI thisMenu = this;

		setItem(8, new MenuItem(new ItemBuilder(Material.DIAMOND,
				ChatColor.GREEN + "Tạo công thức",
				ChatColor.GRAY + "Sau khi đặt xong input/output, nhấn để",
				ChatColor.GRAY + "hoàn thành tạo công thức.")) {
			@Override
			public void onClick(InventoryClickEvent e) {
				e.setCancelled(true);
				
				if(getInventory().getItem(result) == null) {
					e.getWhoClicked().sendMessage(ChatColor.RED + "Vật phẩm kết quả không thể rỗng !");
					return;
				}
				
				ItemStack[] matrixx = new ItemStack[9];
				for(int so = 0; so < 9; so++) {
					matrixx[so] = getInventory().getItem(matrix[so]);
				}
				ItemStack item = getInventory().getItem(result);

				committingRecipe = new Recipe("test-id", item, matrixx);
				editingIDMap.put(e.getWhoClicked().getUniqueId(), thisMenu);
				e.getWhoClicked().closeInventory();
				e.getWhoClicked().sendMessage(ChatColor.GREEN + " Vui lòng nhập ID cho công thức này. Nếu muốn hủy, gõ 'cancel'.");
			}
		});
		
	}

}

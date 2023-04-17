package vn.provl.api.recipe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pdx.mantlecore.item.ItemBuilder;
import pdx.mantlecore.item.ItemCheckFactory;
import pdx.mantlecore.item.ItemNameUtil;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Recipe {

    private String id;
    private ItemStack result;
    private ItemStack[] inputMatrix = new ItemStack[3];

    public Recipe(String ID, ItemStack result, ItemStack... inputMatrix){
        this.id = ID;
        this.result = result;
        this.inputMatrix = inputMatrix;
    }

    public String getId() {
        return id;
    }

    public ItemStack getResult() {
        return result;
    }

    public ItemStack[] getInputMatrix() {
        return inputMatrix;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public void setInputMatrix(ItemStack... inputs) {
        this.inputMatrix = inputs;
    }

    public boolean matches(ItemStack... itemStacks) {
        if (getInputMatrix().length != itemStacks.length) return false;
        for (int i = 0; i < getInputMatrix().length; i++) {
            ItemStack model = getInputMatrix()[i];
            ItemStack testItem = itemStacks[i];
            if(model == null || testItem == null)
            {
                if(testItem != null || model != null) return false;
                continue;
            }

            if (!ItemCheckFactory.of(model).compare(testItem)) return false;
        }
        return true;
    }

    public ItemStack generateUnlockItem() {
        File configFile = new File("plugins/RecipeX/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        String name = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("unlock-item.name")));
        List<String> lore = config.getStringList("unlock-item.lore");
        Material material = Material.valueOf(config.getString("unlock-item.type").toUpperCase(Locale.ROOT));
        boolean glow = config.getBoolean("unlock-item.glow");

        ItemBuilder itemBuilder = new ItemBuilder(material).setDisplayName(name).addLoreLine(lore.toArray(new String[0]));
        if (glow)
            itemBuilder.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1).addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return itemBuilder.create();
    }
}

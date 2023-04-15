package vn.provl.api.recipe;

import org.bukkit.inventory.ItemStack;
import pdx.mantlecore.item.ItemCheckFactory;

public class Recipe {

    private String id;
    private ItemStack result;
    private ItemStack[] inputMatrix = new ItemStack[3];

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
            if(!ItemCheckFactory.of(model).compare(testItem)) return false;
        }
        return true;
    }
}

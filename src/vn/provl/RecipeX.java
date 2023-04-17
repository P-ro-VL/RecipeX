package vn.provl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pdx.mantlecore.menu.Menu;
import vn.provl.api.manager.RecipeManager;
import vn.provl.api.recipe.Recipe;
import vn.provl.listener.EnterRecipeIDListener;
import vn.provl.listener.RecipeCraftingListener;
import vn.provl.listener.UnlockItemListener;
import vn.provl.ui.MainUI;
import vn.provl.ui.RecipeCreatorUI;

public class RecipeX extends JavaPlugin {

    private static RecipeX instance;

    private RecipeManager recipeManager;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        this.recipeManager = new RecipeManager();
        getRecipeManager().load();

        registerListeners();
    }

    @Override
    public void onDisable() {
        getRecipeManager().save();
    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new EnterRecipeIDListener(), this);
        Bukkit.getPluginManager().registerEvents(new RecipeCraftingListener(), this);
        Bukkit.getPluginManager().registerEvents(new UnlockItemListener(), this);
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    /**
     * Return the plugin's API instance.
     */
    public static RecipeX getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be player to use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("congthuc")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (player.hasPermission("recipex.create")) {
                        Menu.open(player, new RecipeCreatorUI());
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                    }
                }

                if(args[0].equalsIgnoreCase("give")){
                    if (player.hasPermission("recipex.give")) {
                        try {
                            Player receiver = Bukkit.getPlayer(args[1]);
                            String id = args[2];

                            Recipe recipe = getRecipeManager().getRecipeByID(id).get();
                            assert receiver != null;
                            receiver.getInventory().addItem(recipe.generateUnlockItem());

                            player.sendMessage(ChatColor.GREEN + "Trao vật phẩm mở khóa công thức có id là '" + id.toLowerCase() +
                                    "' cho người chơi " + receiver.getName() + " thành công!");
                        } catch (Exception ex){
                            ex.printStackTrace();
                            player.sendMessage(ChatColor.RED + "Usage: /congthuc give <player> <id>");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                        return true;
                    }
                }
                return true;
            }
            Menu.open(player, new MainUI(player, 0));
        }

        return true;
    }
}

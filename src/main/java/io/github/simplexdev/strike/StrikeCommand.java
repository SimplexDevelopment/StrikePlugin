package io.github.simplexdev.strike;

import io.github.simplexdev.strike.api.ConfigUser;
import java.util.Arrays;

import io.github.simplexdev.strike.api.utils.InventoryEditConfigManager;
import io.github.simplexdev.strike.listeners.InventoryEditGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class StrikeCommand implements CommandExecutor {

    private static ConfigUser[] configUsers;
    private final JavaPlugin plugin;

    public StrikeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void loadInstances(ConfigUser... configUsers) {
        StrikeCommand.configUsers = configUsers;
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args[0].isEmpty() || args.length > 1) {
            return true;
        }
        if ("reload".equalsIgnoreCase(args[0])) {
            this.plugin.reloadConfig();
            Arrays.stream(configUsers).forEach(configUser -> configUser.refresh());
        }

        else if ("edit".equalsIgnoreCase(args[0]) && sender instanceof Player) {
            new InventoryEditGUI(plugin).openInventory((Player) sender);
        }

        else if ("get".equalsIgnoreCase(args[0]) && sender instanceof Player) {
            Player player = (Player) sender;

            player.getInventory().setContents(new InventoryEditConfigManager(plugin).getInventory(player));
        }


        return true;
    }
}
package io.github.simplexdev.strike;

import io.github.simplexdev.strike.api.ConfigUser;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        if ("reload".equals(args[0].toLowerCase())) {
            this.plugin.reloadConfig();
            Arrays.<ConfigUser>stream(configUsers).forEach(configUser -> configUser.refresh());
        }


        return true;
    }
}
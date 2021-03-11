package io.github.simplexdev.strike;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.profile.Profile;
import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.utils.InventoryEditConfigManager;
import io.github.simplexdev.strike.listeners.InventoryEditGUI;
import io.github.simplexdev.strike.listeners.SpawnController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public class StrikeCommand implements CommandExecutor {

    private static Map<Integer, NPC> npcMap = new HashMap<>();

    private static ConfigUser[] configUsers;
    private final JavaPlugin plugin;
    private final NPCPool npcPool;

    public StrikeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.npcPool = new NPCPool(plugin);
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
            sender.sendMessage(ChatColor.GREEN + "[Strike] Plugin Config Reloaded");
        } else if ("edit".equalsIgnoreCase(args[0]) && sender instanceof Player) {
            new InventoryEditGUI(plugin).openInventory((Player) sender);
        } else if ("get".equalsIgnoreCase(args[0]) && sender instanceof Player) {
            Player player = (Player) sender;

            player.getInventory().setContents(new InventoryEditConfigManager(plugin).getInventoryItems(player));
        } else if ("set-spawn".equalsIgnoreCase(args[0]) && sender instanceof Player) {
            Player player = ((Player) sender);
            new SpawnController(plugin).setSpawn(player.getLocation());
        }

        else if ("spawn-npc".equalsIgnoreCase(args[0]) && sender instanceof Player) {
            NPC.Builder zHenna = new NPC.Builder(new Profile(UUID.randomUUID(), "zHenna", null));
            NPC npc = zHenna.build(npcPool);
            npc.getLocation().set(((Player) sender).getLocation().getX(), ((Player) sender).getLocation().getY(), ((Player) sender).getLocation().getZ());

            int random;

            do {
                 random = new Random().nextInt(200000);
            }
            while (npcMap.containsKey(random));

            npcMap.put(random, npc);
        }

        else if ("remove-npc".equalsIgnoreCase(args[0]) && sender instanceof Player) {
            if (npcMap.isEmpty() || args.length > 3)
                return true;
            Integer integer;
            try {
                integer = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return true;
            }

            if (!npcMap.containsKey(integer))
                return true;

            npcPool.removeNPC(npcMap.get(integer).getEntityId());
        }


        return true;
    }

    public static Map<Integer, NPC> getNpcMap() {
        return npcMap;
    }
}
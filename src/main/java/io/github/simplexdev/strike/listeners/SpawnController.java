package io.github.simplexdev.strike.listeners;

import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.api.utils.InventoryEditConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class SpawnController implements ConfigUser {
    private final JavaPlugin plugin;

    public SpawnController(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (player.getWorld().equals(Spawn.getWorld()))
            player.getInventory().clear();
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onRespawn(PlayerRespawnEvent e) {
        if (e.getPlayer().getWorld().equals(Spawn.getWorld())) {
            e.setRespawnLocation(Spawn.getSpawn());
            e.getPlayer().getInventory().setContents(new InventoryEditConfigManager(plugin).getInventory(e.getPlayer()));
        }
    }

    public void setSpawn(Location location) {
        Spawn.setSpawn(location, plugin);
        Spawn.setWorld(location.getWorld(), plugin);
    }

    public void refresh() {
        Spawn.loadConfig(this.plugin);
    }
}
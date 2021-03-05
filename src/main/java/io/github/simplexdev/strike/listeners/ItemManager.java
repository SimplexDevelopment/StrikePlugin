package io.github.simplexdev.strike.listeners;

import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.api.utils.InventoryEditConfigManager;
import io.github.simplexdev.strike.events.GrenadeKillEvent;
import io.github.simplexdev.strike.events.GunKillEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemManager implements ConfigUser {

    private final JavaPlugin plugin;
    private ItemStack healthPackage;
    private ItemStack grenade;

    public ItemManager(JavaPlugin plugin) {
        this.plugin = plugin;
        refresh();
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (player.getWorld().equals(Spawn.getWorld())) {

            addItem(player);

            if (player.getKiller() != null);
        }
    }

    @EventHandler
    private void onDeath(GunKillEvent e) {
        if (e.getDead() instanceof Player)
            addItem(e.getKiller());
    }

    @EventHandler
    private void onDeath(GrenadeKillEvent e) {
        addItem(e.getKiller());
    }

    private void addItem(Player player) {
        InventoryEditConfigManager configManager = new InventoryEditConfigManager(plugin);

        if (!hasItem(player.getInventory(), grenade))
            player.getInventory().setItem(configManager.getItemSlot(grenade, player), grenade);

        if (!hasItem(player.getInventory(), healthPackage))
            player.getInventory().setItem(configManager.getItemSlot(healthPackage, player), healthPackage);


    }

    private boolean hasItem(Inventory inventory, ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++)
            if (inventory.getItem(0).isSimilar(itemStack))
                return true;

        return false;
    }

    @Override
    public void refresh() {
        grenade = new Grenade(plugin).createItem();
        healthPackage = new HealthPackage(plugin).createItem();
    }
}

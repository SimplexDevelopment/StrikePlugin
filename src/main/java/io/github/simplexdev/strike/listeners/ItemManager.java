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
import org.bukkit.inventory.PlayerInventory;
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
    private void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (player.getWorld().equals(Spawn.getWorld())) {
            if (player.getKiller() != null)
                addItem(player.getKiller());
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

    public void addItem(Player player) {
        InventoryEditConfigManager configManager = new InventoryEditConfigManager(plugin);

        setItem(player, grenade, configManager);
        setItem(player, healthPackage, configManager);

        player.updateInventory();
    }

    private void setItem(Player player, ItemStack itemStack, InventoryEditConfigManager configManager) {
        PlayerInventory playerInventory = player.getInventory();

        if (!hasItem(playerInventory, itemStack)) {
            int slot = configManager.getItemSlot(itemStack, player);

            if (playerInventory.getItem(slot) != null)
                playerInventory.addItem(itemStack);
            else
                playerInventory.setItem(slot, itemStack);
        }
    }

    private boolean hasItem(Inventory inventory, ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++) {

            if (inventory.getItem(i) != null && inventory.getItem(i).isSimilar(itemStack))
                return true;
        }


        return false;
    }

    @Override
    public void refresh() {
        grenade = new Grenade(plugin).createItem();
        healthPackage = new HealthPackage(plugin).createItem();
    }
}

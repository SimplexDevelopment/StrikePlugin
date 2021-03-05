package io.github.simplexdev.strike.listeners;

import io.github.simplexdev.strike.api.utils.InventoryEditConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryEditGUI implements Listener {

    private static List<Inventory> inventories = new ArrayList<>();
    private final InventoryEditConfigManager configManager;
    private final JavaPlugin plugin;

    public InventoryEditGUI(JavaPlugin plugin) {
        configManager = new InventoryEditConfigManager(plugin);
        this.plugin = plugin;
    }


    public void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9);
        inventories.add(inventory);

        inventory.setContents(configManager.getInventory(player));
        player.openInventory(inventory);
    }


    @EventHandler
    private void onCloseInventory(InventoryCloseEvent e) {
        if (!inventories.contains(e.getInventory()))
            return;

        try {
            configManager.setInventory((Player) e.getPlayer(), e.getInventory().getContents());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory openedInventory = player.getOpenInventory().getTopInventory();

        if (inventories.contains(openedInventory) && !e.getClickedInventory().equals(openedInventory))
            e.setCancelled(true);

    }

}

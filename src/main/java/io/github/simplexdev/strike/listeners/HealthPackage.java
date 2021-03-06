package io.github.simplexdev.strike.listeners;

import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.api.utils.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public class HealthPackage implements ConfigUser {
    private final JavaPlugin plugin;
    private ItemStack healthPackage;
    private String usedMessage;
    private int regainHealth;

    public HealthPackage(JavaPlugin plugin) {
        this.plugin = plugin;
        refresh();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!player.getWorld().equals(Spawn.getWorld())) {
            return;
        }
        if (player.getInventory().getItemInMainHand().isSimilar(this.healthPackage)) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                return;
            }
            player.setHealth(this.regainHealth);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', usedMessage));

            if (item.getAmount() == 1) {
                player.getInventory().setItemInMainHand(null);
            } else {

                item.setAmount(item.getAmount() - 1);
            }
            e.setCancelled(true);
        }
    }

    public ItemStack createItem() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGExNTU4YTgzZjQwMjZkYjIzMmY4MGJjOTYxNWNjM2JhNDE1ZGM0MDk0MGE1YTEzYWUyYThjOTBiMTVjM2MzZSJ9fX0=";

        ItemStack healthPackage = SkullCreator.createSkull();
        ItemMeta meta = healthPackage.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("health-package.name")));

        healthPackage.setItemMeta(meta);

        healthPackage = SkullCreator.itemWithBase64(healthPackage, base64);

        return healthPackage.clone();
    }

    public void refresh() {
        this.usedMessage = plugin.getConfig().getString("health-package.restore-health-message", "You have restored your health");
        this.regainHealth = plugin.getConfig().getInt("health-package.restore-health");
        this.healthPackage = createItem();
    }
}
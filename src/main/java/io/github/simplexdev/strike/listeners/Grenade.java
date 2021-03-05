package io.github.simplexdev.strike.listeners;

import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.events.GrenadeKillEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Grenade implements ConfigUser {
    private static final List<Item> items = new ArrayList<>();
    private static final Map<Player, List<Player>> map = new HashMap<>();
    private final ItemStack grenadeItem;
    private final JavaPlugin plugin;
    private final int explosionTime;

    public Grenade(JavaPlugin plugin) {
        this.plugin = plugin;
        this.grenadeItem = createItem();
        this.explosionTime = plugin.getConfig().getInt("grenade.explosion-time", 1);
    }


    public ItemStack createItem() {
        ItemStack stack = new ItemStack(Material.MAGMA_CREAM, 1);
        ItemMeta meta = this.plugin.getServer().getItemFactory().getItemMeta(Material.MAGMA_CREAM);

        meta.setDisplayName(ChatColor.RED + "Grenade");

        stack.setItemMeta(meta);

        this.plugin.getServer().getOnlinePlayers().forEach(player -> player.getInventory().addItem(new ItemStack[]{stack}));

        return stack;
    }


    @EventHandler
    private void throwGrenade(PlayerInteractEvent e) {
        ItemStack itemStack = e.getItem();
        final Player player = e.getPlayer();

        if (!player.getWorld().equals(Spawn.getWorld()) || itemStack == null || !itemStack.isSimilar(this.grenadeItem) || e.getAction().toString().startsWith("LEFT")) {
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (itemStack.getAmount() == 1) {
                itemStack = null;
            } else {
                itemStack.setAmount(itemStack.getAmount() - 1);
            }
            player.getInventory().setItemInMainHand(itemStack);
        }

        final Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.MAGMA_CREAM));
        item.setVelocity(player.getEyeLocation().getDirection().multiply(0.75D));

        (new BukkitRunnable() {
            public void run() {
                if (item != null) {
                    item.getLocation().createExplosion(4.0F, false, false);
                    Collection<LivingEntity> entities = item.getLocation().getNearbyLivingEntities(4.0D);

                    List<Player> players = new ArrayList<>();

                    entities.forEach(livingEntity -> {
                        if (livingEntity instanceof Player) {
                            players.add((Player) livingEntity);
                        }
                    });
                    Grenade.map.put(player, players);

                    Grenade.items.remove(item);
                    item.remove();
                }
            }
        }).runTaskLater(this.plugin, (20 * this.explosionTime));

        items.add(item);
    }


    @EventHandler
    private void onPlayerDamage(EntityDamageEvent e) {
        if (!e.getEntity().getWorld().equals(Spawn.getWorld()) || !(e.getEntity() instanceof Player))
            return;

        Player player = (Player) e.getEntity();
        Player killer = null;

        for (Map.Entry<Player, List<Player>> entry : map.entrySet()) {

            if (entry.getValue().contains(player)) {

                if (player.getHealth() <= e.getFinalDamage())
                    killer = entry.getKey();
                
                entry.getValue().remove(player);
            }
        }

        if (killer != null)
            Bukkit.getPluginManager().callEvent(new GrenadeKillEvent(killer, player));
    }

    @EventHandler
    private void cancelPickup(PlayerAttemptPickupItemEvent e) {
        if (!items.contains(e.getItem()))
            return;
        e.setCancelled(true);
    }

    public void refresh() {
    }
}
package io.github.simplexdev.strike.listeners;

import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.events.GunKillEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;

public class Gun implements ConfigUser {
    private static final HashMap<ItemStack, Integer> ammoMap = new HashMap<>();

    private final ItemStack gunItemStack;

    private final JavaPlugin plugin;

    private int maxAmmo;

    private int maxDistance;


    public Gun(JavaPlugin plugin) {
        this.plugin = plugin;
        this.gunItemStack = createItem();
        this.maxAmmo = plugin.getConfig().getInt("gun.ammo");
        this.maxDistance = plugin.getConfig().getInt("gun.range");
    }

    public ItemStack createItem() {
        ItemStack stack = new ItemStack(Material.IRON_HORSE_ARMOR, 1);
        ItemMeta meta = this.plugin.getServer().getItemFactory().getItemMeta(Material.IRON_HORSE_ARMOR);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("gun.name")));

        stack.setItemMeta(meta);

        this.plugin.getServer().getOnlinePlayers().forEach(player -> player.getInventory().addItem(new ItemStack[]{stack}));

        return stack.clone();
    }


    @EventHandler
    private void activeActionBar(PlayerItemHeldEvent e) {
        final Player player = e.getPlayer();

        if (!player.getWorld().equals(Spawn.getWorld()) || player.getInventory().getItem(e.getNewSlot()) == null || !player.getInventory().getItem(e.getNewSlot()).equals(this.gunItemStack)) {
            return;
        }
        (new BukkitRunnable() {
            public void run() {
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                if (!mainHandItem.isSimilar(Gun.this.gunItemStack)) {
                    cancel();

                    return;
                }
                String ammoText = (Gun.ammoMap.containsKey(mainHandItem) ? ((Integer) Gun.ammoMap.get(mainHandItem)).intValue() : Gun.this.maxAmmo) + " | " + Gun.this.maxAmmo;

                player.sendActionBar(ammoText);
            }
        }).runTaskTimer((Plugin) this.plugin, 0L, 7L);
    }


    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        final ItemStack itemStack = e.getItem();
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (!player.getWorld().equals(Spawn.getWorld()) || itemStack == null || !itemStack.equals(this.gunItemStack) || !action.toString().startsWith("RIGHT_CLICK")) {
            return;
        }
        int ammo = this.maxAmmo;

        if (!ammoMap.containsKey(itemStack)) {
            ammoMap.put(itemStack, Integer.valueOf(this.maxAmmo - 1));
        } else {

            ammo = ((Integer) ammoMap.get(itemStack)).intValue();

            if (ammo == 1) {
                (new BukkitRunnable() {
                    public void run() {
                        Gun.ammoMap.replace(itemStack, Integer.valueOf(Gun.this.maxAmmo));
                    }
                }).runTaskLater((Plugin) this.plugin, 20L * this.plugin.getConfig().getInt("gun.reload-time"));
            }

            if (((Integer) ammoMap.get(itemStack)).intValue() != 0) {
                ammoMap.replace(itemStack, Integer.valueOf(ammo - 1));
            }
        }
        if (ammo <= 0) {
            return;
        }
        player.sendMessage(String.valueOf(ammo));

        spawnParticle(player, player.getEyeLocation().clone(), 0.0D);
        Entity entity = player.getTargetEntity(this.maxDistance);

        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        double currentHealth = livingEntity.getHealth();
        double damageHealth = this.plugin.getConfig().getInt("gun.damage");

        if (currentHealth <= damageHealth) {
            Bukkit.getServer().getPluginManager().callEvent((Event) new GunKillEvent(player, livingEntity));
        }
        livingEntity.damage(damageHealth);
    }


    private void spawnParticle(Player player, Location location, double distance) {
        World world = location.getWorld();

        location.add(location.getDirection().multiply(0.1D));
        world.spawnParticle(Particle.CRIT, location, 1, 0.0D, 0.0D, 0.0D, 0.001D);

        distance += 0.1D;

        if (location.getBlock().getType() != Material.AIR || distance > this.maxDistance) {
            return;
        }
        Collection<LivingEntity> entities = location.getNearbyLivingEntities(0.1D);

        if (!entities.isEmpty() && entities.size() == 1 && !entities.contains(player)) {
            return;
        }
        spawnParticle(player, location, distance);
    }


    public void refresh() {
        this.maxAmmo = this.plugin.getConfig().getInt("gun.ammo");
        this.maxDistance = this.plugin.getConfig().getInt("gun.range");
    }
}

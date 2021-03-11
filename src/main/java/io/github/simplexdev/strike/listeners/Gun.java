package io.github.simplexdev.strike.listeners;

import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.api.events.GunKillEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Gun implements ConfigUser {
    private static final HashMap<Player, Integer> ammoMap = new HashMap<>();
    private static final List<Material> passThroughBlocks = setPassThroughBlocks();
    private static List<Player> delay = new ArrayList<>();

    private final ItemStack gunItemStack;

    private final JavaPlugin plugin;

    private int maxAmmo;

    private int maxDistance;

    private static List<Material> setPassThroughBlocks() {

        ArrayList<Material> blocks = new ArrayList<>();
        blocks.add(Material.TALL_GRASS);
        blocks.add(Material.IRON_BARS);
        blocks.add(Material.WATER);
        blocks.add(Material.LAVA);
        blocks.add(Material.AIR);
        blocks.add(Material.COBWEB);

        for (Material material : Material.values()) {
            if (
                    material.toString().endsWith("FENCE")
                            || material.toString().endsWith("TORCH")
                            || material.toString().endsWith("GATE")
                            || material.toString().endsWith("LEAVES")
                            || material.toString().endsWith("PRESSURE_PLATE")
                            || material.toString().endsWith("BUTTON")
                            || material.toString().endsWith("GLASS")
                            || material.toString().endsWith("GLASS_PANE")
                            || material.toString().endsWith("TRAPDOOR")
            )
                blocks.add(material);
        }

        return blocks;
    }


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
                String ammoText = (Gun.ammoMap.containsKey(player) ? Gun.ammoMap.get(player).intValue() : Gun.this.maxAmmo) + " | " + Gun.this.maxAmmo;

                player.sendActionBar(ammoText);
            }
        }).runTaskTimer(this.plugin, 0L, 7L);
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent e) {
        if (ammoMap.containsKey(e.getEntity()))
            ammoMap.replace(e.getEntity(), maxAmmo);
    }


    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        final ItemStack itemStack = e.getItem();
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (!player.getWorld().equals(Spawn.getWorld()) || itemStack == null || !itemStack.equals(this.gunItemStack) || delay.contains(player) || !action.toString().startsWith("RIGHT_CLICK")) {
            return;
        }
        int ammo = this.maxAmmo;

        if (!ammoMap.containsKey(player)) {
            ammoMap.put(player, Integer.valueOf(this.maxAmmo - 1));
        } else {

            ammo = ammoMap.get(player).intValue();

            if (ammo == 1) {
                (new BukkitRunnable() {
                    public void run() {
                        Gun.ammoMap.replace(player, Integer.valueOf(Gun.this.maxAmmo));
                    }
                }).runTaskLater(this.plugin, 20L * this.plugin.getConfig().getInt("gun.reload-time"));
            }

            if (ammoMap.get(player).intValue() != 0) {
                ammoMap.replace(player, Integer.valueOf(ammo - 1));
            }
        }
        if (ammo <= 0) {
            return;
        }
        delay.add(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                delay.remove(player);
            }
        }.runTaskLater(plugin, 2);

        Entity entity = getEntity(player, player.getEyeLocation().clone(), 0.0D);

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


    private Entity getEntity(Player player, Location location, double distance) {
        World world = location.getWorld();

        location.add(location.getDirection().multiply(0.1D));
        world.spawnParticle(Particle.CRIT, location, 1, 0.0D, 0.0D, 0.0D, 0.001D);

        distance += 0.1D;

        if (!passThroughBlocks.contains(location.getBlock().getType()) || distance > this.maxDistance) {
            return null;
        }
        Collection<LivingEntity> entities = location.getNearbyLivingEntities(0.1D);

        if (!entities.isEmpty() && entities.size() == 1 && !entities.contains(player)) {
            return entities.iterator().next();
        }
        Entity entity = getEntity(player, location, distance);

        return entity;
    }


    public void refresh() {
        this.maxAmmo = this.plugin.getConfig().getInt("gun.ammo");
        this.maxDistance = this.plugin.getConfig().getInt("gun.range");
    }
}

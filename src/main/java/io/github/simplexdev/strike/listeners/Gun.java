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

    private final ItemStack gunItemStack;

    private final JavaPlugin plugin;

    private int maxAmmo;

    private int maxDistance;

    private static List<Material> setPassThroughBlocks() {
        ArrayList<Material> blocks = new ArrayList<>();
        blocks.add(Material.TALL_GRASS);
        blocks.add(Material.IRON_BARS);
        blocks.add(Material.ACACIA_FENCE);
        blocks.add(Material.BIRCH_FENCE);
        blocks.add(Material.CRIMSON_FENCE);
        blocks.add(Material.JUNGLE_FENCE);
        blocks.add(Material.OAK_FENCE);
        blocks.add(Material.SPRUCE_FENCE);
        blocks.add(Material.WARPED_FENCE);
        blocks.add(Material.DARK_OAK_FENCE);
        blocks.add(Material.NETHER_BRICK_FENCE);
        blocks.add(Material.WALL_TORCH);
        blocks.add(Material.TORCH);
        blocks.add(Material.REDSTONE_TORCH);
        blocks.add(Material.REDSTONE_WALL_TORCH);
        blocks.add(Material.SOUL_TORCH);
        blocks.add(Material.ACACIA_FENCE_GATE);
        blocks.add(Material.BIRCH_FENCE_GATE);
        blocks.add(Material.CRIMSON_FENCE_GATE);
        blocks.add(Material.JUNGLE_FENCE_GATE);
        blocks.add(Material.OAK_FENCE_GATE);
        blocks.add(Material.SPRUCE_FENCE_GATE);
        blocks.add(Material.WARPED_FENCE_GATE);
        blocks.add(Material.DARK_OAK_FENCE_GATE);
        blocks.add(Material.WATER);
        blocks.add(Material.LAVA);
        blocks.add(Material.AIR);

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
                String ammoText = (Gun.ammoMap.containsKey(player) ? ((Integer) Gun.ammoMap.get(player)).intValue() : Gun.this.maxAmmo) + " | " + Gun.this.maxAmmo;

                player.sendActionBar(ammoText);
            }
        }).runTaskTimer((Plugin) this.plugin, 0L, 7L);
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

        if (!player.getWorld().equals(Spawn.getWorld()) || itemStack == null || !itemStack.equals(this.gunItemStack) || !action.toString().startsWith("RIGHT_CLICK")) {
            return;
        }
        int ammo = this.maxAmmo;

        if (!ammoMap.containsKey(player)) {
            ammoMap.put(player, Integer.valueOf(this.maxAmmo - 1));
        } else {

            ammo = ((Integer) ammoMap.get(player)).intValue();

            if (ammo == 1) {
                (new BukkitRunnable() {
                    public void run() {
                        Gun.ammoMap.replace(player, Integer.valueOf(Gun.this.maxAmmo));
                    }
                }).runTaskLater((Plugin) this.plugin, 20L * this.plugin.getConfig().getInt("gun.reload-time"));
            }

            if (((Integer) ammoMap.get(player)).intValue() != 0) {
                ammoMap.replace(player, Integer.valueOf(ammo - 1));
            }
        }
        if (ammo <= 0) {
            return;
        }

        Entity entity = getEntity(player, player.getEyeLocation().clone(), 0.0D);

        System.out.println(entity);

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

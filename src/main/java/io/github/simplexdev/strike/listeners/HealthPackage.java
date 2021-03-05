 package io.github.simplexdev.strike.listeners;

 import io.github.simplexdev.strike.api.ConfigUser;
 import io.github.simplexdev.strike.api.Spawn;
 import io.github.simplexdev.strike.events.GrenadeKillEvent;
 import io.github.simplexdev.strike.events.GunKillEvent;
 import io.github.simplexdev.strike.api.utils.SkullCreator;
 import org.bukkit.ChatColor;
 import org.bukkit.Material;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.block.Action;
 import org.bukkit.event.entity.PlayerDeathEvent;
 import org.bukkit.event.player.PlayerInteractEvent;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 import org.bukkit.plugin.java.JavaPlugin;

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

         ItemStack healthPackage = SkullCreator.itemWithUrl(SkullCreator.createSkull(), base64);
         ItemMeta meta = plugin.getServer().getItemFactory().getItemMeta(Material.PLAYER_HEAD);

         meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("health-package.name")));

         healthPackage.setItemMeta(meta);

         return healthPackage.clone();
     }

     @EventHandler
     public void onDeath(PlayerDeathEvent e) {
         Player player = e.getEntity();

         if (player.getWorld().equals(Spawn.getWorld())) {

             if (player.getKiller() != null)
                 player.getKiller().getInventory().addItem(new ItemStack[]{this.healthPackage});

             e.getDrops().clear();
         }
     }

     @EventHandler
     private void onDeath(GunKillEvent e) {
         if (e.getDead() instanceof Player)
             e.getKiller().getInventory().addItem(new ItemStack[]{this.healthPackage});
     }

     @EventHandler
     private void onDeath(GrenadeKillEvent e) {
         e.getKiller().getInventory().addItem(this.healthPackage);
     }

     public void refresh() {
         this.usedMessage = plugin.getConfig().getString("health-package.restore-health-message", "You have restored your health");
         this.regainHealth = plugin.getConfig().getInt("health-package.restore-health");
         this.healthPackage = createItem();
     }
 }
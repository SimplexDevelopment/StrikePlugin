/*    */ package io.github.simplexdev.simplexcore.strike.listeners;
/*    */ 
/*    */ import io.github.simplexdev.simplexcore.strike.api.ConfigUser;
/*    */ import io.github.simplexdev.simplexcore.strike.api.Spawn;
/*    */ import io.github.simplexdev.simplexcore.strike.events.GunKillEvent;
/*    */ import io.github.simplexdev.simplexcore.strike.utils.SkullCreator;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.block.Action;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ public class HealthPackage
/*    */   implements ConfigUser
/*    */ {
/*    */   private final ItemStack healthPackage;
/*    */   private final JavaPlugin plugin;
/*    */   private String usedMessage;
/*    */   private int regainHealth;
/*    */   
/*    */   public HealthPackage(JavaPlugin plugin) {
/* 26 */     this.plugin = plugin;
/* 27 */     this.usedMessage = plugin.getConfig().getString("health-package.restore-health-message", "You have restored your health");
/* 28 */     this.regainHealth = plugin.getConfig().getInt("health-package.restore-health");
/* 29 */     this.healthPackage = createHealthPackage();
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onInteract(PlayerInteractEvent e) {
/* 34 */     Player player = e.getPlayer();
/* 35 */     ItemStack item = player.getInventory().getItemInMainHand();
/*    */     
/* 37 */     if (!player.getWorld().equals(Spawn.getWorld())) {
/*    */       return;
/*    */     }
/* 40 */     if (player.getInventory().getItemInMainHand().isSimilar(this.healthPackage)) {
/* 41 */       if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
/*    */         return;
/*    */       }
/* 44 */       player.setHealth(this.regainHealth);
/* 45 */       player.sendMessage(ChatColor.translateAlternateColorCodes('&', "usedMessage"));
/*    */       
/* 47 */       if (item.getAmount() == 1) {
/* 48 */         player.getInventory().setItemInMainHand(null);
/*    */       } else {
/*    */         
/* 51 */         item.setAmount(item.getAmount() - 1);
/*    */       } 
/* 53 */       e.setCancelled(true);
/*    */     } 
/*    */   }
/*    */   public ItemStack createHealthPackage() {
/* 57 */     String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGExNTU4YTgzZjQwMjZkYjIzMmY4MGJjOTYxNWNjM2JhNDE1ZGM0MDk0MGE1YTEzYWUyYThjOTBiMTVjM2MzZSJ9fX0=";
/*    */     
/* 59 */     ItemStack healthPackage = SkullCreator.itemFromBase64(base64);
/* 60 */     ItemMeta meta = healthPackage.getItemMeta();
/* 61 */     meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("health-package.name")));
/* 62 */     healthPackage.setItemMeta(meta);
/*    */     
/* 64 */     return healthPackage;
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onDeath(PlayerDeathEvent e) {
/* 69 */     Player player = e.getEntity();
/*    */     
/* 71 */     if (!player.getWorld().equals(Spawn.getWorld())) {
/*    */       return;
/*    */     }
/* 74 */     if (player.getWorld().equals(Spawn.getWorld())) {
/* 75 */       player.getKiller().getInventory().addItem(new ItemStack[] { this.healthPackage });
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   private void onDeath(GunKillEvent e) {
/* 81 */     if (e.getDead() instanceof Player)
/* 82 */       e.getKiller().getInventory().addItem(new ItemStack[] { this.healthPackage }); 
/*    */   }
/*    */   
/*    */   public void refresh() {}
/*    */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\listeners\HealthPackage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
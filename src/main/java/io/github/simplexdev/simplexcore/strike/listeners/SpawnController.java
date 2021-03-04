/*    */ package io.github.simplexdev.simplexcore.strike.listeners;
/*    */ 
/*    */ import io.github.simplexdev.simplexcore.strike.api.ConfigUser;
/*    */ import io.github.simplexdev.simplexcore.strike.api.Spawn;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ import org.bukkit.event.player.PlayerRespawnEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ 
/*    */ public class SpawnController
/*    */   implements ConfigUser
/*    */ {
/*    */   private final JavaPlugin plugin;
/*    */   
/*    */   public SpawnController(JavaPlugin plugin) {
/* 21 */     this.plugin = plugin;
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   private void onDeath(PlayerDeathEvent e) {
/* 26 */     Player player = e.getEntity();
/*    */     
/* 28 */     if (player.getWorld().equals(Spawn.getWorld()))
/* 29 */       player.getInventory().clear(); 
/*    */   }
/*    */   
/*    */   @EventHandler(priority = EventPriority.HIGH)
/*    */   private void onRespawn(PlayerRespawnEvent e) {
/* 34 */     if (e.getPlayer().getWorld().equals(Spawn.getWorld())) {
/* 35 */       e.setRespawnLocation(Spawn.getSpawn());
/* 36 */       giveItems(e.getPlayer());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void giveItems(Player player) {
/* 42 */     player.getInventory().addItem(new ItemStack[] { (new Gun(this.plugin)).createItem() });
/* 43 */     player.getInventory().addItem(new ItemStack[] { (new Grenade(this.plugin)).createItem() });
/* 44 */     player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.WOODEN_SWORD) });
/*    */   }
/*    */ 
/*    */   
/*    */   public void refresh() {
/* 49 */     Spawn.loadConfig(this.plugin);
/*    */   }
/*    */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\listeners\SpawnController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
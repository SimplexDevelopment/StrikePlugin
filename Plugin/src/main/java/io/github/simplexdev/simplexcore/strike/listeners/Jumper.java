/*    */ package io.github.simplexdev.simplexcore.strike.listeners;
/*    */ 
/*    */ import com.destroystokyo.paper.event.player.PlayerJumpEvent;
/*    */ import io.github.simplexdev.simplexcore.strike.api.ConfigUser;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.GameMode;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.player.PlayerToggleFlightEvent;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ 
/*    */ public final class Jumper
/*    */   implements ConfigUser
/*    */ {
/* 20 */   private static final Map<UUID, Long> playersOnCoolDown = new HashMap<>();
/*    */   private final JavaPlugin plugin;
/*    */   private Integer coolDownTime;
/*    */   
/*    */   public Jumper(JavaPlugin plugin) {
/* 25 */     this.plugin = plugin;
/* 26 */     this.coolDownTime = Integer.valueOf(plugin.getConfig().getInt("double-jump.cooldown"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   private void onPlayerJump(PlayerJumpEvent e) {
/* 33 */     Player player = e.getPlayer();
/* 34 */     GameMode mode = player.getGameMode();
/*    */     
/* 36 */     if (mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR) {
/*    */       return;
/*    */     }
/* 39 */     if (playersOnCoolDown.containsKey(player.getUniqueId())) {
/*    */       return;
/*    */     }
/* 42 */     player.setAllowFlight(true);
/*    */   }
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   private void onFlightAccessChange(PlayerToggleFlightEvent e) {
/* 48 */     final Player player = e.getPlayer();
/* 49 */     GameMode mode = player.getGameMode();
/*    */     
/* 51 */     if (mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR) {
/*    */       return;
/*    */     }
/* 54 */     e.setCancelled(true);
/* 55 */     player.setAllowFlight(false);
/*    */     
/* 57 */     player.setVelocity(player.getLocation().getDirection().multiply(0.5D).setY(0.5D));
/*    */     
/* 59 */     playersOnCoolDown.put(player.getPlayer().getUniqueId(), Long.valueOf((this.coolDownTime.intValue() * 1000) + System.currentTimeMillis()));
/*    */     
/* 61 */     (new BukkitRunnable()
/*    */       {
/*    */         public void run() {
/* 64 */           Jumper.playersOnCoolDown.remove(player.getPlayer().getUniqueId());
/* 65 */           player.sendMessage(ChatColor.translateAlternateColorCodes('&', Jumper.this.plugin.getConfig().getString("double-jump.cooldown-finish-message")));
/*    */         }
/* 67 */       }).runTaskLater((Plugin)this.plugin, 20L * this.coolDownTime.intValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public void refresh() {
/* 72 */     this.coolDownTime = Integer.valueOf(this.plugin.getConfig().getInt("double-jump.cooldown"));
/*    */   }
/*    */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\listeners\Jumper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
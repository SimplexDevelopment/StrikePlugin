/*    */ package io.github.simplexdev.simplexcore.strike.events;
/*    */ 
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ 
/*    */ public class GunKillEvent
/*    */   extends Event {
/* 10 */   private static final HandlerList handlers = new HandlerList();
/*    */   private final Player killer;
/*    */   private final LivingEntity dead;
/*    */   
/*    */   public GunKillEvent(Player killer, LivingEntity dead) {
/* 15 */     this.killer = killer;
/* 16 */     this.dead = dead;
/*    */   }
/*    */   
/*    */   public Player getKiller() {
/* 20 */     return this.killer;
/*    */   }
/*    */   
/*    */   public LivingEntity getDead() {
/* 24 */     return this.dead;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 29 */     return handlers;
/*    */   }
/*    */ 
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 34 */     return handlers;
/*    */   }
/*    */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\events\GunKillEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
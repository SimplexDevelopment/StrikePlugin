/*    */ package io.github.simplexdev.simplexcore.strike.events;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ 
/*    */ public class GrenadeKillEvent
/*    */   extends Event
/*    */ {
/* 12 */   private static final HandlerList handlers = new HandlerList();
/*    */   private final Player killer;
/*    */   private final List<LivingEntity> deadList;
/*    */   
/*    */   public GrenadeKillEvent(Player killer, List<LivingEntity> deadList) {
/* 17 */     this.killer = killer;
/* 18 */     this.deadList = deadList;
/*    */   }
/*    */   
/*    */   public Player getKiller() {
/* 22 */     return this.killer;
/*    */   }
/*    */   
/*    */   public List<LivingEntity> getDeadList() {
/* 26 */     return this.deadList;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 31 */     return handlers;
/*    */   }
/*    */ 
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 36 */     return handlers;
/*    */   }
/*    */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\events\GrenadeKillEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package io.github.simplexdev.simplexcore.strike.events;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ 
/*    */ public class UseHealthPackageEvent
/*    */   extends Event {
/*  9 */   private static final HandlerList handlers = new HandlerList();
/*    */   private final Player player;
/*    */   
/*    */   public UseHealthPackageEvent(Player player) {
/* 13 */     this.player = player;
/*    */   }
/*    */   
/*    */   public Player getPlayer() {
/* 17 */     return this.player;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 22 */     return handlers;
/*    */   }
/*    */ 
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 27 */     return handlers;
/*    */   }
/*    */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\events\UseHealthPackageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
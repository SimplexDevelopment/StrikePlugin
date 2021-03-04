/*    */ package io.github.simplexdev.simplexcore.strike;
/*    */ 
/*    */ import io.github.simplexdev.simplexcore.strike.api.ConfigUser;
/*    */ import io.github.simplexdev.simplexcore.strike.api.Spawn;
/*    */ import io.github.simplexdev.simplexcore.strike.listeners.Grenade;
/*    */ import io.github.simplexdev.simplexcore.strike.listeners.Gun;
/*    */ import io.github.simplexdev.simplexcore.strike.listeners.Jumper;
/*    */ import io.github.simplexdev.simplexcore.strike.listeners.SpawnController;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class StrikePlugin
/*    */   extends JavaPlugin
/*    */ {
/*    */   public void onEnable() {
/* 32 */     saveDefaultConfig();
/* 33 */     Spawn.loadConfig(this);
/*    */     
/* 35 */     Gun gun = new Gun(this);
/* 36 */     Jumper jumper = new Jumper(this);
/* 37 */     SpawnController spawnController = new SpawnController(this);
/*    */     
/* 39 */     getServer().getPluginManager().registerEvents((Listener)gun, (Plugin)this);
/* 40 */     getServer().getPluginManager().registerEvents((Listener)jumper, (Plugin)this);
/*    */     
/* 42 */     getServer().getPluginManager().registerEvents((Listener)new Grenade(this), (Plugin)this);
/* 43 */     getServer().getPluginManager().registerEvents((Listener)spawnController, (Plugin)this);
/*    */     
/* 45 */     getCommand("strike").setExecutor(new StrikeCommand(this));
/*    */     
/* 47 */     StrikeCommand.loadInstances(new ConfigUser[] { (ConfigUser)gun, (ConfigUser)jumper, (ConfigUser)spawnController });
/*    */   }
/*    */   
/*    */   public void onDisable() {}
/*    */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\StrikePlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
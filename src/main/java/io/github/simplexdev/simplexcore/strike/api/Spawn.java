/*    */ package io.github.simplexdev.simplexcore.strike.api;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ public class Spawn {
/*  9 */   private static World world = null;
/* 10 */   private static Location spawn = null;
/*    */   
/*    */   public static void loadConfig(JavaPlugin plugin) {
/* 13 */     FileConfiguration config = plugin.getConfig();
/*    */     
/* 15 */     world = plugin.getServer().getWorld(config.getString("spawn.world"));
/* 16 */     spawn = new Location(world, config.getInt("spawn.location.x"), config.getInt("spawn.location.y"), config.getInt("spawn.location.z"));
/*    */   }
/*    */   
/*    */   public static void setSpawn(Location spawn, JavaPlugin plugin) {
/* 20 */     FileConfiguration config = plugin.getConfig();
/* 21 */     config.set("spawn.coords.x", Double.valueOf(spawn.getX()));
/* 22 */     config.set("spawn.coords.y", Double.valueOf(spawn.getY()));
/* 23 */     config.set("spawn.coords.z", Double.valueOf(spawn.getZ()));
/*    */     
/* 25 */     Spawn.spawn = spawn;
/*    */   }
/*    */   
/*    */   public static void setWorld(World world, JavaPlugin plugin) {
/* 29 */     plugin.getConfig().set("spawn.world", world.getName());
/* 30 */     Spawn.world = world;
/*    */   }
/*    */   
/*    */   public static World getWorld() {
/* 34 */     return world;
/*    */   }
/*    */   
/*    */   public static Location getSpawn() {
/* 38 */     return spawn;
/*    */   }
/*    */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\api\Spawn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
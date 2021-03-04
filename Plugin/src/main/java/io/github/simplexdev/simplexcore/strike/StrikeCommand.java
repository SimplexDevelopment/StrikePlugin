/*    */ package io.github.simplexdev.simplexcore.strike;
/*    */ 
/*    */ import io.github.simplexdev.simplexcore.strike.api.ConfigUser;
/*    */ import java.util.Arrays;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ 
/*    */ public class StrikeCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   private static ConfigUser[] configUsers;
/*    */   private final JavaPlugin plugin;
/*    */   
/*    */   public StrikeCommand(JavaPlugin plugin) {
/* 18 */     this.plugin = plugin;
/*    */   }
/*    */   
/*    */   public static void loadInstances(ConfigUser... configUsers) {
/* 22 */     StrikeCommand.configUsers = configUsers;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
/* 28 */     if (args[0].isEmpty() || args.length > 1) {
/* 29 */       return true;
/*    */     }
/* 31 */     switch (args[0].toLowerCase()) {
/*    */       case "reload":
/* 33 */         this.plugin.reloadConfig();
/* 34 */         Arrays.<ConfigUser>stream(configUsers).forEach(configUser -> configUser.refresh());
/*    */         break;
/*    */     } 
/*    */ 
/*    */     
/* 39 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\StrikeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
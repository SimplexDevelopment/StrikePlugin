/*     */ package io.github.simplexdev.simplexcore.strike.listeners;
/*     */ 
/*     */ import io.github.simplexdev.simplexcore.strike.api.ConfigUser;
/*     */ import io.github.simplexdev.simplexcore.strike.api.Spawn;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class Grenade
/*     */   implements ConfigUser {
/*  28 */   private static List<Item> items = new ArrayList<>();
/*  29 */   private static Map<Player, List<Player>> map = new HashMap<>();
/*     */   private final ItemStack grenadeItem;
/*     */   private final JavaPlugin plugin;
/*     */   private int explosionTime;
/*     */   
/*     */   public Grenade(JavaPlugin plugin) {
/*  35 */     this.plugin = plugin;
/*  36 */     this.grenadeItem = createItem();
/*  37 */     this.explosionTime = plugin.getConfig().getInt("grenade.explosion-time", 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack createItem() {
/*  42 */     ItemStack stack = new ItemStack(Material.MAGMA_CREAM, 1);
/*  43 */     ItemMeta meta = this.plugin.getServer().getItemFactory().getItemMeta(Material.MAGMA_CREAM);
/*     */     
/*  45 */     meta.setDisplayName(ChatColor.RED + "Grenade");
/*     */     
/*  47 */     stack.setItemMeta(meta);
/*     */     
/*  49 */     this.plugin.getServer().getOnlinePlayers().forEach(player -> player.getInventory().addItem(new ItemStack[] { stack }));
/*     */     
/*  51 */     return stack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   private void throwGrenade(PlayerInteractEvent e) {
/*  58 */     ItemStack itemStack = e.getItem();
/*  59 */     final Player player = e.getPlayer();
/*     */     
/*  61 */     if (!player.getWorld().equals(Spawn.getWorld()) || itemStack == null || !itemStack.isSimilar(this.grenadeItem) || e.getAction().toString().startsWith("LEFT")) {
/*     */       return;
/*     */     }
/*  64 */     if (player.getGameMode() != GameMode.CREATIVE) {
/*  65 */       if (itemStack.getAmount() == 1) {
/*  66 */         itemStack = null;
/*     */       } else {
/*  68 */         itemStack.setAmount(itemStack.getAmount() - 1);
/*     */       } 
/*  70 */       player.getInventory().setItemInMainHand(itemStack);
/*     */     } 
/*     */     
/*  73 */     final Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.MAGMA_CREAM));
/*  74 */     item.setVelocity(player.getEyeLocation().getDirection().multiply(0.75D));
/*     */     
/*  76 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/*  79 */           if (item != null) {
/*  80 */             item.getLocation().createExplosion(4.0F, false, false);
/*  81 */             Collection<LivingEntity> entities = item.getLocation().getNearbyLivingEntities(4.0D);
/*     */             
/*  83 */             List<Player> players = new ArrayList<>();
/*     */             
/*  85 */             entities.forEach(livingEntity -> {
/*     */                   if (livingEntity instanceof Player) {
/*     */                     players.add((Player)livingEntity);
/*     */                   }
/*     */                 });
/*  90 */             Grenade.map.put(player, players);
/*     */             
/*  92 */             Grenade.items.remove(item);
/*  93 */             item.remove();
/*     */           } 
/*     */         }
/*  96 */       }).runTaskLater((Plugin)this.plugin, (20 * this.explosionTime));
/*     */     
/*  98 */     items.add(item);
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   private void onPlayerDamage(EntityDamageEvent e) {
/* 104 */     if (!e.getEntity().getWorld().equals(Spawn.getWorld()) || !(e.getEntity() instanceof Player)) {
/*     */       return;
/*     */     }
/*     */     
/* 108 */     Player player = (Player)e.getEntity();
/*     */     
/* 110 */     map.values().forEach(players -> System.out.println(players));
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   private void cancelPickup(PlayerAttemptPickupItemEvent e) {
/* 115 */     if (!items.contains(e.getItem()))
/*     */       return; 
/* 117 */     e.setCancelled(true);
/*     */   }
/*     */   
/*     */   public void refresh() {}
/*     */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\listeners\Grenade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
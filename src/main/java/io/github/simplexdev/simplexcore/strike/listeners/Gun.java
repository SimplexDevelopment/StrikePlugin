/*     */ package io.github.simplexdev.simplexcore.strike.listeners;
/*     */ import io.github.simplexdev.simplexcore.strike.api.Spawn;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public final class Gun implements ConfigUser {
/*  24 */   private static final HashMap<ItemStack, Integer> ammoMap = new HashMap<>();
/*     */   
/*     */   private final ItemStack gunItemStack;
/*     */   
/*     */   private final JavaPlugin plugin;
/*     */   
/*     */   private int maxAmmo;
/*     */   
/*     */   private int maxDistance;
/*     */ 
/*     */   
/*     */   public Gun(JavaPlugin plugin) {
/*  36 */     this.plugin = plugin;
/*  37 */     this.gunItemStack = createItem();
/*  38 */     this.maxAmmo = plugin.getConfig().getInt("gun.ammo");
/*  39 */     this.maxDistance = plugin.getConfig().getInt("gun.range");
/*     */   }
/*     */   
/*     */   public ItemStack createItem() {
/*  43 */     ItemStack stack = new ItemStack(Material.IRON_HORSE_ARMOR, 1);
/*  44 */     ItemMeta meta = this.plugin.getServer().getItemFactory().getItemMeta(Material.IRON_HORSE_ARMOR);
/*     */     
/*  46 */     meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("gun.name")));
/*     */     
/*  48 */     stack.setItemMeta(meta);
/*     */     
/*  50 */     this.plugin.getServer().getOnlinePlayers().forEach(player -> player.getInventory().addItem(new ItemStack[] { stack }));
/*     */     
/*  52 */     return stack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   private void activeActionBar(PlayerItemHeldEvent e) {
/*  75 */     final Player player = e.getPlayer();
/*     */     
/*  77 */     if (!player.getWorld().equals(Spawn.getWorld()) || player.getInventory().getItem(e.getNewSlot()) == null || !player.getInventory().getItem(e.getNewSlot()).equals(this.gunItemStack)) {
/*     */       return;
/*     */     }
/*  80 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/*  83 */           ItemStack mainHandItem = player.getInventory().getItemInMainHand();
/*  84 */           if (!mainHandItem.isSimilar(Gun.this.gunItemStack)) {
/*  85 */             cancel();
/*     */             
/*     */             return;
/*     */           } 
/*  89 */           String ammoText = (Gun.ammoMap.containsKey(mainHandItem) ? ((Integer)Gun.ammoMap.get(mainHandItem)).intValue() : Gun.this.maxAmmo) + " | " + Gun.this.maxAmmo;
/*     */           
/*  91 */           player.sendActionBar(ammoText);
/*     */         }
/*  94 */       }).runTaskTimer((Plugin)this.plugin, 0L, 7L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   private void onRightClick(PlayerInteractEvent e) {
/* 101 */     final ItemStack itemStack = e.getItem();
/* 102 */     Player player = e.getPlayer();
/* 103 */     Action action = e.getAction();
/*     */     
/* 105 */     if (!player.getWorld().equals(Spawn.getWorld()) || itemStack == null || !itemStack.equals(this.gunItemStack) || !action.toString().startsWith("RIGHT_CLICK")) {
/*     */       return;
/*     */     }
/* 108 */     int ammo = this.maxAmmo;
/*     */     
/* 110 */     if (!ammoMap.containsKey(itemStack)) {
/* 111 */       ammoMap.put(itemStack, Integer.valueOf(this.maxAmmo - 1));
/*     */     } else {
/*     */       
/* 114 */       ammo = ((Integer)ammoMap.get(itemStack)).intValue();
/*     */       
/* 116 */       if (ammo == 1)
/*     */       {
/* 118 */         (new BukkitRunnable()
/*     */           {
/*     */             public void run() {
/* 121 */               Gun.ammoMap.replace(itemStack, Integer.valueOf(Gun.this.maxAmmo));
/*     */             }
/* 123 */           }).runTaskLater((Plugin)this.plugin, 20L * this.plugin.getConfig().getInt("gun.reload-time"));
/*     */       }
/*     */       
/* 126 */       if (((Integer)ammoMap.get(itemStack)).intValue() != 0) {
/* 127 */         ammoMap.replace(itemStack, Integer.valueOf(ammo - 1));
/*     */       }
/*     */     } 
/* 130 */     if (ammo <= 0) {
/*     */       return;
/*     */     }
/* 133 */     player.sendMessage(String.valueOf(ammo));
/*     */     
/* 135 */     spawnParticle(player, player.getEyeLocation().clone(), 0.0D);
/* 136 */     Entity entity = player.getTargetEntity(this.maxDistance);
/*     */     
/* 138 */     if (!(entity instanceof LivingEntity)) {
/*     */       return;
/*     */     }
/* 141 */     LivingEntity livingEntity = (LivingEntity)entity;
/* 142 */     double currentHealth = livingEntity.getHealth();
/* 143 */     double damageHealth = this.plugin.getConfig().getInt("gun.damage");
/*     */     
/* 145 */     if (currentHealth <= damageHealth) {
/* 146 */       Bukkit.getServer().getPluginManager().callEvent((Event)new GunKillEvent(player, livingEntity));
/*     */     }
/* 148 */     livingEntity.damage(damageHealth);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void spawnParticle(Player player, Location location, double distance) {
/* 154 */     World world = location.getWorld();
/*     */     
/* 156 */     location.add(location.getDirection().multiply(0.1D));
/* 157 */     world.spawnParticle(Particle.CRIT, location, 1, 0.0D, 0.0D, 0.0D, 0.001D);
/*     */     
/* 159 */     distance += 0.1D;
/*     */     
/* 161 */     if (location.getBlock().getType() != Material.AIR || distance > this.maxDistance) {
/*     */       return;
/*     */     }
/* 164 */     Collection<LivingEntity> entities = location.getNearbyLivingEntities(0.1D);
/*     */     
/* 166 */     if (!entities.isEmpty() && entities.size() == 1 && !entities.contains(player)) {
/*     */       return;
/*     */     }
/* 169 */     spawnParticle(player, location, distance);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void refresh() {
/* 175 */     this.maxAmmo = this.plugin.getConfig().getInt("gun.ammo");
/* 176 */     this.maxDistance = this.plugin.getConfig().getInt("gun.range");
/*     */   }
/*     */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strike\listeners\Gun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
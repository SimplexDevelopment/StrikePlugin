/*     */ package io.github.simplexdev.simplexcore.strike.utils;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Base64;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.SkullType;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Skull;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.SkullMeta;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SkullCreator
/*     */ {
/*     */   private static boolean warningPosted = false;
/*     */   private static Field blockProfileField;
/*     */   private static Method metaSetProfileMethod;
/*     */   private static Field metaProfileField;
/*     */   
/*     */   public static ItemStack createSkull() {
/*  36 */     checkLegacy();
/*     */     
/*     */     try {
/*  39 */       return new ItemStack(Material.valueOf("PLAYER_HEAD"));
/*  40 */     } catch (IllegalArgumentException e) {
/*  41 */       return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short)3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack itemFromName(String name) {
/*  53 */     return itemWithName(createSkull(), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack itemFromUuid(UUID id) {
/*  63 */     return itemWithUuid(createSkull(), id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack itemFromUrl(String url) {
/*  73 */     return itemWithUrl(createSkull(), url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack itemFromBase64(String base64) {
/*  83 */     return itemWithBase64(createSkull(), base64);
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
/*     */   @Deprecated
/*     */   public static ItemStack itemWithName(ItemStack item, String name) {
/*  96 */     notNull(item, "item");
/*  97 */     notNull(name, "name");
/*     */     
/*  99 */     SkullMeta meta = (SkullMeta)item.getItemMeta();
/* 100 */     meta.setOwner(name);
/* 101 */     item.setItemMeta((ItemMeta)meta);
/*     */     
/* 103 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack itemWithUuid(ItemStack item, UUID id) {
/* 114 */     notNull(item, "item");
/* 115 */     notNull(id, "id");
/*     */     
/* 117 */     SkullMeta meta = (SkullMeta)item.getItemMeta();
/* 118 */     meta.setOwner(Bukkit.getOfflinePlayer(id).getName());
/* 119 */     item.setItemMeta((ItemMeta)meta);
/*     */     
/* 121 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack itemWithUrl(ItemStack item, String url) {
/* 132 */     notNull(item, "item");
/* 133 */     notNull(url, "url");
/*     */     
/* 135 */     return itemWithBase64(item, urlToBase64(url));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack itemWithBase64(ItemStack item, String base64) {
/* 146 */     notNull(item, "item");
/* 147 */     notNull(base64, "base64");
/*     */     
/* 149 */     if (!(item.getItemMeta() instanceof SkullMeta)) {
/* 150 */       return null;
/*     */     }
/* 152 */     SkullMeta meta = (SkullMeta)item.getItemMeta();
/* 153 */     mutateItemMeta(meta, base64);
/* 154 */     item.setItemMeta((ItemMeta)meta);
/*     */     
/* 156 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void blockWithName(Block block, String name) {
/* 168 */     notNull(block, "block");
/* 169 */     notNull(name, "name");
/*     */     
/* 171 */     Skull state = (Skull)block.getState();
/* 172 */     state.setOwner(name);
/* 173 */     state.update(false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void blockWithUuid(Block block, UUID id) {
/* 183 */     notNull(block, "block");
/* 184 */     notNull(id, "id");
/*     */     
/* 186 */     setToSkull(block);
/* 187 */     Skull state = (Skull)block.getState();
/* 188 */     state.setOwner(Bukkit.getOfflinePlayer(id).getName());
/* 189 */     state.update(false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void blockWithUrl(Block block, String url) {
/* 199 */     notNull(block, "block");
/* 200 */     notNull(url, "url");
/*     */     
/* 202 */     blockWithBase64(block, urlToBase64(url));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void blockWithBase64(Block block, String base64) {
/* 212 */     notNull(block, "block");
/* 213 */     notNull(base64, "base64");
/*     */     
/* 215 */     setToSkull(block);
/* 216 */     Skull state = (Skull)block.getState();
/* 217 */     mutateBlockState(state, base64);
/* 218 */     state.update(false, false);
/*     */   }
/*     */   
/*     */   private static void setToSkull(Block block) {
/* 222 */     checkLegacy();
/*     */     
/*     */     try {
/* 225 */       block.setType(Material.valueOf("PLAYER_HEAD"), false);
/* 226 */     } catch (IllegalArgumentException e) {
/* 227 */       block.setType(Material.valueOf("SKULL"), false);
/* 228 */       Skull state = (Skull)block.getState();
/* 229 */       state.setSkullType(SkullType.PLAYER);
/* 230 */       state.update(false, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void notNull(Object o, String name) {
/* 235 */     if (o == null) {
/* 236 */       throw new NullPointerException(name + " should not be null!");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static String urlToBase64(String url) {
/*     */     URI actualUrl;
/*     */     try {
/* 244 */       actualUrl = new URI(url);
/* 245 */     } catch (URISyntaxException e) {
/* 246 */       throw new RuntimeException(e);
/*     */     } 
/* 248 */     String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl.toString() + "\"}}}";
/* 249 */     return Base64.getEncoder().encodeToString(toEncode.getBytes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static GameProfile makeProfile(String b64) {
/* 256 */     UUID id = new UUID(b64.substring(b64.length() - 20).hashCode(), b64.substring(b64.length() - 10).hashCode());
/*     */     
/* 258 */     GameProfile profile = new GameProfile(id, "aaaaa");
/* 259 */     profile.getProperties().put("textures", new Property("textures", b64));
/* 260 */     return profile;
/*     */   }
/*     */   
/*     */   private static void mutateBlockState(Skull block, String b64) {
/*     */     try {
/* 265 */       if (blockProfileField == null) {
/* 266 */         blockProfileField = block.getClass().getDeclaredField("profile");
/* 267 */         blockProfileField.setAccessible(true);
/*     */       } 
/* 269 */       blockProfileField.set(block, makeProfile(b64));
/* 270 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 271 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void mutateItemMeta(SkullMeta meta, String b64) {
/*     */     try {
/* 277 */       if (metaSetProfileMethod == null) {
/* 278 */         metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", new Class[] { GameProfile.class });
/* 279 */         metaSetProfileMethod.setAccessible(true);
/*     */       } 
/* 281 */       metaSetProfileMethod.invoke(meta, new Object[] { makeProfile(b64) });
/* 282 */     } catch (NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException ex) {
/*     */ 
/*     */       
/*     */       try {
/* 286 */         if (metaProfileField == null) {
/* 287 */           metaProfileField = meta.getClass().getDeclaredField("profile");
/* 288 */           metaProfileField.setAccessible(true);
/*     */         } 
/* 290 */         metaProfileField.set(meta, makeProfile(b64));
/*     */       }
/* 292 */       catch (NoSuchFieldException|IllegalAccessException ex2) {
/* 293 */         ex2.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkLegacy() {
/*     */     try {
/* 305 */       Material.class.getDeclaredField("PLAYER_HEAD");
/* 306 */       Material.valueOf("SKULL");
/*     */       
/* 308 */       if (!warningPosted) {
/* 309 */         Bukkit.getLogger().warning("SKULLCREATOR API - Using the legacy bukkit API with 1.13+ bukkit versions is not supported!");
/* 310 */         warningPosted = true;
/*     */       } 
/* 312 */     } catch (NoSuchFieldException|IllegalArgumentException noSuchFieldException) {}
/*     */   }
/*     */ }


/* Location:              E:\Rishi\Codes\Java Projects\Minecraft Plugins\PaperMC\1.16.4\Server Testing\plugins\strike-1.0-SNAPSHOT.jar!\io\github\simplexdev\simplexcore\strik\\utils\SkullCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
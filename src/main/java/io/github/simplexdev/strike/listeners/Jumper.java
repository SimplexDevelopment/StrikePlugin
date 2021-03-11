package io.github.simplexdev.strike.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.api.events.PlayerDoubleJumpEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Jumper implements ConfigUser {
    private static final Map<UUID, Long> playersOnCoolDown = new HashMap<>();
    private final JavaPlugin plugin;
    private Integer coolDownTime;

    public Jumper(JavaPlugin plugin) {
        this.plugin = plugin;
        this.coolDownTime = Integer.valueOf(plugin.getConfig().getInt("double-jump.cooldown"));
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;
        Player player = (Player) e.getEntity();
        if (!player.getWorld().equals(Spawn.getWorld()) || e.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;

        e.setCancelled(true);
    }

    @EventHandler
    private void onPlayerJump(PlayerJumpEvent e) {
        Player player = e.getPlayer();
        GameMode mode = player.getGameMode();

        if (!player.getWorld().equals(Spawn.getWorld()) || mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR)
            return;

        if (playersOnCoolDown.containsKey(player.getUniqueId()))
            return;

        player.setAllowFlight(true);

    }

    @EventHandler
    private void onFlightAccessChange(PlayerToggleFlightEvent e) {
        final Player player = e.getPlayer();
        GameMode mode = player.getGameMode();

        if (!player.getWorld().equals(Spawn.getWorld()) || mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR) {
            return;
        }
        player.setAllowFlight(false);
        e.setCancelled(true);


        double multiplier = plugin.getConfig().getDouble("double-jump.forward-distance");
        double yMultiplier = plugin.getConfig().getDouble("double-jump.upward-distance");

        Vector vector = player.getLocation().getDirection().multiply(multiplier).setY(yMultiplier);

        player.setVelocity(vector);

        Bukkit.getPluginManager().callEvent(new PlayerDoubleJumpEvent(player, vector));

        playersOnCoolDown.put(player.getPlayer().getUniqueId(), Long.valueOf((this.coolDownTime.intValue() * 1000) + System.currentTimeMillis()));

        (new BukkitRunnable() {
            public void run() {
                Jumper.playersOnCoolDown.remove(player.getPlayer().getUniqueId());
                if (plugin.getConfig().getBoolean("double-jump.message-enabled"))
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Jumper.this.plugin.getConfig().getString("double-jump.cooldown-finish-message")));

            }
        }).runTaskLater(this.plugin, 20L * this.coolDownTime.intValue());
    }

    @EventHandler
    private void onDoubleJump(PlayerDoubleJumpEvent e) {
        e.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, e.getPlayer().getLocation(), 1);
        e.getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1 , 1);
    }


    public void refresh() {
        this.coolDownTime = Integer.valueOf(this.plugin.getConfig().getInt("double-jump.cooldown"));
    }
}
package io.github.simplexdev.strike.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.Spawn;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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

        player.setVelocity(player.getLocation().getDirection().multiply(0.5D).setY(0.5D));

        playersOnCoolDown.put(player.getPlayer().getUniqueId(), Long.valueOf((this.coolDownTime.intValue() * 1000) + System.currentTimeMillis()));

        (new BukkitRunnable() {
            public void run() {
                Jumper.playersOnCoolDown.remove(player.getPlayer().getUniqueId());
                if (plugin.getConfig().getBoolean("double-jump.message-enabled"))
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Jumper.this.plugin.getConfig().getString("double-jump.cooldown-finish-message")));

            }
        }).runTaskLater((Plugin) this.plugin, 20L * this.coolDownTime.intValue());
    }


    public void refresh() {
        this.coolDownTime = Integer.valueOf(this.plugin.getConfig().getInt("double-jump.cooldown"));
    }
}
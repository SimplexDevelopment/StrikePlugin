package io.github.simplexdev.strike.listeners;

import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.utils.yml.manager.LaunchPad;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class LaunchPadListener implements ConfigUser {

    public static Map<Location, Integer> locationMap;
    private final JavaPlugin plugin;

    public LaunchPadListener(JavaPlugin plugin) {
        this.plugin = plugin;
        LaunchPad.init(plugin);
        locationMap = LaunchPad.getMap();
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        Location location = e.getPlayer().getLocation().getBlock().getLocation()/*.clone().subtract(0, -1, 0)*/;

        if (!locationMap.containsKey(location))
            return;

        Player player = e.getPlayer();

        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

        armorStand.setVisible(false);
        armorStand.addPassenger(player);
        armorStand.setVelocity(player.getEyeLocation().getDirection().multiply(4));

        Consumer<BukkitTask> consumer = bukkitTask -> killDriver(armorStand, bukkitTask);

        Bukkit.getScheduler().runTaskTimer(plugin, consumer, 5, 5);

    }


    private void killDriver(ArmorStand armorStand, BukkitTask task) {
        if (!armorStand.isOnGround())
            return;

        armorStand.remove();
        task.cancel();
    }

    @Override
    public void refresh() {

    }
}

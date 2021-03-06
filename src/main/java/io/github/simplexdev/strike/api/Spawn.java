package io.github.simplexdev.strike.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawn {
    private static World world = null;
    private static Location spawn = null;

    public static void loadConfig(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();

        world = plugin.getServer().getWorld(config.getString("spawn.world"));
        spawn = new Location(world, config.getInt("spawn.location.x"), config.getInt("spawn.location.y"), config.getInt("spawn.location.z"));
    }

    public static void setSpawn(Location spawn, JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        config.set("spawn.coords.x", spawn.getX());
        config.set("spawn.coords.y", spawn.getY());
        config.set("spawn.coords.z", spawn.getZ());

        plugin.saveConfig();

        Spawn.spawn = spawn;
    }

    public static void setWorld(World world, JavaPlugin plugin) {
        plugin.getConfig().set("spawn.world", world.getName());

        plugin.saveConfig();

        Spawn.world = world;
    }

    public static World getWorld() {
        return world;
    }

    public static Location getSpawn() {
        return spawn;
    }
}
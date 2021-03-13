package io.github.simplexdev.strike.api.utils.yml.manager;

import io.github.simplexdev.strike.listeners.LaunchPadListener;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LaunchPad {

    private static File ymlFile;
    private static FileConfiguration ymlConfig;


    public static void init(JavaPlugin plugin) {

        try {

            ymlFile = new File(plugin.getDataFolder().toString() + "\\launchPads.yml");

            if (!ymlFile.exists()) {
                ymlFile.getParentFile().mkdirs();
                ymlFile.createNewFile();
            }


            ymlConfig = new YamlConfiguration();

            ymlConfig.load(ymlFile);


        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static int getID(Location location) {
        for (Map.Entry<String, Object> entry : ymlConfig.getValues(false).entrySet()) {
            if (!(entry.getValue() instanceof Location))
                continue;

            if (entry.getValue().equals(location))
                return Integer.parseInt(entry.getKey());
        }

        return 0;
    }

    public static Map<Location, Integer> getMap() {
        HashMap<Location, Integer> map = new HashMap<>();

        for (Map.Entry<String, Object> entry : ymlConfig.getValues(false).entrySet()) {
            if (entry.getValue() instanceof Location)
                map.put((Location) entry.getValue(), Integer.parseInt(entry.getKey()));
        }

        return map;
    }

    public static void addLocation(Location location, int id) {
        do {
            id = new Random().nextInt(200000);
        }
        while (id == 0 || LaunchPadListener.locationMap.containsKey(id));

        ymlConfig.set(String.valueOf(id), location);
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getLocation(int id) {
        return ymlConfig.getLocation(String.valueOf(id));
    }
}
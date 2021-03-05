package io.github.simplexdev.strike.api.utils;

import io.github.simplexdev.strike.listeners.Grenade;
import io.github.simplexdev.strike.listeners.Gun;
import io.github.simplexdev.strike.listeners.HealthPackage;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class InventoryEditConfigManager {

    private final JavaPlugin plugin;
    private final FileConfiguration dataConfig;
    private final File dataFile;

    public InventoryEditConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;

        dataFile = new File("plugins\\Strike\\inventories.yml");

        if (!dataFile.isFile()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dataConfig = new YamlConfiguration();
        try {
            dataConfig.load(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    public void setInventory(Player player, ItemStack[] items) throws IOException {

        if (items.length != 9)
            throw new IllegalArgumentException("The length of the items can be only 9");

        for (int i = 0; i < items.length; i++) {
            dataConfig.set(player.getUniqueId().toString() + "." + i, items[i]);
            dataConfig.save(dataFile);
        }
    }


    public ItemStack[] getInventory(Player player) {
        ItemStack[] items = new ItemStack[9];
        String uuid = player.getUniqueId().toString();

        if (dataConfig.get(uuid) == null) {
            ItemStack[] defaultItems = new ItemStack[9];

            defaultItems[0] = new ItemStack(Material.WOODEN_SWORD);
            defaultItems[1] = new Gun(plugin).createItem();
            defaultItems[2] = new Grenade(plugin).createItem();
            defaultItems[8] = new HealthPackage(plugin).createItem();

            try {
                setInventory(player, defaultItems);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return defaultItems;
        }

        Set<String> setString = dataConfig.getKeys(true);

        for (String string : setString) {
            if (string.startsWith(uuid) && !string.equalsIgnoreCase(uuid)) {

                Integer integer = Integer.parseInt(string.replace(uuid + ".", "").trim());

                items[integer] = dataConfig.getItemStack(uuid + "." + integer);

            }
        }


        return items;

    }

    public int getItemSlot(ItemStack itemStack, Player player) {

        ItemStack[] itemStacks = getInventory(player);

        for (int i = 0; i < itemStacks.length; i++)
            if (itemStacks[i].isSimilar(itemStack))
                return i;

        return 0;
    }

    ;

}

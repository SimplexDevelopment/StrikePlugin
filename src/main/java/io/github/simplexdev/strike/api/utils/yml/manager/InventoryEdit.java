package io.github.simplexdev.strike.api.utils.yml.manager;

import io.github.simplexdev.strike.listeners.Grenade;
import io.github.simplexdev.strike.listeners.Gun;
import io.github.simplexdev.strike.listeners.HealthPackage;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class InventoryEdit {

    private final JavaPlugin plugin;
    private final File dataFile;
    private FileConfiguration dataConfig;

    public InventoryEdit(JavaPlugin plugin) {
        this.plugin = plugin;

        dataFile = new File("plugins\\Strike\\inventories.yml");
        try {
            if (!dataFile.exists()) {
                plugin.getDataFolder().mkdirs();
                dataFile.createNewFile();
                plugin.saveResource("inventories.yml", false);
            }

            dataConfig = new YamlConfiguration();
            dataConfig.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
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


    public ItemStack[] getInventoryItems(Player player) {
        ItemStack[] items = new ItemStack[9];
        ItemStack[] defaultItems = new ItemStack[9];
        ItemMeta meta;

        defaultItems[0] = new ItemStack(Material.WOODEN_SWORD);
        meta = defaultItems[0].getItemMeta();
        meta.setUnbreakable(true);

        defaultItems[0].setItemMeta(meta);

        defaultItems[1] = new Gun(plugin).createItem();
        defaultItems[2] = new Grenade(plugin).createItem();
        defaultItems[8] = new HealthPackage(plugin).createItem();
        String uuid = player.getUniqueId().toString();

        if (dataConfig.get(uuid) == null) {

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

                int integer = Integer.parseInt(string.replace(uuid + ".", "").trim());

                items[integer] = dataConfig.getItemStack(uuid + "." + integer);

            }
        }

        if (!Arrays.asList(defaultItems).containsAll(Arrays.asList(items))) {
            try {
                setInventory(player, defaultItems);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return defaultItems;
        }


        return items;

    }

    public int getItemSlot(ItemStack itemStack, Player player) {

        ItemStack[] itemStacks = getInventoryItems(player);

        for (int i = 0; i < itemStacks.length; i++)
            if (itemStacks[i] != null && itemStacks[i].isSimilar(itemStack))
                return i;

        return 0;
    }

}

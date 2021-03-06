package io.github.simplexdev.strike;

import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;


public final class StrikePlugin extends JavaPlugin {
    //TODO
    // NPC Edit
    // LaunchPad

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Spawn.loadConfig(this);

        Gun gun = new Gun(this);
        Jumper jumper = new Jumper(this);
        SpawnController spawnController = new SpawnController(this);
        HealthPackage healthPackage = new HealthPackage(this);
        Grenade grenade = new Grenade(this);
        ItemManager itemManager = new ItemManager(this);

        getServer().getPluginManager().registerEvents(gun, this);
        getServer().getPluginManager().registerEvents(jumper, this);
        getServer().getPluginManager().registerEvents(spawnController, this);
        getServer().getPluginManager().registerEvents(healthPackage, this);
        getServer().getPluginManager().registerEvents(grenade, this);
        getServer().getPluginManager().registerEvents(itemManager, this);

        getServer().getPluginManager().registerEvents(new InventoryEditGUI(this), this);

        getCommand("strike").setExecutor(new StrikeCommand(this));

        StrikeCommand.loadInstances(gun, jumper, spawnController, grenade, itemManager);
    }

    @Override
    public void onDisable() {
    }
}
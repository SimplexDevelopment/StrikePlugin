package io.github.simplexdev.strike;

import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.listeners.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;


public final class StrikePlugin extends JavaPlugin {

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
        LaunchPadListener launchPad = new LaunchPadListener(this);

        getServer().getPluginManager().registerEvents(gun, this);
        getServer().getPluginManager().registerEvents(jumper, this);
        getServer().getPluginManager().registerEvents(spawnController, this);
        getServer().getPluginManager().registerEvents(healthPackage, this);
        getServer().getPluginManager().registerEvents(grenade, this);
        getServer().getPluginManager().registerEvents(itemManager, this);
        getServer().getPluginManager().registerEvents(launchPad, this);

        getServer().getPluginManager().registerEvents(new InventoryEditGUI(this), this);

        PluginCommand command = getCommand("strike");
        command.setExecutor(new StrikeCommand(this));
        command.setTabCompleter(new StrikeCommandCompleter());

        StrikeCommand.loadInstances(gun, jumper, spawnController, grenade, itemManager);
    }

    @Override
    public void onDisable() {
    }
}
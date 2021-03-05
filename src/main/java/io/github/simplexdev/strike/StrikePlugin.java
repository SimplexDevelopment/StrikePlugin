package io.github.simplexdev.strike;

import io.github.simplexdev.strike.api.ConfigUser;
import io.github.simplexdev.strike.api.Spawn;
import io.github.simplexdev.strike.listeners.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
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

        getServer().getPluginManager().registerEvents(gun, this);
        getServer().getPluginManager().registerEvents(jumper, this);
        getServer().getPluginManager().registerEvents(spawnController, this);
        getServer().getPluginManager().registerEvents(healthPackage, this);

        getServer().getPluginManager().registerEvents(new Grenade(this), this);

        getCommand("strike").setExecutor(new StrikeCommand(this));

        StrikeCommand.loadInstances(gun, jumper, spawnController);
    }

    @Override
    public void onDisable() {
    }
}
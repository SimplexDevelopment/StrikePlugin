package io.github.simplexdev.strike.api.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GunKillEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player killer;
    private final LivingEntity dead;

    public GunKillEvent(Player killer, LivingEntity dead) {
        this.killer = killer;
        this.dead = dead;
    }

    public Player getKiller() {
        return this.killer;
    }

    public LivingEntity getDead() {
        return this.dead;
    }


    public HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}


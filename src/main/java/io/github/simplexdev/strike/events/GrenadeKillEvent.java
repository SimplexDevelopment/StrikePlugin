package io.github.simplexdev.strike.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GrenadeKillEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player killer;
    private final Player dead;

    public GrenadeKillEvent(Player killer, Player dead) {
        this.killer = killer;
        this.dead = dead;
    }

    public Player getKiller() {
        return this.killer;
    }

    public Player getDead() {
        return dead;
    }

    public HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
package io.github.simplexdev.strike.api.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class PlayerDoubleJumpEvent extends PlayerJumpEvent {
    private static HandlerList handlerList = new HandlerList();
    private final World world;

    public PlayerDoubleJumpEvent(Player player, Vector vector) {
        super(player, player.getLocation(), player.getLocation().clone().add(vector));
        this.world = player.getWorld();
    }

    public World getWorld() {
        return world;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

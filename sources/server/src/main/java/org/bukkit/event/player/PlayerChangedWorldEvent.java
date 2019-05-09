/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerChangedWorldEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final World from;

    public PlayerChangedWorldEvent(Player player, World from) {
        super(player);
        this.from = from;
    }

    public World getFrom() {
        return this.from;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


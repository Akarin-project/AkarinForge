/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public abstract class PlayerChannelEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String channel;

    public PlayerChannelEvent(Player player, String channel) {
        super(player);
        this.channel = channel;
    }

    public final String getChannel() {
        return this.channel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


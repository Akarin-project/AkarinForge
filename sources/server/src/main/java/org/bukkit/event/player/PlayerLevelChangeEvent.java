/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerLevelChangeEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final int oldLevel;
    private final int newLevel;

    public PlayerLevelChangeEvent(Player player, int oldLevel, int newLevel) {
        super(player);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public int getOldLevel() {
        return this.oldLevel;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


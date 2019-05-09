/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerBedLeaveEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Block bed;

    public PlayerBedLeaveEvent(Player who, Block bed2) {
        super(who);
        this.bed = bed2;
    }

    public Block getBed() {
        return this.bed;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


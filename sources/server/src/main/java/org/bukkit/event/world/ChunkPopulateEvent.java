/*
 * Akarin Forge
 */
package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkEvent;

public class ChunkPopulateEvent
extends ChunkEvent {
    private static final HandlerList handlers = new HandlerList();

    public ChunkPopulateEvent(Chunk chunk) {
        super(chunk);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


/*
 * Akarin Forge
 */
package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkEvent;

public class ChunkUnloadEvent
extends ChunkEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private boolean saveChunk;

    public ChunkUnloadEvent(Chunk chunk) {
        this(chunk, true);
    }

    public ChunkUnloadEvent(Chunk chunk, boolean save) {
        super(chunk);
        this.saveChunk = save;
    }

    public boolean isSaveChunk() {
        return this.saveChunk;
    }

    public void setSaveChunk(boolean saveChunk) {
        this.saveChunk = saveChunk;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


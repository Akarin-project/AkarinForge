/*
 * Akarin Forge
 */
package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.world.WorldEvent;

public abstract class ChunkEvent
extends WorldEvent {
    protected Chunk chunk;

    protected ChunkEvent(Chunk chunk) {
        super(chunk.getWorld());
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return this.chunk;
    }
}


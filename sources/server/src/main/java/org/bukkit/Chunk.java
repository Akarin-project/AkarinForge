/*
 * Akarin Forge
 */
package org.bukkit;

import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

public interface Chunk {
    public int getX();

    public int getZ();

    public World getWorld();

    public Block getBlock(int var1, int var2, int var3);

    public ChunkSnapshot getChunkSnapshot();

    public ChunkSnapshot getChunkSnapshot(boolean var1, boolean var2, boolean var3);

    public Entity[] getEntities();

    public BlockState[] getTileEntities();

    public boolean isLoaded();

    public boolean load(boolean var1);

    public boolean load();

    @Deprecated
    public boolean unload(boolean var1, boolean var2);

    public boolean unload(boolean var1);

    public boolean unload();

    public boolean isSlimeChunk();
}


/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.chunkio;

import org.bukkit.craftbukkit.v1_12_R1.chunkio.ChunkIOProvider;
import org.bukkit.craftbukkit.v1_12_R1.chunkio.QueuedChunk;
import org.bukkit.craftbukkit.v1_12_R1.util.AsynchronousExecutor;

public class ChunkIOExecutor {
    static final int BASE_THREADS = 1;
    static final int PLAYERS_PER_THREAD = 50;
    private static final AsynchronousExecutor<QueuedChunk, axw, Runnable, RuntimeException> instance = new AsynchronousExecutor(new ChunkIOProvider(), 1);

    public static axw syncChunkLoad(amu world, aye loader, on provider, int x2, int z2) {
        return instance.getSkipQueue(new QueuedChunk(x2, z2, loader, world, provider));
    }

    public static void queueChunkLoad(amu world, aye loader, on provider, int x2, int z2, Runnable runnable) {
        instance.add(new QueuedChunk(x2, z2, loader, world, provider), runnable);
    }

    public static void dropQueuedChunkLoad(amu world, int x2, int z2, Runnable runnable) {
        instance.drop(new QueuedChunk(x2, z2, null, world, null), runnable);
    }

    public static void adjustPoolSize(int players) {
        int size = Math.max(1, (int)Math.ceil(players / 50));
        instance.setActiveThreads(size);
    }

    public static void tick() {
        instance.finishActive();
    }
}


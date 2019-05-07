package org.bukkit.craftbukkit.chunkio;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import org.bukkit.craftbukkit.util.AsynchronousExecutor;

import com.destroystokyo.paper.PaperConfig;

import io.akarin.forge.utils.MCUtil;
import net.minecraft.world.World;

public class ChunkIOExecutor {
    static final int BASE_THREADS = PaperConfig.minChunkLoadThreads; // Paper
    static final int PLAYERS_PER_THREAD = 50;

    private static final AsynchronousExecutor<QueuedChunk, Chunk, Runnable, RuntimeException> instance = new AsynchronousExecutor<QueuedChunk, Chunk, Runnable, RuntimeException>(new ChunkIOProvider(), BASE_THREADS);

    public static Chunk syncChunkLoad(World world, AnvilChunkLoader loader, ChunkProviderServer provider, int x, int z) {
        return MCUtil.ensureMain("Async Chunk Load", () -> instance.getSkipQueue(new QueuedChunk(x, z, loader, world, provider))); // Paper
    }

    public static void queueChunkLoad(World world, AnvilChunkLoader loader, ChunkProviderServer provider, int x, int z, Runnable runnable) {
        instance.add(new QueuedChunk(x, z, loader, world, provider), runnable);
    }

    // Abuses the fact that hashCode and equals for QueuedChunk only use world and coords
    public static void dropQueuedChunkLoad(World world, int x, int z, Runnable runnable) {
        instance.drop(new QueuedChunk(x, z, null, world, null), runnable);
    }

    public static void adjustPoolSize(int players) {
        int size = Math.max(BASE_THREADS, (int) Math.ceil(players / PLAYERS_PER_THREAD));
        instance.setActiveThreads(size);
    }

    public static void tick() {
        instance.finishActive();
    }

    // Paper start
    public static boolean hasQueuedChunkLoad(World world, int x, int z) {
        return instance.hasTask(new QueuedChunk(x, z, null, world, null));
    }
    // Paper end
}

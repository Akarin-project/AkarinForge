/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 */
package org.bukkit.craftbukkit.v1_12_R1.chunkio;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.craftbukkit.v1_12_R1.SpigotTimings;
import org.bukkit.craftbukkit.v1_12_R1.chunkio.QueuedChunk;
import org.bukkit.craftbukkit.v1_12_R1.util.AsynchronousExecutor;
import org.spigotmc.CustomTimingsHandler;

class ChunkIOProvider
implements AsynchronousExecutor.CallBackProvider<QueuedChunk, axw, Runnable, RuntimeException> {
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    ChunkIOProvider() {
    }

    @Override
    public axw callStage1(QueuedChunk queuedChunk) throws RuntimeException {
        try {
            aye loader = queuedChunk.loader;
            Object[] data = loader.loadChunk__Async(queuedChunk.world, queuedChunk.x, queuedChunk.z);
            if (data != null) {
                queuedChunk.compound = (fy)data[1];
                return (axw)data[0];
            }
            return null;
        }
        catch (IOException ex2) {
            throw new RuntimeException(ex2);
        }
    }

    @Override
    public void callStage2(QueuedChunk queuedChunk, axw chunk) throws RuntimeException {
        if (chunk == null) {
            queuedChunk.provider.c(queuedChunk.x, queuedChunk.z);
            return;
        }
        queuedChunk.loader.loadEntities(queuedChunk.world, queuedChunk.compound.p("Level"), chunk);
        chunk.b(queuedChunk.provider.f.R());
        queuedChunk.provider.e.put(amn.a(queuedChunk.x, queuedChunk.z), (Object)chunk);
        chunk.c();
        if (queuedChunk.provider.c != null) {
            queuedChunk.provider.f.timings.syncChunkLoadStructuresTimer.startTiming();
            queuedChunk.provider.c.b(chunk, queuedChunk.x, queuedChunk.z);
            queuedChunk.provider.f.timings.syncChunkLoadStructuresTimer.stopTiming();
        }
        chunk.populateCB(queuedChunk.provider, queuedChunk.provider.c, false);
    }

    @Override
    public void callStage3(QueuedChunk queuedChunk, axw chunk, Runnable runnable) throws RuntimeException {
        runnable.run();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "Chunk I/O Executor Thread-" + this.threadNumber.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}


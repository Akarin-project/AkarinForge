package net.minecraft.world.chunk;

import javax.annotation.Nullable;

public interface IChunkProvider
{
    @Nullable
    Chunk getLoadedChunk(int x, int z);

    Chunk provideChunk(int x, int z);

    boolean tick();

    String makeString();

    boolean isChunkGeneratedAt(int x, int z);
}
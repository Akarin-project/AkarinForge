package io.akarin.forge.server;

import gnu.trove.map.hash.TObjectIntHashMap;

public abstract class AkarinChunk {
    protected int neighbors = 0x1 << 12;
    public long chunkKey;
    public org.bukkit.Chunk bukkitChunk;
    public boolean mustSave;
    public TObjectIntHashMap<Class> entityCount = new TObjectIntHashMap<>();
    private final int[] itemCounts = new int[16];
    private final int[] inventoryEntityCounts = new int[16];
}

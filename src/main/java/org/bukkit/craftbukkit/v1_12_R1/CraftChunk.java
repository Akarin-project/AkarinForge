/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunkSnapshot;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class CraftChunk
implements Chunk {
    private WeakReference<axw> weakChunk;
    private final oo worldServer;
    private final int x;
    private final int z;
    private static final byte[] emptyData = new byte[2048];
    private static final short[] emptyBlockIDs = new short[4096];
    private static final byte[] emptySkyLight = new byte[2048];

    public CraftChunk(axw chunk) {
        this.weakChunk = new WeakReference<axw>(chunk);
        this.worldServer = (oo)this.getHandle().q();
        this.x = this.getHandle().b;
        this.z = this.getHandle().c;
    }

    @Override
    public World getWorld() {
        return this.worldServer.getWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld)this.getWorld();
    }

    public axw getHandle() {
        axw c2 = this.weakChunk.get();
        if (c2 == null) {
            c2 = this.worldServer.a(this.x, this.z);
            this.weakChunk = new WeakReference<axw>(c2);
        }
        return c2;
    }

    void breakLink() {
        this.weakChunk.clear();
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    public String toString() {
        return "CraftChunk{x=" + this.getX() + "z=" + this.getZ() + '}';
    }

    @Override
    public Block getBlock(int x2, int y2, int z2) {
        return new CraftBlock(this, this.getX() << 4 | x2 & 15, y2, this.getZ() << 4 | z2 & 15);
    }

    @Override
    public Entity[] getEntities() {
        int count = 0;
        int index = 0;
        axw chunk = this.getHandle();
        for (int i2 = 0; i2 < 16; ++i2) {
            count += chunk.t()[i2].size();
        }
        Entity[] entities = new Entity[count];
        for (int i3 = 0; i3 < 16; ++i3) {
            for (Object obj : chunk.t()[i3].toArray()) {
                if (!(obj instanceof vg)) continue;
                entities[index++] = ((vg)obj).getBukkitEntity();
            }
        }
        return entities;
    }

    @Override
    public BlockState[] getTileEntities() {
        int index = 0;
        axw chunk = this.getHandle();
        BlockState[] entities = new BlockState[chunk.s().size()];
        for (Object obj : chunk.s().keySet().toArray()) {
            if (!(obj instanceof et)) continue;
            et position = (et)obj;
            entities[index++] = this.worldServer.getWorld().getBlockAt(position.p(), position.q(), position.r()).getState();
        }
        return entities;
    }

    @Override
    public boolean isLoaded() {
        return this.getWorld().isChunkLoaded(this);
    }

    @Override
    public boolean load() {
        return this.getWorld().loadChunk(this.getX(), this.getZ(), true);
    }

    @Override
    public boolean load(boolean generate) {
        return this.getWorld().loadChunk(this.getX(), this.getZ(), generate);
    }

    @Override
    public boolean unload() {
        return this.getWorld().unloadChunk(this.getX(), this.getZ());
    }

    @Override
    public boolean isSlimeChunk() {
        return this.getHandle().a(987234911).nextInt(10) == 0;
    }

    @Override
    public boolean unload(boolean save) {
        return this.getWorld().unloadChunk(this.getX(), this.getZ(), save);
    }

    @Override
    public boolean unload(boolean save, boolean safe) {
        return this.getWorld().unloadChunk(this.getX(), this.getZ(), save, safe);
    }

    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return this.getChunkSnapshot(true, false, false);
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain) {
        axw chunk = this.getHandle();
        axx[] cs2 = chunk.h();
        short[][] sectionBlockIDs = new short[cs2.length][];
        byte[][] sectionBlockData = new byte[cs2.length][];
        byte[][] sectionSkyLights = new byte[cs2.length][];
        byte[][] sectionEmitLights = new byte[cs2.length][];
        boolean[] sectionEmpty = new boolean[cs2.length];
        for (int i2 = 0; i2 < cs2.length; ++i2) {
            if (cs2[i2] == null) {
                sectionBlockIDs[i2] = emptyBlockIDs;
                sectionBlockData[i2] = emptyData;
                sectionSkyLights[i2] = emptySkyLight;
                sectionEmitLights[i2] = emptyData;
                sectionEmpty[i2] = true;
                continue;
            }
            short[] blockids = new short[4096];
            byte[] rawIds = new byte[4096];
            axs data = new axs();
            cs2[i2].g().a(rawIds, data);
            sectionBlockData[i2] = data.a();
            byte[] dataValues = sectionBlockData[i2];
            for (int j2 = 0; j2 < 4096; ++j2) {
                blockids[j2] = (short)(rawIds[j2] & 255);
            }
            sectionBlockIDs[i2] = blockids;
            if (cs2[i2].i() == null) {
                sectionSkyLights[i2] = emptyData;
            } else {
                sectionSkyLights[i2] = new byte[2048];
                System.arraycopy(cs2[i2].i().a(), 0, sectionSkyLights[i2], 0, 2048);
            }
            sectionEmitLights[i2] = new byte[2048];
            System.arraycopy(cs2[i2].h().a(), 0, sectionEmitLights[i2], 0, 2048);
        }
        int[] hmap = null;
        if (includeMaxBlockY) {
            hmap = new int[256];
            System.arraycopy(chunk.r(), 0, hmap, 0, 256);
        }
        anh[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;
        if (includeBiome || includeBiomeTempRain) {
            anl wcm = chunk.q().C();
            if (includeBiome) {
                biome = new anh[256];
                for (int i3 = 0; i3 < 256; ++i3) {
                    biome[i3] = chunk.a(new et(i3 & 15, 0, i3 >> 4), wcm);
                }
            }
            if (includeBiomeTempRain) {
                biomeTemp = new double[256];
                biomeRain = new double[256];
                float[] dat = CraftChunk.getTemperatures(wcm, this.getX() << 4, this.getZ() << 4);
                for (int i4 = 0; i4 < 256; ++i4) {
                    biomeTemp[i4] = dat[i4];
                }
            }
        }
        World world = this.getWorld();
        return new CraftChunkSnapshot(this.getX(), this.getZ(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionBlockData, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, biome, biomeTemp, biomeRain);
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x2, int z2, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        anh[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;
        if (includeBiome || includeBiomeTempRain) {
            anl wcm = world.getHandle().C();
            if (includeBiome) {
                biome = new anh[256];
                for (int i2 = 0; i2 < 256; ++i2) {
                    biome[i2] = world.getHandle().b(new et((x2 << 4) + (i2 & 15), 0, (z2 << 4) + (i2 >> 4)));
                }
            }
            if (includeBiomeTempRain) {
                biomeTemp = new double[256];
                biomeRain = new double[256];
                float[] dat = CraftChunk.getTemperatures(wcm, x2 << 4, z2 << 4);
                for (int i3 = 0; i3 < 256; ++i3) {
                    biomeTemp[i3] = dat[i3];
                }
            }
        }
        int hSection = world.getMaxHeight() >> 4;
        short[][] blockIDs = new short[hSection][];
        byte[][] skyLight = new byte[hSection][];
        byte[][] emitLight = new byte[hSection][];
        byte[][] blockData = new byte[hSection][];
        boolean[] empty = new boolean[hSection];
        for (int i4 = 0; i4 < hSection; ++i4) {
            blockIDs[i4] = emptyBlockIDs;
            skyLight[i4] = emptySkyLight;
            emitLight[i4] = emptyData;
            blockData[i4] = emptyData;
            empty[i4] = true;
        }
        return new CraftChunkSnapshot(x2, z2, world.getName(), world.getFullTime(), blockIDs, blockData, skyLight, emitLight, empty, new int[256], biome, biomeTemp, biomeRain);
    }

    private static float[] getTemperatures(anl chunkmanager, int chunkX, int chunkZ) {
        anh[] biomes = chunkmanager.b(null, chunkX, chunkZ, 16, 16);
        float[] temps = new float[biomes.length];
        for (int i2 = 0; i2 < biomes.length; ++i2) {
            float temp = biomes[i2].n();
            if (temp > 1.0f) {
                temp = 1.0f;
            }
            temps[i2] = temp;
        }
        return temps;
    }

    static {
        Arrays.fill(emptySkyLight, -1);
    }
}


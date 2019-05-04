/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;

public class CraftChunkSnapshot
implements ChunkSnapshot {
    private final int x;
    private final int z;
    private final String worldname;
    private final short[][] blockids;
    private final byte[][] blockdata;
    private final byte[][] skylight;
    private final byte[][] emitlight;
    private final boolean[] empty;
    private final int[] hmap;
    private final long captureFulltime;
    private final anh[] biome;
    private final double[] biomeTemp;
    private final double[] biomeRain;

    CraftChunkSnapshot(int x2, int z2, String wname, long wtime, short[][] sectionBlockIDs, byte[][] sectionBlockData, byte[][] sectionSkyLights, byte[][] sectionEmitLights, boolean[] sectionEmpty, int[] hmap, anh[] biome, double[] biomeTemp, double[] biomeRain) {
        this.x = x2;
        this.z = z2;
        this.worldname = wname;
        this.captureFulltime = wtime;
        this.blockids = sectionBlockIDs;
        this.blockdata = sectionBlockData;
        this.skylight = sectionSkyLights;
        this.emitlight = sectionEmitLights;
        this.empty = sectionEmpty;
        this.hmap = hmap;
        this.biome = biome;
        this.biomeTemp = biomeTemp;
        this.biomeRain = biomeRain;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public String getWorldName() {
        return this.worldname;
    }

    @Override
    public Material getBlockType(int x2, int y2, int z2) {
        return Material.getBlockMaterial(this.getBlockTypeId(x2, y2, z2));
    }

    @Override
    public final int getBlockTypeId(int x2, int y2, int z2) {
        return this.blockids[y2 >> 4][(y2 & 15) << 8 | z2 << 4 | x2];
    }

    @Override
    public final int getBlockData(int x2, int y2, int z2) {
        int off = (y2 & 15) << 7 | z2 << 3 | x2 >> 1;
        return this.blockdata[y2 >> 4][off] >> ((x2 & 1) << 2) & 15;
    }

    @Override
    public final int getBlockSkyLight(int x2, int y2, int z2) {
        int off = (y2 & 15) << 7 | z2 << 3 | x2 >> 1;
        return this.skylight[y2 >> 4][off] >> ((x2 & 1) << 2) & 15;
    }

    @Override
    public final int getBlockEmittedLight(int x2, int y2, int z2) {
        int off = (y2 & 15) << 7 | z2 << 3 | x2 >> 1;
        return this.emitlight[y2 >> 4][off] >> ((x2 & 1) << 2) & 15;
    }

    @Override
    public final int getHighestBlockYAt(int x2, int z2) {
        return this.hmap[z2 << 4 | x2];
    }

    @Override
    public final Biome getBiome(int x2, int z2) {
        return CraftBlock.biomeBaseToBiome(this.biome[z2 << 4 | x2]);
    }

    @Override
    public final double getRawBiomeTemperature(int x2, int z2) {
        return this.biomeTemp[z2 << 4 | x2];
    }

    @Override
    public final double getRawBiomeRainfall(int x2, int z2) {
        return this.biomeRain[z2 << 4 | x2];
    }

    @Override
    public final long getCaptureFullTime() {
        return this.captureFulltime;
    }

    @Override
    public final boolean isSectionEmpty(int sy2) {
        return this.empty[sy2];
    }
}


/*
 * Akarin Forge
 */
package org.bukkit;

import org.bukkit.Material;
import org.bukkit.block.Biome;

public interface ChunkSnapshot {
    public int getX();

    public int getZ();

    public String getWorldName();

    public Material getBlockType(int var1, int var2, int var3);

    @Deprecated
    public int getBlockTypeId(int var1, int var2, int var3);

    @Deprecated
    public int getBlockData(int var1, int var2, int var3);

    public int getBlockSkyLight(int var1, int var2, int var3);

    public int getBlockEmittedLight(int var1, int var2, int var3);

    public int getHighestBlockYAt(int var1, int var2);

    public Biome getBiome(int var1, int var2);

    public double getRawBiomeTemperature(int var1, int var2);

    @Deprecated
    public double getRawBiomeRainfall(int var1, int var2);

    public long getCaptureFullTime();

    public boolean isSectionEmpty(int var1);
}


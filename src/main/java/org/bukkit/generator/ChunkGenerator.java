/*
 * Akarin Forge
 */
package org.bukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.material.MaterialData;

public abstract class ChunkGenerator {
    @Deprecated
    public byte[] generate(World world, Random random, int x2, int z2) {
        throw new UnsupportedOperationException("Custom generator is missing required methods: generate(), generateBlockSections() and generateExtBlockSections()");
    }

    @Deprecated
    public short[][] generateExtBlockSections(World world, Random random, int x2, int z2, BiomeGrid biomes) {
        return null;
    }

    @Deprecated
    public byte[][] generateBlockSections(World world, Random random, int x2, int z2, BiomeGrid biomes) {
        return null;
    }

    public ChunkData generateChunkData(World world, Random random, int x2, int z2, BiomeGrid biome) {
        return null;
    }

    protected final ChunkData createChunkData(World world) {
        return Bukkit.getServer().createChunkData(world);
    }

    public boolean canSpawn(World world, int x2, int z2) {
        Block highest = world.getBlockAt(x2, world.getHighestBlockYAt(x2, z2), z2);
        switch (world.getEnvironment()) {
            case NETHER: {
                return true;
            }
            case THE_END: {
                return highest.getType() != Material.AIR && highest.getType() != Material.WATER && highest.getType() != Material.LAVA;
            }
        }
        return highest.getType() == Material.SAND || highest.getType() == Material.GRAVEL;
    }

    public List<BlockPopulator> getDefaultPopulators(World world) {
        return new ArrayList<BlockPopulator>();
    }

    public Location getFixedSpawnLocation(World world, Random random) {
        return null;
    }

    public static interface ChunkData {
        public int getMaxHeight();

        public void setBlock(int var1, int var2, int var3, Material var4);

        public void setBlock(int var1, int var2, int var3, MaterialData var4);

        public void setRegion(int var1, int var2, int var3, int var4, int var5, int var6, Material var7);

        public void setRegion(int var1, int var2, int var3, int var4, int var5, int var6, MaterialData var7);

        public Material getType(int var1, int var2, int var3);

        public MaterialData getTypeAndData(int var1, int var2, int var3);

        @Deprecated
        public void setRegion(int var1, int var2, int var3, int var4, int var5, int var6, int var7);

        @Deprecated
        public void setRegion(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

        @Deprecated
        public void setBlock(int var1, int var2, int var3, int var4);

        @Deprecated
        public void setBlock(int var1, int var2, int var3, int var4, byte var5);

        @Deprecated
        public int getTypeId(int var1, int var2, int var3);

        @Deprecated
        public byte getData(int var1, int var2, int var3);
    }

    public static interface BiomeGrid {
        public Biome getBiome(int var1, int var2);

        public void setBiome(int var1, int var2, Biome var3);
    }

}


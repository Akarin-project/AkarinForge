/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.generator.InternalChunkGenerator;
import org.bukkit.generator.BlockPopulator;

public class NormalChunkGenerator
extends InternalChunkGenerator {
    private final axq generator;

    public NormalChunkGenerator(amu world, long seed) {
        this.generator = world.s.c();
    }

    @Override
    public byte[] generate(World world, Random random, int x2, int z2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean canSpawn(World world, int x2, int z2) {
        return ((CraftWorld)world).getHandle().s.a(x2, z2);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return new ArrayList<BlockPopulator>();
    }

    @Override
    public axw a(int i2, int i1) {
        return this.generator.a(i2, i1);
    }

    @Override
    public void b(int i2, int i1) {
        this.generator.b(i2, i1);
    }

    @Override
    public boolean a(axw chunk, int i2, int i1) {
        return this.generator.a(chunk, i2, i1);
    }

    @Override
    public List<anh.c> a(vr enumCreatureType, et blockPosition) {
        return this.generator.a(enumCreatureType, blockPosition);
    }

    @Override
    public et a(amu world, String s2, et blockPosition, boolean flag) {
        return this.generator.a(world, s2, blockPosition, flag);
    }

    @Override
    public void b(axw chunk, int i2, int i1) {
        this.generator.b(chunk, i2, i1);
    }

    @Override
    public boolean a(amu world, String string, et bp2) {
        return this.generator.a(world, string, bp2);
    }
}


/*
 * Akarin Forge
 */
package org.bukkit.util.noise;

import java.util.Random;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.PerlinNoiseGenerator;

public class PerlinOctaveGenerator
extends OctaveGenerator {
    public PerlinOctaveGenerator(World world, int octaves) {
        this(new Random(world.getSeed()), octaves);
    }

    public PerlinOctaveGenerator(long seed, int octaves) {
        this(new Random(seed), octaves);
    }

    public PerlinOctaveGenerator(Random rand, int octaves) {
        super(PerlinOctaveGenerator.createOctaves(rand, octaves));
    }

    private static NoiseGenerator[] createOctaves(Random rand, int octaves) {
        NoiseGenerator[] result = new NoiseGenerator[octaves];
        for (int i2 = 0; i2 < octaves; ++i2) {
            result[i2] = new PerlinNoiseGenerator(rand);
        }
        return result;
    }
}


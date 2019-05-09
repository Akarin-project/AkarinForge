/*
 * Akarin Forge
 */
package org.bukkit.util.noise;

import java.util.Random;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class SimplexOctaveGenerator
extends OctaveGenerator {
    private double wScale = 1.0;

    public SimplexOctaveGenerator(World world, int octaves) {
        this(new Random(world.getSeed()), octaves);
    }

    public SimplexOctaveGenerator(long seed, int octaves) {
        this(new Random(seed), octaves);
    }

    public SimplexOctaveGenerator(Random rand, int octaves) {
        super(SimplexOctaveGenerator.createOctaves(rand, octaves));
    }

    @Override
    public void setScale(double scale) {
        super.setScale(scale);
        this.setWScale(scale);
    }

    public double getWScale() {
        return this.wScale;
    }

    public void setWScale(double scale) {
        this.wScale = scale;
    }

    public double noise(double x2, double y2, double z2, double w2, double frequency, double amplitude) {
        return this.noise(x2, y2, z2, w2, frequency, amplitude, false);
    }

    public double noise(double x2, double y2, double z2, double w2, double frequency, double amplitude, boolean normalized) {
        double result = 0.0;
        double amp2 = 1.0;
        double freq = 1.0;
        double max = 0.0;
        x2 *= this.xScale;
        y2 *= this.yScale;
        z2 *= this.zScale;
        w2 *= this.wScale;
        for (NoiseGenerator octave : this.octaves) {
            result += ((SimplexNoiseGenerator)octave).noise(x2 * freq, y2 * freq, z2 * freq, w2 * freq) * amp2;
            max += amp2;
            freq *= frequency;
            amp2 *= amplitude;
        }
        if (normalized) {
            result /= max;
        }
        return result;
    }

    private static NoiseGenerator[] createOctaves(Random rand, int octaves) {
        NoiseGenerator[] result = new NoiseGenerator[octaves];
        for (int i2 = 0; i2 < octaves; ++i2) {
            result[i2] = new SimplexNoiseGenerator(rand);
        }
        return result;
    }
}


/*
 * Akarin Forge
 */
package org.bukkit.util.noise;

public abstract class NoiseGenerator {
    protected final int[] perm = new int[512];
    protected double offsetX;
    protected double offsetY;
    protected double offsetZ;

    public static int floor(double x2) {
        return x2 >= 0.0 ? (int)x2 : (int)x2 - 1;
    }

    protected static double fade(double x2) {
        return x2 * x2 * x2 * (x2 * (x2 * 6.0 - 15.0) + 10.0);
    }

    protected static double lerp(double x2, double y2, double z2) {
        return y2 + x2 * (z2 - y2);
    }

    protected static double grad(int hash, double x2, double y2, double z2) {
        double u2;
        double d2 = u2 = (hash &= 15) < 8 ? x2 : y2;
        double v2 = hash < 4 ? y2 : (hash == 12 || hash == 14 ? x2 : z2);
        return ((hash & 1) == 0 ? u2 : - u2) + ((hash & 2) == 0 ? v2 : - v2);
    }

    public double noise(double x2) {
        return this.noise(x2, 0.0, 0.0);
    }

    public double noise(double x2, double y2) {
        return this.noise(x2, y2, 0.0);
    }

    public abstract double noise(double var1, double var3, double var5);

    public double noise(double x2, int octaves, double frequency, double amplitude) {
        return this.noise(x2, 0.0, 0.0, octaves, frequency, amplitude);
    }

    public double noise(double x2, int octaves, double frequency, double amplitude, boolean normalized) {
        return this.noise(x2, 0.0, 0.0, octaves, frequency, amplitude, normalized);
    }

    public double noise(double x2, double y2, int octaves, double frequency, double amplitude) {
        return this.noise(x2, y2, 0.0, octaves, frequency, amplitude);
    }

    public double noise(double x2, double y2, int octaves, double frequency, double amplitude, boolean normalized) {
        return this.noise(x2, y2, 0.0, octaves, frequency, amplitude, normalized);
    }

    public double noise(double x2, double y2, double z2, int octaves, double frequency, double amplitude) {
        return this.noise(x2, y2, z2, octaves, frequency, amplitude, false);
    }

    public double noise(double x2, double y2, double z2, int octaves, double frequency, double amplitude, boolean normalized) {
        double result = 0.0;
        double amp2 = 1.0;
        double freq = 1.0;
        double max = 0.0;
        for (int i2 = 0; i2 < octaves; ++i2) {
            result += this.noise(x2 * freq, y2 * freq, z2 * freq) * amp2;
            max += amp2;
            freq *= frequency;
            amp2 *= amplitude;
        }
        if (normalized) {
            result /= max;
        }
        return result;
    }
}


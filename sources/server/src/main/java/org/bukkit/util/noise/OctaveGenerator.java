/*
 * Akarin Forge
 */
package org.bukkit.util.noise;

import org.bukkit.util.noise.NoiseGenerator;

public abstract class OctaveGenerator {
    protected final NoiseGenerator[] octaves;
    protected double xScale = 1.0;
    protected double yScale = 1.0;
    protected double zScale = 1.0;

    protected OctaveGenerator(NoiseGenerator[] octaves) {
        this.octaves = octaves;
    }

    public void setScale(double scale) {
        this.setXScale(scale);
        this.setYScale(scale);
        this.setZScale(scale);
    }

    public double getXScale() {
        return this.xScale;
    }

    public void setXScale(double scale) {
        this.xScale = scale;
    }

    public double getYScale() {
        return this.yScale;
    }

    public void setYScale(double scale) {
        this.yScale = scale;
    }

    public double getZScale() {
        return this.zScale;
    }

    public void setZScale(double scale) {
        this.zScale = scale;
    }

    public NoiseGenerator[] getOctaves() {
        return (NoiseGenerator[])this.octaves.clone();
    }

    public double noise(double x2, double frequency, double amplitude) {
        return this.noise(x2, 0.0, 0.0, frequency, amplitude);
    }

    public double noise(double x2, double frequency, double amplitude, boolean normalized) {
        return this.noise(x2, 0.0, 0.0, frequency, amplitude, normalized);
    }

    public double noise(double x2, double y2, double frequency, double amplitude) {
        return this.noise(x2, y2, 0.0, frequency, amplitude);
    }

    public double noise(double x2, double y2, double frequency, double amplitude, boolean normalized) {
        return this.noise(x2, y2, 0.0, frequency, amplitude, normalized);
    }

    public double noise(double x2, double y2, double z2, double frequency, double amplitude) {
        return this.noise(x2, y2, z2, frequency, amplitude, false);
    }

    public double noise(double x2, double y2, double z2, double frequency, double amplitude, boolean normalized) {
        double result = 0.0;
        double amp2 = 1.0;
        double freq = 1.0;
        double max = 0.0;
        x2 *= this.xScale;
        y2 *= this.yScale;
        z2 *= this.zScale;
        for (NoiseGenerator octave : this.octaves) {
            result += octave.noise(x2 * freq, y2 * freq, z2 * freq) * amp2;
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


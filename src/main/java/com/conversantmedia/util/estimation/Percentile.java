/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.estimation;

import java.io.PrintStream;
import java.util.Arrays;

public class Percentile {
    private static float[] DEFAULT_PERCENTILE = new float[]{0.05f, 0.5f, 0.683f, 0.75f, 0.85f, 0.954f, 0.99f};
    private final float[] quantiles;
    private final int m;
    private final float[] q;
    private final int[] n;
    private final float[] f;
    private final float[] d;
    private final float[] e;
    private boolean isInitializing;
    private int ni;

    public Percentile() {
        this(DEFAULT_PERCENTILE);
    }

    public Percentile(float[] quantiles) {
        this.m = quantiles.length;
        this.quantiles = Arrays.copyOf(quantiles, this.m);
        int N = 2 * this.m + 3;
        this.q = new float[N + 1];
        this.n = new int[N + 1];
        this.f = new float[N + 1];
        this.d = new float[N + 1];
        this.e = new float[this.m];
        this.clear();
    }

    public void clear() {
        int i2;
        for (i2 = 1; i2 <= 2 * this.m + 3; ++i2) {
            this.n[i2] = i2 + 1;
        }
        this.f[1] = 0.0f;
        this.f[2 * this.m + 3] = 1.0f;
        for (i2 = 1; i2 <= this.m; ++i2) {
            this.f[2 * i2 + 1] = this.quantiles[i2 - 1];
        }
        for (i2 = 1; i2 <= this.m + 1; ++i2) {
            this.f[2 * i2] = (this.f[2 * i2 - 1] + this.f[2 * i2 + 1]) / 2.0f;
        }
        for (i2 = 1; i2 <= 2 * this.m + 3; ++i2) {
            this.d[i2] = 1.0f + (float)(2 * (this.m + 1)) * this.f[i2];
        }
        this.isInitializing = true;
        this.ni = 1;
    }

    public void add(float x2) {
        if (this.isInitializing) {
            this.q[this.ni++] = x2;
            if (this.ni == 2 * this.m + 3 + 1) {
                Arrays.sort(this.q);
                this.isInitializing = false;
            }
        } else {
            this.addMeasurement(x2);
        }
    }

    public float[] getQuantiles() {
        return this.quantiles;
    }

    public boolean isReady() {
        return !this.isInitializing;
    }

    public int getNSamples() {
        if (!this.isInitializing) {
            return this.n[2 * this.m + 3] - 1;
        }
        return this.ni - 1;
    }

    public float[] getEstimates() throws InsufficientSamplesException {
        if (!this.isInitializing) {
            for (int i2 = 1; i2 <= this.m; ++i2) {
                this.e[i2 - 1] = this.q[2 * i2 + 1];
            }
            return this.e;
        }
        throw new InsufficientSamplesException();
    }

    public float getMin() {
        return this.q[1];
    }

    public float getMax() {
        return this.q[2 * this.m + 3];
    }

    private void addMeasurement(float x2) {
        int i2;
        int k2 = 1;
        if (x2 < this.q[1]) {
            k2 = 1;
            this.q[1] = x2;
        } else if (x2 >= this.q[2 * this.m + 3]) {
            k2 = 2 * this.m + 2;
            this.q[2 * this.m + 3] = x2;
        } else {
            for (i2 = 1; i2 <= 2 * this.m + 2; ++i2) {
                if (this.q[i2] > x2 || x2 >= this.q[i2 + 1]) continue;
                k2 = i2;
                break;
            }
        }
        for (i2 = k2 + 1; i2 <= 2 * this.m + 3; ++i2) {
            this.n[i2] = this.n[i2] + 1;
        }
        for (i2 = 1; i2 <= 2 * this.m + 3; ++i2) {
            this.d[i2] = this.d[i2] + this.f[i2];
        }
        for (i2 = 2; i2 <= 2 * this.m + 2; ++i2) {
            float qt2;
            float dval = this.d[i2] - (float)this.n[i2];
            float dp2 = this.n[i2 + 1] - this.n[i2];
            float dm2 = this.n[i2 - 1] - this.n[i2];
            float qp2 = (this.q[i2 + 1] - this.q[i2]) / dp2;
            float qm2 = (this.q[i2 - 1] - this.q[i2]) / dm2;
            if (dval >= 1.0f && dp2 > 1.0f) {
                qt2 = this.q[i2] + ((1.0f - dm2) * qp2 + (dp2 - 1.0f) * qm2) / (dp2 - dm2);
                this.q[i2] = this.q[i2 - 1] < qt2 && qt2 < this.q[i2 + 1] ? qt2 : this.q[i2] + qp2;
                this.n[i2] = this.n[i2] + 1;
                continue;
            }
            if (dval > -1.0f || dm2 >= -1.0f) continue;
            qt2 = this.q[i2] - ((1.0f + dp2) * qm2 - (dm2 + 1.0f) * qp2) / (dp2 - dm2);
            this.q[i2] = this.q[i2 - 1] < qt2 && qt2 < this.q[i2 + 1] ? qt2 : this.q[i2] - qm2;
            this.n[i2] = this.n[i2] - 1;
        }
    }

    public static void print(PrintStream out, String name, Percentile p2) {
        if (p2.isReady()) {
            try {
                StringBuilder sb2 = new StringBuilder(512);
                float[] q2 = p2.getQuantiles();
                float[] e2 = p2.getEstimates();
                int SCREENWIDTH = 80;
                sb2.append(name);
                sb2.append(", min(");
                sb2.append(p2.getMin());
                sb2.append("), max(");
                sb2.append(p2.getMax());
                sb2.append(')');
                sb2.append("\n");
                float max = e2[e2.length - 1];
                for (int i2 = 0; i2 < q2.length; ++i2) {
                    sb2.append(String.format("%4.3f", Float.valueOf(q2[i2])));
                    sb2.append(": ");
                    int len = (int)(e2[i2] / max * 80.0f);
                    for (int j2 = 0; j2 < len; ++j2) {
                        sb2.append('#');
                    }
                    sb2.append(" ");
                    sb2.append(String.format("%4.3f\n", Float.valueOf(e2[i2])));
                }
                out.println(sb2.toString());
            }
            catch (InsufficientSamplesException sb2) {
                // empty catch block
            }
        }
    }

    public class InsufficientSamplesException
    extends Exception {
        private InsufficientSamplesException() {
        }
    }

}


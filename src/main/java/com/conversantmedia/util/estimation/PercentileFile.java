/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.estimation;

import com.conversantmedia.util.estimation.Percentile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

public final class PercentileFile {
    public static void main(String[] arg2) throws IOException {
        for (int i2 = 0; i2 < arg2.length; ++i2) {
            String line;
            String fileName = arg2[i2];
            Percentile pFile = new Percentile();
            BufferedReader br2 = new BufferedReader(new FileReader(fileName));
            while ((line = br2.readLine()) != null) {
                float sample = Float.parseFloat(line.trim());
                pFile.add(sample);
            }
            br2.close();
            Percentile.print(System.out, fileName, pFile);
        }
    }
}


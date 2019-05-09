/*
 * Akarin Forge
 */
package org.bukkit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;

public class FileUtil {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean copy(File inFile, File outFile) {
        if (!inFile.exists()) {
            return false;
        }
        FileChannel in2 = null;
        AbstractInterruptibleChannel out = null;
        try {
            in2 = new FileInputStream(inFile).getChannel();
            out = new FileOutputStream(outFile).getChannel();
            long size = in2.size();
            for (long pos = 0; pos < size; pos += in2.transferTo((long)pos, (long)0xA00000, (WritableByteChannel)out)) {
            }
        }
        catch (IOException ioe) {
            boolean bl2 = false;
            return bl2;
        }
        finally {
            try {
                if (in2 != null) {
                    in2.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException ioe) {
                return false;
            }
        }
        return true;
    }
}


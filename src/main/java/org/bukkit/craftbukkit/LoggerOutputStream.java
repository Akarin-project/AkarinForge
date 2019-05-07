/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Level
 *  org.apache.logging.log4j.Logger
 */
package org.bukkit.craftbukkit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LoggerOutputStream
extends ByteArrayOutputStream {
    private final String separator = System.getProperty("line.separator");
    private final Logger logger;
    private final Level level;

    public LoggerOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void flush() throws IOException {
        LoggerOutputStream loggerOutputStream = this;
        synchronized (loggerOutputStream) {
            super.flush();
            String record = this.toString();
            super.reset();
            if (record.length() > 0 && !record.equals(this.separator)) {
                this.logger.log(this.level, record);
            }
        }
    }
}


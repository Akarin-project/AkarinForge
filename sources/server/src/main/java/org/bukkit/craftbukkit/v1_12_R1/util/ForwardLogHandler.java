/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForwardLogHandler
extends ConsoleHandler {
    private Map<String, Logger> cachedLoggers = new ConcurrentHashMap<String, Logger>();

    private Logger getLogger(String name) {
        Logger logger = this.cachedLoggers.get(name);
        if (logger == null) {
            logger = LogManager.getLogger((String)name);
            this.cachedLoggers.put(name, logger);
        }
        return logger;
    }

    @Override
    public void publish(LogRecord record) {
        Logger logger = this.getLogger(String.valueOf(record.getLoggerName()));
        Throwable exception = record.getThrown();
        Level level = record.getLevel();
        String message = this.getFormatter().formatMessage(record);
        if (level == Level.SEVERE) {
            logger.error(message, exception);
        } else if (level == Level.WARNING) {
            logger.warn(message, exception);
        } else if (level == Level.INFO) {
            logger.info(message, exception);
        } else if (level == Level.CONFIG) {
            logger.debug(message, exception);
        } else {
            logger.trace(message, exception);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}


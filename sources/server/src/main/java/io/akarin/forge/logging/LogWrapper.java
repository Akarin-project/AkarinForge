package io.akarin.forge.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;

public class LogWrapper extends Logger {
	private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getRootLogger();
	
	public static LogWrapper getLogger() {
		return new LogWrapper("", null);
	}
	
	public static LogWrapper getLogger(String name) {
		return new LogWrapper(name, null);
	}
	
    protected LogWrapper(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}

    @Override
    public void severe(String msg) {
        LOGGER.error(msg);
    }

    @Override
    public void warning(String msg) {
    	LOGGER.warn(msg);
    }

    @Override
    public void info(String msg) {
    	LOGGER.info(msg);
    }

    @Override
    public void fine(String msg) {
    	LOGGER.debug(msg);
    }

    @Override
    public void finer(String msg) {
    	LOGGER.trace(msg);
    }

    /**
     * Log a FINEST message.
     * <p>
     * If the logger is currently enabled for the FINEST message
     * level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     * @param   msg     The string message (or a key in the message catalog)
     */
    public void finest(String msg) {
        log(Level.FINEST, msg);
    }

	@Override
    public void log(Level level, String msg) {
		if (level == Level.INFO)
			LOGGER.info(msg);
		else if (level == Level.WARNING)
			LOGGER.warn(msg);
		else if (level == Level.SEVERE)
			LOGGER.error(msg);
		else
			LOGGER.debug(msg);
    }
	
	@Override
    public void log(Level level, String msg, Throwable throwable) {
		if (level == Level.SEVERE)
			LOGGER.error(msg, throwable);
		else if (level == Level.WARNING)
			LOGGER.warn(msg, throwable);
		else if (level == Level.INFO)
			LOGGER.info(msg, throwable);
		else
			LOGGER.debug(msg, throwable);
    }
}


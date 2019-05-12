package io.akarin.forge.misc;

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
    public void config(String msg) {
    	LOGGER.debug(msg);
    }

    @Override
    public void fine(String msg) {
    	LOGGER.trace(msg);
    }

    @Override
    public void finer(String msg) {
    	LOGGER.trace(msg);
    }
    
    @Override
    public void finest(String msg) {
    	LOGGER.trace(msg);
    }

	@Override
    public void log(Level level, String msg) {
		if (level == Level.INFO)
			LOGGER.log(org.apache.logging.log4j.Level.INFO, msg);
		else if (level == Level.WARNING)
			LOGGER.log(org.apache.logging.log4j.Level.WARN, msg);
		else if (level == Level.SEVERE)
			LOGGER.log(org.apache.logging.log4j.Level.ERROR, msg);
		else if (level == Level.FINE || level == Level.FINER || level == Level.FINEST)
			LOGGER.log(org.apache.logging.log4j.Level.TRACE, msg);
		else if (level == Level.CONFIG)
			LOGGER.log(org.apache.logging.log4j.Level.DEBUG, msg);
		else if (level == Level.ALL)
			LOGGER.log(org.apache.logging.log4j.Level.ALL, msg);
    }
	
	@Override
    public void log(Level level, String msg, Throwable throwable) {
		if (level == Level.INFO)
			LOGGER.log(org.apache.logging.log4j.Level.INFO, msg, throwable);
		else if (level == Level.WARNING)
			LOGGER.log(org.apache.logging.log4j.Level.WARN, msg, throwable);
		else if (level == Level.SEVERE)
			LOGGER.log(org.apache.logging.log4j.Level.ERROR, msg, throwable);
		else if (level == Level.FINE || level == Level.FINER || level == Level.FINEST)
			LOGGER.log(org.apache.logging.log4j.Level.TRACE, msg, throwable);
		else if (level == Level.CONFIG)
			LOGGER.log(org.apache.logging.log4j.Level.DEBUG, msg, throwable);
		else if (level == Level.ALL)
			LOGGER.log(org.apache.logging.log4j.Level.ALL, msg, throwable);
    }
}


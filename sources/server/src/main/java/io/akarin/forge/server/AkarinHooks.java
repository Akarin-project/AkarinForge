package io.akarin.forge.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AkarinHooks {
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	private static boolean initalizedConnection;
	private static int initalizeConnectionSteps = 2;
	
	public static boolean verboseMissingMods() {
		if (initalizedConnection)
			// faster access after initalization
			return true;
		
		else if (--initalizeConnectionSteps < 0)
			// after initalized
			return (initalizedConnection = true);
		
		else
			// haven't finish initalization
			return false;
	}
}

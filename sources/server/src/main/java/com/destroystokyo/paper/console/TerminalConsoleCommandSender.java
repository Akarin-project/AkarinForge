package com.destroystokyo.paper.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftConsoleCommandSender;

public class TerminalConsoleCommandSender extends CraftConsoleCommandSender {

    private static final Logger LOGGER = LogManager.getLogger(); // Akarin - do not use root logger

    @Override
    public void sendRawMessage(String message) {
        // TerminalConsoleAppender supports color codes directly in log messages
        LOGGER.info(message);
    }

}

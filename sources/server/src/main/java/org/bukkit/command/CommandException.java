/*
 * Akarin Forge
 */
package org.bukkit.command;

public class CommandException
extends RuntimeException {
    public CommandException() {
    }

    public CommandException(String msg) {
        super(msg);
    }

    public CommandException(String msg, Throwable cause) {
        super(msg, cause);
    }
}


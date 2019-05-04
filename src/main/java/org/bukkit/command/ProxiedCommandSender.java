/*
 * Akarin Forge
 */
package org.bukkit.command;

import org.bukkit.command.CommandSender;

public interface ProxiedCommandSender
extends CommandSender {
    public CommandSender getCaller();

    public CommandSender getCallee();
}


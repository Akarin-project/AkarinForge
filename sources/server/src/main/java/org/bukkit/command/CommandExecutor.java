/*
 * Akarin Forge
 */
package org.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandExecutor {
    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4);
}


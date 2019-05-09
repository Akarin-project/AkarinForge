/*
 * Akarin Forge
 */
package org.bukkit.command;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface TabCompleter {
    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4);
}


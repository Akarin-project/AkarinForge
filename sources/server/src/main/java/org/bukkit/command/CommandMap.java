/*
 * Akarin Forge
 */
package org.bukkit.command;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public interface CommandMap {
    public void registerAll(String var1, List<Command> var2);

    public boolean register(String var1, String var2, Command var3);

    public boolean register(String var1, Command var2);

    public boolean dispatch(CommandSender var1, String var2) throws CommandException;

    public void clearCommands();

    public Command getCommand(String var1);

    public List<String> tabComplete(CommandSender var1, String var2) throws IllegalArgumentException;

    public List<String> tabComplete(CommandSender var1, String var2, Location var3) throws IllegalArgumentException;
}


/*
 * Akarin Forge
 */
package org.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MultipleCommandAlias
extends Command {
    private Command[] commands;

    public MultipleCommandAlias(String name, Command[] commands) {
        super(name);
        this.commands = commands;
    }

    public Command[] getCommands() {
        return this.commands;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean result = false;
        for (Command command : this.commands) {
            result |= command.execute(sender, commandLabel, args);
        }
        return result;
    }
}


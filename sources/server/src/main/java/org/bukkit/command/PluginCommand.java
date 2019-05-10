/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.command;

import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public final class PluginCommand
extends Command
implements PluginIdentifiableCommand {
    private final Plugin owningPlugin;
    private CommandExecutor executor;
    private TabCompleter completer;

    protected PluginCommand(String name, Plugin owner) {
        super(name);
        this.executor = owner;
        this.owningPlugin = owner;
        this.usageMessage = "";
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean success = false;
        if (!this.owningPlugin.isEnabled()) {
            throw new CommandException("Cannot execute command '" + commandLabel + "' in plugin " + this.owningPlugin.getDescription().getFullName() + " - plugin is disabled.");
        }
        if (!this.testPermission(sender)) {
            return true;
        }
        try {
            success = this.executor.onCommand(sender, this, commandLabel, args);
        }
        catch (Throwable ex2) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + this.owningPlugin.getDescription().getFullName(), ex2);
        }
        if (!success && this.usageMessage.length() > 0) {
            for (String line : this.usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }
        return success;
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor == null ? this.owningPlugin : executor;
    }

    public CommandExecutor getExecutor() {
        return this.executor;
    }

    public void setTabCompleter(TabCompleter completer) {
        this.completer = completer;
    }

    public TabCompleter getTabCompleter() {
        return this.completer;
    }

    @Override
    public Plugin getPlugin() {
        return this.owningPlugin;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws CommandException, IllegalArgumentException {
        Validate.notNull((Object)sender, (String)"Sender cannot be null");
        Validate.notNull((Object)args, (String)"Arguments cannot be null");
        Validate.notNull((Object)alias, (String)"Alias cannot be null");
        List<String> completions = null;
        try {
            if (this.completer != null) {
                completions = this.completer.onTabComplete(sender, this, alias, args);
            }
            if (completions == null && this.executor instanceof TabCompleter) {
                completions = ((TabCompleter)((Object)this.executor)).onTabComplete(sender, this, alias, args);
            }
        }
        catch (Throwable ex2) {
            StringBuilder message = new StringBuilder();
            message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
            for (String arg2 : args) {
                message.append(arg2).append(' ');
            }
            message.deleteCharAt(message.length() - 1).append("' in plugin ").append(this.owningPlugin.getDescription().getFullName());
            throw new CommandException(message.toString(), ex2);
        }
        if (completions == null) {
            return super.tabComplete(sender, alias, args);
        }
        return completions;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(", ").append(this.owningPlugin.getDescription().getFullName()).append(')');
        return stringBuilder.toString();
    }
}


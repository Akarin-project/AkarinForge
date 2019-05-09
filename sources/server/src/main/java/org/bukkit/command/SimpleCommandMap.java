/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.FormattedCommandAlias;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.command.defaults.TimingsCommand;
import org.bukkit.command.defaults.VersionCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import io.akarin.forge.command.ChunkStats;
import io.akarin.forge.command.CommandPlugin;
import io.akarin.forge.command.PermissionCommand;

public class SimpleCommandMap
implements CommandMap {
    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", 16);
    protected final Map<String, Command> knownCommands = new HashMap<String, Command>();
    private final Server server;

    public SimpleCommandMap(Server server) {
        this.server = server;
        this.setDefaultCommands();
    }

    private void setDefaultCommands() {
        this.register("bukkit", new VersionCommand("version"));
        this.register("bukkit", new PluginsCommand("plugins"));
        this.register("bukkit", new TimingsCommand("timings"));
        this.register("catserver", new CommandPlugin("plugin"));
        this.register("fakefile", new PermissionCommand("fakefile"));
        this.register("chunkstats", new ChunkStats("chunkstats"));
    }

    public void setFallbackCommands() {
        this.register("bukkit", new HelpCommand());
    }

    @Override
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        if (commands != null) {
            for (Command c2 : commands) {
                this.register(fallbackPrefix, c2);
            }
        }
    }

    @Override
    public boolean register(String fallbackPrefix, Command command) {
        return this.register(command.getName(), fallbackPrefix, command);
    }

    @Override
    public boolean register(String label, String fallbackPrefix, Command command) {
        label = label.toLowerCase(Locale.ENGLISH).trim();
        fallbackPrefix = fallbackPrefix.toLowerCase(Locale.ENGLISH).trim();
        boolean registered = this.register(label, command, false, fallbackPrefix);
        Iterator<String> iterator = command.getAliases().iterator();
        while (iterator.hasNext()) {
            if (this.register(iterator.next(), command, true, fallbackPrefix)) continue;
            iterator.remove();
        }
        if (!registered) {
            command.setLabel(fallbackPrefix + ":" + label);
        }
        command.register(this);
        return registered;
    }

    private synchronized boolean register(String label, Command command, boolean isAlias, String fallbackPrefix) {
        this.knownCommands.put(fallbackPrefix + ":" + label, command);
        if ((command instanceof BukkitCommand || isAlias) && this.knownCommands.containsKey(label)) {
            return false;
        }
        boolean registered = true;
        Command conflict = this.knownCommands.get(label);
        if (conflict != null && conflict.getLabel().equals(label)) {
            return false;
        }
        if (!isAlias) {
            command.setLabel(label);
        }
        this.knownCommands.put(label, command);
        return registered;
    }

    @Override
    public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
        String[] args = PATTERN_ON_SPACE.split(commandLine);
        if (args.length == 0) {
            return false;
        }
        String sentCommandLabel = args[0].toLowerCase(Locale.ENGLISH);
        Command target = this.getCommand(sentCommandLabel);
        if (target == null) {
            return false;
        }
        try {
            target.execute(sender, sentCommandLabel, Arrays.copyOfRange(args, 1, args.length));
        }
        catch (CommandException ex2) {
            throw ex2;
        }
        catch (Throwable ex3) {
            throw new CommandException("Unhandled exception executing '" + commandLine + "' in " + target, ex3);
        }
        return true;
    }

    @Override
    public synchronized void clearCommands() {
        for (Map.Entry<String, Command> entry : this.knownCommands.entrySet()) {
            entry.getValue().unregister(this);
        }
        this.knownCommands.clear();
        this.setDefaultCommands();
    }

    @Override
    public Command getCommand(String name) {
        Command target = this.knownCommands.get(name.toLowerCase(Locale.ENGLISH));
        return target;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String cmdLine) {
        return this.tabComplete(sender, cmdLine, null);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String cmdLine, Location location) {
        Validate.notNull((Object)sender, (String)"Sender cannot be null");
        Validate.notNull((Object)cmdLine, (String)"Command line cannot null");
        int spaceIndex = cmdLine.indexOf(32);
        if (spaceIndex == -1) {
            ArrayList<String> completions = new ArrayList<String>();
            Map<String, Command> knownCommands = this.knownCommands;
            String prefix = sender instanceof Player ? "/" : "";
            for (Map.Entry<String, Command> commandEntry : knownCommands.entrySet()) {
                String name;
                Command command = commandEntry.getValue();
                if (!command.testPermissionSilent(sender) || !StringUtil.startsWithIgnoreCase(name = commandEntry.getKey(), cmdLine)) continue;
                completions.add(prefix + name);
            }
            Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
            return completions;
        }
        String commandName = cmdLine.substring(0, spaceIndex);
        Command target = this.getCommand(commandName);
        if (target == null) {
            return null;
        }
        if (!target.testPermissionSilent(sender)) {
            return null;
        }
        String argLine = cmdLine.substring(spaceIndex + 1, cmdLine.length());
        String[] args = PATTERN_ON_SPACE.split(argLine, -1);
        try {
            return target.tabComplete(sender, commandName, args, location);
        }
        catch (CommandException ex2) {
            throw ex2;
        }
        catch (Throwable ex3) {
            throw new CommandException("Unhandled exception executing tab-completer for '" + cmdLine + "' in " + target, ex3);
        }
    }

    public Collection<Command> getCommands() {
        return Collections.unmodifiableCollection(this.knownCommands.values());
    }

    public void registerServerAliases() {
        Map<String, String[]> values = this.server.getCommandAliases();
        for (Map.Entry<String, String[]> entry : values.entrySet()) {
            String alias = entry.getKey();
            if (alias.contains(" ")) {
                this.server.getLogger().warning("Could not register alias " + alias + " because it contains illegal characters");
                continue;
            }
            String[] commandStrings = entry.getValue();
            ArrayList<String> targets = new ArrayList<String>();
            StringBuilder bad2 = new StringBuilder();
            for (String commandString : commandStrings) {
                String[] commandArgs = commandString.split(" ");
                Command command = this.getCommand(commandArgs[0]);
                if (command == null) {
                    if (bad2.length() > 0) {
                        bad2.append(", ");
                    }
                    bad2.append(commandString);
                    continue;
                }
                targets.add(commandString);
            }
            if (bad2.length() > 0) {
                this.server.getLogger().warning("Could not register alias " + alias + " because it contains commands that do not exist: " + bad2);
                continue;
            }
            if (targets.size() > 0) {
                this.knownCommands.put(alias.toLowerCase(Locale.ENGLISH), new FormattedCommandAlias(alias.toLowerCase(Locale.ENGLISH), targets.toArray(new String[targets.size()])));
                continue;
            }
            this.knownCommands.remove(alias.toLowerCase(Locale.ENGLISH));
        }
    }
}


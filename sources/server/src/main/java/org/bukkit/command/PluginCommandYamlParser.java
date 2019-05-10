/*
 * Akarin Forge
 */
package org.bukkit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PluginCommandYamlParser {
    public static List<Command> parse(Plugin plugin) {
        ArrayList<Command> pluginCmds = new ArrayList<Command>();
        Map<String, Map<String, Object>> map = plugin.getDescription().getCommands();
        if (map == null) {
            return pluginCmds;
        }
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            if (entry.getKey().contains(":")) {
                Bukkit.getServer().getLogger().severe("Could not load command " + entry.getKey() + " for plugin " + plugin.getName() + ": Illegal Characters");
                continue;
            }
            PluginCommand newCmd = new PluginCommand(entry.getKey(), plugin);
            Object description = entry.getValue().get("description");
            Object usage = entry.getValue().get("usage");
            Object aliases = entry.getValue().get("aliases");
            Object permission = entry.getValue().get("permission");
            Object permissionMessage = entry.getValue().get("permission-message");
            if (description != null) {
                newCmd.setDescription(description.toString());
            }
            if (usage != null) {
                newCmd.setUsage(usage.toString());
            }
            if (aliases != null) {
                ArrayList<String> aliasList = new ArrayList<String>();
                if (aliases instanceof List) {
                    for (Object o2 : (List)aliases) {
                        if (o2.toString().contains(":")) {
                            Bukkit.getServer().getLogger().severe("Could not load alias " + o2.toString() + " for plugin " + plugin.getName() + ": Illegal Characters");
                            continue;
                        }
                        aliasList.add(o2.toString());
                    }
                } else if (aliases.toString().contains(":")) {
                    Bukkit.getServer().getLogger().severe("Could not load alias " + aliases.toString() + " for plugin " + plugin.getName() + ": Illegal Characters");
                } else {
                    aliasList.add(aliases.toString());
                }
                newCmd.setAliases(aliasList);
            }
            if (permission != null) {
                newCmd.setPermission(permission.toString());
            }
            if (permissionMessage != null) {
                newCmd.setPermissionMessage(permissionMessage.toString());
            }
            pluginCmds.add(newCmd);
        }
        return pluginCmds;
    }
}


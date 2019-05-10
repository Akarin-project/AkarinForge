/*
 * Akarin Forge
 */
package org.bukkit.command.defaults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PluginsCommand
extends BukkitCommand {
    public PluginsCommand(String name) {
        super(name);
        this.description = "Gets a list of plugins running on the server";
        this.usageMessage = "/plugins";
        this.setPermission("bukkit.command.plugins");
        this.setAliases(Arrays.asList("pl"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        sender.sendMessage("Plugins " + this.getPluginList());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }

    private String getPluginList() {
        Plugin[] plugins;
        StringBuilder pluginList = new StringBuilder();
        for (Plugin plugin : plugins = Bukkit.getPluginManager().getPlugins()) {
            if (pluginList.length() > 0) {
                pluginList.append((Object)ChatColor.WHITE);
                pluginList.append(", ");
            }
            pluginList.append((Object)(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED));
            pluginList.append(plugin.getDescription().getName());
        }
        return "(" + plugins.length + "): " + pluginList.toString();
    }
}


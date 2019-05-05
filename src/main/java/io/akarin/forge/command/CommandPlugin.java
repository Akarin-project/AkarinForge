/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package io.akarin.forge.command;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;

public class CommandPlugin
extends Command {
    public CommandPlugin(String name) {
        super(name);
        this.description = "Load or unload plugin";
        this.usageMessage = "/plugin <load|unload> <name>";
        this.setPermission("catserver.command.plugin");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage((Object)((Object)ChatColor.GOLD) + "Usage: " + this.usageMessage);
            return false;
        }
        String action = args[0].toLowerCase();
        String pluginName = args[1];
        try {
            if (action.equals("unload")) {
                this.unloadPlugin(pluginName, sender);
            } else if (action.equals("load")) {
                this.loadPlugin(pluginName, sender);
            } else {
                sender.sendMessage((Object)((Object)ChatColor.GREEN) + "Invalid action specified.");
            }
        }
        catch (Exception e2) {
            sender.sendMessage((Object)((Object)ChatColor.GREEN) + "Error with " + pluginName + ": " + e2.toString());
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        ArrayList tabs = Lists.newArrayList();
        if (args.length <= 1) return tabs;
        String action = args[0].toLowerCase();
        if (action.equals("unload")) {
            Plugin[] arrplugin = Bukkit.getServer().getPluginManager().getPlugins();
            int n2 = arrplugin.length;
            int n3 = 0;
            while (n3 < n2) {
                Plugin plugin = arrplugin[n3];
                tabs.add(plugin.getName());
                ++n3;
            }
            return tabs;
        }
        if (!action.equals("load")) return tabs;
        File[] arrfile = new File("plugins").listFiles();
        int n4 = arrfile.length;
        int n5 = 0;
        while (n5 < n4) {
            File file = arrfile[n5];
            if (file.isFile() && file.getName().toLowerCase().endsWith(".jar")) {
                tabs.add(file.getName().substring(0, file.getName().length() - 4));
            }
            ++n5;
        }
        return tabs;
    }

    private void unloadPlugin(String pluginName, CommandSender sender) throws Exception {
        SimplePluginManager manager = (SimplePluginManager)Bukkit.getServer().getPluginManager();
        List plugins = (List)ReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "plugins");
        Map lookupNames = (Map)ReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "lookupNames");
        SimpleCommandMap commandMap = (SimpleCommandMap)ReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "commandMap");
        Map knownCommands = (Map)ReflectionHelper.getPrivateValue(SimpleCommandMap.class, commandMap, "knownCommands");
        for (Plugin plugin : manager.getPlugins()) {
            if (!plugin.getDescription().getName().equalsIgnoreCase(pluginName)) continue;
            manager.disablePlugin(plugin);
            plugins.remove(plugin);
            lookupNames.remove(pluginName);
            Iterator it2 = knownCommands.entrySet().iterator();
            while (it2.hasNext()) {
                PluginCommand command;
                Map.Entry entry = it2.next();
                if (!(entry.getValue() instanceof PluginCommand) || (command = (PluginCommand)entry.getValue()).getPlugin() != plugin) continue;
                command.unregister(commandMap);
                it2.remove();
            }
            sender.sendMessage((Object)((Object)ChatColor.GREEN) + "Unloaded " + pluginName + " successfully!");
            return;
        }
        sender.sendMessage((Object)((Object)ChatColor.GREEN) + "Can't found loaded plugin: " + pluginName);
    }

    private void loadPlugin(String pluginName, CommandSender sender) {
        try {
            PluginManager manager = Bukkit.getServer().getPluginManager();
            File pluginFile = new File("plugins", pluginName + ".jar");
            if (!pluginFile.exists() || !pluginFile.isFile()) {
                sender.sendMessage((Object)((Object)ChatColor.GREEN) + "Error loading " + pluginName + ".jar, no plugin with that name was found.");
                return;
            }
            Plugin plugin = manager.loadPlugin(pluginFile);
            plugin.onLoad();
            manager.enablePlugin(plugin);
            sender.sendMessage((Object)((Object)ChatColor.GREEN) + "Loaded " + pluginName + " successfully!");
        }
        catch (Exception e2) {
            sender.sendMessage((Object)((Object)ChatColor.GREEN) + "Error loading " + pluginName + ".jar, this plugin must be reloaded by restarting the server.");
        }
    }
}


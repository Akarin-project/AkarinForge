/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.ImmutableList
 *  com.google.common.io.Resources
 *  org.apache.commons.lang.Validate
 *  org.json.simple.JSONObject
 *  org.json.simple.parser.JSONParser
 *  org.json.simple.parser.ParseException
 */
package org.bukkit.command.defaults;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraftforge.common.ForgeVersion;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.StringUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VersionCommand
extends BukkitCommand {
    private final ReentrantLock versionLock = new ReentrantLock();
    private boolean hasVersion = false;
    private String versionMessage = null;
    private final Set<CommandSender> versionWaiters = new HashSet<CommandSender>();
    private boolean versionTaskStarted = false;
    private long lastCheck = 0;

    public VersionCommand(String name) {
        super(name);
        this.description = "Gets the version of this server including any plugins in use";
        this.usageMessage = "/version [plugin name]";
        this.setPermission("bukkit.command.version");
        this.setAliases(Arrays.asList("ver", "about"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("This server is running " + Bukkit.getName() + " version " + Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ", Forge version " + ForgeVersion.getVersion() + ")");
        } else {
            StringBuilder name = new StringBuilder();
            for (String arg2 : args) {
                if (name.length() > 0) {
                    name.append(' ');
                }
                name.append(arg2);
            }
            String pluginName = name.toString();
            Plugin exactPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (exactPlugin != null) {
                this.describeToSender(exactPlugin, sender);
                return true;
            }
            boolean found = false;
            pluginName = pluginName.toLowerCase(Locale.ENGLISH);
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                if (!plugin.getName().toLowerCase(Locale.ENGLISH).contains(pluginName)) continue;
                this.describeToSender(plugin, sender);
                found = true;
            }
            if (!found) {
                sender.sendMessage("This server is not running any plugin by that name.");
                sender.sendMessage("Use /plugins to get a list of plugins.");
            }
        }
        return true;
    }

    private void describeToSender(Plugin plugin, CommandSender sender) {
        PluginDescriptionFile desc = plugin.getDescription();
        sender.sendMessage((Object)((Object)ChatColor.GREEN) + desc.getName() + (Object)((Object)ChatColor.WHITE) + " version " + (Object)((Object)ChatColor.GREEN) + desc.getVersion());
        if (desc.getDescription() != null) {
            sender.sendMessage(desc.getDescription());
        }
        if (desc.getWebsite() != null) {
            sender.sendMessage("Website: " + (Object)((Object)ChatColor.GREEN) + desc.getWebsite());
        }
        if (!desc.getAuthors().isEmpty()) {
            if (desc.getAuthors().size() == 1) {
                sender.sendMessage("Author: " + this.getAuthors(desc));
            } else {
                sender.sendMessage("Authors: " + this.getAuthors(desc));
            }
        }
    }

    private String getAuthors(PluginDescriptionFile desc) {
        StringBuilder result = new StringBuilder();
        List<String> authors = desc.getAuthors();
        for (int i2 = 0; i2 < authors.size(); ++i2) {
            if (result.length() > 0) {
                result.append((Object)ChatColor.WHITE);
                if (i2 < authors.size() - 1) {
                    result.append(", ");
                } else {
                    result.append(" and ");
                }
            }
            result.append((Object)ChatColor.GREEN);
            result.append(authors.get(i2));
        }
        return result.toString();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull((Object)sender, (String)"Sender cannot be null");
        Validate.notNull((Object)args, (String)"Arguments cannot be null");
        Validate.notNull((Object)alias, (String)"Alias cannot be null");
        if (args.length == 1) {
            ArrayList<String> completions = new ArrayList<String>();
            String toComplete = args[0].toLowerCase(Locale.ENGLISH);
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                if (!StringUtil.startsWithIgnoreCase(plugin.getName(), toComplete)) continue;
                completions.add(plugin.getName());
            }
            return completions;
        }
        return ImmutableList.of();
    }

    private void sendVersion(CommandSender sender) {
        if (this.hasVersion) {
            if (System.currentTimeMillis() - this.lastCheck > 21600000) {
                this.lastCheck = System.currentTimeMillis();
                this.hasVersion = false;
            } else {
                sender.sendMessage(this.versionMessage);
                return;
            }
        }
        this.versionLock.lock();
        try {
            if (this.hasVersion) {
                sender.sendMessage(this.versionMessage);
                return;
            }
            this.versionWaiters.add(sender);
            sender.sendMessage("Checking version, please wait...");
            if (!this.versionTaskStarted) {
                this.versionTaskStarted = true;
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        VersionCommand.this.obtainVersion();
                    }
                }).start();
            }
        }
        finally {
            this.versionLock.unlock();
        }
    }

    private void obtainVersion() {
        String version = Bukkit.getVersion();
        if (version == null) {
            version = "Custom";
        }
        if (version.startsWith("git-Spigot-")) {
            String[] parts = version.substring("git-Spigot-".length()).split("-");
            int cbVersions = VersionCommand.getDistance("craftbukkit", parts[1].substring(0, parts[1].indexOf(32)));
            int spigotVersions = VersionCommand.getDistance("spigot", parts[0]);
            if (cbVersions == -1 || spigotVersions == -1) {
                this.setVersionMessage("Error obtaining version information");
            } else if (cbVersions == 0 && spigotVersions == 0) {
                this.setVersionMessage("You are running the latest version");
            } else {
                this.setVersionMessage("You are " + (cbVersions + spigotVersions) + " version(s) behind");
            }
        } else if (version.startsWith("git-Bukkit-")) {
            int cbVersions = VersionCommand.getDistance("craftbukkit", (version = version.substring("git-Bukkit-".length())).substring(0, version.indexOf(32)));
            if (cbVersions == -1) {
                this.setVersionMessage("Error obtaining version information");
            } else if (cbVersions == 0) {
                this.setVersionMessage("You are running the latest version");
            } else {
                this.setVersionMessage("You are " + cbVersions + " version(s) behind");
            }
        } else {
            this.setVersionMessage("Unknown version, custom build?");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void setVersionMessage(String msg) {
        this.lastCheck = System.currentTimeMillis();
        this.versionMessage = msg;
        this.versionLock.lock();
        try {
            this.hasVersion = true;
            this.versionTaskStarted = false;
            for (CommandSender sender : this.versionWaiters) {
                sender.sendMessage(this.versionMessage);
            }
            this.versionWaiters.clear();
        }
        finally {
            this.versionLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int getDistance(String repo, String hash) {
        int n2;
        BufferedReader reader = Resources.asCharSource((URL)new URL("https://hub.spigotmc.org/stash/rest/api/1.0/projects/SPIGOT/repos/" + repo + "/commits?since=" + URLEncoder.encode(hash, "UTF-8") + "&withCounts=true"), (Charset)Charsets.UTF_8).openBufferedStream();
        try {
            JSONObject obj = (JSONObject)new JSONParser().parse((Reader)reader);
            n2 = ((Number)obj.get((Object)"totalCount")).intValue();
        }
        catch (ParseException ex2) {
            int n3;
            try {
                ex2.printStackTrace();
                n3 = -1;
            }
            catch (Throwable throwable) {
                try {
                    reader.close();
                    throw throwable;
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                    return -1;
                }
            }
            reader.close();
            return n3;
        }
        reader.close();
        return n2;
    }

}


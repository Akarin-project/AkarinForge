/*
 * Akarin Forge
 */
package org.bukkit.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

public interface Plugin
extends TabExecutor {
    public File getDataFolder();

    public PluginDescriptionFile getDescription();

    public FileConfiguration getConfig();

    public InputStream getResource(String var1);

    public void saveConfig();

    public void saveDefaultConfig();

    public void saveResource(String var1, boolean var2);

    public void reloadConfig();

    public PluginLoader getPluginLoader();

    public Server getServer();

    public boolean isEnabled();

    public void onDisable();

    public void onLoad();

    public void onEnable();

    public boolean isNaggable();

    public void setNaggable(boolean var1);

    public ChunkGenerator getDefaultWorldGenerator(String var1, String var2);

    public Logger getLogger();

    public String getName();
}


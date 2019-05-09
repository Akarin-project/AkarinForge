/*
 * Akarin Forge
 */
package org.bukkit.plugin;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;

public interface PluginLoader {
    public Plugin loadPlugin(File var1) throws InvalidPluginException, UnknownDependencyException;

    public PluginDescriptionFile getPluginDescription(File var1) throws InvalidDescriptionException;

    public Pattern[] getPluginFileFilters();

    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener var1, Plugin var2);

    public void enablePlugin(Plugin var1);

    public void disablePlugin(Plugin var1);
}


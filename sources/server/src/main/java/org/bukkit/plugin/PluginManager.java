/*
 * Akarin Forge
 */
package org.bukkit.plugin;

import java.io.File;
import java.util.Set;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.UnknownDependencyException;

public interface PluginManager {
    public void registerInterface(Class<? extends PluginLoader> var1) throws IllegalArgumentException;

    public Plugin getPlugin(String var1);

    public Plugin[] getPlugins();

    public boolean isPluginEnabled(String var1);

    public boolean isPluginEnabled(Plugin var1);

    public Plugin loadPlugin(File var1) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException;

    public Plugin[] loadPlugins(File var1);

    public void disablePlugins();

    public void clearPlugins();

    public void callEvent(Event var1) throws IllegalStateException;

    public void registerEvents(Listener var1, Plugin var2);

    public void registerEvent(Class<? extends Event> var1, Listener var2, EventPriority var3, EventExecutor var4, Plugin var5);

    public void registerEvent(Class<? extends Event> var1, Listener var2, EventPriority var3, EventExecutor var4, Plugin var5, boolean var6);

    public void enablePlugin(Plugin var1);

    public void disablePlugin(Plugin var1);

    public Permission getPermission(String var1);

    public void addPermission(Permission var1);

    public void removePermission(Permission var1);

    public void removePermission(String var1);

    public Set<Permission> getDefaultPermissions(boolean var1);

    public void recalculatePermissionDefaults(Permission var1);

    public void subscribeToPermission(String var1, Permissible var2);

    public void unsubscribeFromPermission(String var1, Permissible var2);

    public Set<Permissible> getPermissionSubscriptions(String var1);

    public void subscribeToDefaultPerms(boolean var1, Permissible var2);

    public void unsubscribeFromDefaultPerms(boolean var1, Permissible var2);

    public Set<Permissible> getDefaultPermSubscriptions(boolean var1);

    public Set<Permission> getPermissions();

    public boolean useTimings();
}


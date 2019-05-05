/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.plugin;

import com.google.common.collect.ImmutableSet;

import io.akarin.forge.AkarinForge;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFuckPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.TimedRegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.FileUtil;

public final class SimplePluginManager
implements PluginManager {
    private final Server server;
    private final Map<Pattern, PluginLoader> fileAssociations = new HashMap<Pattern, PluginLoader>();
    private final List<Plugin> plugins = new ArrayList<Plugin>();
    private final Map<String, Plugin> lookupNames = new HashMap<String, Plugin>();
    private File updateDirectory;
    private final SimpleCommandMap commandMap;
    private final Map<String, Permission> permissions = new HashMap<String, Permission>();
    private final Map<Boolean, Set<Permission>> defaultPerms = new LinkedHashMap<Boolean, Set<Permission>>();
    private final Map<String, Map<Permissible, Boolean>> permSubs = new HashMap<String, Map<Permissible, Boolean>>();
    private final Map<Boolean, Map<Permissible, Boolean>> defSubs = new HashMap<Boolean, Map<Permissible, Boolean>>();
    private boolean useTimings = false;

    public SimplePluginManager(Server instance, SimpleCommandMap commandMap) {
        this.server = instance;
        this.commandMap = commandMap;
        this.defaultPerms.put(true, new HashSet());
        this.defaultPerms.put(false, new HashSet());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void registerInterface(Class<? extends PluginLoader> loader) throws IllegalArgumentException {
        PluginLoader instance;
        if (PluginLoader.class.isAssignableFrom(loader)) {
            try {
                Constructor<? extends PluginLoader> constructor = loader.getConstructor(Server.class);
                instance = constructor.newInstance(this.server);
            }
            catch (NoSuchMethodException ex2) {
                String className = loader.getName();
                throw new IllegalArgumentException(String.format("Class %s does not have a public %s(Server) constructor", className, className), ex2);
            }
            catch (Exception ex3) {
                throw new IllegalArgumentException(String.format("Unexpected exception %s while attempting to construct a new instance of %s", ex3.getClass().getName(), loader.getName()), ex3);
            }
        } else {
            throw new IllegalArgumentException(String.format("Class %s does not implement interface PluginLoader", loader.getName()));
        }
        Pattern[] patterns = instance.getPluginFileFilters();
        SimplePluginManager ex3 = this;
        synchronized (ex3) {
            for (Pattern pattern : patterns) {
                this.fileAssociations.put(pattern, instance);
            }
        }
    }

    @Override
    public Plugin[] loadPlugins(File directory) {
        Validate.notNull((Object)directory, (String)"Directory cannot be null");
        Validate.isTrue((boolean)directory.isDirectory(), (String)"Directory must be a directory");
        ArrayList<Plugin> result = new ArrayList<Plugin>();
        Set<Pattern> filters = this.fileAssociations.keySet();
        if (!this.server.getUpdateFolder().equals("")) {
            this.updateDirectory = new File(directory, this.server.getUpdateFolder());
        }
        HashMap<String, File> plugins = new HashMap<String, File>();
        HashSet<String> loadedPlugins = new HashSet<String>();
        HashMap<String, LinkedList<String>> dependencies = new HashMap<String, LinkedList<String>>();
        HashMap softDependencies = new HashMap();
        for (File file2 : directory.listFiles()) {
            List<String> softDependencySet;
            List<String> dependencySet;
            PluginDescriptionFile description;
            List<String> loadBeforeSet;
            block29 : {
                PluginLoader loader = null;
                for (Pattern filter : filters) {
                    Matcher match = filter.matcher(file2.getName());
                    if (!match.find()) continue;
                    loader = this.fileAssociations.get(filter);
                }
                if (loader == null) continue;
                description = null;
                try {
                    description = loader.getPluginDescription(file2);
                    String name = description.getName();
                    if (name.equalsIgnoreCase("bukkit") || name.equalsIgnoreCase("minecraft") || name.equalsIgnoreCase("mojang")) {
                        this.server.getLogger().log(Level.SEVERE, "Could not load '" + file2.getPath() + "' in folder '" + directory.getPath() + "': Restricted Name");
                        continue;
                    }
                    if (description.rawName.indexOf(32) != -1) {
                        this.server.getLogger().log(Level.SEVERE, "Could not load '" + file2.getPath() + "' in folder '" + directory.getPath() + "': uses the space-character (0x20) in its name");
                    }
                    break block29;
                }
                catch (InvalidDescriptionException ex2) {
                    this.server.getLogger().log(Level.SEVERE, "Could not load '" + file2.getPath() + "' in folder '" + directory.getPath() + "'", ex2);
                }
                continue;
            }
            File replacedFile = plugins.put(description.getName(), file2);
            if (replacedFile != null) {
                this.server.getLogger().severe(String.format("Ambiguous plugin name `%s' for files `%s' and `%s' in `%s'", description.getName(), file2.getPath(), replacedFile.getPath(), directory.getPath()));
            }
            if ((softDependencySet = description.getSoftDepend()) != null && !softDependencySet.isEmpty()) {
                if (softDependencies.containsKey(description.getName())) {
                    ((Collection)softDependencies.get(description.getName())).addAll(softDependencySet);
                } else {
                    softDependencies.put(description.getName(), new LinkedList<String>(softDependencySet));
                }
            }
            if ((dependencySet = description.getDepend()) != null && !dependencySet.isEmpty()) {
                dependencies.put(description.getName(), new LinkedList<String>(dependencySet));
            }
            if ((loadBeforeSet = description.getLoadBefore()) == null || loadBeforeSet.isEmpty()) continue;
            for (String loadBeforeTarget : loadBeforeSet) {
                if (softDependencies.containsKey(loadBeforeTarget)) {
                    ((Collection)softDependencies.get(loadBeforeTarget)).add(description.getName());
                    continue;
                }
                LinkedList<String> shortSoftDependency = new LinkedList<String>();
                shortSoftDependency.add(description.getName());
                softDependencies.put(loadBeforeTarget, shortSoftDependency);
            }
        }
        while (!plugins.isEmpty()) {
            String plugin;
            File file;
            boolean missingDependency = true;
            Iterator pluginIterator = plugins.entrySet().iterator();
            while (pluginIterator.hasNext()) {
                Map.Entry entry = pluginIterator.next();
                plugin = (String)entry.getKey();
                if (dependencies.containsKey(plugin)) {
                    Iterator dependencyIterator = ((Collection)dependencies.get(plugin)).iterator();
                    while (dependencyIterator.hasNext()) {
                        String dependency = (String)dependencyIterator.next();
                        if (loadedPlugins.contains(dependency)) {
                            dependencyIterator.remove();
                            continue;
                        }
                        if (plugins.containsKey(dependency)) continue;
                        missingDependency = false;
                        pluginIterator.remove();
                        softDependencies.remove(plugin);
                        dependencies.remove(plugin);
                        this.server.getLogger().log(Level.SEVERE, "Could not load '" + ((File)entry.getValue()).getPath() + "' in folder '" + directory.getPath() + "'", new UnknownDependencyException(dependency));
                        break;
                    }
                    if (dependencies.containsKey(plugin) && ((Collection)dependencies.get(plugin)).isEmpty()) {
                        dependencies.remove(plugin);
                    }
                }
                if (softDependencies.containsKey(plugin)) {
                    Iterator softDependencyIterator = ((Collection)softDependencies.get(plugin)).iterator();
                    while (softDependencyIterator.hasNext()) {
                        String softDependency = (String)softDependencyIterator.next();
                        if (plugins.containsKey(softDependency)) continue;
                        softDependencyIterator.remove();
                    }
                    if (((Collection)softDependencies.get(plugin)).isEmpty()) {
                        softDependencies.remove(plugin);
                    }
                }
                if (dependencies.containsKey(plugin) || softDependencies.containsKey(plugin) || !plugins.containsKey(plugin)) continue;
                file = (File)plugins.get(plugin);
                pluginIterator.remove();
                missingDependency = false;
                try {
                    result.add(this.loadPlugin(file));
                    loadedPlugins.add(plugin);
                }
                catch (InvalidPluginException ex3) {
                    this.server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "'", ex3);
                }
            }
            if (!missingDependency) continue;
            pluginIterator = plugins.entrySet().iterator();
            while (pluginIterator.hasNext()) {
                Map.Entry entry = pluginIterator.next();
                plugin = (String)entry.getKey();
                if (dependencies.containsKey(plugin)) continue;
                softDependencies.remove(plugin);
                missingDependency = false;
                file = (File)entry.getValue();
                pluginIterator.remove();
                try {
                    result.add(this.loadPlugin(file));
                    loadedPlugins.add(plugin);
                    break;
                }
                catch (InvalidPluginException ex4) {
                    this.server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "'", ex4);
                    continue;
                }
            }
            if (!missingDependency) continue;
            softDependencies.clear();
            dependencies.clear();
            Iterator failedPluginIterator = plugins.values().iterator();
            while (failedPluginIterator.hasNext()) {
                File file2;
                file2 = (File)failedPluginIterator.next();
                failedPluginIterator.remove();
                this.server.getLogger().log(Level.SEVERE, "Could not load '" + file2.getPath() + "' in folder '" + directory.getPath() + "': circular dependency detected");
            }
        }
        return result.toArray(new Plugin[result.size()]);
    }

    @Override
    public synchronized Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
        Validate.notNull((Object)file, (String)"File cannot be null");
        this.checkUpdate(file);
        Set<Pattern> filters = this.fileAssociations.keySet();
        Plugin result = null;
        for (Pattern filter : filters) {
            String name;
            Matcher match = filter.matcher(name = file.getName());
            if (!match.find()) continue;
            PluginLoader loader = this.fileAssociations.get(filter);
            result = loader.loadPlugin(file);
        }
        if (result != null) {
            this.plugins.add(result);
            this.lookupNames.put(result.getDescription().getName(), result);
        }
        return result;
    }

    private void checkUpdate(File file) {
        if (this.updateDirectory == null || !this.updateDirectory.isDirectory()) {
            return;
        }
        File updateFile = new File(this.updateDirectory, file.getName());
        if (updateFile.isFile() && FileUtil.copy(updateFile, file)) {
            updateFile.delete();
        }
    }

    @Override
    public synchronized Plugin getPlugin(String name) {
        return this.lookupNames.get(name.replace(' ', '_'));
    }

    @Override
    public synchronized Plugin[] getPlugins() {
        return this.plugins.toArray(new Plugin[this.plugins.size()]);
    }

    @Override
    public boolean isPluginEnabled(String name) {
        Plugin plugin = this.getPlugin(name);
        return this.isPluginEnabled(plugin);
    }

    @Override
    public boolean isPluginEnabled(Plugin plugin) {
        if (plugin != null && this.plugins.contains(plugin)) {
            return plugin.isEnabled();
        }
        return false;
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        if (!plugin.isEnabled()) {
            List<Command> pluginCommands = PluginCommandYamlParser.parse(plugin);
            if (!pluginCommands.isEmpty()) {
                this.commandMap.registerAll(plugin.getDescription().getName(), pluginCommands);
            }
            try {
                plugin.getPluginLoader().enablePlugin(plugin);
            }
            catch (Throwable ex2) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex2);
            }
            HandlerList.bakeAll();
        }
    }

    @Override
    public void disablePlugins() {
        Plugin[] plugins = this.getPlugins();
        for (int i2 = plugins.length - 1; i2 >= 0; --i2) {
            this.disablePlugin(plugins[i2]);
        }
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        if (plugin.isEnabled()) {
            try {
                plugin.getPluginLoader().disablePlugin(plugin);
            }
            catch (Throwable ex2) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex2);
            }
            try {
                this.server.getScheduler().cancelTasks(plugin);
            }
            catch (Throwable ex3) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while cancelling tasks for " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex3);
            }
            try {
                this.server.getServicesManager().unregisterAll(plugin);
            }
            catch (Throwable ex4) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while unregistering services for " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex4);
            }
            try {
                HandlerList.unregisterAll(plugin);
            }
            catch (Throwable ex5) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while unregistering events for " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex5);
            }
            try {
                this.server.getMessenger().unregisterIncomingPluginChannel(plugin);
                this.server.getMessenger().unregisterOutgoingPluginChannel(plugin);
            }
            catch (Throwable ex6) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while unregistering plugin channels for " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex6);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void clearPlugins() {
        SimplePluginManager simplePluginManager = this;
        synchronized (simplePluginManager) {
            this.disablePlugins();
            this.plugins.clear();
            this.lookupNames.clear();
            HandlerList.unregisterAll();
            this.fileAssociations.clear();
            this.permissions.clear();
            this.defaultPerms.get(true).clear();
            this.defaultPerms.get(false).clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void callEvent(Event event) {
        if (AkarinForge.fakePlayerEventPass && event instanceof PlayerEvent && ((PlayerEvent)event).getPlayer() instanceof CraftFuckPlayer) {
            return;
        }
        if (event.isAsynchronous() || !Bukkit.isPrimaryThread()) {
            if (Thread.holdsLock(this)) {
                throw new IllegalStateException(event.getEventName() + " cannot be triggered asynchronously from inside synchronized code.");
            }
            if (this.server.isPrimaryThread()) {
                throw new IllegalStateException(event.getEventName() + " cannot be triggered asynchronously from primary server thread.");
            }
            this.fireEvent(event);
        } else {
            SimplePluginManager simplePluginManager = this;
            synchronized (simplePluginManager) {
                this.fireEvent(event);
            }
        }
    }

    private void fireEvent(Event event) {
        RegisteredListener[] listeners;
        HandlerList handlers = event.getHandlers();
        for (RegisteredListener registration : listeners = handlers.getRegisteredListeners()) {
            if (!registration.getPlugin().isEnabled()) continue;
            try {
                registration.callEvent(event);
                continue;
            }
            catch (AuthorNagException ex2) {
                Plugin plugin = registration.getPlugin();
                if (!plugin.isNaggable()) continue;
                plugin.setNaggable(false);
                this.server.getLogger().log(Level.SEVERE, String.format("Nag author(s): '%s' of '%s' about the following: %s", plugin.getDescription().getAuthors(), plugin.getDescription().getFullName(), ex2.getMessage()));
                continue;
            }
            catch (Throwable ex3) {
                this.server.getLogger().log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getFullName(), ex3);
            }
        }
    }

    @Override
    public void registerEvents(Listener listener, Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
        }
        for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin).entrySet()) {
            this.getEventListeners(this.getRegistrationClass(entry.getKey())).registerAll((Collection)entry.getValue());
        }
    }

    @Override
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin) {
        this.registerEvent(event, listener, priority, executor, plugin, false);
    }

    @Override
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin, boolean ignoreCancelled) {
        Validate.notNull((Object)listener, (String)"Listener cannot be null");
        Validate.notNull((Object)((Object)priority), (String)"Priority cannot be null");
        Validate.notNull((Object)executor, (String)"Executor cannot be null");
        Validate.notNull((Object)plugin, (String)"Plugin cannot be null");
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + event + " while not enabled");
        }
        if (this.useTimings) {
            this.getEventListeners(event).register(new TimedRegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
        } else {
            this.getEventListeners(event).register(new RegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
        }
    }

    private HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = this.getRegistrationClass(type).getDeclaredMethod("getHandlerList", new Class[0]);
            method.setAccessible(true);
            return (HandlerList)method.invoke(null, new Object[0]);
        }
        catch (Exception e2) {
            throw new IllegalPluginAccessException(e2.toString());
        }
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList", new Class[0]);
            return clazz;
        }
        catch (NoSuchMethodException e2) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return this.getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            }
            throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
        }
    }

    @Override
    public Permission getPermission(String name) {
        return this.permissions.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public void addPermission(Permission perm) {
        this.addPermission(perm, true);
    }

    @Deprecated
    public void addPermission(Permission perm, boolean dirty) {
        String name = perm.getName().toLowerCase(Locale.ENGLISH);
        if (this.permissions.containsKey(name)) {
            throw new IllegalArgumentException("The permission " + name + " is already defined!");
        }
        this.permissions.put(name, perm);
        this.calculatePermissionDefault(perm, dirty);
    }

    @Override
    public Set<Permission> getDefaultPermissions(boolean op2) {
        return ImmutableSet.copyOf((Collection)this.defaultPerms.get(op2));
    }

    @Override
    public void removePermission(Permission perm) {
        this.removePermission(perm.getName());
    }

    @Override
    public void removePermission(String name) {
        this.permissions.remove(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public void recalculatePermissionDefaults(Permission perm) {
        if (perm != null && this.permissions.containsKey(perm.getName().toLowerCase(Locale.ENGLISH))) {
            this.defaultPerms.get(true).remove(perm);
            this.defaultPerms.get(false).remove(perm);
            this.calculatePermissionDefault(perm, true);
        }
    }

    private void calculatePermissionDefault(Permission perm, boolean dirty) {
        if (perm.getDefault() == PermissionDefault.OP || perm.getDefault() == PermissionDefault.TRUE) {
            this.defaultPerms.get(true).add(perm);
            if (dirty) {
                this.dirtyPermissibles(true);
            }
        }
        if (perm.getDefault() == PermissionDefault.NOT_OP || perm.getDefault() == PermissionDefault.TRUE) {
            this.defaultPerms.get(false).add(perm);
            if (dirty) {
                this.dirtyPermissibles(false);
            }
        }
    }

    @Deprecated
    public void dirtyPermissibles() {
        this.dirtyPermissibles(true);
        this.dirtyPermissibles(false);
    }

    private void dirtyPermissibles(boolean op2) {
        Set<Permissible> permissibles = this.getDefaultPermSubscriptions(op2);
        for (Permissible p2 : permissibles) {
            p2.recalculatePermissions();
        }
    }

    @Override
    public void subscribeToPermission(String permission, Permissible permissible) {
        String name = permission.toLowerCase(Locale.ENGLISH);
        Map<Permissible, Boolean> map = this.permSubs.get(name);
        if (map == null) {
            map = new WeakHashMap<Permissible, Boolean>();
            this.permSubs.put(name, map);
        }
        map.put(permissible, true);
    }

    @Override
    public void unsubscribeFromPermission(String permission, Permissible permissible) {
        String name = permission.toLowerCase(Locale.ENGLISH);
        Map<Permissible, Boolean> map = this.permSubs.get(name);
        if (map != null) {
            map.remove(permissible);
            if (map.isEmpty()) {
                this.permSubs.remove(name);
            }
        }
    }

    @Override
    public Set<Permissible> getPermissionSubscriptions(String permission) {
        String name = permission.toLowerCase(Locale.ENGLISH);
        Map<Permissible, Boolean> map = this.permSubs.get(name);
        if (map == null) {
            return ImmutableSet.of();
        }
        return ImmutableSet.copyOf(map.keySet());
    }

    @Override
    public void subscribeToDefaultPerms(boolean op2, Permissible permissible) {
        Map<Permissible, Boolean> map = this.defSubs.get(op2);
        if (map == null) {
            map = new WeakHashMap<Permissible, Boolean>();
            this.defSubs.put(op2, map);
        }
        map.put(permissible, true);
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean op2, Permissible permissible) {
        Map<Permissible, Boolean> map = this.defSubs.get(op2);
        if (map != null) {
            map.remove(permissible);
            if (map.isEmpty()) {
                this.defSubs.remove(op2);
            }
        }
    }

    @Override
    public Set<Permissible> getDefaultPermSubscriptions(boolean op2) {
        Map<Permissible, Boolean> map = this.defSubs.get(op2);
        if (map == null) {
            return ImmutableSet.of();
        }
        return ImmutableSet.copyOf(map.keySet());
    }

    @Override
    public Set<Permission> getPermissions() {
        return new HashSet<Permission>(this.permissions.values());
    }

    @Override
    public boolean useTimings() {
        return this.useTimings;
    }

    public void useTimings(boolean use) {
        this.useTimings = use;
    }
}


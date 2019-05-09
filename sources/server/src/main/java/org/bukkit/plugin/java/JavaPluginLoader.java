package org.bukkit.plugin.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.spigotmc.CustomTimingsHandler;
import org.yaml.snakeyaml.error.YAMLException;

import io.akarin.forge.executor.ReflectionExecutor;

public final class JavaPluginLoader
implements PluginLoader {
    final Server server;
    private final Pattern[] fileFilters = new Pattern[]{Pattern.compile("\\.jar$")};
    private final Map<String, Class<?>> classes = new ConcurrentHashMap();
    private final List<PluginClassLoader> loaders = new CopyOnWriteArrayList<PluginClassLoader>();
    public static final CustomTimingsHandler pluginParentTimer = new CustomTimingsHandler("** Plugins");

    @Deprecated
    public JavaPluginLoader(Server instance) {
        Validate.notNull((Object)instance, (String)"Server cannot be null");
        this.server = instance;
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException {
        PluginDescriptionFile description;
        PluginClassLoader loader;
        Validate.notNull((Object)file, (String)"File cannot be null");
        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        }
        try {
            description = this.getPluginDescription(file);
        }
        catch (InvalidDescriptionException ex2) {
            throw new InvalidPluginException(ex2);
        }
        File parentFile = file.getParentFile();
        File dataFolder = new File(parentFile, description.getName());
        File oldDataFolder = new File(parentFile, description.getRawName());
        if (!dataFolder.equals(oldDataFolder)) {
            if (dataFolder.isDirectory() && oldDataFolder.isDirectory()) {
                this.server.getLogger().warning(String.format("While loading %s (%s) found old-data folder: `%s' next to the new one `%s'", description.getFullName(), file, oldDataFolder, dataFolder));
            } else if (oldDataFolder.isDirectory() && !dataFolder.exists()) {
                if (!oldDataFolder.renameTo(dataFolder)) {
                    throw new InvalidPluginException("Unable to rename old data folder: `" + oldDataFolder + "' to: `" + dataFolder + "'");
                }
                this.server.getLogger().log(Level.INFO, String.format("While loading %s (%s) renamed data folder: `%s' to `%s'", description.getFullName(), file, oldDataFolder, dataFolder));
            }
        }
        if (dataFolder.exists() && !dataFolder.isDirectory()) {
            throw new InvalidPluginException(String.format("Projected datafolder: `%s' for %s (%s) exists and is not a directory", dataFolder, description.getFullName(), file));
        }
        for (String pluginName : description.getDepend()) {
            Plugin current = this.server.getPluginManager().getPlugin(pluginName);
            if (current != null) continue;
            throw new UnknownDependencyException(pluginName);
        }
        try {
            loader = new PluginClassLoader(this, this.getClass().getClassLoader(), description, dataFolder, file);
        }
        catch (InvalidPluginException ex3) {
            throw ex3;
        }
        catch (Throwable ex4) {
            throw new InvalidPluginException(ex4);
        }
        this.loaders.add(loader);
        return loader.plugin;
    }

    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
        Validate.notNull((Object)file, (String)"File cannot be null");
        JarFile jar = null;
        InputStream stream = null;
        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("plugin.yml");
            if (entry == null) {
                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }
            stream = jar.getInputStream(entry);
            PluginDescriptionFile pluginDescriptionFile = new PluginDescriptionFile(stream);
            return pluginDescriptionFile;
        }
        catch (IOException ex2) {
            throw new InvalidDescriptionException(ex2);
        }
        catch (YAMLException ex3) {
            throw new InvalidDescriptionException((Throwable)ex3);
        }
        finally {
            if (jar != null) {
                try {
                    jar.close();
                }
                catch (IOException iOException) {}
            }
            if (stream != null) {
                try {
                    stream.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    @Override
    public Pattern[] getPluginFileFilters() {
        return (Pattern[])this.fileFilters.clone();
    }

    Class<?> getClassByName(String name) {
        Class cachedClass = this.classes.get(name);
        if (cachedClass != null) {
            return cachedClass;
        }
        for (PluginClassLoader loader : this.loaders) {
            try {
                cachedClass = loader.findClass(name, false);
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
            if (cachedClass == null) continue;
            return cachedClass;
        }
        return null;
    }

    void setClass(String name, Class<?> clazz) {
        if (!this.classes.containsKey(name)) {
            this.classes.put(name, clazz);
            if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.registerClass(serializable);
            }
        }
    }

    private void removeClass(String name) {
        Class clazz = this.classes.remove(name);
        try {
            if (clazz != null && ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.unregisterClass(serializable);
            }
        }
        catch (NullPointerException serializable) {
            // empty catch block
        }
    }

    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin) {
        HashSet<Method> methods;
        Validate.notNull((Object)plugin, (String)"Plugin can not be null");
        Validate.notNull((Object)listener, (String)"Listener can not be null");
        boolean useTimings = this.server.getPluginManager().useTimings();
        HashMap<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<Class<? extends Event>, Set<RegisteredListener>>();
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<Method>(publicMethods.length + privateMethods.length, 1.0f);
            for (Method method : publicMethods) {
                methods.add(method);
            }
            for (Method method : privateMethods) {
                methods.add(method);
            }
        }
        catch (NoClassDefFoundError e2) {
            plugin.getLogger().severe("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + e2.getMessage() + " does not exist.");
            return ret;
        }
        for (Method method : methods) {
            EventExecutor executor;
            Class checkClass;
            EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null || method.isBridge() || method.isSynthetic()) continue;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                plugin.getLogger().severe(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            Class<Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.get(eventClass);
            if (eventSet == null) {
                eventSet = new HashSet<RegisteredListener>();
                ret.put(eventClass, eventSet);
            }
            Class<? extends Event> clazz = eventClass;
            while (Event.class.isAssignableFrom(clazz)) {
                if (clazz.getAnnotation(Deprecated.class) != null) {
                    Warning warning = clazz.getAnnotation(Warning.class);
                    Warning.WarningState warningState = this.server.getWarningState();
                    if (!warningState.printFor(warning)) break;
                    Object[] arrobject = new Object[5];
                    arrobject[0] = plugin.getDescription().getFullName();
                    arrobject[1] = clazz.getName();
                    arrobject[2] = method.toGenericString();
                    arrobject[3] = warning != null && warning.reason().length() != 0 ? warning.reason() : "Server performance will be affected";
                    arrobject[4] = Arrays.toString(plugin.getDescription().getAuthors().toArray());
                    plugin.getLogger().log(Level.WARNING, String.format("\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated. \"%s\"; please notify the authors %s.", arrobject), warningState == Warning.WarningState.ON ? new AuthorNagException(null) : null);
                    break;
                }
                clazz = (Class<? extends Event>) clazz.getSuperclass();
            }
            CustomTimingsHandler timings = new CustomTimingsHandler("Plugin: " + plugin.getDescription().getFullName() + " Event: " + listener.getClass().getName() + "::" + method.getName() + "(" + eventClass.getSimpleName() + ")", pluginParentTimer);
            try {
                executor = EventExecutor.create(method, eventClass);
            }
            catch (Exception e3) {
                executor = new ReflectionExecutor(method, eventClass, timings);
            }
            eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
        }
        return ret;
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        Validate.isTrue((boolean)(plugin instanceof JavaPlugin), (String)"Plugin is not associated with this PluginLoader");
        if (!plugin.isEnabled()) {
            plugin.getLogger().info("Enabling " + plugin.getDescription().getFullName());
            JavaPlugin jPlugin = (JavaPlugin)plugin;
            PluginClassLoader pluginLoader = (PluginClassLoader)jPlugin.getClassLoader();
            if (!this.loaders.contains(pluginLoader)) {
                this.loaders.add(pluginLoader);
                this.server.getLogger().log(Level.WARNING, "Enabled plugin with unregistered PluginClassLoader " + plugin.getDescription().getFullName());
            }
            try {
                jPlugin.setEnabled(true);
            }
            catch (Throwable ex2) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex2);
            }
            this.server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        Validate.isTrue((boolean)(plugin instanceof JavaPlugin), (String)"Plugin is not associated with this PluginLoader");
        if (plugin.isEnabled()) {
            String message = String.format("Disabling %s", plugin.getDescription().getFullName());
            plugin.getLogger().info(message);
            this.server.getPluginManager().callEvent(new PluginDisableEvent(plugin));
            JavaPlugin jPlugin = (JavaPlugin)plugin;
            ClassLoader cloader = jPlugin.getClassLoader();
            try {
                jPlugin.setEnabled(false);
            }
            catch (Throwable ex2) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex2);
            }
            if (cloader instanceof PluginClassLoader) {
                PluginClassLoader loader = (PluginClassLoader)cloader;
                this.loaders.remove(loader);
                Set<String> names = loader.getClasses();
                for (String name : names) {
                    this.removeClass(name);
                }
                try {
                    loader.close();
                }
                catch (IOException e2) {
                    this.server.getLogger().log(Level.WARNING, "Error closing the Plugin Class Loader for " + plugin.getDescription().getFullName());
                    e2.printStackTrace();
                }
            }
        }
    }
}


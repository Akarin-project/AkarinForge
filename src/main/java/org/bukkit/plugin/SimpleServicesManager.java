/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  com.google.common.collect.ImmutableSet
 */
package org.bukkit.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class SimpleServicesManager
implements ServicesManager {
    private final Map<Class<?>, List<RegisteredServiceProvider<?>>> providers = new HashMap();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> void register(Class<T> service, T provider, Plugin plugin, ServicePriority priority) {
        RegisteredServiceProvider<T> registeredProvider = null;
        Map map = this.providers;
        synchronized (map) {
            int position;
            List registered = this.providers.get(service);
            if (registered == null) {
                registered = new ArrayList();
                this.providers.put(service, registered);
            }
            if ((position = Collections.binarySearch(registered, registeredProvider = new RegisteredServiceProvider<T>(service, provider, priority, plugin))) < 0) {
                registered.add(- position + 1, registeredProvider);
            } else {
                registered.add(position, registeredProvider);
            }
        }
        Bukkit.getServer().getPluginManager().callEvent(new ServiceRegisterEvent(registeredProvider));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void unregisterAll(Plugin plugin) {
        ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList<ServiceUnregisterEvent>();
        Map map = this.providers;
        synchronized (map) {
            Iterator it2 = this.providers.entrySet().iterator();
            try {
                while (it2.hasNext()) {
                    Map.Entry entry = it2.next();
                    Iterator it22 = entry.getValue().iterator();
                    try {
                        while (it22.hasNext()) {
                            RegisteredServiceProvider registered = it22.next();
                            if (!registered.getPlugin().equals(plugin)) continue;
                            it22.remove();
                            unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                        }
                    }
                    catch (NoSuchElementException registered) {
                        // empty catch block
                    }
                    if (entry.getValue().size() != 0) continue;
                    it2.remove();
                }
            }
            catch (NoSuchElementException entry) {
                // empty catch block
            }
        }
        for (ServiceUnregisterEvent event : unregisteredEvents) {
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void unregister(Class<?> service, Object provider) {
        ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList<ServiceUnregisterEvent>();
        Map map = this.providers;
        synchronized (map) {
            Iterator it2 = this.providers.entrySet().iterator();
            try {
                while (it2.hasNext()) {
                    Map.Entry entry = it2.next();
                    if (entry.getKey() != service) continue;
                    Iterator it22 = entry.getValue().iterator();
                    try {
                        while (it22.hasNext()) {
                            RegisteredServiceProvider registered = it22.next();
                            if (registered.getProvider() != provider) continue;
                            it22.remove();
                            unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                        }
                    }
                    catch (NoSuchElementException registered) {
                        // empty catch block
                    }
                    if (entry.getValue().size() != 0) continue;
                    it2.remove();
                }
            }
            catch (NoSuchElementException entry) {
                // empty catch block
            }
        }
        for (ServiceUnregisterEvent event : unregisteredEvents) {
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void unregister(Object provider) {
        ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList<ServiceUnregisterEvent>();
        Map map = this.providers;
        synchronized (map) {
            Iterator it2 = this.providers.entrySet().iterator();
            try {
                while (it2.hasNext()) {
                    Map.Entry entry = it2.next();
                    Iterator it22 = entry.getValue().iterator();
                    try {
                        while (it22.hasNext()) {
                            RegisteredServiceProvider registered = it22.next();
                            if (!registered.getProvider().equals(provider)) continue;
                            it22.remove();
                            unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                        }
                    }
                    catch (NoSuchElementException registered) {
                        // empty catch block
                    }
                    if (entry.getValue().size() != 0) continue;
                    it2.remove();
                }
            }
            catch (NoSuchElementException entry) {
                // empty catch block
            }
        }
        for (ServiceUnregisterEvent event : unregisteredEvents) {
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> T load(Class<T> service) {
        Map map = this.providers;
        synchronized (map) {
            List registered = this.providers.get(service);
            if (registered == null) {
                return null;
            }
            return service.cast(registered.get(0).getProvider());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> RegisteredServiceProvider<T> getRegistration(Class<T> service) {
        Map map = this.providers;
        synchronized (map) {
            List registered = this.providers.get(service);
            if (registered == null) {
                return null;
            }
            return registered.get(0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<RegisteredServiceProvider<?>> getRegistrations(Plugin plugin) {
        ImmutableList.Builder ret = ImmutableList.builder();
        Map map = this.providers;
        synchronized (map) {
            for (List registered : this.providers.values()) {
                for (RegisteredServiceProvider provider : registered) {
                    if (!provider.getPlugin().equals(plugin)) continue;
                    ret.add(provider);
                }
            }
        }
        return ret.build();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public <T> List<RegisteredServiceProvider<T>> getRegistrations(Class<T> service) {
        ImmutableList.Builder ret;
        Map map = this.providers;
        synchronized (map) {
            List registered = this.providers.get(service);
            if (registered == null) {
                return ImmutableList.of();
            }
            ret = ImmutableList.builder();
            for (RegisteredServiceProvider provider : registered) {
                ret.add(provider);
            }
        }
        return ret.build();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Set<Class<?>> getKnownServices() {
        Map map = this.providers;
        synchronized (map) {
            return ImmutableSet.copyOf(this.providers.keySet());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> boolean isProvidedFor(Class<T> service) {
        Map map = this.providers;
        synchronized (map) {
            return this.providers.containsKey(service);
        }
    }
}


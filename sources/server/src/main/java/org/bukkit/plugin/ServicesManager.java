/*
 * Akarin Forge
 */
package org.bukkit.plugin;

import java.util.Collection;
import java.util.List;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public interface ServicesManager {
    public <T> void register(Class<T> var1, T var2, Plugin var3, ServicePriority var4);

    public void unregisterAll(Plugin var1);

    public void unregister(Class<?> var1, Object var2);

    public void unregister(Object var1);

    public <T> T load(Class<T> var1);

    public <T> RegisteredServiceProvider<T> getRegistration(Class<T> var1);

    public List<RegisteredServiceProvider<?>> getRegistrations(Plugin var1);

    public <T> Collection<RegisteredServiceProvider<T>> getRegistrations(Class<T> var1);

    public Collection<Class<?>> getKnownServices();

    public <T> boolean isProvidedFor(Class<T> var1);
}


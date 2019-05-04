/*
 * Akarin Forge
 */
package org.bukkit.plugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

public class RegisteredServiceProvider<T>
implements Comparable<RegisteredServiceProvider<?>> {
    private Class<T> service;
    private Plugin plugin;
    private T provider;
    private ServicePriority priority;

    public RegisteredServiceProvider(Class<T> service, T provider, ServicePriority priority, Plugin plugin) {
        this.service = service;
        this.plugin = plugin;
        this.provider = provider;
        this.priority = priority;
    }

    public Class<T> getService() {
        return this.service;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public T getProvider() {
        return this.provider;
    }

    public ServicePriority getPriority() {
        return this.priority;
    }

    @Override
    public int compareTo(RegisteredServiceProvider<?> other) {
        if (this.priority.ordinal() == other.getPriority().ordinal()) {
            return 0;
        }
        return this.priority.ordinal() < other.getPriority().ordinal() ? 1 : -1;
    }
}


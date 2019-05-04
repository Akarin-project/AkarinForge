/*
 * Akarin Forge
 */
package org.bukkit.event.server;

import org.bukkit.event.server.ServerEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public abstract class ServiceEvent
extends ServerEvent {
    private final RegisteredServiceProvider<?> provider;

    public ServiceEvent(RegisteredServiceProvider<?> provider) {
        this.provider = provider;
    }

    public RegisteredServiceProvider<?> getProvider() {
        return this.provider;
    }
}


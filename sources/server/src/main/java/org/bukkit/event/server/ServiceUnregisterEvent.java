/*
 * Akarin Forge
 */
package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServiceEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ServiceUnregisterEvent
extends ServiceEvent {
    private static final HandlerList handlers = new HandlerList();

    public ServiceUnregisterEvent(RegisteredServiceProvider<?> serviceProvider) {
        super(serviceProvider);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


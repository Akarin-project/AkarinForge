/*
 * Akarin Forge
 */
package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.PluginEvent;
import org.bukkit.plugin.Plugin;

public class PluginDisableEvent
extends PluginEvent {
    private static final HandlerList handlers = new HandlerList();

    public PluginDisableEvent(Plugin plugin) {
        super(plugin);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


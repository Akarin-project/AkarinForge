/*
 * Akarin Forge
 */
package org.bukkit.event.server;

import org.bukkit.event.server.ServerEvent;
import org.bukkit.plugin.Plugin;

public abstract class PluginEvent
extends ServerEvent {
    private final Plugin plugin;

    public PluginEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }
}


/*
 * Akarin Forge
 */
package org.bukkit.event.server;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerCommandEvent;

public class RemoteServerCommandEvent
extends ServerCommandEvent {
    private static final HandlerList handlers = new HandlerList();

    public RemoteServerCommandEvent(CommandSender sender, String command) {
        super(sender, command);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


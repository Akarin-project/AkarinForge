/*
 * Akarin Forge
 */
package org.bukkit.event.server;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;

public class ServerCommandEvent
extends ServerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String command;
    private final CommandSender sender;
    private boolean cancel = false;

    public ServerCommandEvent(CommandSender sender, String command) {
        this.command = command;
        this.sender = sender;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String message) {
        this.command = message;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}


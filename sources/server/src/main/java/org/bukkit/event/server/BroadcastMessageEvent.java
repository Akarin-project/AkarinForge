/*
 * Akarin Forge
 */
package org.bukkit.event.server;

import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;

public class BroadcastMessageEvent
extends ServerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String message;
    private final Set<CommandSender> recipients;
    private boolean cancelled = false;

    public BroadcastMessageEvent(String message, Set<CommandSender> recipients) {
        this.message = message;
        this.recipients = recipients;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<CommandSender> getRecipients() {
        return this.recipients;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


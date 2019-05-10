/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.event.server;

import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TabCompleteEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final CommandSender sender;
    private final String buffer;
    private List<String> completions;
    private boolean cancelled;

    public TabCompleteEvent(CommandSender sender, String buffer, List<String> completions) {
        Validate.notNull((Object)sender, (String)"sender");
        Validate.notNull((Object)buffer, (String)"buffer");
        Validate.notNull(completions, (String)"completions");
        this.sender = sender;
        this.buffer = buffer;
        this.completions = completions;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public String getBuffer() {
        return this.buffer;
    }

    public List<String> getCompletions() {
        return this.completions;
    }

    public void setCompletions(List<String> completions) {
        Validate.notNull(completions);
        this.completions = completions;
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


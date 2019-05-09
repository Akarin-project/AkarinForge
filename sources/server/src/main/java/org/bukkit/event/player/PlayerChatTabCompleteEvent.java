/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.event.player;

import java.util.Collection;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerChatTabCompleteEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String message;
    private final String lastToken;
    private final Collection<String> completions;

    public PlayerChatTabCompleteEvent(Player who, String message, Collection<String> completions) {
        super(who);
        Validate.notNull((Object)message, (String)"Message cannot be null");
        Validate.notNull(completions, (String)"Completions cannot be null");
        this.message = message;
        int i2 = message.lastIndexOf(32);
        this.lastToken = i2 < 0 ? message : message.substring(i2 + 1);
        this.completions = completions;
    }

    public String getChatMessage() {
        return this.message;
    }

    public String getLastToken() {
        return this.lastToken;
    }

    public Collection<String> getTabCompletions() {
        return this.completions;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


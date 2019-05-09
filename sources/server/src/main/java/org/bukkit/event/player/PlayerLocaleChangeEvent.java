/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerLocaleChangeEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String locale;

    public PlayerLocaleChangeEvent(Player who, String locale) {
        super(who);
        this.locale = locale;
    }

    public String getLocale() {
        return this.locale;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


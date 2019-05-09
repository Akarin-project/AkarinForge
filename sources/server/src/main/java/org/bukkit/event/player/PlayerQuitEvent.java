/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerQuitEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private String quitMessage;

    public PlayerQuitEvent(Player who, String quitMessage) {
        super(who);
        this.quitMessage = quitMessage;
    }

    public String getQuitMessage() {
        return this.quitMessage;
    }

    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


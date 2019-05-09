/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerResourcePackStatusEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Status status;

    public PlayerResourcePackStatusEvent(Player who, Status resourcePackStatus) {
        super(who);
        this.status = resourcePackStatus;
    }

    public Status getStatus() {
        return this.status;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static enum Status {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED;
        

        private Status() {
        }
    }

}


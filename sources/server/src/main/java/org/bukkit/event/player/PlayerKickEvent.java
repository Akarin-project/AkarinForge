/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerKickEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String leaveMessage;
    private String kickReason;
    private Boolean cancel;

    public PlayerKickEvent(Player playerKicked, String kickReason, String leaveMessage) {
        super(playerKicked);
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
        this.cancel = false;
    }

    public String getReason() {
        return this.kickReason;
    }

    public String getLeaveMessage() {
        return this.leaveMessage;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public void setReason(String kickReason) {
        this.kickReason = kickReason;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


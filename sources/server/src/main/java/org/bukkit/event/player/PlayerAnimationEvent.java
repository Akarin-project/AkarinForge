/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerEvent;

public class PlayerAnimationEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final PlayerAnimationType animationType = PlayerAnimationType.ARM_SWING;
    private boolean isCancelled = false;

    public PlayerAnimationEvent(Player player) {
        super(player);
    }

    public PlayerAnimationType getAnimationType() {
        return this.animationType;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


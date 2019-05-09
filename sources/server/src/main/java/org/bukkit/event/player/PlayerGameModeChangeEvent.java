/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerGameModeChangeEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final GameMode newGameMode;

    public PlayerGameModeChangeEvent(Player player, GameMode newGameMode) {
        super(player);
        this.newGameMode = newGameMode;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public GameMode getNewGameMode() {
        return this.newGameMode;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


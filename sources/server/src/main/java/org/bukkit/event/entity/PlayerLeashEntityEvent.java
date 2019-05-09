/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeashEntityEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity leashHolder;
    private final Entity entity;
    private boolean cancelled = false;
    private final Player player;

    public PlayerLeashEntityEvent(Entity what, Entity leashHolder, Player leasher) {
        this.leashHolder = leashHolder;
        this.entity = what;
        this.player = leasher;
    }

    public Entity getLeashHolder() {
        return this.leashHolder;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public final Player getPlayer() {
        return this.player;
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
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}


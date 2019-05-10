/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityTeleportEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private Location from;
    private Location to;

    public EntityTeleportEvent(Entity what, Location from, Location to2) {
        super(what);
        this.from = from;
        this.to = to2;
        this.cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Location getFrom() {
        return this.from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return this.to;
    }

    public void setTo(Location to2) {
        this.to = to2;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


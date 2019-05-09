/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class ItemDespawnEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final Location location;

    public ItemDespawnEvent(Item despawnee, Location loc) {
        super(despawnee);
        this.location = loc;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    @Override
    public Item getEntity() {
        return (Item)this.entity;
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


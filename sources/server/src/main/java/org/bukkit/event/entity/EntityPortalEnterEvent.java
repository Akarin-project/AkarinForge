/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityPortalEnterEvent
extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Location location;

    public EntityPortalEnterEvent(Entity entity, Location location) {
        super(entity);
        this.location = location;
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


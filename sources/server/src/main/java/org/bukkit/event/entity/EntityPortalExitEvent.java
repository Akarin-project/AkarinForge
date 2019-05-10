/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.util.Vector;

public class EntityPortalExitEvent
extends EntityTeleportEvent {
    private static final HandlerList handlers = new HandlerList();
    private Vector before;
    private Vector after;

    public EntityPortalExitEvent(Entity entity, Location from, Location to2, Vector before, Vector after) {
        super(entity, from, to2);
        this.before = before;
        this.after = after;
    }

    public Vector getBefore() {
        return this.before.clone();
    }

    public Vector getAfter() {
        return this.after.clone();
    }

    public void setAfter(Vector after) {
        this.after = after.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


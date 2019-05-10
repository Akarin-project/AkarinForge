/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class ExplosionPrimeEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private float radius;
    private boolean fire;

    public ExplosionPrimeEvent(Entity what, float radius, boolean fire) {
        super(what);
        this.radius = radius;
        this.fire = fire;
    }

    public ExplosionPrimeEvent(Explosive explosive) {
        this(explosive, explosive.getYield(), explosive.isIncendiary());
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean getFire() {
        return this.fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


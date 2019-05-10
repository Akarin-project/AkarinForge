/*
 * Akarin Forge
 */
package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleCollisionEvent;

public class VehicleEntityCollisionEvent
extends VehicleCollisionEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;
    private boolean cancelled = false;
    private boolean cancelledPickup = false;
    private boolean cancelledCollision = false;

    public VehicleEntityCollisionEvent(Vehicle vehicle, Entity entity) {
        super(vehicle);
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isPickupCancelled() {
        return this.cancelledPickup;
    }

    public void setPickupCancelled(boolean cancel) {
        this.cancelledPickup = cancel;
    }

    public boolean isCollisionCancelled() {
        return this.cancelledCollision;
    }

    public void setCollisionCancelled(boolean cancel) {
        this.cancelledCollision = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


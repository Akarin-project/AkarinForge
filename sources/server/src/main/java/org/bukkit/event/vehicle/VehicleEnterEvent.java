/*
 * Akarin Forge
 */
package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleEvent;

public class VehicleEnterEvent
extends VehicleEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Entity entered;

    public VehicleEnterEvent(Vehicle vehicle, Entity entered) {
        super(vehicle);
        this.entered = entered;
    }

    public Entity getEntered() {
        return this.entered;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


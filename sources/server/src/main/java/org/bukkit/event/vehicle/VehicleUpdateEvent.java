/*
 * Akarin Forge
 */
package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleEvent;

public class VehicleUpdateEvent
extends VehicleEvent {
    private static final HandlerList handlers = new HandlerList();

    public VehicleUpdateEvent(Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


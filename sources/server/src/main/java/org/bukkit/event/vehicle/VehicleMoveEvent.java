/*
 * Akarin Forge
 */
package org.bukkit.event.vehicle;

import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleEvent;

public class VehicleMoveEvent
extends VehicleEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Location from;
    private final Location to;

    public VehicleMoveEvent(Vehicle vehicle, Location from, Location to2) {
        super(vehicle);
        this.from = from;
        this.to = to2;
    }

    public Location getFrom() {
        return this.from;
    }

    public Location getTo() {
        return this.to;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


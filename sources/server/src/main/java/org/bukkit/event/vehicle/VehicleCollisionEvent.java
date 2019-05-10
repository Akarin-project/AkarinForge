/*
 * Akarin Forge
 */
package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleEvent;

public abstract class VehicleCollisionEvent
extends VehicleEvent {
    public VehicleCollisionEvent(Vehicle vehicle) {
        super(vehicle);
    }
}


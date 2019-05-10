/*
 * Akarin Forge
 */
package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleEvent;

public class VehicleDamageEvent
extends VehicleEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity attacker;
    private double damage;
    private boolean cancelled;

    public VehicleDamageEvent(Vehicle vehicle, Entity attacker, double damage) {
        super(vehicle);
        this.attacker = attacker;
        this.damage = damage;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
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


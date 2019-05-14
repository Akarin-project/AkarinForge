/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

public class BrewingStandFuelEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack fuel;
    private int fuelPower;
    private boolean cancelled;
    private boolean consuming = true;

    public BrewingStandFuelEvent(Block brewingStand, ItemStack fuel, int fuelPower) {
        super(brewingStand);
        this.fuel = fuel;
        this.fuelPower = fuelPower;
    }

    public ItemStack getFuel() {
        return this.fuel;
    }

    public int getFuelPower() {
        return this.fuelPower;
    }

    public void setFuelPower(int fuelPower) {
        this.fuelPower = fuelPower;
    }

    public boolean isConsuming() {
        return this.consuming;
    }

    public void setConsuming(boolean consuming) {
        this.consuming = consuming;
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


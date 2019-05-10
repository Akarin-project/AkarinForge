/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.block.Container;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;

public interface BrewingStand
extends Container,
Nameable {
    public int getBrewingTime();

    public void setBrewingTime(int var1);

    public int getFuelLevel();

    public void setFuelLevel(int var1);

    @Override
    public BrewerInventory getInventory();

    @Override
    public BrewerInventory getSnapshotInventory();
}


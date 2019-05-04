/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class DoubleChest
implements InventoryHolder {
    private DoubleChestInventory inventory;

    public DoubleChest(DoubleChestInventory chest) {
        this.inventory = chest;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public InventoryHolder getLeftSide() {
        return this.inventory.getLeftSide().getHolder();
    }

    public InventoryHolder getRightSide() {
        return this.inventory.getRightSide().getHolder();
    }

    public Location getLocation() {
        return this.getInventory().getLocation();
    }

    public World getWorld() {
        return this.getLocation().getWorld();
    }

    public double getX() {
        return this.getLocation().getX();
    }

    public double getY() {
        return this.getLocation().getY();
    }

    public double getZ() {
        return this.getLocation().getZ();
    }
}


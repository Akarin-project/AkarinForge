/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface DoubleChestInventory
extends Inventory {
    public Inventory getLeftSide();

    public Inventory getRightSide();

    @Override
    public DoubleChest getHolder();
}


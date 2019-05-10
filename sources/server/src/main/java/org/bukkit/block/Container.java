/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.block.BlockState;
import org.bukkit.block.Lockable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface Container
extends BlockState,
InventoryHolder,
Lockable {
    @Override
    public Inventory getInventory();

    public Inventory getSnapshotInventory();
}


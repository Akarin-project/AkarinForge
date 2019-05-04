/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.block.Container;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;

public interface Furnace
extends Container,
Nameable {
    public short getBurnTime();

    public void setBurnTime(short var1);

    public short getCookTime();

    public void setCookTime(short var1);

    @Override
    public FurnaceInventory getInventory();

    @Override
    public FurnaceInventory getSnapshotInventory();
}


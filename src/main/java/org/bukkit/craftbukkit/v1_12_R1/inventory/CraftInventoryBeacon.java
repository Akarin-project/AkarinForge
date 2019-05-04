/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBeacon
extends CraftInventory
implements BeaconInventory {
    public CraftInventoryBeacon(avh beacon) {
        super(beacon);
    }

    public CraftInventoryBeacon(tv beacon) {
        super(beacon);
    }

    @Override
    public void setItem(ItemStack item) {
        this.setItem(0, item);
    }

    @Override
    public ItemStack getItem() {
        return this.getItem(0);
    }
}


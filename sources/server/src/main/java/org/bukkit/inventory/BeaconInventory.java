/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface BeaconInventory
extends Inventory {
    public void setItem(ItemStack var1);

    public ItemStack getItem();
}


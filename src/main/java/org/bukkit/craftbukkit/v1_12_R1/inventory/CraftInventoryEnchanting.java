/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryEnchanting
extends CraftInventory
implements EnchantingInventory {
    public CraftInventoryEnchanting(tv inventory) {
        super(inventory);
    }

    @Override
    public void setItem(ItemStack item) {
        this.setItem(0, item);
    }

    @Override
    public ItemStack getItem() {
        return this.getItem(0);
    }

    @Override
    public void setSecondary(ItemStack item) {
        this.setItem(1, item);
    }

    @Override
    public ItemStack getSecondary() {
        return this.getItem(1);
    }
}


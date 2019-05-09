/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface EnchantingInventory
extends Inventory {
    public void setItem(ItemStack var1);

    public ItemStack getItem();

    public void setSecondary(ItemStack var1);

    public ItemStack getSecondary();
}


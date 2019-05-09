/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface BrewerInventory
extends Inventory {
    public ItemStack getIngredient();

    public void setIngredient(ItemStack var1);

    public ItemStack getFuel();

    public void setFuel(ItemStack var1);

    @Override
    public BrewingStand getHolder();
}


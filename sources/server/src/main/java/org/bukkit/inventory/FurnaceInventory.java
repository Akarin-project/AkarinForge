/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.block.Furnace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface FurnaceInventory
extends Inventory {
    public ItemStack getResult();

    public ItemStack getFuel();

    public ItemStack getSmelting();

    public void setFuel(ItemStack var1);

    public void setResult(ItemStack var1);

    public void setSmelting(ItemStack var1);

    @Override
    public Furnace getHolder();
}


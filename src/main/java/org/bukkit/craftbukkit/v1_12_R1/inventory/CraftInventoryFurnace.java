/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryFurnace
extends CraftInventory
implements FurnaceInventory {
    public CraftInventoryFurnace(avu inventory) {
        super(inventory);
    }

    public CraftInventoryFurnace(tv inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getResult() {
        return this.getItem(2);
    }

    @Override
    public ItemStack getFuel() {
        return this.getItem(1);
    }

    @Override
    public ItemStack getSmelting() {
        return this.getItem(0);
    }

    @Override
    public void setFuel(ItemStack stack) {
        this.setItem(1, stack);
    }

    @Override
    public void setResult(ItemStack stack) {
        this.setItem(2, stack);
    }

    @Override
    public void setSmelting(ItemStack stack) {
        this.setItem(0, stack);
    }

    @Override
    public Furnace getHolder() {
        InventoryHolder owner = this.inventory.getOwner();
        return owner instanceof Furnace ? (Furnace)owner : null;
    }
}


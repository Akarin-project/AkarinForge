/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBrewer
extends CraftInventory
implements BrewerInventory {
    public CraftInventoryBrewer(tv inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getIngredient() {
        return this.getItem(3);
    }

    @Override
    public void setIngredient(ItemStack ingredient) {
        this.setItem(3, ingredient);
    }

    @Override
    public BrewingStand getHolder() {
        InventoryHolder owner = this.inventory.getOwner();
        return owner instanceof BrewingStand ? (BrewingStand)owner : null;
    }

    @Override
    public ItemStack getFuel() {
        return this.getItem(4);
    }

    @Override
    public void setFuel(ItemStack fuel) {
        this.setItem(4, fuel);
    }
}


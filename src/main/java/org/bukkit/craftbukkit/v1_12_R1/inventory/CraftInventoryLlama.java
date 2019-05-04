/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryAbstractHorse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LlamaInventory;

public class CraftInventoryLlama
extends CraftInventoryAbstractHorse
implements LlamaInventory {
    public CraftInventoryLlama(tv inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getDecor() {
        return this.getItem(1);
    }

    @Override
    public void setDecor(ItemStack stack) {
        this.setItem(1, stack);
    }
}


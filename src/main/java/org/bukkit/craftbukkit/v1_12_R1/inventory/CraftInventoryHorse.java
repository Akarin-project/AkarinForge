/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryAbstractHorse;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse
extends CraftInventoryAbstractHorse
implements HorseInventory {
    public CraftInventoryHorse(tv inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getArmor() {
        return this.getItem(1);
    }

    @Override
    public void setArmor(ItemStack stack) {
        this.setItem(1, stack);
    }
}


package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.IInventory;

import org.bukkit.inventory.SaddledHorseInventory;

public class CraftSaddledInventory extends CraftInventoryAbstractHorse implements SaddledHorseInventory {

    public CraftSaddledInventory(IInventory inventory) {
        super(inventory);
    }

}

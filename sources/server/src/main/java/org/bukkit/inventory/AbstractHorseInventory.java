/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface AbstractHorseInventory
extends Inventory {
    public ItemStack getSaddle();

    public void setSaddle(ItemStack var1);
}


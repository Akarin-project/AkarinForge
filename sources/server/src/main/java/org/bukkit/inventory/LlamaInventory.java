/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

public interface LlamaInventory
extends AbstractHorseInventory {
    public ItemStack getDecor();

    public void setDecor(ItemStack var1);
}


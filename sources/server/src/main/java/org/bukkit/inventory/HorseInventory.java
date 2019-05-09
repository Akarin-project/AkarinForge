/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

public interface HorseInventory
extends AbstractHorseInventory {
    public ItemStack getArmor();

    public void setArmor(ItemStack var1);
}


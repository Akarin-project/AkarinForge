/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.MerchantRecipe;

public interface MerchantInventory
extends Inventory {
    public int getSelectedRecipeIndex();

    public MerchantRecipe getSelectedRecipe();
}


/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface CraftingInventory
extends Inventory {
    public ItemStack getResult();

    public ItemStack[] getMatrix();

    public void setResult(ItemStack var1);

    public void setMatrix(ItemStack[] var1);

    public Recipe getRecipe();
}


/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchantRecipe;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

public class CraftInventoryMerchant
extends CraftInventory
implements MerchantInventory {
    public CraftInventoryMerchant(agj merchant) {
        super(merchant);
    }

    @Override
    public int getSelectedRecipeIndex() {
        return this.getInventory().e;
    }

    @Override
    public MerchantRecipe getSelectedRecipe() {
        return this.getInventory().j().asBukkit();
    }

    @Override
    public agj getInventory() {
        return (agj)this.inventory;
    }
}


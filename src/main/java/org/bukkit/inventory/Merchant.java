/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import java.util.List;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.MerchantRecipe;

public interface Merchant {
    public List<MerchantRecipe> getRecipes();

    public void setRecipes(List<MerchantRecipe> var1);

    public MerchantRecipe getRecipe(int var1) throws IndexOutOfBoundsException;

    public void setRecipe(int var1, MerchantRecipe var2) throws IndexOutOfBoundsException;

    public int getRecipeCount();

    public boolean isTrading();

    public HumanEntity getTrader();
}


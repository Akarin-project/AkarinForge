/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.collect.Lists
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchantRecipe;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

public class CraftMerchant
implements Merchant {
    protected final amf merchant;

    public CraftMerchant(amf merchant) {
        this.merchant = merchant;
    }

    public amf getMerchant() {
        return this.merchant;
    }

    @Override
    public List<MerchantRecipe> getRecipes() {
        return Collections.unmodifiableList(Lists.transform((List)this.merchant.b_(null), (Function)new Function<amg, MerchantRecipe>(){

            public MerchantRecipe apply(amg recipe) {
                return recipe.asBukkit();
            }
        }));
    }

    @Override
    public void setRecipes(List<MerchantRecipe> recipes) {
        amh recipesList = this.merchant.b_(null);
        recipesList.clear();
        for (MerchantRecipe recipe : recipes) {
            recipesList.add(CraftMerchantRecipe.fromBukkit(recipe).toMinecraft());
        }
    }

    @Override
    public MerchantRecipe getRecipe(int i2) {
        return ((amg)this.merchant.b_(null).get(i2)).asBukkit();
    }

    @Override
    public void setRecipe(int i2, MerchantRecipe merchantRecipe) {
        this.merchant.b_(null).set(i2, CraftMerchantRecipe.fromBukkit(merchantRecipe).toMinecraft());
    }

    @Override
    public int getRecipeCount() {
        return this.merchant.b_(null).size();
    }

    @Override
    public boolean isTrading() {
        return this.getTrader() != null;
    }

    @Override
    public HumanEntity getTrader() {
        aed eh = this.merchant.t_();
        return eh == null ? null : eh.getBukkitEntity();
    }

    public int hashCode() {
        return this.merchant.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof CraftMerchant && ((CraftMerchant)obj).merchant.equals(this.merchant);
    }

}


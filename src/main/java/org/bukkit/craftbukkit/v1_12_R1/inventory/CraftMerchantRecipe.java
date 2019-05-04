/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Preconditions;
import java.util.List;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class CraftMerchantRecipe
extends MerchantRecipe {
    private final amg handle;

    public CraftMerchantRecipe(amg merchantRecipe) {
        super(CraftItemStack.asBukkitCopy(merchantRecipe.c), 0);
        this.handle = merchantRecipe;
        this.addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.a));
        this.addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.b));
    }

    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward) {
        super(result, uses, maxUses, experienceReward);
        this.handle = new amg(aip.a, aip.a, CraftItemStack.asNMSCopy(result), uses, maxUses, this);
    }

    @Override
    public int getUses() {
        return this.handle.d;
    }

    @Override
    public void setUses(int uses) {
        this.handle.d = uses;
    }

    @Override
    public int getMaxUses() {
        return this.handle.e;
    }

    @Override
    public void setMaxUses(int maxUses) {
        this.handle.e = maxUses;
    }

    @Override
    public boolean hasExperienceReward() {
        return this.handle.f;
    }

    @Override
    public void setExperienceReward(boolean flag) {
        this.handle.f = flag;
    }

    public amg toMinecraft() {
        List<ItemStack> ingredients = this.getIngredients();
        Preconditions.checkState((boolean)(!ingredients.isEmpty()), (Object)"No offered ingredients");
        this.handle.a = CraftItemStack.asNMSCopy(ingredients.get(0));
        if (ingredients.size() > 1) {
            this.handle.b = CraftItemStack.asNMSCopy(ingredients.get(1));
        }
        return this.handle;
    }

    public static CraftMerchantRecipe fromBukkit(MerchantRecipe recipe) {
        if (recipe instanceof CraftMerchantRecipe) {
            return (CraftMerchantRecipe)recipe;
        }
        CraftMerchantRecipe craft = new CraftMerchantRecipe(recipe.getResult(), recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward());
        craft.setIngredients(recipe.getIngredients());
        return craft;
    }
}


/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class MerchantRecipe
implements Recipe {
    private ItemStack result;
    private List<ItemStack> ingredients = new ArrayList<ItemStack>();
    private int uses;
    private int maxUses;
    private boolean experienceReward;

    public MerchantRecipe(ItemStack result, int maxUses) {
        this(result, 0, maxUses, false);
    }

    public MerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward) {
        this.result = result;
        this.uses = uses;
        this.maxUses = maxUses;
        this.experienceReward = experienceReward;
    }

    @Override
    public ItemStack getResult() {
        return this.result;
    }

    public void addIngredient(ItemStack item) {
        Preconditions.checkState((boolean)(this.ingredients.size() < 2), (Object)"MerchantRecipe can only have 2 ingredients");
        this.ingredients.add(item.clone());
    }

    public void removeIngredient(int index) {
        this.ingredients.remove(index);
    }

    public void setIngredients(List<ItemStack> ingredients) {
        this.ingredients = new ArrayList<ItemStack>();
        for (ItemStack item : ingredients) {
            this.ingredients.add(item.clone());
        }
    }

    public List<ItemStack> getIngredients() {
        ArrayList<ItemStack> copy = new ArrayList<ItemStack>();
        for (ItemStack item : this.ingredients) {
            copy.add(item.clone());
        }
        return copy;
    }

    public int getUses() {
        return this.uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public boolean hasExperienceReward() {
        return this.experienceReward;
    }

    public void setExperienceReward(boolean flag) {
        this.experienceReward = flag;
    }
}


/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.MaterialData;

public class FurnaceRecipe
implements Recipe {
    private ItemStack output;
    private ItemStack ingredient;
    private float experience;

    public FurnaceRecipe(ItemStack result, Material source) {
        this(result, source, 0, 0.0f);
    }

    public FurnaceRecipe(ItemStack result, MaterialData source) {
        this(result, source.getItemType(), source.getData(), 0.0f);
    }

    public FurnaceRecipe(ItemStack result, MaterialData source, float experience) {
        this(result, source.getItemType(), source.getData(), experience);
    }

    @Deprecated
    public FurnaceRecipe(ItemStack result, Material source, int data) {
        this(result, source, data, 0.0f);
    }

    @Deprecated
    public FurnaceRecipe(ItemStack result, Material source, int data, float experience) {
        this.output = new ItemStack(result);
        this.ingredient = new ItemStack(source, 1, (short)data);
        this.experience = experience;
    }

    public FurnaceRecipe setInput(MaterialData input) {
        return this.setInput(input.getItemType(), input.getData());
    }

    public FurnaceRecipe setInput(Material input) {
        return this.setInput(input, 0);
    }

    @Deprecated
    public FurnaceRecipe setInput(Material input, int data) {
        this.ingredient = new ItemStack(input, 1, (short)data);
        return this;
    }

    public ItemStack getInput() {
        return this.ingredient.clone();
    }

    @Override
    public ItemStack getResult() {
        return this.output.clone();
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }

    public float getExperience() {
        return this.experience;
    }
}


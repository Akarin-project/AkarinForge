/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import java.util.Arrays;
import java.util.List;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import io.akarin.forge.inventory.CustomModRecipe;

public class CraftInventoryCrafting
extends CraftInventory
implements CraftingInventory {
    private final tv resultInventory;

    public CraftInventoryCrafting(afy inventory, tv resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public tv getResultInventory() {
        return this.resultInventory;
    }

    public tv getMatrixInventory() {
        return this.inventory;
    }

    @Override
    public int getSize() {
        return this.getResultInventory().w_() + this.getMatrixInventory().w_();
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (this.getSize() > items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + this.getSize() + " or less");
        }
        this.setContents(items[0], Arrays.copyOfRange(items, 1, items.length));
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[this.getSize()];
        List<aip> mcResultItems = this.getResultInventory().getContents();
        int i2 = 0;
        for (i2 = 0; i2 < mcResultItems.size(); ++i2) {
            items[i2] = CraftItemStack.asCraftMirror(mcResultItems.get(i2));
        }
        List<aip> mcItems = this.getMatrixInventory().getContents();
        for (int j2 = 0; j2 < mcItems.size(); ++j2) {
            items[i2 + j2] = CraftItemStack.asCraftMirror(mcItems.get(j2));
        }
        return items;
    }

    public void setContents(ItemStack result, ItemStack[] contents) {
        this.setResult(result);
        this.setMatrix(contents);
    }

    @Override
    public CraftItemStack getItem(int index) {
        if (index < this.getResultInventory().w_()) {
            aip item = this.getResultInventory().a(index);
            return item.b() ? null : CraftItemStack.asCraftMirror(item);
        }
        aip item = this.getMatrixInventory().a(index - this.getResultInventory().w_());
        return item.b() ? null : CraftItemStack.asCraftMirror(item);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < this.getResultInventory().w_()) {
            this.getResultInventory().a(index, CraftItemStack.asNMSCopy(item));
        } else {
            this.getMatrixInventory().a(index - this.getResultInventory().w_(), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public ItemStack[] getMatrix() {
        List<aip> matrix = this.getMatrixInventory().getContents();
        return this.asCraftMirror(matrix);
    }

    @Override
    public ItemStack getResult() {
        aip item = this.getResultInventory().a(0);
        if (!item.b()) {
            return CraftItemStack.asCraftMirror(item);
        }
        return null;
    }

    @Override
    public void setMatrix(ItemStack[] contents) {
        if (this.getMatrixInventory().w_() > contents.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + this.getMatrixInventory().w_() + " or less");
        }
        for (int i2 = 0; i2 < this.getMatrixInventory().w_(); ++i2) {
            if (i2 < contents.length) {
                this.getMatrixInventory().a(i2, CraftItemStack.asNMSCopy(contents[i2]));
                continue;
            }
            this.getMatrixInventory().a(i2, aip.a);
        }
    }

    @Override
    public void setResult(ItemStack item) {
        List<aip> contents = this.getResultInventory().getContents();
        contents.set(0, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public Recipe getRecipe() {
        akt recipe = ((afy)this.getInventory()).currentRecipe;
        try {
            return recipe == null ? null : recipe.toBukkitRecipe();
        }
        catch (AbstractMethodError ex2) {
            return recipe == null ? null : new CustomModRecipe(recipe, recipe.getRegistryName());
        }
    }
}


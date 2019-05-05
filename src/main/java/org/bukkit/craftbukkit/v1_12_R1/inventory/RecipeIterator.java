/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import io.akarin.forge.inventory.CustomModRecipe;

public class RecipeIterator
implements Iterator<Recipe> {
    private final Iterator<akt> recipes = aku.a.iterator();
    private final Iterator<aip> smeltingCustom;
    private final Iterator<aip> smeltingVanilla;
    private Iterator<?> removeFrom = null;

    public RecipeIterator() {
        this.smeltingCustom = akp.a().customRecipes.keySet().iterator();
        this.smeltingVanilla = akp.a().b.keySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return this.recipes.hasNext() || this.smeltingCustom.hasNext() || this.smeltingVanilla.hasNext();
    }

    @Override
    public Recipe next() {
        aip item;
        if (this.recipes.hasNext()) {
            this.removeFrom = this.recipes;
            akt recipe = this.recipes.next();
            try {
                return recipe.toBukkitRecipe();
            }
            catch (AbstractMethodError ex2) {
                return recipe == null ? null : new CustomModRecipe(recipe, recipe.getRegistryName());
            }
        }
        if (this.smeltingCustom.hasNext()) {
            this.removeFrom = this.smeltingCustom;
            item = this.smeltingCustom.next();
        } else {
            this.removeFrom = this.smeltingVanilla;
            item = this.smeltingVanilla.next();
        }
        CraftItemStack stack = CraftItemStack.asCraftMirror(akp.a().a(item));
        return new CraftFurnaceRecipe(stack, CraftItemStack.asCraftMirror(item));
    }

    @Override
    public void remove() {
        if (this.removeFrom == null) {
            throw new IllegalStateException();
        }
        this.removeFrom.remove();
    }
}


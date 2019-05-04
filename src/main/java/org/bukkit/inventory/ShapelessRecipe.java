/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.MaterialData;

public class ShapelessRecipe
implements Recipe,
Keyed {
    private final NamespacedKey key;
    private final ItemStack output;
    private final List<ItemStack> ingredients = new ArrayList<ItemStack>();

    @Deprecated
    public ShapelessRecipe(ItemStack result) {
        this.key = NamespacedKey.randomKey();
        this.output = new ItemStack(result);
    }

    public ShapelessRecipe(NamespacedKey key, ItemStack result) {
        this.key = key;
        this.output = new ItemStack(result);
    }

    public ShapelessRecipe addIngredient(MaterialData ingredient) {
        return this.addIngredient(1, ingredient);
    }

    public ShapelessRecipe addIngredient(Material ingredient) {
        return this.addIngredient(1, ingredient, 0);
    }

    @Deprecated
    public ShapelessRecipe addIngredient(Material ingredient, int rawdata) {
        return this.addIngredient(1, ingredient, rawdata);
    }

    public ShapelessRecipe addIngredient(int count, MaterialData ingredient) {
        return this.addIngredient(count, ingredient.getItemType(), ingredient.getData());
    }

    public ShapelessRecipe addIngredient(int count, Material ingredient) {
        return this.addIngredient(count, ingredient, 0);
    }

    @Deprecated
    public ShapelessRecipe addIngredient(int count, Material ingredient, int rawdata) {
        Validate.isTrue((boolean)(this.ingredients.size() + count <= 9), (String)"Shapeless recipes cannot have more than 9 ingredients");
        if (rawdata == -1) {
            rawdata = 32767;
        }
        while (count-- > 0) {
            this.ingredients.add(new ItemStack(ingredient, 1, (short)rawdata));
        }
        return this;
    }

    public ShapelessRecipe removeIngredient(Material ingredient) {
        return this.removeIngredient(ingredient, 0);
    }

    public ShapelessRecipe removeIngredient(MaterialData ingredient) {
        return this.removeIngredient(ingredient.getItemType(), ingredient.getData());
    }

    public ShapelessRecipe removeIngredient(int count, Material ingredient) {
        return this.removeIngredient(count, ingredient, 0);
    }

    public ShapelessRecipe removeIngredient(int count, MaterialData ingredient) {
        return this.removeIngredient(count, ingredient.getItemType(), ingredient.getData());
    }

    @Deprecated
    public ShapelessRecipe removeIngredient(Material ingredient, int rawdata) {
        return this.removeIngredient(1, ingredient, rawdata);
    }

    @Deprecated
    public ShapelessRecipe removeIngredient(int count, Material ingredient, int rawdata) {
        Iterator<ItemStack> iterator = this.ingredients.iterator();
        while (count > 0 && iterator.hasNext()) {
            ItemStack stack = iterator.next();
            if (stack.getType() != ingredient || stack.getDurability() != rawdata) continue;
            iterator.remove();
            --count;
        }
        return this;
    }

    @Override
    public ItemStack getResult() {
        return this.output.clone();
    }

    public List<ItemStack> getIngredientList() {
        ArrayList<ItemStack> result = new ArrayList<ItemStack>(this.ingredients.size());
        for (ItemStack ingredient : this.ingredients) {
            result.add(ingredient.clone());
        }
        return result;
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }
}


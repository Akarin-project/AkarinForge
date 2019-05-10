/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.MaterialData;

public class ShapedRecipe
implements Recipe,
Keyed {
    private final NamespacedKey key;
    private final ItemStack output;
    private String[] rows;
    private Map<Character, ItemStack> ingredients = new HashMap<Character, ItemStack>();

    @Deprecated
    public ShapedRecipe(ItemStack result) {
        this.key = NamespacedKey.randomKey();
        this.output = new ItemStack(result);
    }

    public ShapedRecipe(NamespacedKey key, ItemStack result) {
        Preconditions.checkArgument((boolean)(key != null), (Object)"key");
        this.key = key;
        this.output = new ItemStack(result);
    }

    public /* varargs */ ShapedRecipe shape(String ... shape) {
        Validate.notNull((Object)shape, (String)"Must provide a shape");
        Validate.isTrue((boolean)(shape.length > 0 && shape.length < 4), (String)"Crafting recipes should be 1, 2, 3 rows, not ", (long)shape.length);
        int lastLen = -1;
        for (String row : shape) {
            Validate.notNull((Object)row, (String)"Shape cannot have null rows");
            Validate.isTrue((boolean)(row.length() > 0 && row.length() < 4), (String)"Crafting rows should be 1, 2, or 3 characters, not ", (long)row.length());
            Validate.isTrue((boolean)(lastLen == -1 || lastLen == row.length()), (String)"Crafting recipes must be rectangular");
            lastLen = row.length();
        }
        this.rows = new String[shape.length];
        for (int i2 = 0; i2 < shape.length; ++i2) {
            this.rows[i2] = shape[i2];
        }
        HashMap<Character, ItemStack> newIngredients = new HashMap<Character, ItemStack>();
        for (String row : shape) {
            char[] arrc = row.toCharArray();
            int n2 = arrc.length;
            for (int i3 = 0; i3 < n2; ++i3) {
                Character c2 = Character.valueOf(arrc[i3]);
                newIngredients.put(c2, this.ingredients.get(c2));
            }
        }
        this.ingredients = newIngredients;
        return this;
    }

    public ShapedRecipe setIngredient(char key, MaterialData ingredient) {
        return this.setIngredient(key, ingredient.getItemType(), ingredient.getData());
    }

    public ShapedRecipe setIngredient(char key, Material ingredient) {
        return this.setIngredient(key, ingredient, 0);
    }

    @Deprecated
    public ShapedRecipe setIngredient(char key, Material ingredient, int raw) {
        Validate.isTrue((boolean)this.ingredients.containsKey(Character.valueOf(key)), (String)"Symbol does not appear in the shape:", (long)key);
        if (raw == -1) {
            raw = 32767;
        }
        this.ingredients.put(Character.valueOf(key), new ItemStack(ingredient, 1, (short)raw));
        return this;
    }

    public Map<Character, ItemStack> getIngredientMap() {
        HashMap<Character, ItemStack> result = new HashMap<Character, ItemStack>();
        for (Map.Entry<Character, ItemStack> ingredient : this.ingredients.entrySet()) {
            if (ingredient.getValue() == null) {
                result.put(ingredient.getKey(), null);
                continue;
            }
            result.put(ingredient.getKey(), ingredient.getValue().clone());
        }
        return result;
    }

    public String[] getShape() {
        return (String[])this.rows.clone();
    }

    @Override
    public ItemStack getResult() {
        return this.output.clone();
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }
}


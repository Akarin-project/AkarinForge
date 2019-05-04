/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftShapedRecipe
extends ShapedRecipe
implements CraftRecipe {
    private akw recipe;

    public CraftShapedRecipe(NamespacedKey key, ItemStack result) {
        super(key, result);
    }

    public CraftShapedRecipe(ItemStack result, akw recipe) {
        this(recipe.key != null ? CraftNamespacedKey.fromMinecraft(recipe.key) : NamespacedKey.randomKey(), result);
        this.recipe = recipe;
    }

    public static CraftShapedRecipe fromBukkitRecipe(ShapedRecipe recipe) {
        if (recipe instanceof CraftShapedRecipe) {
            return (CraftShapedRecipe)recipe;
        }
        CraftShapedRecipe ret = new CraftShapedRecipe(recipe.getKey(), recipe.getResult());
        String[] shape = recipe.getShape();
        ret.shape(shape);
        Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
        Iterator<Character> iterator = ingredientMap.keySet().iterator();
        while (iterator.hasNext()) {
            char c2 = iterator.next().charValue();
            ItemStack stack = ingredientMap.get(Character.valueOf(c2));
            if (stack == null) continue;
            ret.setIngredient(c2, stack.getType(), stack.getDurability());
        }
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        String[] shape = this.getShape();
        Map<Character, ItemStack> ingred = this.getIngredientMap();
        int width = shape[0].length();
        fi<akq> data = fi.a(shape.length * width, akq.a);
        for (int i2 = 0; i2 < shape.length; ++i2) {
            String row = shape[i2];
            for (int j2 = 0; j2 < row.length(); ++j2) {
                data.set(i2 * width + j2, akq.a(CraftItemStack.asNMSCopy(ingred.get(Character.valueOf(row.charAt(j2))))));
            }
        }
        akw recipe = new akw("", width, shape.length, data, CraftItemStack.asNMSCopy(this.getResult()));
        recipe.setKey(CraftNamespacedKey.toMinecraft(this.getKey()));
        recipe.setRegistryName(recipe.key);
        ((ForgeRegistry)ForgeRegistries.RECIPES).unfreeze();
        ForgeRegistries.RECIPES.register(recipe);
        ((ForgeRegistry)ForgeRegistries.RECIPES).freeze();
    }
}


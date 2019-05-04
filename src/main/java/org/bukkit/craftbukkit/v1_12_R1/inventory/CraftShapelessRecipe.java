/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import java.util.List;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class CraftShapelessRecipe
extends ShapelessRecipe
implements CraftRecipe {
    private akx recipe;

    public CraftShapelessRecipe(NamespacedKey key, ItemStack result) {
        super(key, result);
    }

    public CraftShapelessRecipe(ItemStack result, akx recipe) {
        this(recipe.key != null ? CraftNamespacedKey.fromMinecraft(recipe.key) : NamespacedKey.randomKey(), result);
        this.recipe = recipe;
    }

    public static CraftShapelessRecipe fromBukkitRecipe(ShapelessRecipe recipe) {
        if (recipe instanceof CraftShapelessRecipe) {
            return (CraftShapelessRecipe)recipe;
        }
        CraftShapelessRecipe ret = new CraftShapelessRecipe(recipe.getKey(), recipe.getResult());
        for (ItemStack ingred : recipe.getIngredientList()) {
            ret.addIngredient(ingred.getType(), ingred.getDurability());
        }
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        List<ItemStack> ingred = this.getIngredientList();
        fi<akq> data = fi.a(ingred.size(), akq.a);
        for (int i2 = 0; i2 < ingred.size(); ++i2) {
            data.set(i2, akq.a(CraftItemStack.asNMSCopy(ingred.get(i2))));
        }
        akx recipe = new akx("", CraftItemStack.asNMSCopy(this.getResult()), data);
        recipe.setKey(CraftNamespacedKey.toMinecraft(this.getKey()));
        recipe.setRegistryName(recipe.key);
        ((ForgeRegistry)ForgeRegistries.RECIPES).unfreeze();
        ForgeRegistries.RECIPES.register(recipe);
        ((ForgeRegistry)ForgeRegistries.RECIPES).freeze();
    }
}


package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapelessRecipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ShapelessRecipes extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    private final ItemStack recipeOutput;
    public final NonNullList<Ingredient> recipeItems;
    private final String group;
    private final boolean isSimple;
    // CraftBukkit start
    public ResourceLocation key;

    @Override
    public void setKey(ResourceLocation key) {
        this.key = key;
    }
    
    public org.bukkit.inventory.ShapelessRecipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(this.recipeOutput);
        CraftShapelessRecipe recipe = new CraftShapelessRecipe(result, this);
        for (Ingredient list : this.recipeItems) {
            if (list != null && list.matchingStacks.length > 0) {
                net.minecraft.item.ItemStack stack = list.matchingStacks[0];
                recipe.addIngredient(org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers.getMaterial(stack.getItem()), (list.matchingStacks.length) > 1 ? 32767 : stack.getMetadata());
            }
        }
        return recipe;
    }
    // CraftBukkit end

    public ShapelessRecipes(String group, ItemStack output, NonNullList<Ingredient> ingredients)
    {
        this.group = group;
        this.recipeOutput = output;
        this.recipeItems = ingredients;
        boolean simple = true;
        for (Ingredient i : ingredients)
            simple &= i.isSimple();
        this.isSimple = simple;
    }

    public String getGroup()
    {
        return this.group;
    }

    public ItemStack getRecipeOutput()
    {
        return this.recipeOutput;
    }

    public NonNullList<Ingredient> getIngredients()
    {
        return this.recipeItems;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);

            nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return nonnulllist;
    }

    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        int ingredientCount = 0;
        net.minecraft.client.util.RecipeItemHelper recipeItemHelper = new net.minecraft.client.util.RecipeItemHelper();
        List<ItemStack> inputs = Lists.newArrayList();

        for (int i = 0; i < inv.getHeight(); ++i)
        {
            for (int j = 0; j < inv.getWidth(); ++j)
            {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

                if (!itemstack.isEmpty())
                {
                    ++ingredientCount;
                    if (this.isSimple)
                        recipeItemHelper.accountStack(itemstack, 1);
                    else
                        inputs.add(itemstack);
                }
            }
        }

        if (ingredientCount != this.recipeItems.size())
            return false;

        if (this.isSimple)
            return recipeItemHelper.canCraft(this, null);

        return net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.recipeItems) != null;
    }

    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        return this.recipeOutput.copy();
    }

    public static ShapelessRecipes deserialize(JsonObject json)
    {
        String s = JsonUtils.getString(json, "group", "");
        NonNullList<Ingredient> nonnulllist = deserializeIngredients(JsonUtils.getJsonArray(json, "ingredients"));

        if (nonnulllist.isEmpty())
        {
            throw new JsonParseException("No ingredients for shapeless recipe");
        }
        else if (nonnulllist.size() > 9)
        {
            throw new JsonParseException("Too many ingredients for shapeless recipe");
        }
        else
        {
            ItemStack itemstack = ShapedRecipes.deserializeItem(JsonUtils.getJsonObject(json, "result"), true);
            return new ShapelessRecipes(s, itemstack, nonnulllist);
        }
    }

    private static NonNullList<Ingredient> deserializeIngredients(JsonArray array)
    {
        NonNullList<Ingredient> nonnulllist = NonNullList.<Ingredient>create();

        for (int i = 0; i < array.size(); ++i)
        {
            Ingredient ingredient = ShapedRecipes.deserializeIngredient(array.get(i));

            if (ingredient != Ingredient.EMPTY)
            {
                nonnulllist.add(ingredient);
            }
        }

        return nonnulllist;
    }

    public boolean canFit(int width, int height)
    {
        return width * height >= this.recipeItems.size();
    }
}
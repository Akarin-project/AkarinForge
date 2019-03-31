package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IMerchant
{
    void setCustomer(@Nullable EntityPlayer player);

    @Nullable
    EntityPlayer getCustomer();

    @Nullable
    MerchantRecipeList getRecipes(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    void setRecipes(@Nullable MerchantRecipeList recipeList);

    void useRecipe(MerchantRecipe recipe);

    void verifySellingItem(ItemStack stack);

    ITextComponent getDisplayName();

    World getWorld();

    BlockPos getPos();
}
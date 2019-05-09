package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentDigging extends Enchantment
{
    protected EnchantmentDigging(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, EnumEnchantmentType.DIGGER, slots);
        this.setName("digging");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 1 + 10 * (enchantmentLevel - 1);
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public int getMaxLevel()
    {
        return 5;
    }

    public boolean canApply(ItemStack stack)
    {
        return stack.getItem() == Items.SHEARS ? true : super.canApply(stack);
    }
}
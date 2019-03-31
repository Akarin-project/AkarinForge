package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class EnchantmentDurability extends Enchantment
{
    protected EnchantmentDurability(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, EnumEnchantmentType.BREAKABLE, slots);
        this.setName("durability");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 5 + (enchantmentLevel - 1) * 8;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public int getMaxLevel()
    {
        return 3;
    }

    public boolean canApply(ItemStack stack)
    {
        return stack.isItemStackDamageable() ? true : super.canApply(stack);
    }

    public static boolean negateDamage(ItemStack stack, int level, Random rand)
    {
        if (stack.getItem() instanceof ItemArmor && rand.nextFloat() < 0.6F)
        {
            return false;
        }
        else
        {
            return rand.nextInt(level + 1) > 0;
        }
    }
}
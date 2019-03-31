package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowKnockback extends Enchantment
{
    public EnchantmentArrowKnockback(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, EnumEnchantmentType.BOW, slots);
        this.setName("arrowKnockback");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 12 + (enchantmentLevel - 1) * 20;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 25;
    }

    public int getMaxLevel()
    {
        return 2;
    }
}
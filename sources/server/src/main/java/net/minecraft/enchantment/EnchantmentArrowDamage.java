package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowDamage extends Enchantment
{
    public EnchantmentArrowDamage(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, EnumEnchantmentType.BOW, slots);
        this.setName("arrowDamage");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 1 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }

    public int getMaxLevel()
    {
        return 5;
    }
}
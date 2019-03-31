package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentOxygen extends Enchantment
{
    public EnchantmentOxygen(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, EnumEnchantmentType.ARMOR_HEAD, slots);
        this.setName("oxygen");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 10 * enchantmentLevel;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }

    public int getMaxLevel()
    {
        return 3;
    }
}
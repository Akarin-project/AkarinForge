package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentMending extends Enchantment
{
    public EnchantmentMending(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, EnumEnchantmentType.BREAKABLE, slots);
        this.setName("mending");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return enchantmentLevel * 25;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    public boolean isTreasureEnchantment()
    {
        return true;
    }

    public int getMaxLevel()
    {
        return 1;
    }
}
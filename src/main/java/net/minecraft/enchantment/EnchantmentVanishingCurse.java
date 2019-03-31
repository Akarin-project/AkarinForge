package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentVanishingCurse extends Enchantment
{
    public EnchantmentVanishingCurse(Enchantment.Rarity p_i47252_1_, EntityEquipmentSlot... p_i47252_2_)
    {
        super(p_i47252_1_, EnumEnchantmentType.ALL, p_i47252_2_);
        this.setName("vanishing_curse");
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 25;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return 50;
    }

    public int getMaxLevel()
    {
        return 1;
    }

    public boolean isTreasureEnchantment()
    {
        return true;
    }

    public boolean isCurse()
    {
        return true;
    }
}
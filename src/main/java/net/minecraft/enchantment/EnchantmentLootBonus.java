package net.minecraft.enchantment;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentLootBonus extends Enchantment
{
    protected EnchantmentLootBonus(Enchantment.Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, typeIn, slots);

        if (typeIn == EnumEnchantmentType.DIGGER)
        {
            this.setName("lootBonusDigger");
        }
        else if (typeIn == EnumEnchantmentType.FISHING_ROD)
        {
            this.setName("lootBonusFishing");
        }
        else
        {
            this.setName("lootBonus");
        }
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 15 + (enchantmentLevel - 1) * 9;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public int getMaxLevel()
    {
        return 3;
    }

    public boolean canApplyTogether(Enchantment ench)
    {
        return super.canApplyTogether(ench) && ench != Enchantments.SILK_TOUCH;
    }
}
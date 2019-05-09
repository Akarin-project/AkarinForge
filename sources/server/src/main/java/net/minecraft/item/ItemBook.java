package net.minecraft.item;

public class ItemBook extends Item
{
    public boolean isEnchantable(ItemStack stack)
    {
        return stack.getCount() == 1;
    }

    public int getItemEnchantability()
    {
        return 1;
    }
}
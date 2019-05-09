package net.minecraft.item;

import net.minecraft.util.text.TextFormatting;

public enum EnumRarity implements net.minecraftforge.common.IRarity
{
    COMMON(TextFormatting.WHITE, "Common"),
    UNCOMMON(TextFormatting.YELLOW, "Uncommon"),
    RARE(TextFormatting.AQUA, "Rare"),
    EPIC(TextFormatting.LIGHT_PURPLE, "Epic");

    public final TextFormatting rarityColor;
    public final String rarityName;

    private EnumRarity(TextFormatting color, String name)
    {
        this.rarityColor = color;
        this.rarityName = name;
    }

    @Override
    public TextFormatting getColor()
    {
        return this.rarityColor;
    }

    @Override
    public String getName()
    {
        return this.rarityName;
    }
}
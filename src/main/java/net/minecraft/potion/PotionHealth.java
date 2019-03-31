package net.minecraft.potion;

public class PotionHealth extends Potion
{
    public PotionHealth(boolean isBadEffectIn, int liquidColorIn)
    {
        super(isBadEffectIn, liquidColorIn);
    }

    public boolean isInstant()
    {
        return true;
    }

    public boolean isReady(int duration, int amplifier)
    {
        return duration >= 1;
    }
}
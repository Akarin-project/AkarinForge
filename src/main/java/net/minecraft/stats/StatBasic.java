package net.minecraft.stats;

import net.minecraft.util.text.ITextComponent;

public class StatBasic extends StatBase
{
    public StatBasic(String statIdIn, ITextComponent statNameIn, IStatType typeIn)
    {
        super(statIdIn, statNameIn, typeIn);
    }

    public StatBasic(String statIdIn, ITextComponent statNameIn)
    {
        super(statIdIn, statNameIn);
    }

    public StatBase registerStat()
    {
        super.registerStat();
        StatList.BASIC_STATS.add(this);
        return this;
    }
}
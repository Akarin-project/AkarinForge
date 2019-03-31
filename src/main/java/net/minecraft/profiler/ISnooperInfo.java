package net.minecraft.profiler;

public interface ISnooperInfo
{
    void addServerStatsToSnooper(Snooper playerSnooper);

    void addServerTypeToSnooper(Snooper playerSnooper);

    boolean isSnooperEnabled();
}
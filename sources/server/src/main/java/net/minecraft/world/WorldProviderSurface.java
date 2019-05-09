package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider
{
    public DimensionType getDimensionType()
    {
        return DimensionType.OVERWORLD;
    }

    public boolean canDropChunk(int x, int z)
    {
        return !this.world.isSpawnChunk(x, z) || !this.world.provider.getDimensionType().shouldLoadSpawn();
    }
}
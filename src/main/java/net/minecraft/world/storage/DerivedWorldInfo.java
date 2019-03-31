package net.minecraft.world.storage;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DerivedWorldInfo extends WorldInfo
{
    private final WorldInfo delegate;

    public DerivedWorldInfo(WorldInfo worldInfoIn)
    {
        this.delegate = worldInfoIn;
    }

    public NBTTagCompound cloneNBTCompound(@Nullable NBTTagCompound nbt)
    {
        return this.delegate.cloneNBTCompound(nbt);
    }

    public long getSeed()
    {
        return this.delegate.getSeed();
    }

    public int getSpawnX()
    {
        return this.delegate.getSpawnX();
    }

    public int getSpawnY()
    {
        return this.delegate.getSpawnY();
    }

    public int getSpawnZ()
    {
        return this.delegate.getSpawnZ();
    }

    public long getWorldTotalTime()
    {
        return this.delegate.getWorldTotalTime();
    }

    public long getWorldTime()
    {
        return this.delegate.getWorldTime();
    }

    @SideOnly(Side.CLIENT)
    public long getSizeOnDisk()
    {
        return this.delegate.getSizeOnDisk();
    }

    public NBTTagCompound getPlayerNBTTagCompound()
    {
        return this.delegate.getPlayerNBTTagCompound();
    }

    public String getWorldName()
    {
        return this.delegate.getWorldName();
    }

    public int getSaveVersion()
    {
        return this.delegate.getSaveVersion();
    }

    @SideOnly(Side.CLIENT)
    public long getLastTimePlayed()
    {
        return this.delegate.getLastTimePlayed();
    }

    public boolean isThundering()
    {
        return this.delegate.isThundering();
    }

    public int getThunderTime()
    {
        return this.delegate.getThunderTime();
    }

    public boolean isRaining()
    {
        return this.delegate.isRaining();
    }

    public int getRainTime()
    {
        return this.delegate.getRainTime();
    }

    public GameType getGameType()
    {
        return this.delegate.getGameType();
    }

    @SideOnly(Side.CLIENT)
    public void setSpawnX(int x)
    {
    }

    @SideOnly(Side.CLIENT)
    public void setSpawnY(int y)
    {
    }

    public void setWorldTotalTime(long time)
    {
    }

    @SideOnly(Side.CLIENT)
    public void setSpawnZ(int z)
    {
    }

    public void setWorldTime(long time)
    {
    }

    public void setSpawn(BlockPos spawnPoint)
    {
    }

    public void setWorldName(String worldName)
    {
    }

    public void setSaveVersion(int version)
    {
    }

    public void setThundering(boolean thunderingIn)
    {
    }

    public void setThunderTime(int time)
    {
    }

    public void setRaining(boolean isRaining)
    {
    }

    public void setRainTime(int time)
    {
    }

    public boolean isMapFeaturesEnabled()
    {
        return this.delegate.isMapFeaturesEnabled();
    }

    public boolean isHardcoreModeEnabled()
    {
        return this.delegate.isHardcoreModeEnabled();
    }

    public WorldType getTerrainType()
    {
        return this.delegate.getTerrainType();
    }

    public void setTerrainType(WorldType type)
    {
    }

    public boolean areCommandsAllowed()
    {
        return this.delegate.areCommandsAllowed();
    }

    public void setAllowCommands(boolean allow)
    {
    }

    public boolean isInitialized()
    {
        return this.delegate.isInitialized();
    }

    public void setServerInitialized(boolean initializedIn)
    {
    }

    public GameRules getGameRulesInstance()
    {
        return this.delegate.getGameRulesInstance();
    }

    public EnumDifficulty getDifficulty()
    {
        return this.delegate.getDifficulty();
    }

    public void setDifficulty(EnumDifficulty newDifficulty)
    {
    }

    public boolean isDifficultyLocked()
    {
        return this.delegate.isDifficultyLocked();
    }

    public void setDifficultyLocked(boolean locked)
    {
    }

    @Deprecated
    public void setDimensionData(DimensionType dimensionIn, NBTTagCompound compound)
    {
        this.delegate.setDimensionData(dimensionIn, compound);
    }

    @Deprecated
    public NBTTagCompound getDimensionData(DimensionType dimensionIn)
    {
        return this.delegate.getDimensionData(dimensionIn);
    }

    public void setDimensionData(int dimensionID, NBTTagCompound compound)
    {
        this.delegate.setDimensionData(dimensionID, compound);
    }

    public NBTTagCompound getDimensionData(int dimensionID)
    {
        return this.delegate.getDimensionData(dimensionID);
    }
}
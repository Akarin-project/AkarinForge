package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SaveHandlerMP implements ISaveHandler
{
    public WorldInfo loadWorldInfo()
    {
        return null;
    }

    public void checkSessionLock() throws MinecraftException
    {
    }

    public IChunkLoader getChunkLoader(WorldProvider provider)
    {
        return null;
    }

    public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound)
    {
    }

    public void saveWorldInfo(WorldInfo worldInformation)
    {
    }

    public IPlayerFileData getPlayerNBTManager()
    {
        return null;
    }

    public void flush()
    {
    }

    public File getMapFileFromName(String mapName)
    {
        return null;
    }

    public File getWorldDirectory()
    {
        return null;
    }

    public TemplateManager getStructureTemplateManager()
    {
        return null;
    }
}
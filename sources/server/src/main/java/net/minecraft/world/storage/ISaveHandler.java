package net.minecraft.world.storage;

import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;

public interface ISaveHandler
{
    @Nullable
    WorldInfo loadWorldInfo();

    void checkSessionLock() throws MinecraftException;

    IChunkLoader getChunkLoader(WorldProvider provider);

    void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound);

    void saveWorldInfo(WorldInfo worldInformation);

    IPlayerFileData getPlayerNBTManager();

    void flush();

    File getWorldDirectory();

    File getMapFileFromName(String mapName);

    TemplateManager getStructureTemplateManager();
}
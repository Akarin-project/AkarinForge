package net.minecraft.world.storage;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerFileData
{
    void writePlayerData(EntityPlayer player);

    @Nullable
    NBTTagCompound readPlayerData(EntityPlayer player);

    String[] getAvailablePlayerDat();
}
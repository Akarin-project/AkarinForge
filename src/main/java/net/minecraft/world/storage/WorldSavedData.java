package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData implements net.minecraftforge.common.util.INBTSerializable<NBTTagCompound>
{
    public final String mapName;
    private boolean dirty;

    public WorldSavedData(String name)
    {
        this.mapName = name;
    }

    public abstract void readFromNBT(NBTTagCompound nbt);

    public abstract NBTTagCompound writeToNBT(NBTTagCompound compound);

    public void markDirty()
    {
        this.setDirty(true);
    }

    public void setDirty(boolean isDirty)
    {
        this.dirty = isDirty;
    }

    public boolean isDirty()
    {
        return this.dirty;
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.readFromNBT(nbt);
    }

    public NBTTagCompound serializeNBT()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
}
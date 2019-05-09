package net.minecraft.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class MultiPartEntityPart extends Entity
{
    public final IEntityMultiPart parent;
    public final String partName;

    public MultiPartEntityPart(IEntityMultiPart parent, String partName, float width, float height)
    {
        super(parent.getWorld());
        this.setSize(width, height);
        this.parent = parent;
        this.partName = partName;
    }

    protected void entityInit()
    {
    }

    protected void readEntityFromNBT(NBTTagCompound compound)
    {
    }

    protected void writeEntityToNBT(NBTTagCompound compound)
    {
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return this.isEntityInvulnerable(source) ? false : this.parent.attackEntityFromPart(this, source, amount);
    }

    public boolean isEntityEqual(Entity entityIn)
    {
        return this == entityIn || this.parent == entityIn;
    }
}
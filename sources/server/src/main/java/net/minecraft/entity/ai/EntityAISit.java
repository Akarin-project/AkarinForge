package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase
{
    private final EntityTameable tameable;
    private boolean isSitting;

    public EntityAISit(EntityTameable entityIn)
    {
        this.tameable = entityIn;
        this.setMutexBits(5);
    }

    public boolean shouldExecute()
    {
        if (!this.tameable.isTamed())
        {
            return false;
        }
        else if (this.tameable.isInWater())
        {
            return false;
        }
        else if (!this.tameable.onGround)
        {
            return false;
        }
        else
        {
            EntityLivingBase entitylivingbase = this.tameable.getOwner();

            if (entitylivingbase == null)
            {
                return true;
            }
            else
            {
                return this.tameable.getDistanceSq(entitylivingbase) < 144.0D && entitylivingbase.getRevengeTarget() != null ? false : this.isSitting;
            }
        }
    }

    public void startExecuting()
    {
        this.tameable.getNavigator().clearPath();
        this.tameable.setSitting(true);
    }

    public void resetTask()
    {
        this.tameable.setSitting(false);
    }

    public void setSitting(boolean sitting)
    {
        this.isSitting = sitting;
    }
}
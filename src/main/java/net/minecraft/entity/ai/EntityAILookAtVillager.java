package net.minecraft.entity.ai;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAILookAtVillager extends EntityAIBase
{
    private final EntityIronGolem ironGolem;
    private EntityVillager villager;
    private int lookTime;

    public EntityAILookAtVillager(EntityIronGolem ironGolemIn)
    {
        this.ironGolem = ironGolemIn;
        this.setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        if (!this.ironGolem.world.isDaytime())
        {
            return false;
        }
        else if (this.ironGolem.getRNG().nextInt(8000) != 0)
        {
            return false;
        }
        else
        {
            this.villager = (EntityVillager)this.ironGolem.world.findNearestEntityWithinAABB(EntityVillager.class, this.ironGolem.getEntityBoundingBox().grow(6.0D, 2.0D, 6.0D), this.ironGolem);
            return this.villager != null;
        }
    }

    public boolean shouldContinueExecuting()
    {
        return this.lookTime > 0;
    }

    public void startExecuting()
    {
        this.lookTime = 400;
        this.ironGolem.setHoldingRose(true);
    }

    public void resetTask()
    {
        this.ironGolem.setHoldingRose(false);
        this.villager = null;
    }

    public void updateTask()
    {
        this.ironGolem.getLookHelper().setLookPositionWithEntity(this.villager, 30.0F, 30.0F);
        --this.lookTime;
    }
}
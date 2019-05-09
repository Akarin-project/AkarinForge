package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITradePlayer extends EntityAIBase
{
    private final EntityVillager villager;

    public EntityAITradePlayer(EntityVillager villagerIn)
    {
        this.villager = villagerIn;
        this.setMutexBits(5);
    }

    public boolean shouldExecute()
    {
        if (!this.villager.isEntityAlive())
        {
            return false;
        }
        else if (this.villager.isInWater())
        {
            return false;
        }
        else if (!this.villager.onGround)
        {
            return false;
        }
        else if (this.villager.velocityChanged)
        {
            return false;
        }
        else
        {
            EntityPlayer entityplayer = this.villager.getCustomer();

            if (entityplayer == null)
            {
                return false;
            }
            else if (this.villager.getDistanceSq(entityplayer) > 16.0D)
            {
                return false;
            }
            else
            {
                return entityplayer.openContainer != null;
            }
        }
    }

    public void startExecuting()
    {
        this.villager.getNavigator().clearPath();
    }

    public void resetTask()
    {
        this.villager.setCustomer((EntityPlayer)null);
    }
}
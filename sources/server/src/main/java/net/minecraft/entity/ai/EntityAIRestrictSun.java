package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIRestrictSun extends EntityAIBase
{
    private final EntityCreature entity;

    public EntityAIRestrictSun(EntityCreature creature)
    {
        this.entity = creature;
    }

    public boolean shouldExecute()
    {
        return this.entity.world.isDaytime() && this.entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
    }

    public void startExecuting()
    {
        ((PathNavigateGround)this.entity.getNavigator()).setAvoidSun(true);
    }

    public void resetTask()
    {
        ((PathNavigateGround)this.entity.getNavigator()).setAvoidSun(false);
    }
}
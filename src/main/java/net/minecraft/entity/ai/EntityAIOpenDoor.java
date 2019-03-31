package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAIOpenDoor extends EntityAIDoorInteract
{
    boolean closeDoor;
    int closeDoorTemporisation;

    public EntityAIOpenDoor(EntityLiving entitylivingIn, boolean shouldClose)
    {
        super(entitylivingIn);
        this.entity = entitylivingIn;
        this.closeDoor = shouldClose;
    }

    public boolean shouldContinueExecuting()
    {
        return this.closeDoor && this.closeDoorTemporisation > 0 && super.shouldContinueExecuting();
    }

    public void startExecuting()
    {
        this.closeDoorTemporisation = 20;
        this.doorBlock.toggleDoor(this.entity.world, this.doorPosition, true);
    }

    public void resetTask()
    {
        if (this.closeDoor)
        {
            this.doorBlock.toggleDoor(this.entity.world, this.doorPosition, false);
        }
    }

    public void updateTask()
    {
        --this.closeDoorTemporisation;
        super.updateTask();
    }
}
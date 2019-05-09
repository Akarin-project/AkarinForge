package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;

public class EntityAIBreakDoor extends EntityAIDoorInteract
{
    private int breakingTime;
    private int previousBreakProgress = -1;

    public EntityAIBreakDoor(EntityLiving entityIn)
    {
        super(entityIn);
    }

    public boolean shouldExecute()
    {
        if (!super.shouldExecute())
        {
            return false;
        }
        else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entity.world, this.entity) || !this.entity.world.getBlockState(this.doorPosition).getBlock().canEntityDestroy(this.entity.world.getBlockState(this.doorPosition), this.entity.world, this.doorPosition, this.entity) || !net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this.entity, this.doorPosition, this.entity.world.getBlockState(this.doorPosition)))
        {
            return false;
        }
        else
        {
            BlockDoor blockdoor = this.doorBlock;
            return !BlockDoor.isOpen(this.entity.world, this.doorPosition);
        }
    }

    public void startExecuting()
    {
        super.startExecuting();
        this.breakingTime = 0;
    }

    public boolean shouldContinueExecuting()
    {
        double d0 = this.entity.getDistanceSq(this.doorPosition);
        boolean flag;

        if (this.breakingTime <= 240)
        {
            BlockDoor blockdoor = this.doorBlock;

            if (!BlockDoor.isOpen(this.entity.world, this.doorPosition) && d0 < 4.0D)
            {
                flag = true;
                return flag;
            }
        }

        flag = false;
        return flag;
    }

    public void resetTask()
    {
        super.resetTask();
        this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, -1);
    }

    public void updateTask()
    {
        super.updateTask();

        if (this.entity.getRNG().nextInt(20) == 0)
        {
            this.entity.world.playEvent(1019, this.doorPosition, 0);
        }

        ++this.breakingTime;
        int i = (int)((float)this.breakingTime / 240.0F * 10.0F);

        if (i != this.previousBreakProgress)
        {
            this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, i);
            this.previousBreakProgress = i;
        }

        if (this.breakingTime == 240 && this.entity.world.getDifficulty() == EnumDifficulty.HARD)
        {
            this.entity.world.setBlockToAir(this.doorPosition);
            this.entity.world.playEvent(1021, this.doorPosition, 0);
            this.entity.world.playEvent(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
        }
    }
}
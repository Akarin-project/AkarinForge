package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIOcelotSit extends EntityAIMoveToBlock
{
    private final EntityOcelot ocelot;

    public EntityAIOcelotSit(EntityOcelot ocelotIn, double p_i45315_2_)
    {
        super(ocelotIn, p_i45315_2_, 8);
        this.ocelot = ocelotIn;
    }

    public boolean shouldExecute()
    {
        return this.ocelot.isTamed() && !this.ocelot.isSitting() && super.shouldExecute();
    }

    public void startExecuting()
    {
        super.startExecuting();
        this.ocelot.getAISit().setSitting(false);
    }

    public void resetTask()
    {
        super.resetTask();
        this.ocelot.setSitting(false);
    }

    public void updateTask()
    {
        super.updateTask();
        this.ocelot.getAISit().setSitting(false);

        if (!this.getIsAboveDestination())
        {
            this.ocelot.setSitting(false);
        }
        else if (!this.ocelot.isSitting())
        {
            this.ocelot.setSitting(true);
        }
    }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        if (!worldIn.isAirBlock(pos.up()))
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (block == Blocks.CHEST)
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntityChest && ((TileEntityChest)tileentity).numPlayersUsing < 1)
                {
                    return true;
                }
            }
            else
            {
                if (block == Blocks.LIT_FURNACE)
                {
                    return true;
                }

                if (block == Blocks.BED && iblockstate.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD)
                {
                    return true;
                }
            }

            return false;
        }
    }
}
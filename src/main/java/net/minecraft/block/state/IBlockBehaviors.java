package net.minecraft.block.state;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockBehaviors
{
    boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param);

    void neighborChanged(World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos);
}
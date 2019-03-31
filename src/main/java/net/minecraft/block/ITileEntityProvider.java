package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface ITileEntityProvider
{
    @Nullable
    TileEntity createNewTileEntity(World worldIn, int meta);
}
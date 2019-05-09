package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStructure extends BlockContainer
{
    public static final PropertyEnum<TileEntityStructure.Mode> MODE = PropertyEnum.<TileEntityStructure.Mode>create("mode", TileEntityStructure.Mode.class);

    public BlockStructure()
    {
        super(Material.IRON, MapColor.SILVER);
        this.setDefaultState(this.blockState.getBaseState());
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityStructure();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityStructure ? ((TileEntityStructure)tileentity).usedBy(playerIn) : false;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityStructure)
            {
                TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity;
                tileentitystructure.createdBy(placer);
            }
        }
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(MODE, TileEntityStructure.Mode.DATA);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(MODE, TileEntityStructure.Mode.getById(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((TileEntityStructure.Mode)state.getValue(MODE)).getModeId();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {MODE});
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityStructure)
            {
                TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity;
                boolean flag = worldIn.isBlockPowered(pos);
                boolean flag1 = tileentitystructure.isPowered();

                if (flag && !flag1)
                {
                    tileentitystructure.setPowered(true);
                    this.trigger(tileentitystructure);
                }
                else if (!flag && flag1)
                {
                    tileentitystructure.setPowered(false);
                }
            }
        }
    }

    private void trigger(TileEntityStructure p_189874_1_)
    {
        switch (p_189874_1_.getMode())
        {
            case SAVE:
                p_189874_1_.save(false);
                break;
            case LOAD:
                p_189874_1_.load(false);
                break;
            case CORNER:
                p_189874_1_.unloadStructure();
            case DATA:
        }
    }
}
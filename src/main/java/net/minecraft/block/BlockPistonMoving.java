package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonMoving extends BlockContainer
{
    public static final PropertyDirection FACING = BlockPistonExtension.FACING;
    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = BlockPistonExtension.TYPE;

    public BlockPistonMoving()
    {
        super(Material.PISTON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.setHardness(-1.0F);
    }

    @Nullable
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return null;
    }

    public static TileEntity createTilePiston(IBlockState blockStateIn, EnumFacing facingIn, boolean extendingIn, boolean shouldHeadBeRenderedIn)
    {
        return new TileEntityPiston(blockStateIn, facingIn, extendingIn, shouldHeadBeRenderedIn);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityPiston)
        {
            ((TileEntityPiston)tileentity).clearPistonTileEntity();
        }
        else
        {
            super.breakBlock(worldIn, pos, state);
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return false;
    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockPos blockpos = pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (iblockstate.getBlock() instanceof BlockPistonBase && ((Boolean)iblockstate.getValue(BlockPistonBase.EXTENDED)).booleanValue())
        {
            worldIn.setBlockToAir(blockpos);
        }
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null)
        {
            worldIn.setBlockToAir(pos);
            return true;
        }
        else
        {
            return false;
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (false && !worldIn.isRemote) // Forge: Noop this out
        {
            TileEntityPiston tileentitypiston = this.getTilePistonAt(worldIn, pos);

            if (tileentitypiston != null)
            {
                IBlockState iblockstate = tileentitypiston.getPistonState();
                iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
            }
        }
        super.dropBlockAsItemWithChance(worldIn, pos, state, 1, fortune); // mimic vanilla behavior from above and ignore chance
    }

    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
    {
        return null;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            worldIn.getTileEntity(pos);
        }
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntityPiston tileentitypiston = this.getTilePistonAt(worldIn, pos);
        return tileentitypiston == null ? null : tileentitypiston.getAABB(worldIn, pos);
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        TileEntityPiston tileentitypiston = this.getTilePistonAt(worldIn, pos);

        if (tileentitypiston != null)
        {
            tileentitypiston.addCollissionAABBs(worldIn, pos, entityBox, collidingBoxes, entityIn);
        }
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        TileEntityPiston tileentitypiston = this.getTilePistonAt(source, pos);
        return tileentitypiston != null ? tileentitypiston.getAABB(source, pos) : FULL_BLOCK_AABB;
    }

    @Nullable
    private TileEntityPiston getTilePistonAt(IBlockAccess iBlockAccessIn, BlockPos blockPosIn)
    {
        TileEntity tileentity = iBlockAccessIn.getTileEntity(blockPosIn);
        return tileentity instanceof TileEntityPiston ? (TileEntityPiston)tileentity : null;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return ItemStack.EMPTY;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, BlockPistonExtension.getFacing(meta)).withProperty(TYPE, (meta & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (state.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY)
        {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, TYPE});
    }

    @Override
    public void getDrops(net.minecraft.util.NonNullList<net.minecraft.item.ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileEntityPiston tileentitypiston = this.getTilePistonAt(world, pos);
        if (tileentitypiston != null)
        {
            IBlockState pushed = tileentitypiston.getPistonState();
            drops.addAll(pushed.getBlock().getDrops(world, pos, pushed, fortune)); // use the old method until it gets removed, for backward compatibility
        }
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}